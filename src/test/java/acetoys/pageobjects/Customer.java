package acetoys.pageobjects;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class Customer {

    private static Iterator<Map<String, Object>> loginFeeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                Random rand = new Random();
                int userId = rand.nextInt(3 - 1 + 1) + 1;

                HashMap<String, Object> hmap = new HashMap<String, Object>();
                hmap.put("userId", "user" + userId);
                hmap.put("password", "pass");
                return hmap;
            }).iterator();

    public static ChainBuilder login =
            feed(loginFeeder)
                    .exec(
            http("Login User")
                    .post("/login")
                    .formParam("_csrf", "#{csrfToken}")
                    .formParam("username", "#{userId}")
                    .formParam("password", "#{password}")
                    .check(css("#_csrf", "content").saveAs("csrfTokenLoggedIn"))
    )
                    .exec(session -> session.set("customerLoggedIn", true));

    public static ChainBuilder logout =
            randomSwitch().on(
                    Choice.withWeight(10, exec(
                            http("Logout")
                                    .post("/logout")
                                    .formParam("_csrf", "#{csrfTokenLoggedIn}")
                                    .check(css("#LoginLink").is("Login"))
                    )));
}
