package security

import play.api.mvc._
import service._

trait Secured extends Controller {
  def userService: UserService

  private def username(request: RequestHeader) = request.session.get("email")

  private def onUnauthorized(request: RequestHeader) = Results.Redirect(controllers.features.routes.AuthenticationFeature.login)

  //private def onNotLogged(request: RequestHeader) = Results.Redirect(routes.Application.login)

  /*  def IsLogged(f: => String => Request[AnyContent] => Action[AnyContent]) = Security.Authenticated(username, onNotLogged) { user =>
    (request => f(user)(request))
  }*/

  def IsAuth(f: => EssentialAction) = Security.Authenticated(username, onUnauthorized) { user =>
    f
  }

  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }

  def IsAuthenticatedMultipart(f: => String => Request[play.api.mvc.MultipartFormData[play.api.libs.Files.TemporaryFile]] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    Action(parse.multipartFormData) { request => f(user)(request) }
  }
  // implicit conversion from String to Option[User]
  implicit def emailToUser(email: String): Option[User] = userService.findByEmail(email)
}
