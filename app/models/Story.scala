package models
import java.util.Date

import play.api.db.DB
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Story (id: Long, title: String,  author: String, text: String, points: Integer, data: Date)

object Story{

  def all(): List[Story] = {
    DB.withConnection { implicit c =>
      SQL("select * from story").as(story *)

    }
  }

  def create(title: String,  author: String, text: String, points: Integer, data: Date) ={
    DB.withConnection { implicit c =>
      SQL("insert into story(title,text,points,data) values ({title},{text}, {points}, {data})").on(
        'title -> title, 'author -> author , 'text -> text, 'points -> points, 'data -> data ).executeUpdate()
    }

  }
  def delete (id: Long) ={
    DB.withConnection { implicit c =>
      SQL("delete from story where id = {id}").on(
        'id -> id).executeUpdate()

    }
  }

  val story= {
    get[Long]("id") ~ get[String]("title") ~ get[String]("author") ~ get[String]("text") ~ get[Int]("points")~ get[Date]("data") map{
       case id ~ title ~ author ~ text ~ points ~ data  => Story(id, title, author,text,  points, data)
    }
  }
}
/*

[MatchError: ~(~(~(~(~(Some(1),Some(dasdasd)),None),Some(dasdassd)),Some(0)),Some(2015-04-14)) (of class anorm.$tilde)]
*/