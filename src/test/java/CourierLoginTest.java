import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {
    private final File newCourierJson = new File("src/test/resources/newCourier.json");
    public void CreateCourier() {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(newCourierJson)
                .when()
                .post("/api/v1/courier");
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @Test
    @DisplayName("Авторизация курьера")
    public void courierLogin() {
        CreateCourier();
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"nanana\", \"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Авторазация курьера без логина")
    public void courierLoginNoLogin() {
        CreateCourier();
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"\", \"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", notNullValue())
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Авторазация курьера без пароля")
    public void courierLoginNoPassword() {
        CreateCourier();
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"nanana\", \"password\": \"\"}")
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", notNullValue())
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Попытка авторизации несуществующим курьером")
    public void courierLoginCourierNotExist() {
        CreateCourier();
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"hakuna\", \"password\": \"matata\"}")
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", notNullValue())
                .and()
                .statusCode(404);
    }

    @After
    public void courierDelete() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"nanana\", \"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login");
        String bodyReq = response.body().asString();
        String idCourier = bodyReq.substring(6, bodyReq.length()-1);
        given()
                .delete("/api/v1/courier/" + idCourier);
    }
}