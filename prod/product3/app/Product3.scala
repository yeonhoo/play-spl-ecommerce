package controllers.products

import play.api.mvc._
import javax.inject.{ Inject, Singleton }

import play.api.mvc._
import service._
import utils._
import play.api.data.{ Form, _ }
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import controllers.features._

class Product3 @Inject() (
  userServiceInj: UserService,
  productServiceInj: ProductService,
  categoryService: CategoryService,
  override val messagesApiInj: MessagesApi
)
    extends CommonFeature(userServiceInj, productServiceInj, categoryService, messagesApiInj,
      Seq("StaticHomePage", "Product"))
    with StaticHomePageFeature
    with ProductFeature {

}