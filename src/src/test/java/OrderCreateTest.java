import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String color;

    public OrderCreateTest(String firstName, String lastName, String address, String metroStation, String phone, String rentTime, String deliveryDate, String comment, String color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }
    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                //набор данных с color: BLACK
                {"{\"firstName\": \"Hakuna\",",
                        "\"lastName\": \"Matata\",",
                        "\"address\": \"Africa\",",
                        "\"metroStation\": 4,",
                        "\"phone\": \"+79995550011\",",
                        "\"rentTime\":  5,",
                        "\"deliveryDate\": \"2023-04-04\",",
                        "\"comment\": \"съешь еще этих французских булок\",",
                        "\"color\": [\"BLACK\"]}"
                },
                //набор данных с color: GREY
                {"{\"firstName\": \"Hakuna\",",
                        "\"lastName\": \"Matata\",",
                        "\"address\": \"Africa\",",
                        "\"metroStation\": 4,",
                        "\"phone\": \"+79995550011\",",
                        "\"rentTime\":  5,",
                        "\"deliveryDate\": \"2023-04-04\",",
                        "\"comment\": \"съешь еще этих французских булок\",",
                        "\"color\": [\"GREY\"]}"
                },
                //набор данных с color: BLACK, GREY
                {"{\"firstName\": \"Hakuna\",",
                        "\"lastName\": \"Matata\",",
                        "\"address\": \"Africa\",",
                        "\"metroStation\": 4,",
                        "\"phone\": \"+79995550011\",",
                        "\"rentTime\":  5,",
                        "\"deliveryDate\": \"2023-04-04\",",
                        "\"comment\": \"съешь еще этих французских булок\",",
                        "\"color\": [\"BLACK\", \"GREY\"]}"
                },
                //набор данных без цвета
                {"{\"firstName\": \"Hakuna\",",
                        "\"lastName\": \"Matata\",",
                        "\"address\": \"Africa\",",
                        "\"metroStation\": 4,",
                        "\"phone\": \"+79995550011\",",
                        "\"rentTime\":  5,",
                        "\"deliveryDate\": \"2023-04-04\",",
                        "\"comment\": \"съешь еще этих французских булок\"",
                        "}"
                },
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Order create. Test color field")
    @Description("Test with parameterized")
    public void orderCreateTestColorField() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(firstName+lastName+address+metroStation+phone+rentTime+deliveryDate+comment+color)
                        .when()
                        .post("/api/v1/orders");
                response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }
}