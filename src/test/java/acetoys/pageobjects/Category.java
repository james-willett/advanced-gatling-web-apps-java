package acetoys.pageobjects;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import java.util.Map;

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

    public static ChainBuilder cyclePagesOfProducts =
            exec(session -> {
                int currentPageNumber = session.getInt("productsListPageNumber");
                int totalPages = session.getInt("categoryPages");
                boolean morePages = currentPageNumber < totalPages;
                return session.setAll(Map.of(
                        "currentPageNumber", currentPageNumber,
                        "nextPageNumber", (currentPageNumber + 1),
                        "morePages", morePages));
            })
                    .asLongAs("#{morePages}").on(
                            exec(http("Load page #{currentPageNumber} of Products - Category: #{categoryName}")
                                    .get("/category/#{categorySlug}?page=#{currentPageNumber}")
                                    .check(css(".page-item.active").isEL("#{nextPageNumber}")))
                                    .exec(session -> {
                                        int currentPageNumber = session.getInt("currentPageNumber");
                                        int totalPages = session.getInt("categoryPages");
                                        currentPageNumber++;
                                        boolean morePages = currentPageNumber < totalPages;
                                        return session.setAll(Map.of(
                                                "currentPageNumber", currentPageNumber,
                                                "nextPageNumber", (currentPageNumber + 1),
                                                "morePages", morePages));

                                    })
                    );
}
