package automation.api.test;

import automation.api.HandleMessage;
import automation.api.RouteActions;
import automation.api.data.MLGenerator;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import java.io.IOException;

import static automation.api.access.AccessUsersParams.SLACK_AUTH_BOT_ACCESS_TOKEN;
import static automation.api.access.AccessUsersParams.SLACK_AUTH_BOT_TOKEN;
import static automation.api.access.AccessUsersParams.SLACK_AUTH_TOKEN;
import static com.jayway.restassured.RestAssured.given;


public class test {

    public static void main(String[] args) throws IOException {

        RouteActions routeActions = new RouteActions();
        HandleMessage handleMessage = new HandleMessage();
        MLGenerator mlGenerator = new MLGenerator();
        SlackSession session = SlackSessionFactory.createWebSocketSlackSession(SLACK_AUTH_TOKEN);
        session.addMessagePostedListener(handleMessage.dropAndPushRoute(routeActions, mlGenerator));
        session.connect();


    }
}