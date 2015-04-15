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

  val story= {
    get[Long]("id") ~ get[String]("title") ~ get[String]("author") ~ get[String]("text") ~ get[Int]("points")~ get[Date]("data") map{
       case id ~ title ~ author ~ text ~ points ~ data  => Story(id, title, author,text,  points, data)
    }
  }
}
/*

[MatchError: ~(~(~(~(~(Some(1),Some(dasdasd)),None),Some(dasdassd)),Some(0)),Some(2015-04-14)) (of class anorm.$tilde)]
*/