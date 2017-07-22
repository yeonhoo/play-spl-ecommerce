package service

import javax.inject.Inject

import anorm.SqlParser._
import anorm._

import play.api.db.DBApi
/**
 * Created by yeonhoo on 07/02/2017.
 */

case class User(id: Int, email: String, name: String, password: String)

//        case (email, name, cpf, dateOfBirth, address, password) =>
case class User2(
  id: Int,
  email: String, name: String, cpf: String, dateOfBirth: String, address: String, password: String
)

@javax.inject.Singleton
class UserService @Inject() (dbapi: DBApi) {

  private val db = dbapi.database("default")

  val simple = {
    get[Int]("user.id") ~
      get[String]("user.email") ~
      get[String]("user.name") ~
      get[String]("user.pw") map {
        case id ~ email ~ name ~ pw => User(id, email, name, pw)
      }
  }

  val simpleUser = {
    get[Int]("id") ~
      get[String]("email") ~
      get[String]("name") ~
      get[String]("cpf") ~
      get[String]("date_of_birth") ~
      get[String]("address") ~
      get[String]("password") map {
        case id ~ email ~ name ~ cpf ~ date_of_birth ~ address ~ password => User2(id, email, name, cpf, date_of_birth, address, password)
      }
  }

  def authenticate(email: String, pw: String): Option[User] = db.withConnection { implicit c =>
    SQL("SELECT * FROM user WHERE email = {email} AND pw = {pw}").on('email -> email, 'pw -> pw).as(simple singleOpt)
  }

  def findByEmail(email: String): Option[User] = db.withConnection { implicit c =>
    SQL("SELECT * FROM user WHERE email = {email}").on('email -> email).as(simple singleOpt)
  }

  def changePassword(email: String, newPw: String) = db.withConnection { implicit c =>
    SQL("UPDATE user set pw = {pw} WHERE email = {email}").on(
      'pw -> newPw, 'email -> email
    ).executeUpdate()

  }

  def findByEmail2(email: String): Option[User2] = db.withConnection { implicit c =>
    SQL("select * from user_test where email = {email}").on('email -> email).as(simpleUser singleOpt)
  }

  def insert(email: String, name: String, cpf: String, dateOfBirth: String, address: String, password: String) = db.withConnection { implicit connection =>

    SQL(
      """
        insert into user_test values (
          (select next value for user_test_id_seq),
          {email}, {name}, {cpf}, {dateOfBirth}, {address}, {password}
        )
      """
    ).on(
        'email -> email,
        'name -> name,
        'cpf -> cpf,
        'dateOfBirth -> dateOfBirth,
        'address -> address,
        'password -> password
      ).executeUpdate()
  }

  def findById(id: Long): Option[User2] = db.withConnection { implicit c =>
    SQL("select * from user_test where id = {id}").on('id -> id).as(simpleUser.singleOpt)
  }

  //id ~ email ~ name ~ cpf ~ date_of_birth ~ address ~ password
  def update(id: Long, user: User2) = db.withConnection { implicit c =>

    SQL(
      """
         update user_test
         set email = {email}, cpf = {cpf}, date_of_birth = {date_of_birth}, address = {address}, password = {password}
         where id = {id}
      """
    ).on(
        'id -> id,
        'email -> user.email,
        'name -> user.name,
        'cpf -> user.cpf,
        'date_of_birth -> user.dateOfBirth,
        'address -> user.address,
        'password -> user.password
      ).executeUpdate()
  }

}
