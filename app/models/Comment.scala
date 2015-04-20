package models

import java.util.Date

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current

case class Comment (id: Long, author: String, story: Long, text: String, date: Date,var idParent: Option[Long], var comments: List[Comment]){
  def this(id: Long, author: String, story: Long, text: String, date: Date) = this(id, author, story, text, date, None, List())
  def getComments(comment: Comment): List[Comment] ={

    Comment.all.foreach(println(_))
    Comment.all().filter(c => c.idParent.getOrElse(-1) == comment.id)
  }



}

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

  def update (id: Long, idParent: Long) ={
    DB.withConnection { implicit c =>
      SQL("update comments set idParent = {idParent} where id = {id}").on('id -> id, 'idParent -> idParent).executeUpdate()

    }
  }

  val comment= {
    get[Long]("id") ~ get[String]("author") ~ get[Long]("story")~ get[String]("text") ~ get[Date]("data") map{
      case id ~ author ~ story ~ text ~ data  => new Comment(id, author, story,text, data)
    }
  }

  def getById( id: Long)= {
    val comments =all().filter(comment => comment.id == id)
    comments.head
  }

  def addComment(comment: Comment, commentToAdd: Comment): Unit ={
    Comment.update(commentToAdd.id, comment.id)

    commentToAdd.idParent = Some(comment.id)
    comment.comments = comment.comments.:+(commentToAdd)

    println("LOSC OMMENTs del comment: " + comment.text + " --- tiene como comments: " + comment.getComments(comment) )
  }

}
