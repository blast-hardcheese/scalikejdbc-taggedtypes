package controllers

import play.api._
import play.api.mvc._
import org.joda.time._

import scalikejdbc._

trait TaggedTypes {
  import scala.language.implicitConversions

  type Tagged[A] = { type Tag = A }
  type @@[A, B] = A with Tagged[B]

  implicit def lift[A, B](x: A): A @@ B = x.asInstanceOf[A @@ B]
  implicit def unlift[A, B](x: A @@ B): A = x.asInstanceOf[A]
}

trait Types {
  self: TaggedTypes =>

  class UserId
  class FirstName
  class LastName
  class CreatedAt

  type User = (String @@ FirstName, String @@ LastName, DateTime @@ CreatedAt)
}

object Application extends Controller with TaggedTypes with Types {
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")

  implicit val session = AutoSession

  sql"""
  create table members (
    id serial not null primary key,
    name varchar(64),
    created_at timestamp not null
  )
  """.execute.apply()

  Seq("Alice", "Bob", "Chris") foreach { name =>
    sql"insert into members (name, created_at) values (${name}, current_timestamp)".update.apply()
  }

  def getUser(n: String @@ FirstName): Option[User] = {
    sql"select * from members where name = ${n}".map({ r => (r.string("name"), "Smith", r.jodaDateTime("created_at")): User }).first.apply
  }

  def index = Action {
    val user = getUser("Alice")
    println(s"Person: $user")

    Ok(s"""Found "Alice": $user""")
  }
}
