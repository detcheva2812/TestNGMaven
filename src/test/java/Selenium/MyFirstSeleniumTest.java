package Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MyFirstSeleniumTest {
    public WebDriver driver;
    public Actions actions;
    public WebDriverWait wait;


    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--window-size=1600x900");
        //стандартно се използва 1920x1080

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        //Можем да използваме и това:
        //driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); //explicit wait
        actions = new Actions(driver); //actions --> няколко actions едно след друго с точки
    }

    @AfterMethod
    public void tearDown() {
        driver.close();
        //driver.quit is to quit it as a whole, while driver.close is for the current test
    }

    @Test
    public void testLandingPage() throws InterruptedException {
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

    @Test
    public void testLoginPage() {
        driver.get("http://training.skillo-bg.com/users/login");

        By usernameBy = By.xpath("//input[@id = 'defaultLoginFormUsername']");
        By passwordBy = By.xpath("//input[@id = 'defaultLoginFormPassword']");
        By signInBtnBy = By.xpath("//button[@id = 'sign-in-button']");

        WebElement username = driver.findElement(usernameBy);
        WebElement password = driver.findElement(passwordBy);
        WebElement signInBtn = driver.findElement(signInBtnBy);

        username.sendKeys("dilianadet");
        //actions.click(username).sendKeys("dilianadet").perform();
        password.sendKeys("123456");
        signInBtn.click();

        By logoutLinkBy = By.xpath("//i[@class = 'fas fa-sign-out-alt fa-lg']");
        WebElement logoutBtn = driver.findElement(logoutLinkBy);

        //wait.until(ExpectedConditions.visibilityOf(driver.findElement(logoutLinkBy))); //explicit wait we can use

        Assert.assertTrue(logoutBtn.isDisplayed());

    }

    @Test
    public void testMobileSearchByMarkaAndModel() {
        String carMarka = "Seat";

        String carModel = "Ibiza";

        driver.get("https://www.mobile.bg/pcgi/mobile.cgi");

        By markaBy = By.xpath("//select[@name='marka']");
        By modelBy = By.xpath("//select[@name='model']");
        By searchby = By.xpath("//input[@name='button2']");
        By toSiteBy = By.xpath("//p[text()='Към сайта']");

        driver.findElement(toSiteBy).click();

        Select markaDropdown = new Select(driver.findElement(markaBy));
        markaDropdown.selectByVisibleText(carMarka);

        Select modelDropdown = new Select(driver.findElement(modelBy));
        modelDropdown.selectByVisibleText(carModel);

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(searchby)));
        driver.findElement(searchby).click();

        List<WebElement> listAdv = driver.findElements(By.xpath("//form[@name='search']//a[@class='mmm']"));

        listAdv.forEach(adv -> {
            Assert.assertTrue(adv.getText().contains(carMarka + " " + carModel));
        });
    }

    @Test
    public void testMobileSearchByRegion() {
        String region = "София";
        String populatedPlace = "гр. София";

        driver.get("https://www.mobile.bg/pcgi/mobile.cgi");

        By regionBy = By.xpath("//select[@name='location']");
        By populatedPlaceBy = By.xpath("//select[@name='locationc']");
        By searchBy = By.xpath("//input[@name='button2']");
        By toSiteBy = By.xpath("//p[text()='Към сайта']");

        driver.findElement(toSiteBy).click();

        Select regionDropdown = new Select(driver.findElement(regionBy));
        regionDropdown.selectByVisibleText(region);

        Select populatedPlaceDropdown = new Select(driver.findElement(populatedPlaceBy));
        populatedPlaceDropdown.selectByVisibleText(populatedPlace);

        driver.findElement(searchBy).click();

        List<WebElement> listAdv = driver.findElements(By.xpath("//td[@style='width:334px;height:50px;padding-left:4px']"));

        listAdv.forEach(adv -> {
            Assert.assertTrue(adv.getText().contains("Регион: " + region + ", " + populatedPlace));
        });

    }

    @Test(invocationCount = 100)
    public void testMobileSortByPrice() {

        String sortByPrice = "Цена";

        driver.get("https://www.mobile.bg/pcgi/mobile.cgi");

        By sortByPriceBy = By.xpath("//select[@class='sw300 selectHeight24']");
        By searchBy = By.xpath("//input[@name='button2']");
        By toSiteBy = By.xpath("//p[text()='Към сайта']");

        driver.findElement(toSiteBy).click();

        Select priceDropdown = new Select(driver.findElement(sortByPriceBy));
        priceDropdown.selectByVisibleText(sortByPrice);

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(searchBy)));
        driver.findElement(searchBy).click();

        List<WebElement> listAdv = driver.findElements(By.xpath("//span[@class='price']"));
        List<String> listTextAdv = new ArrayList<>();
        List<Integer> listPriceInt = new ArrayList<>();

        for (WebElement el : listAdv) {
            String text = el.getText();
            listTextAdv.add(text);
        }

        int priceInt = 0;
        for (String el : listTextAdv) {
            priceInt = Integer.parseInt(el.replaceAll("[^0-9]", ""));
            listPriceInt.add(priceInt);
        }

        boolean isAscending =true;
        int currentPrice = 0;
        for (int i = 0; i < listPriceInt.size(); i++) {
            if(listPriceInt.get(i) < currentPrice){
                isAscending = false;
                currentPrice = listPriceInt.get(i);
            }
        }
        System.out.println(listPriceInt);
        Assert.assertTrue(isAscending = true);

    }
}


//за задача - да се използват няколко динамични модели и марки на коли при теста
//test za region Sofia - selector za tam deto pishe region v obqvite //td[@style='width:334px;height:50px;padding-left:4px']
////select[@class='sw300']