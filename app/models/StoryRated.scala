package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current


case class StoryRated (id: Long, author: String, story : Integer)
object StoryRatedDAO{

  def all(): List[StoryRated] = {
    DB.withConnection { implicit c =>
      SQL("select * from storiesrated").as(storyRated *)

    }
  }

  def create( author: String,  story : Long) ={
    DB.withConnection { implicit c =>
      SQL("insert into storiesrated ( author, story) values ({by}, {story})").on(
        'by -> author , 'story -> story ).executeInsert()
    }
  }

  def delete (id: Long) ={
    DB.withConnection { implicit c =>
      SQL("delete from storiesrated where id = {id}").on(
        'id -> id).executeUpdate()

    }
  }

  val storyRated= {
    get[Long]("id") ~ get[String]("author") ~ get[Int]("story") map{
      case id ~ author ~ story  => StoryRated(id, author,story)
    }
  }
  /*
  def getById(id: Long)={
    val stories =all().filter(story => story.id == id)
    stories.head

  }
  */
  def exists(user: User, story: Story): Boolean ={
    val storiesRated: List[StoryRated] = all()
    storiesRated.exists(x=> x.author == user.username && x.story == story.id)
  }

  def getStoriesRatedOfUser(user: User)={
      all().filter(s => s.author == user.username)
  }



}
