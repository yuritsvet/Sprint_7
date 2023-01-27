import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CourierCreateTest {
    private final File newCourierJson = new File("src/test/resources/newCourier.json");

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание курьера")
    public void courierCreate() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(newCourierJson)
                        .when()
                        .post("/api/v1/courier");
        response.then().body("ok", notNullValue())
                .and()
                .statusCode(201);
    }

    @Test
    @DisplayName("Попытка создания уже существующего курьера")
    public void courierCreateExisting() {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(newCourierJson)
                .when()
                .post("/api/v1/courier");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(newCourierJson)
                        .when()
                        .post("/api/v1/courier");
        response.then().body("message", notNullValue())
                .and()
                .statusCode(409);
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void courierCreateNoLogin() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"password\": \"1234\", \"firstName\": \"asdf\"}")
                        .when()
                        .post("/api/v1/courier");
        response.then().body("message", notNullValue())
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void courierCreateNoPassword() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"nanana\", \"firstName\": \"asdf\"}")
                        .when()
                        .post("/api/v1/courier");
        response.then().body("message", notNullValue())
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создание курьера без имени")
    public void courierCreateNoFirstName() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"login\": \"nanana\", \"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier");
        response.then().body("ok", notNullValue())
                .and()
                .statusCode(201);
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