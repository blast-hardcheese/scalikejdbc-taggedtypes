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

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
