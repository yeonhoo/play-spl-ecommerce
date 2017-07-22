package model

import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import service.{ Product, ProductService }

class ProductSpec extends PlaySpec with GuiceOneAppPerSuite {

  val productService: ProductService = app.injector.instanceOf(classOf[ProductService])

  "Product model" should {

    "be retrieved by id" in {

      val product = productService.findById(1).get

      product.name must equal("iPhone 4S")
    }
  }
}
