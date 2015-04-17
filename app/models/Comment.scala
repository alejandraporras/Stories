package models

import java.util.Date

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current

case class Comment (id: Long, author: String, story: Long, text: String, date: Date)

object Comment{


  def all(): List[Comment] = {
    DB.withConnection { implicit c =>
      SQL("select * from comments").as(comment *)

    }
  }

  def create( author: String, story: Long, text: String, data: Date) ={
    DB.withConnection { implicit c =>
      SQL("insert into comments ( author, story, text, data) values ({by}, {story},{txt},{data})").on(
        'by -> author ,'story -> story,'txt -> text , 'data -> data ).executeInsert()
    }

  }
  def delete (id: Long) ={
    DB.withConnection { implicit c =>
      SQL("delete from comments where id = {id}").on(
        'id -> id).executeUpdate()

    }
  }

  val comment= {
    get[Long]("id") ~ get[String]("author") ~ get[Long]("story")~ get[String]("text") ~ get[Date]("data") map{
      case id ~ author ~ story ~ text ~ data  => Comment(id, author, story,text, data)
    }
  }

  def getById( id: Long)= {
    val comments =all().filter(comment => comment.id == id)
    comments.head
  }



}
