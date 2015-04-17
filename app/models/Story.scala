package models
import java.util.Date

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB

case class Story (id: Long, title: String,  author: String, text: String,var points: Integer, data: Date, var comments: List[Comment]){
  def this(id: Long, title: String,  author: String, text: String,  points: Integer, data: Date) = this(id, title, author, text, points, data, List())
  def getComments(story: Story): List[Comment] ={
    comments.filter(comment => comment.story == story.id)
  }
}

object Story{


  def all(): List[Story] = {
    DB.withConnection { implicit c =>
      SQL("select * from stories").as(story *)

    }
  }

  def create(title: String,  author: String, text: String, points: Integer, data: Date) ={
    DB.withConnection { implicit c =>
      SQL("insert into stories (title, author, text, points, data) values ({tit},{by}, {txt}, {pts}, {data})").on(
        'tit -> title, 'by -> author ,'txt -> text , 'pts -> points, 'data -> data ).executeInsert()
    }

  }
  def delete (id: Long) ={
    DB.withConnection { implicit c =>
      SQL("delete from stories where id = {id}").on(
        'id -> id).executeUpdate()

    }
  }

  def update (id: Long, points: Integer) ={
    DB.withConnection { implicit c =>
      SQL("update stories set points = {points} where id = {id}").on('id -> id, 'points -> points).executeUpdate()

    }
  }



  val story= {
    get[Long]("id") ~ get[String]("title") ~ get[String]("author") ~ get[String]("text") ~ get[Int]("points")~ get[Date]("data") map{
       case id ~ title ~ author ~ text ~ points ~ data  => new Story(id, title, author,text,  points, data)
    }
  }

  def getById(id: Long)={
    val stories =all().filter(story => story.id == id)
    stories.head

  }

  def addComment(user: User, story: Story, comment: Comment): Unit ={
    story.comments = story.comments.:+(comment)
    println("ADDING COMMENT:" + story.getComments(story).toString())

  }

}
