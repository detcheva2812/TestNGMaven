package Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class MyFirstSeleniumTest {

    public WebDriver driver;

    @BeforeMethod
    public void setUp(){
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");
        options.addArguments("--window-size=1600x900");
        //стандартно се използва 1920x1080

        WebDriverManager.chromedriver().setup();
        driver= new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        //Можем да използваме и това:
        //driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

    }

    @AfterMethod
    public void tearDown() {
        driver.close();
        //driver.quit is to quit it as a whole, while driver.close is for the current test
    }

    @Test
    public void myFirstTest() throws InterruptedException {
        driver.get("http://training.skillo-bg.com");

        By loginLinkBy = By.id("nav-link-login");
        WebElement loginLink = driver.findElement(loginLinkBy);

        loginLink.click();

        By signInTextBy = By.xpath("//p[text()='Sign in']");
        WebElement signInText = driver.findElement(signInTextBy);
        //WebElement signInText = driver.findElement(By.xpath("//p[text()='Sign in']"));

        Assert.assertTrue(signInText.isDisplayed());

        //Thread.sleep(3000); --> Това се използваше, преди да сме добавили implicitlyWait в BeforeMethod-а, само за да видим какво се случва
    }
}
