package service

/**
 * Created by yeonhoo on 11/01/2017.
 */

import javax.inject.Inject

import anorm.SqlParser._
import anorm._

import play.api.db.DBApi

case class Category(id: Option[Long] = None, name: String, description: Option[String], url: String)

@javax.inject.Singleton
class CategoryService @Inject() (dbapi: DBApi) {

  private val db = dbapi.database("default")

  val simple = {
    get[Option[Long]]("category.id") ~
      get[String]("category.name") ~
      get[Option[String]]("category.description") ~
      get[String]("category.url") map {
        case id ~ name ~ description ~ url => Category(id, name, description, url)
      }

  }

  def findByName(urlName: String): Option[Category] = db.withConnection { implicit c =>
    SQL("select * from category where url = {urlName}").on('urlName -> urlName).as(simple singleOpt)
  }

  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Page[Category] = {
    //eu preciso pegar todas categorias, nao posso usar o leftjoin pq ele nao pegaria a categoria que nao estiver associada a nenhum produto

    val offset = pageSize * page
    db.withConnection { implicit connection =>

      val categories = SQL(
        """
      select * from category
      where category.name like {filter}
      order by {orderBy} nulls last
      limit {pageSize} offset {offset}
    """
      ).on(

          'pageSize -> pageSize,
          'offset -> offset,
          'filter -> filter,
          'orderBy -> orderBy
        ).as(simple *)

      val totalRows = SQL(
        """
        select count(*) from category
        where category.name like {filter}
      """
      ).on(
          'filter -> filter
        ).as(scalar[Long].single)

      Page(categories, page, offset, totalRows)
    }
  }

  def options: Seq[(String, String)] = db.withConnection { implicit connection =>
    SQL("select * from company order by name").as(simple *).
      foldLeft[Seq[(String, String)]](Nil) { (cs, c) =>
        c.id.fold(cs) { id => cs :+ (id.toString -> c.name) }
      }
  }

  def all(): List[Category] = db.withConnection { implicit c =>
    SQL("select * from category order by name").as(simple *)
  }

}
