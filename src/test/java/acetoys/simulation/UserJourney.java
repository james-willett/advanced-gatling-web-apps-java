package acetoys.simulation;

import acetoys.pageobjects.*;
import io.gatling.javaapi.core.ChainBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;

import static acetoys.session.UserSession.*;

public class UserJourney {

    private static final Duration LOW_PAUSE = Duration.ofMillis(1000);
    private static final Duration HIGH_PAUSE = Duration.ofMillis(3000);

    public static ChainBuilder browseStore =
            exec(initSession)
                    .exec(StaticPages.homepage)
                    .pause(HIGH_PAUSE)
                    .exec(StaticPages.ourStory)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .exec(StaticPages.getInTouch)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .repeat(3).on(
                            exec(Category.productListByCategory)
                                    .pause(LOW_PAUSE, HIGH_PAUSE)
                                    .exec(Category.cyclePagesOfProducts)
                                    .pause(LOW_PAUSE, HIGH_PAUSE)
                                    .exec(Product.loadProductDetailsPage)
                    );

    public static ChainBuilder abandonBasket =
            exec(initSession)
                    .exec(StaticPages.homepage)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .exec(Category.productListByCategory)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .exec(Product.loadProductDetailsPage)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .exec(Product.addProductToCart);

    public static ChainBuilder completePurchase =
            exec(initSession)
                    .exec(StaticPages.homepage)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .exec(Category.productListByCategory)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .exec(Product.loadProductDetailsPage)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .exec(Product.addProductToCart)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .exec(Cart.viewCart)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .exec(Cart.increaseQuantityInCart)
                    .pause(LOW_PAUSE)
                    .exec(Cart.decreaseQuantityInCart)
                    .pause(LOW_PAUSE)
                    .exec(Cart.checkout)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .exec(Customer.logout);

}
