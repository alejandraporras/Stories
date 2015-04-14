package models

import play.api.db.DB
import play.api.Play.current
import anorm._
import anorm.SqlParser._


case class User (username: String, password: String)

object User{
  def all(): List[User] = DB.withConnection { implicit c =>
    SQL("select * from user").as(user *)

  }
  def create(username: String, password: String) ={
    DB.withConnection { implicit c =>
      SQL("insert into user values ({username}, {password})").on(
        'username -> username, 'password -> password).executeUpdate()
    }

  }
  def delete (username: String) ={
    DB.withConnection { implicit c =>
      SQL("delete from user where username = {username}").on(
        'username -> username).executeUpdate()

    }
  }


  val user= {
    get[String]("username") ~ get[String]("password") map {
      case username~password => User(username, password)
    }
  }


}
