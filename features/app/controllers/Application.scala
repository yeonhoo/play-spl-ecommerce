package controllers.features

import play.api.mvc._
import javax.inject.{ Inject, Singleton }

import play.api.mvc._
import service._
import utils._
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.twirl.api.Html
import security.Secured

@Singleton
class CommonFeature @Inject() (
  userServiceInj: UserService,
  productServiceInj: ProductService,
  categoryService: CategoryService,
  val messagesApiInj: MessagesApi,
  implicit val featureList: Seq[String]
)
    extends Controller with I18nSupport with CommonHelper {

  val userService = userServiceInj
  val productService = productServiceInj
  val messagesApi = messagesApiInj

  def main(body: Option[Html] = None): Result = {
    Ok(views.html.index(body.get))
  }

  def index(body: Option[Html] = None): EssentialAction = Action { implicit request =>

    Ok(views.html.index(body.get))

  }
}
// test commit
trait UserFeature extends CommonFeature {

  // pq nao comita?
  // o problema eh que o view precisa levar a request como parametro pq o view precisa de Messages
  //
  def signUp = Action { implicit request =>
    super.main(Some(
      views.html.sign_up() // mudar
    ))
  }
  //once again, bindFromRequest, how can I
  def addUser = Action { implicit request =>
    FormHelper.signUpForm.bindFromRequest.fold(
      formWithErrors => {
        println(formWithErrors.data)
        BadRequest(
          views.html.index(body = views.html.sign_up())
        )
      }, {
        case (email, name, cpf, dateOfBirth, address, password) =>

          println("salvou?")
          println("commit test")
          println("commit test")
          println("commit test")
          println("commit test")

          userService.insert(email, name, cpf, dateOfBirth, address, password)
          // aqui eh so definir o metodo nesse Trait e colocar o route no route file
          Redirect(routes.UserFeature.signUpSuccess())
      }
    )
  }

  def signUpSuccess = Action { implicit request =>
    super.main(Some(
      views.html.sign_up_success()
    ))
  }

}

trait DynamicHomePageFeature extends CommonFeature {

  def homePage: EssentialAction = {

    val page = 0
    val orderBy = 2
    val filter = ""

    super.index(Some(views.html.dynamic_homepage(
      productService.list(page = page, orderBy = orderBy, filter = ("%" + filter + "%")),
      orderBy, filter
    )))
  }
}

trait StaticHomePageFeature extends CommonFeature {

  def homePage: EssentialAction = {
    super.index(Some(
      views.html.static_homepage()
    ))
  }

  /*
  override abstract def index(body: Option[Html] = None): EssentialAction = {

    //implicit val body = views.html.index()
    super.index(Some(views.html.index()))
  }*/
}

trait CartFeature extends CommonFeature {

  def showCart: EssentialAction = Action { implicit request =>
    println("*** ENTERED 'showCart' ACTION ***")
    println(s"  session data : ${request.session.data}")

    val cartWithoutEmail = request.session.data.filter(_._1 != "email")

    println(s"  cartWithoutEmail data : ${cartWithoutEmail}")

    val cart: List[SessionToCart] = cartWithoutEmail.map {
      case (k, v) =>

        val number = k.filter(_.isDigit)

        SessionToCart(number, v)

    }.toList

    println(s"Cart content : ${cart}")

    Ok(views.html.index(
      body = views.html.cart(cart)
    ))
  }

  def addItemToCart(id: Long) = Action { implicit request =>
    productService.findById(id).map { product =>
      Redirect(routes.ProductFeature.productList(0, 2, ""))
        .withSession(request.session + (product.id.toString -> product.name))
    }.getOrElse(NotFound)
  }

  def removeItemFromCart(id: String) = Action { implicit request =>
    println("*** ENTERED 'removeFromCart' ACTION ***")

    println(s"Cart session before : ${request.session}")
    println(s"Cart content before : ${request.session.data}")

    val notEqual = request.session.data.filter { (cart) =>
      cart._1.filter(_.isDigit) != id

    }
    println(s"Not equal content : ${notEqual}")
    println(s"what is ID : ${id}")

    val newSession = Session(notEqual)
    println(s"New session content : ${newSession}")

    val cartWithoutEmail = newSession.data.filter(_._1 != "email")

    // digit 이 아닌것을 다 필터해서
    val cart: List[SessionToCart] = cartWithoutEmail map { item => SessionToCart(item._1 filter (_.isDigit), item._2) } toList
    //val cart = request.session.data.filter{ (cart) => cart._1 != id}.map { item => SessionToCart(item._1, item._2)}.toList

    println(s"Cart content after : ${cart}")

    Ok(views.html.index(
      body = views.html.cart(cart)
    )).withSession(newSession)
  }

}

trait ProductFeature extends CommonFeature {

  def productList(page: Int, orderBy: Int, filter: String): EssentialAction = {

    super.index(
      Some(views.html.product_list(
        productService.list(page = page, orderBy = orderBy, filter = ("%" + filter + "%")),
        orderBy, filter
      ))
    )

  }

  //deletar o metodo no Common
  def itemDetails(id: Long): EssentialAction = {

    productService.findById(id).map { product =>

      super.index(Some(
        views.html.item_details(product)
      ))

    }.getOrElse(super.index(None))
  }
}

trait AuthenticationFeature extends CommonFeature with Secured {

  override abstract def index(body: Option[Html] = None): EssentialAction = {

    IsAuth { super.index(None) }
  }

  def login: EssentialAction = {
    super.index(Some(
      views.html.login(loginForm)
    ))
  }

  def logout = Action { implicit request =>

    Redirect(controllers.features.routes.AuthenticationFeature.login).withNewSession
  }

  //def authenticate = TODO

  def authenticate: EssentialAction = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Redirect(controllers.features.routes.AuthenticationFeature.login),
      user => Redirect(controllers.features.routes.ProductFeature.productList(0, 2, "")).withSession("email" -> user._1)
    )
  }

  /* def authenticate2 = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.main(body = views.html.login(formWithErrors))),
      user => Redirect(controllers.features.routes.AuthenticationFeature.authenticatedIndex).withSession("email" -> user._1)
    )
  }*/
}

