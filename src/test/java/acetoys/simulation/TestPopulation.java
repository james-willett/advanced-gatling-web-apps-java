package acetoys.simulation;

import io.gatling.javaapi.core.PopulationBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;

public class TestPopulation {

    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USERS", "5"));
    private static final Duration RAMP_DURATION = Duration.ofSeconds(Integer.parseInt(System.getProperty("RAMP_DURATION", "10")));

    public static PopulationBuilder instantUsers =
            TestScenario.defaultLoadTest
                    .injectOpen(
                            nothingFor(5),
                            atOnceUsers(USER_COUNT));

    public static PopulationBuilder rampUsers =
            TestScenario.defaultLoadTest
                    .injectOpen(
                            nothingFor(5),
                            rampUsers(USER_COUNT).during(RAMP_DURATION));

    public static PopulationBuilder complexInjection =
            TestScenario.defaultLoadTest
                    .injectOpen(
                            constantUsersPerSec(10).during(20).randomized(),
                            rampUsersPerSec(10).to(20).during(20).randomized()
                    );

    public static PopulationBuilder closedModel =
            TestScenario.highPurchaseLoadTest
                    .injectClosed(
                            constantConcurrentUsers(10).during(20),
                            rampConcurrentUsers(10).to(20).during(20)
                    );
}
