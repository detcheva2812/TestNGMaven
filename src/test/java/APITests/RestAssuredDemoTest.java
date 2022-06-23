package APITests;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;


public class RestAssuredDemoTest {

    @BeforeTest
    public void setUp() {
        baseURI = "http://training.skillo-bg.com:3100";
    }

    @Test
    public void testLogin() {

        LoginDto loginDto = new LoginDto();

        loginDto.setUsernameOrEmail("dilianat");
        loginDto.setPassword("123456");

        Response response = given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(loginDto)
                .when()
                .post("/users/login")
                .then()
                .log()
                .all()
                .extract()
                .response();

        int statusCode = response.statusCode();

        Assert.assertEquals(statusCode, HttpStatus.SC_CREATED);
    }
}
