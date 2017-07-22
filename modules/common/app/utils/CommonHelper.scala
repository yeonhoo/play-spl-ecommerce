package utils

import play.api.mvc._
import service._

import javax.inject.{ Inject, Singleton }

import play.api.data.{ Form, _ }
import play.api.data.Forms._
import service.UserService

/**
 * Created by yeonhoo on 03/05/2017.
 */
trait CommonHelper extends Controller {

  def userService: UserService

  val fooForm = Form(
    tuple(
      "email" -> text,
      "password" -> text,
      "remember" -> text
    )
  )

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
        case (email, password) => userService.authenticate(email, password).isDefined
      })
  )

  val changePassForm = Form(
    tuple(
      "currPass" -> nonEmptyText,
      "newPass" -> nonEmptyText,
      "newPassRepeat" -> nonEmptyText
    ) verifying ("new password and the repeated new password are different", fields => fields match {
        case (currPass, newPass, newPassRepeat) => newPass == newPassRepeat
      })
  )

}

import play.api.i18n.{ I18nSupport, MessagesApi }

class FormHelper @Inject() (
    userServiceInj: UserService,
    val messagesApiInj: MessagesApi
) extends CommonHelper with I18nSupport {
  val userService = userServiceInj
  val messagesApi = messagesApiInj

}

object FormHelper {
  val fooForm = Form(
    tuple(
      "email" -> text,
      "password" -> text,
      "remember" -> text
    )
  )

  val signUpForm = Form(
    tuple(
      "name" -> text,
      "email" -> text,
      "cpf" -> text,
      "dateOfBirth" -> text,
      "address" -> text,
      "password" -> text
    )
  )
}

//object CommonHelper extends CommonHelper {
//  import play.api.db.DBApi
//
//  @Inject def userService: UserService
//
//
//
//}