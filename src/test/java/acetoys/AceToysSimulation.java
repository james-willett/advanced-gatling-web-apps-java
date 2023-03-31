package acetoys;

import java.time.Duration;
import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.jdbc.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

public class AceToysSimulation extends Simulation {

    private static final String DOMAIN = "acetoys.uk";

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://" + DOMAIN)
            .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*"))
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("en-US,en;q=0.9,bg;q=0.8,ru;q=0.7,de;q=0.6,sr;q=0.5,el;q=0.4");

    private ScenarioBuilder scn = scenario("AceToysSimulation")
            .exec(
                    http("Load Home Page")
                            .get("/")
                            .check(status().is(200)) // Gatling does this automatically
                            .check(status().not(404), status().not(405))
                            .check(css("#_csrf", "content").saveAs("csrfToken"))
            )
            .pause(2)
            .exec(
                    http("Load Our Story Page")
                            .get("/our-story")
            )
            .pause(2)
            .exec(
                    http("Load Get In Touch")
                            .get("/get-in-touch")
            )
            .pause(2)
            .exec(
                    http("Load Products List Page - Category: All Products")
                            .get("/category/all")
            )
            .pause(2)
            .exec(
                    http("Load Next Page of Products - Page 1")
                            .get("/category/all?page=1")
            )
            .pause(2)
            .exec(
                    http("Load Next Page of Products - Page 2")
                            .get("/category/all?page=2")
            )
            .pause(2)
            .exec(
                    http("Load Products Details Page - Product: Darts Board")
                            .get("/product/darts-board")
            )
            .pause(2)
            .exec(
                    http("Add Product to Cart - ProductId: 19")
                            .get("/cart/add/19")
            )
            .pause(2)
            .exec(
                    http("Load Products List Page - Category: Babies Toys")
                            .get("/category/babies-toys")
            )
            .pause(2)
            .exec(
                    http("Add Product to Cart - ProductId: 4")
                            .get("/cart/add/4")
            )
            .pause(2)
            .exec(
                    http("Add Product to Cart - ProductId: 4")
                            .get("/cart/add/4")
            )
            .pause(2)
            .exec(
                    http("View Cart")
                            .get("/cart/view")
            )
            .pause(2)
            .exec(
                    http("Login User")
                            .post("/login")
                            .formParam("_csrf", "#{csrfToken}")
                            .formParam("username", "user1")
                            .formParam("password", "pass")
                            .check(css("#_csrf", "content").saveAs("csrfTokenLoggedIn"))
            ).exec(
                    session -> {
                        System.out.println(session);
                        System.out.println("csrfTokenLoggedIn value is: " + session.getString("csrfTokenLoggedIn"));
                        return session;
                    }
            )
            .pause(2)
            .exec(
                    http("Increase Product Quantity in Cart - Product: 19")
                            .get("/cart/add/19?cartPage=true")
            )
            .pause(2)
            .exec(
                    http("Increase Product Quantity in Cart - Product: 19")
                            .get("/cart/add/19?cartPage=true")
            )
            .pause(2)
            .exec(
                    http("Subtract Product Quantity in Cart - Product: 19")
                            .get("/cart/subtract/19")
            )
            .pause(2)
            .exec(
                    http("Checkout")
                            .get("/cart/checkout")
            )
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
