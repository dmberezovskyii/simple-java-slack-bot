package automation.api;

import automation.api.data.MLGenerator;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;

import static automation.api.access.AccessUsersParams.SLACK_AUTH_BOT_ACCESS_TOKEN;
import static automation.api.access.AccessUsersParams.SLACK_AUTH_BOT_TOKEN;
import static automation.api.access.AccessUsersParams.SLACK_AUTH_TOKEN;

public class HandleMessage {
    private SlackSession slackSession;
    private SlackMessagePostedListener messagePostedListener;


    public HandleMessage() {
    }

    private HandleMessage openSlackSession() throws IOException {
        slackSession = SlackSessionFactory
                .createWebSocketSlackSession(SLACK_AUTH_TOKEN);
        slackSession.connect();
        return this;
    }

    public HandleMessage getMessageListener() {
        slackSession.addMessagePostedListener(messagePostedListener);
        return this;
    }


    public SlackMessagePostedListener dropAndPushRoute(RouteActions routeActions, MLGenerator mlGenerator) throws IOException {
        openSlackSession();
        return (event, session) -> {
            String messageContent = event.getMessageContent();
//               SlackUser messageSender = event.getSender();
            SlackChannel channel = session.findChannelByName("droproute");
            if (messageContent.equals("h") || messageContent.equals("k")
                    || messageContent.equals("b") || messageContent.equals("p")
                    || messageContent.equals("s")) {

                try {
                    routeActions.dropRoute(messageContent);
                    session.sendMessage(channel, dropMessageForP(messageContent));
                    routeActions.pushML(messageContent);
                    session.sendMessage(channel, pushMessageForP(messageContent));
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            }


            if (messageContent.equals("h-d") || messageContent.equals("k-d")
                    || messageContent.equals("bulba-d") || messageContent.equals("s-d")) {
                try {
                    routeActions.dropRoute(messageContent);
                    session.sendMessage(channel, dropMessageForP(messageContent));

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            if (messageContent.equals("h-g") || messageContent.equals("k-g")
                    || messageContent.equals("b-g") || messageContent.equals("s-g")) {

                String fileName = messageContent.substring(0, messageContent.length() - 2);
                mlGenerator.getHeaderData(fileName);
                slackSession.sendMessage(channel, getDataMessage(messageContent));
            }
        };
    }

    /*
      TODO  The @Contract annotation has two attributes — value and pure. The value attribute contains clauses describing causal relationship between arguments and the returned value.
     TODO   The pure attribute is intended for methods that do not change the state of their objects, but just return a new value. If its return value is not used, removing its invocation will not affect program state or change the semantics, unless the method call throws an exception (exception is not considered to be a side effect).
    */
    @NotNull
    @Contract(pure = true)
    private String dropMessageForP(String messageContent) {
        return "drop route for: " + messageContent;
    }

    @NotNull
    @Contract(pure = true)
    private String pushMessageForP(String messageContent) {
        return "push route for: " + messageContent;
    }

    @NotNull
    @Contract(pure = true)
    private String getDataMessage(String messageContent) {
        return "get ML for: " + messageContent;
    }

    public String getMessageContent(String messageContent) {
        return messageContent;
    }
}
