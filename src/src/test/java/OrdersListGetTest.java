import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersListGetTest {
    private final File newCourierJson = new File("src/test/resources/newCourier.json");
    String courierId = "";
    String orderId = "";
    String orderTrack = "";

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    public void courierCreate() {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(newCourierJson)
                .when()
                .post("/api/v1/courier");
    }

    public void courierGetId() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(newCourierJson)
                        .when()
                        .post("/api/v1/courier/login");
        String bodyReq = response.body().asString();
        courierId = bodyReq.substring(6, bodyReq.length() - 1);
    }

    public void orderCreateGetTrack() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"firstName\": \"Hakuna1\",\"lastName\": \"Matata\",\"address\": \"Africa\",\"metroStation\": 4,\"phone\": \"+79995550011\",\"rentTime\": 5,\"deliveryDate\": \"2023-04-04\",\"comment\": \"just comment\"}")
                        .when()
                        .post("/api/v1/orders");
        String bodyReq = response.body().asString();
        orderTrack = bodyReq.substring(9, bodyReq.length() - 1);
    }

    public void getOrderId() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"firstName\": \"Hakuna1\",\"lastName\": \"Matata\",\"address\": \"Africa\",\"metroStation\": 4,\"phone\": \"+79995550011\",\"rentTime\": 5,\"deliveryDate\": \"2023-04-04\",\"comment\": \"just comment\"}")
                        .when()
                        .get("/api/v1/orders/track?t=" + orderTrack);
        String bodyReq = response.body().asString();
        orderId = bodyReq.substring(15, 21);
    }

    public void orderAccept() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders/accept/" + orderId + "?courierId=" + courierId);
    }


    @Test
    @DisplayName("Тест проверки ответа при создании заказа")
    public void orderListGetResponseOrderList() {
        courierCreate();
        courierGetId();
        orderCreateGetTrack();
        getOrderId();
        orderAccept();

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders/?courierId=" + courierId);
        response.then().assertThat().body(notNullValue())
                .and()
                .statusCode(200);
    }

    @After
    public void courierDelete() {
        given()
                .delete("/api/v1/courier/" + courierId);
    }
}