package acetoys.pageobjects;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class Category {

    private static final FeederBuilder<String> categoryFeeder =
            csv("data/categoryDetails.csv").circular();

    public static ChainBuilder productListByCategory =
            feed(categoryFeeder)
                    .exec(
                    http("Load Products List Page - Category: #{categoryName}")
                            .get("/category/#{categorySlug}")
                            .check(css("#CategoryName").isEL("#{categoryName}"))
            );

    public static ChainBuilder loadSecondPageOfProducts =
            exec(
                    http("Load second page of products")
                            .get("/category/all?page=1")
                            .check(css(".page-item.active").is("2"))
            );

    public static ChainBuilder loadThirdPageOfProducts =
            exec(
                    http("Load third page of products")
                            .get("/category/all?page=2")
                            .check(css(".page-item.active").is("3"))
            );


}
