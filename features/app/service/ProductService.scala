package service

import java.util.Date
import javax.inject.Inject
import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

/**
 * Created by yeonhoo on 12/01/2017.
 */

case class AddItems(
  name: String,
  description: String,
  minValue: BigDecimal,
  category: String,
  pictures: List[String]
)

case class Cart(
  id: Option[Long] = None,
  name: String,
  description: Option[String],
  quantity: Int,
  image: Option[String] = None,
  price: Option[Long] = None
)

case class Product(
  id: Option[Long] = None,
  name: String,
  description: Option[String],
  publish: Option[Date],
  categoryId: Option[Long],
  image: Option[String] = None,
  price: Option[Long] = None
)

// Testing
case class SessionToCart(id: String, description: String)

@javax.inject.Singleton
class ProductService @Inject() (dbapi: DBApi, categoryService: CategoryService) {

  private val db = dbapi.database("default")

  val simple = {
    get[Option[Long]]("product.id") ~
      get[String]("product.name") ~
      get[Option[String]]("product.description") ~
      get[Option[Date]]("product.publish") ~
      get[Option[Long]]("product.category_id") ~
      get[Option[String]]("product.imgkey") map {
        case id ~ name ~ description ~ publish ~ categoryId ~ imgkey =>
          Product(id, name, description, publish, categoryId, imgkey)
      }
  }

  /**
   * Parse a (Product, Category) from a ResultSet
   */
  //
  val withCategory: RowParser[(Product, Option[Category])] = simple ~ (categoryService.simple ?) map {
    case product ~ category => (product, category)
  }

  // -- Queries

  /**
   * Retrieve a product from the id.
   */
  def findById(id: Long): Option[Product] = {
    db.withConnection { implicit connection =>
      SQL("select * from product where id = {id}").on('id -> id).as(simple.singleOpt)
    }
  }

  /*
  def allFromCategory(category: Category): List[Product] = db.withConnection { implicit c =>
    SQL("select * from product where category_id = {catId} ORDER_BY created_date DESC").on('catId -> category.id).as(simple *)
  }
*/

  def allFromCategory(cat: Category, page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Page[(Product, Option[Category])] = {
    val offset = pageSize * page

    db.withConnection { implicit connection =>

      val products = SQL(
        """
          select * from product
          left join category on product.category_id = category.id
          where product.name like {filter} and product.category_id = {catId}
          order by {orderBy} nulls last
          limit {pageSize} offset {offset}
        """
      ).on(
          'pageSize -> pageSize,
          'offset -> offset,
          'filter -> filter,
          'orderBy -> orderBy,
          'catId -> cat.id
        ).as(withCategory *)

      val totalRows = SQL(
        """
          select count(*) from product
          left join category on product.category_id = category.id
          where product.name like {filter}
        """
      ).on(
          'filter -> filter
        ).as(scalar[Long].single)

      Page(products, page, offset, totalRows)
    }
  }

  /**
   * Return page of (Product, Category)
   *
   */
  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Page[(Product, Option[Category])] = {

    val offset = pageSize * page

    db.withConnection { implicit connection =>

      val products = SQL(
        """
          select * from product
          left join category on product.category_id = category.id
          where product.name like {filter}
          order by {orderBy} nulls last
          limit {pageSize} offset {offset}
        """
      ).on(
          'pageSize -> pageSize,
          'offset -> offset,
          'filter -> filter,
          'orderBy -> orderBy
        ).as(withCategory *)

      val totalRows = SQL(
        """
          select count(*) from product
          left join category on product.category_id = category.id
          where product.name like {filter}
        """
      ).on(
          'filter -> filter
        ).as(scalar[Long].single)

      Page(products, page, offset, totalRows)
    }
  }

  def insert(product: Product) = {
    db.withConnection { implicit connection =>
      SQL(
        """
          insert into product values (
            (select next value for product_seq),
            {name}, {description}, {imgkey}, {publish}, {category_id}
          )
        """
      ).on(
          'name -> product.name,
          'description -> product.description,
          'imgkey -> product.image,
          'publish -> product.publish,
          'category_id -> product.categoryId
        ).executeUpdate()
    }
  }

}

