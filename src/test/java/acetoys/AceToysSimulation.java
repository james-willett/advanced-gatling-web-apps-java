package acetoys;

import acetoys.pageobjects.Cart;
import acetoys.pageobjects.Category;
import acetoys.pageobjects.Product;
import acetoys.pageobjects.StaticPages;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class AceToysSimulation extends Simulation {

  private static final String DOMAIN = "acetoys.uk";

  private HttpProtocolBuilder httpProtocol = http
    .baseUrl("https://" + DOMAIN)
    .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*"))
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-GB,en;q=0.9");

  private ScenarioBuilder scn = scenario("AceToysSimulation")
    .exec(StaticPages.homepage)
    .pause(2)
    .exec(StaticPages.ourStory)
    .pause(2)
    .exec(StaticPages.getInTouch)
    .pause(2)
    .exec(Category.productListByCategory_AllProducts)
    .pause(2)
    .exec(Category.loadSecondPageOfProducts)
    .pause(2)
    .exec(Category.loadThirdPageOfProducts)
    .pause(2)
    .exec(Product.loadProductDetailsPage_DartBoards)
    .pause(2)
    .exec(Product.addProductToCart_Product19)
    .pause(2)
    .exec(Category.productListByCategory_BabiesToys)
    .pause(2)
    .exec(Product.addProductToCart_Product4)
    .pause(2)
    .exec(Product.addProductToCart_Product5)
    .pause(2)
    .exec(Cart.viewCart)
    .pause(2)
    .exec(
      http("Login User")
        .post("/login")
        .formParam("_csrf", "#{csrfToken}")
        .formParam("username", "user1")
        .formParam("password", "pass")
              .check(css("#_csrf", "content").saveAs("csrfTokenLoggedIn"))
    )
    .pause(2)
    .exec(Cart.increaseQuantityInCart)
    .pause(2)
    .exec(Cart.increaseQuantityInCart)
    .pause(2)
    .exec(Cart.decreaseQuantityInCart)
    .pause(2)
    .exec(Cart.checkout)
    .pause(2)
    .exec(
      http("Logout")
        .post("/logout")
        .formParam("_csrf", "#{csrfTokenLoggedIn}")
    );

  {
	  setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
  }
}
