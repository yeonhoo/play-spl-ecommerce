import javax.inject.Inject

import play.api.http._
import play.api.mvc.RequestHeader

class VirtualHostRequestHandler @Inject() (
  errorHandler: HttpErrorHandler,
  configuration: HttpConfiguration,
  filters: HttpFilters,
  commonRouter: common.Routes
) extends DefaultHttpRequestHandler(
  commonRouter, errorHandler, configuration, filters
) {

  /*
	* Gets the subdomain: "admin" o "www"
	*/
  private def getSubdomain(request: RequestHeader) = request.domain.replaceFirst("[\\.]?[^\\.]+[\\.][^\\.]+$", "")

  /*
  * Delegates to each router depending on the corresponding subdomain
  */
  override def routeRequest(request: RequestHeader) = getSubdomain(request) match {
    case "admin" => commonRouter.routes.lift(rewriteAssets("common", request))
  }

  /*
	* Rewrite the Assets routes for the root project, accessing to the corresponding lib.
	*/
  private def rewriteAssets(subproject: String, request: RequestHeader): RequestHeader = {
    val pub = s"""/public/(.*)""".r
    val css = s"""/css/(.*)""".r
    val js = s"""/js/(.*)""".r
    val img = s"""/img/(.*)""".r
    request.path match {
      case pub(file) => request.copy(path = s"/lib/$subproject/$file")
      case css(file) => request.copy(path = s"/lib/$subproject/stylesheets/$file")
      case js(file) => request.copy(path = s"/lib/$subproject/javascripts/$file")
      case img(file) => request.copy(path = s"/lib/$subproject/images/$file")
      case _ => request
    }
  }
}