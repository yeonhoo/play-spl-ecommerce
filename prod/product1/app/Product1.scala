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

class Product1 @Inject() (
  userServiceInj: UserService,
  productServiceInj: ProductService,
  categoryService: CategoryService,
  override val messagesApiInj: MessagesApi
)
    extends CommonFeature(userServiceInj, productServiceInj, categoryService, messagesApiInj,
      Seq("StaticHomePage", "Product", "Cart", "Auth", "User"))
    with StaticHomePageFeature
    with ProductFeature
    with CartFeature
    with AuthenticationFeature
    with UserFeature {

  // HomePageFeature
  override def homePage: EssentialAction = {
    IsAuth {
      super.homePage
    }
  }

  // ProductFeature
  override def productList(page: Int, orderBy: Int, filter: String): EssentialAction = {
    println("Product1 - productList")
    IsAuth {
      super.productList(page, orderBy, filter)
    }
  }

  override def itemDetails(id: Long): EssentialAction = {
    println("Product1 - productList")
    IsAuth {
      super.itemDetails(id)
    }
  }

  override def showCart: EssentialAction = {
    IsAuth {
      super.showCart
    }
  }
}