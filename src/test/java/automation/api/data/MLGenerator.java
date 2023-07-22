package automation.api.data;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;

public class MLGenerator {
    private Response response;
    private final String BASE_URL = "https://my.api.mockaroo.com";
    private final String mockAuthKey = "";

    public MLGenerator() {

    }

    @Nullable
    private String getAPI(String apiName) {
        switch (apiName) {
            case "k":
                return "/k_object";
            case "h":
                return "/h_object";
            case "b":
                return "/b_object";
            case "s":
                return "/s_object";
            case "p":
                return "/p_object";
            default:
                return null;
        }
    }

    public void getHeaderData(String fName) {
        System.out.println(BASE_URL + getAPI(fName) + mockAuthKey);

        response = given()
                .when()
                .contentType(ContentType.JSON)
                .get(BASE_URL + getAPI(fName) + mockAuthKey)
                .then().assertThat().statusCode(200)
                .and().extract().response();
        System.out.println(response.asString());
        File file = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\" + fName + ".json");
//        System.out.println(file);
//        System.out.println(file.getAbsolutePath());
        if (file.exists()) {
            file.delete();
        }
        appendStrToJSONFile(String.valueOf(file), response.asString());
    }

    private void appendStrToJSONFile(String fName,
                                     String responseStr) {

        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(fName, true));
            out.write(responseStr);
            out.close();
        } catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }
}
