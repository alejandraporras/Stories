package models

import java.util.Date

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current

case class Comment (id: Long, author: String, story: Long, text: String, date: Date,var idParent: Option[Long], var comments: List[Comment]){
  def this(id: Long, author: String, story: Long, text: String, date: Date, idParent: Option[Long]= None) = this(id, author, story, text, date, idParent, List())
  def getComments(): List[Comment] ={

    CommentDAO.all().filter(c => c.idParent.getOrElse(-1) == this.id)
  }
  def addComment(comment: Comment): Unit ={

    CommentDAO.update(comment.id, this.id)
    comment.idParent = Some(this.id)
    this.comments = this.comments.:+(comment)

    comments.foreach(x=>  println("tengo este coment: " + x))

  }


}

object CommentDAO{


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
    get[Long]("id") ~ get[String]("author") ~ get[Long]("story")~ get[String]("text") ~ get[Date]("data") ~get[Option[Long]]("idParent") map{
      case id ~ author ~ story ~ text ~ data ~idParent => new Comment(id, author, story,text, data, idParent)
    }
  }

  def getById( id: Long)= {
    val comments =all().filter(comment => comment.id == id)
    comments.head
  }



}
