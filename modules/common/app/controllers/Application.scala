package controllers.common

import javax.inject.{ Inject, Singleton }

import play.api.mvc._
import service._
import utils._
import play.api.data.{ Form, _ }
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.twirl.api.Html

@Singleton
class Application @Inject() (
    userServiceInj: UserService,
    productServiceInj: ProductService,
    categoryService: CategoryService,
    val messagesApiInj: MessagesApi
) extends Controller with I18nSupport with CommonHelper {

  val userService = userServiceInj
  val productService = productServiceInj
  val messagesApi = messagesApiInj

  //def commonDefaultIndex = Action { Ok { "default index" } }

  def staticList = Action { implicit request =>
    Ok(views.html.common.index(
      body = views.html.common.static_product_list()
    ))
  }

  /*  def itemDetails(id: Long) = Action { implicit request =>
    productService.findById(id).map { product =>
      Ok(views.html.common.index(
        body = views.html.common.item_details(product)
      ))
    }.getOrElse(NotFound)
  }*/

  /*  def signUpFormSave = Action { implicit request =>
    FormHelper.signUpForm.bindFromRequest.fold(
      formWithErrors => {
        println(formWithErrors.data)
        BadRequest(views.html.common.index(body = views.html.common.signupform()))
      }, {
        case (email, name, cpf, dateOfBirth, address, password) =>

          println("salvou?")

          userService.insert(email, name, cpf, dateOfBirth, address, password)

          Redirect(routes.Application.signUpSuccess())
      }
    )
  }

  def signUpSuccess = Action { implicit request =>
    Ok(views.html.common.index(body = views.html.common.sign_up_success()))
  }*/

  /** ***************************************************************************************************/
  /** ****************************Dynamic Page ***********************************************************/

  //  def loginToDynamicPage = Action { implicit request =>
  //    Ok(views.html.common.index(body = views.html.common.loginToDynamicPage(loginForm)))
  //  }
  //

  /*  def authenticateToDynamicPage = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.common.index(body = views.html.common.login(formWithErrors))),
      user => Redirect(routes.Application.toDynamicPage).withSession("email" -> user._1)
    )
  }*/

  //def toDynamicPage = IsAuthenticated { username => implicit request => dynamicPageHome(username) }

  def dynamicPageHome(user: Option[User], changePassForm: Form[(String, String, String)] = changePassForm)(implicit request: Request[AnyContent]) =
    user.map { u =>

      //Redirect(routes.Application.productList())
      Ok(views.html.common.index(body = views.html.common.dynamic_index(), user = Some(u)))
    }.getOrElse(
      Forbidden
    )

  /** ***************************************************************************************************/

  /*
  def authenticateToStaticPage = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.common.index(body = views.html.common.login(formWithErrors))),
      user => Redirect(routes.Application.toStaticPage).withSession("email" -> user._1)
    )
  }

  def toStaticPage = IsAuthenticated { username => implicit request => staticPageHome(username) }

  def staticPageHome(user: Option[User], changePassForm: Form[(String, String, String)] = changePassForm)(implicit request: Request[AnyContent]) =
    user.map { u =>

      //Redirect(routes.Application.productList())
      Ok(views.html.common.index(body = views.html.common.static_index(), user = Some(u)))
    }.getOrElse(
      Forbidden
    )
*/

  /** ***************************************************************************************************/

  /*
  def login = Action { implicit request =>
    Ok(views.html.common.index(body = views.html.common.login(loginForm)))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.common.index(body = views.html.common.login(formWithErrors))),
      user => Redirect(routes.Application.authenticatedIndex).withSession("email" -> user._1)
    )
  }

  // two new methods
  def authenticatedIndex = IsAuthenticated { username => implicit request => index(username) }
*/
  //implicit val body = views.html.common.static_index()
  // multiple overloaded alternatives of method index define default arguments.

  /*
    def index(user: Option[User], changePassForm: Form[(String, String, String)] = changePassForm)(implicit request: Request[AnyContent]) =
    user.map { u =>

      //Redirect(routes.Application.productList())
      Ok {
        "Common page"
      }
      //Ok(views.html.common.index(body = views.html.common.change_pw(changePassForm), user = Some(u)))
    }.getOrElse(
      Forbidden
    )
*/

  // an action that should be wrapped by a authentication and this action should be a productList

  /*  def productList(page: Int, orderBy: Int, filter: String): EssentialAction = Action { implicit request =>
    println("*** ENTERED 'productList' ACTION ***")

    Ok(views.html.common.index(body = views.html.common.list_product(
      productService.list(page = page, orderBy = orderBy, filter = ("%" + filter + "%")),
      orderBy, filter
    )))
  }*/

  def categoryList(page: Int, orderBy: Int, filter: String) = TODO

  /*def categoryList(page: Int, orderBy: Int, filter: String) = Action { implicit request =>

    Ok(views.html.common.index(body = views.html.common.list_category(
      categoryService.list(page = page, orderBy = orderBy, filter = ("%" + filter + "%")),
      orderBy, filter
    )))
  }*/

  def status = Action {
    Ok("yay")
  }

  def changePass = TODO

  /*  def formTest = Action { implicit request =>
    Ok(views.html.common.index(body = views.html.common.signupform()))
    //Ok(views.html.common.signupform())
  }*/

}