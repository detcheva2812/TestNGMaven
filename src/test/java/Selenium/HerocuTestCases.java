package Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Alert;
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

public class HerocuTestCases {
    public WebDriver driver;
    public Actions actions;
    public WebDriverWait wait;


    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");
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
    public void testAddRemoveElements() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");

        By listElements = By.xpath("//div[@id='elements']/button");
        List<WebElement> listDeletes = driver.findElements(listElements);
        Assert.assertEquals(listDeletes.size(), 0);

        WebElement addElementBtn = driver.findElement(By.xpath("//div[@class='example']/button"));
        addElementBtn.click();
        addElementBtn.click();

        listDeletes = driver.findElements(listElements);
        Assert.assertEquals(listDeletes.size(), 2);

        listDeletes.get(0).click();

        listDeletes = driver.findElements(listElements);
        Assert.assertEquals(listDeletes.size(), 1);
    }

    @Test
    public void testBasicAuth() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/basic_auth");

        By baseTxtAuth = By.xpath("//p");
        Assert.assertEquals(driver.findElement(baseTxtAuth).getText(), "Congratulations! You must have the proper credentials.");
    }

    @Test
    public void testCheckBoxes() {
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        By checkboxes = By.xpath("//form/input");
        WebElement checkboxOne = driver.findElements(checkboxes).get(0);
        WebElement checkboxTwo = driver.findElements(checkboxes).get(1);

        Assert.assertFalse(checkboxOne.isSelected());
        Assert.assertTrue(checkboxTwo.isSelected());

        checkboxOne.click();
        checkboxTwo.click();

        Assert.assertTrue(checkboxOne.isSelected());
        Assert.assertFalse(checkboxTwo.isSelected());
    }

    @Test
    public void testContextMenu() {
        driver.get("https://the-internet.herokuapp.com/context_menu");

        By hotSpotBy = By.xpath("//div[@id='hot-spot']");
        WebElement hotSpot = driver.findElement(hotSpotBy);

        actions.contextClick(hotSpot).perform();

        Alert alert = driver.switchTo().alert();

        Assert.assertEquals(alert.getText(), "You selected a context menu");
        alert.accept();

        driver.switchTo().defaultContent();
    }

    @Test
    public void testDragAndDrop() {
        driver.get("https://jqueryui.com/droppable/");

        WebElement iFrame = driver.findElement(By.xpath("//iframe"));

        driver.switchTo().frame(iFrame);

        By elementABy = By.xpath("//div[@id='draggable']");
        By elementBBy = By.xpath("//div[@id='droppable']");
        WebElement elementA = driver.findElement(elementABy);
        WebElement elementB = driver.findElement(elementBBy);

        WebElement dropTxt = driver.findElement(By.xpath("//div[@id='droppable']/p"));

        Assert.assertEquals(dropTxt.getText(), "Drop here");

        actions.dragAndDrop(elementA, elementB).perform();

        dropTxt = driver.findElement(By.xpath("//div[@id='droppable']/p"));

        Assert.assertEquals(dropTxt.getText(), "Dropped!");

    }

    @Test(invocationCount = 10)
    public void testDisappearingElements() {

        driver.get("https://the-internet.herokuapp.com/disappearing_elements");

        By elementsBy = By.xpath("//div[@class='example']//a");
        List<WebElement> listElements = driver.findElements(elementsBy);

        if (listElements.size() == 5) {
            Assert.assertEquals(listElements.get(0).getText(), "Home");
            Assert.assertEquals(listElements.get(1).getText(), "About");
            Assert.assertEquals(listElements.get(2).getText(), "Contact Us");
            Assert.assertEquals(listElements.get(3).getText(), "Portfolio");
            Assert.assertEquals(listElements.get(4).getText(), "Gallery");
        } else {
            Assert.assertTrue(listElements.size() == 4);
            Assert.assertEquals(listElements.get(0).getText(), "Home");
            Assert.assertEquals(listElements.get(1).getText(), "About");
            Assert.assertEquals(listElements.get(2).getText(), "Contact Us");
            Assert.assertEquals(listElements.get(3).getText(), "Portfolio");

            driver.navigate().refresh();

            listElements = driver.findElements(elementsBy);

            if (listElements.size() == 5) {
                Assert.assertEquals(listElements.get(0).getText(), "Home");
                Assert.assertEquals(listElements.get(1).getText(), "About");
                Assert.assertEquals(listElements.get(2).getText(), "Contact Us");
                Assert.assertEquals(listElements.get(3).getText(), "Portfolio");
                Assert.assertEquals(listElements.get(4).getText(), "Gallery");
            } else {
                Assert.assertTrue(listElements.size() == 4);
                Assert.assertEquals(listElements.get(0).getText(), "Home");
                Assert.assertEquals(listElements.get(1).getText(), "About");
                Assert.assertEquals(listElements.get(2).getText(), "Contact Us");
                Assert.assertEquals(listElements.get(3).getText(), "Portfolio");
            }
        }
    }

    //Не разбирам закономерността кога изчезва и кога се появява Gallery, за да успея да напиша стабилен тест, изглежда, че не е на всяко лоудване


    @Test
    public void testChallengingDOM() {
        driver.get("https://the-internet.herokuapp.com/challenging_dom");

        By listButtonsBy = By.xpath("//div[@class = 'large-2 columns']/a");
        List<WebElement> listButtons = driver.findElements(listButtonsBy);
        Assert.assertFalse(listButtons.isEmpty());

        By canvasBy = By.xpath("//canvas");
        WebElement canvas = driver.findElement(canvasBy);
        Assert.assertTrue(canvas.isDisplayed());

        By tableBy = By.xpath("//table");
        WebElement table = driver.findElement(tableBy);
        Assert.assertTrue(table.isDisplayed());

        By listEditDeleteLinksBy = By.xpath("//table//td/a");
        List<WebElement> listEditDeleteLinks = driver.findElements(listEditDeleteLinksBy);
        Assert.assertFalse(listEditDeleteLinks.isEmpty());
    }

    @Test
    public void testDropdown() {
        driver.get("https://the-internet.herokuapp.com/dropdown");

        String option1 = "Option 1";
        String option2 = "Option 2";

        By dropdownBy = By.xpath("//select");
        WebElement dropdown = driver.findElement(dropdownBy);

        Select optionsDropdown = new Select(driver.findElement(dropdownBy));
        optionsDropdown.selectByVisibleText(option1);

        WebElement optionOne = driver.findElement(By.xpath("//option[@value = '1']"));
        Assert.assertTrue(optionOne.isSelected());

        optionsDropdown.selectByVisibleText(option2);

        WebElement optionTwo = driver.findElement(By.xpath("//option[@value = '2']"));
        Assert.assertTrue(optionTwo.isSelected());
    }
}


