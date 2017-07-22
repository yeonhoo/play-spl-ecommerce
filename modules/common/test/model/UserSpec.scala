package model

/**
 * Created by yeonhoo on 12/05/2017.
 */

import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import service.{ User2, UserService }

class UserSpec extends PlaySpec with GuiceOneAppPerSuite {

  // why am I not using the dependency injection?
  var userService: UserService = app.injector.instanceOf(classOf[UserService])

  "User model" should {

    "be retrieved by email" in {

      val user = userService.findByEmail2("admin@scala.com").get

      user.name must equal("lee")
    }

    "create a new user" in {

      val newUser = userService.insert(email = "lee@scala.com", name = "Lee", cpf = "123.456.789-00",
        dateOfBirth = "010101", address = "Rua Lidio Tiburcio", password = "1234")

      userService.findByEmail2("lee@scala.com").get must have(
        'email("lee@scala.com"),
        'name("Lee"),
        'cpf("123.456.789-00")
      )
    }

    "be retrieved by id" in {

      val user = userService.findById(1).get

      user.name must equal("lee")
    }

    "be updated if needed" in {
      // how can I create a User without providing the id field?
      // should I define a id field as Option[Long] ?
      userService.update(1, User2(
        id = 1,
        email = "updated@scala.com",
        name = "updated",
        cpf = "000.000.000-00",
        dateOfBirth = "0000-00-00",
        address = "Rua Tiburcio Pires",
        password = "1"
      ))

      val user = userService.findById(1).get

      user.email must equal("updated@scala.com")
    }

  }
}
