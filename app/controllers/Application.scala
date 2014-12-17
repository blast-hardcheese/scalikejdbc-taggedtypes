package controllers

import play.api._
import play.api.mvc._
import org.joda.time._

import scalikejdbc._

object Application extends Controller {
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

  type FirstName = String
  type LastName = String
  type CreatedAt = DateTime
  type Person = (FirstName, LastName, CreatedAt)

  def getUser(n: FirstName): Option[Person] = {
    sql"select * from members where name = ${n}".map({ r => (r.string("name"), "Smith", r.jodaDateTime("created_at")) }).first.apply
  }

  def index = Action {
    val entities: List[Map[String, Any]] = sql"select * from members".map(_.toMap).list.apply()
    val user = getUser("Alice")
    println(s"Person: $user")

    Ok(entities.toString)
  }

}
