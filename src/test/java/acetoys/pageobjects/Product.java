package acetoys.pageobjects;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class Product {

    private static final FeederBuilder<Object> productFeeder =
            jsonFile("data/productDetails.json").random();

    public static ChainBuilder loadProductDetailsPage =
            feed(productFeeder)
                    .exec(
                    http("Load Products Details Page - Product: #{name}")
                            .get("/product/#{slug}")
            );

    public static ChainBuilder addProductToCart =
            exec(
                    http("Add Product to Cart - Product Name: #{name}")
                            .get("/cart/add/#{id}")
                            .check(substring("You have <span>1</span> products in your Basket"))
            );
}
