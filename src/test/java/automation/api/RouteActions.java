package automation.api;

import automation.api.utils.FileFactory;
import automation.api.db.DbConnector;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sun.istack.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import static automation.api.access.AccessUsersParams.AUTH_TOKEN;
import static automation.api.access.AccessUsersParams.mockarooAPIKey;
import static com.jayway.restassured.RestAssured.config;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.EncoderConfig.encoderConfig;

public class RouteActions {

    private Response response;
    private RequestSpecification requestSpecification;

    public RouteActions() {
        requestSpecification = new RequestSpecBuilder().
                setConfig(config()
                        .encoderConfig(encoderConfig()
                                .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .setContentType(ContentType.JSON)
//                .log(LogDetail.ALL)
                .build();
    }

    @Nullable
    private String getCourier(String name) {
        switch (name) {
            case ("k-d"):
            case ("k"):
                return "35003532-92f1-11e2-a57a-d4ae527baec3";
            case ("h-d"):
            case ("h"):
                return "eb0658f0-e20f-11e1-ba0d-d4ae527baec9";
            case ("b-d"):
            case ("b"):
                return "eb244c8b-a03c-11dd-baea-001d92f78697";
            case ("p-d"):
            case ("p"):
                return "70b0d1f3-c6f3-11e4-8ceb-005056884982";
            case ("s-d"):
            case ("s"):
                return "090b57c5-c249-11e4-8ceb-005056884982";
            default:
                return null;
        }
    }


    @NotNull
    private String generateStringFromResource(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    boolean dropRoute(String name) throws SQLException {
        Connection con = DbConnector
                .getInstance()
                .getConnection();
        CallableStatement cStmt = con.prepareCall("{CALL sp_deleRByPRefAtCurrentDay(?)}");
        cStmt.setString(1, getCourier(name));
        boolean result = cStmt.execute();
        cStmt.close();
        return result;
    }

    protected void pushML(String fileName) throws IOException {

        RestAssured.useRelaxedHTTPSValidation();
        String jsonBody = generateStringFromResource(String.valueOf(FileFactory.getFile(fileName)));
        response = given(requestSpecification)
                .header("Authorization", AUTH_TOKEN)
//                .contentType("application/json")
//                .config(config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .body(jsonBody)
                .post("https://test.io/ml")
                .then()
                .extract().response();
        try {
            JSONObject j = new JSONObject(jsonBody);
            System.out.println("Status code: " + j.getString("number"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

       
    }


}