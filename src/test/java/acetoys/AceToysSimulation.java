package acetoys;

import acetoys.pageobjects.*;
import acetoys.session.UserSession;
import acetoys.simulation.TestPopulation;
import acetoys.simulation.TestScenario;
import acetoys.simulation.UserJourney;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class AceToysSimulation extends Simulation {

    private static final String TEST_TYPE = System.getProperty("TEST_TYPE", "INSTANT_USERS");

  private static final String DOMAIN = "acetoys.uk";

  private HttpProtocolBuilder httpProtocol = http
    .baseUrl("https://" + DOMAIN)
    .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*"))
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-GB,en;q=0.9");

  {
      if (TEST_TYPE == "INSTANT_USERS") {
          setUp(TestPopulation.instantUsers).protocols(httpProtocol);
      } else if (TEST_TYPE == "RAMP_USERS") {
          setUp(TestPopulation.rampUsers).protocols(httpProtocol);
      } else if (TEST_TYPE == "COMPLEX_INJECTION") {
          setUp(TestPopulation.complexInjection).protocols(httpProtocol);
      } else if (TEST_TYPE == "CLOSED_MODEL") {
          setUp(TestPopulation.closedModel).protocols(httpProtocol);
      } else {
          setUp(TestPopulation.instantUsers).protocols(httpProtocol);
      }
  }

}
