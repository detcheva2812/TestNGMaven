package Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.plaf.metal.MetalScrollButton;

public class HerocuTestCases {
    public WebDriver driver;
    public Actions actions;
    public WebDriverWait wait;
    public JavascriptExecutor executor;


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
        executor = (JavascriptExecutor) driver;
    }

    @AfterMethod
    public void tearDown() {
        // driver.close();
        driver.quit();
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
        //В тази страница няма закономерност

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

//    @Test
//    public void testDynamicContent() {
//        //unstable because of Herocu bug
//        driver.get("https://the-internet.herokuapp.com/dynamic_content");
//
//        By rowsTextBy = By.cssSelector(".large-10.columns:not(.large-centered)");
//        By rowsImagesBy = By.cssSelector(".large-2.columns:not(.large-centered)>img");
//
//        List<WebElement> listTextElements = driver.findElements(rowsTextBy);
//        List<WebElement> listImagesElements = driver.findElements(rowsImagesBy);
//
//        List<String> listTextsInitial = new ArrayList<>();
//        List<String> listImagesInitial = new ArrayList<>();
//
//        for (WebElement el : listTextElements) {
//            listTextsInitial.add(el.getText());
//        }
//
//        for (WebElement el : listImagesElements) {
//            listImagesInitial.add(el.getAttribute("src"));
//        }
//
//        WebElement clickHere = driver.findElement(By.xpath("//a[@href='/dynamic_content?with_content=static']"));
//
//        clickHere.click();
//
//        listTextElements = driver.findElements(rowsTextBy);
//        listImagesElements = driver.findElements(rowsImagesBy);
//
//        List<String> listTextsAfterClick = new ArrayList<>();
//        List<String> listImagesAfterClick = new ArrayList<>();
//
//        for (WebElement el : listTextElements) {
//            listTextsAfterClick.add(el.getText());
//        }
//
//        for (WebElement el : listImagesElements) {
//            listImagesAfterClick.add(el.getAttribute("src"));
//        }
//
//        Assert.assertTrue(listImagesInitial.get(0).equals(listImagesAfterClick.get(0)));
//        Assert.assertTrue(listImagesInitial.get(1).equals(listImagesAfterClick.get(1)));
//        Assert.assertFalse(listImagesInitial.get(2).equals(listImagesAfterClick.get(2)));
//
//        Assert.assertTrue(listTextsInitial.get(0).equals(listTextsAfterClick.get(0)));
//        Assert.assertTrue(listTextsInitial.get(1).equals(listTextsAfterClick.get(1)));
//        Assert.assertFalse(listTextsInitial.get(2).equals(listTextsAfterClick.get(2)));
//
//    }

    @Test
    public void testDynamicControlsRemoveAdd() {
        driver.get("https://the-internet.herokuapp.com/dynamic_controls");

        By checkBoxBy = By.xpath("//input[@type='checkbox']");
        By removeBtnBy = By.xpath("//button[text()='Remove']");

        WebElement checkBox = driver.findElement(checkBoxBy);
        WebElement removeBtn = driver.findElement(removeBtnBy);

        Assert.assertTrue(checkBox.isDisplayed());

        removeBtn.click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//p[@id='message']"))));

        By addBtnBy = By.xpath("//button[text()= 'Add']");
        WebElement addBtn = driver.findElement(addBtnBy);

        Assert.assertTrue(addBtn.isDisplayed());

        List<WebElement> checkBoxList = driver.findElements(checkBoxBy);

        Assert.assertTrue(checkBoxList.isEmpty());
    }

    @Test
    public void testDynamicControlsEnableDisable() {
        driver.get("https://the-internet.herokuapp.com/dynamic_controls");

        By inputTxtBy = By.xpath("//input[@type='text']");
        WebElement inputTxt = driver.findElement(inputTxtBy);

        By enableBtnBy = By.xpath("//button[text()='Enable']");
        WebElement enableBtn = driver.findElement(enableBtnBy);

        Assert.assertFalse(inputTxt.isEnabled());

        enableBtn.click();

        inputTxt = driver.findElement(inputTxtBy);

        By loadingBy = By.xpath("//div[@id='loading']");
        WebElement loading = driver.findElement(loadingBy);

        Assert.assertTrue(loading.isDisplayed());

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//p[@id='message']"))));

        Assert.assertTrue(inputTxt.isEnabled());

    }

    @Test
    public void testDynamicLoadingHiddenElement() {
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");

        By startBtnBy = By.xpath("//button");
        WebElement startBtn = driver.findElement(startBtnBy);

        startBtn.click();

        By loadingBy = By.xpath("//div[@id='loading']");
        WebElement loading = driver.findElement(loadingBy);

        Assert.assertTrue(loading.isDisplayed());

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h4[text()='Hello World!']"))));

        Assert.assertEquals(loading.getAttribute("style"), "display: none;");
        Assert.assertEquals(driver.findElement(By.xpath("//div[@id='start']")).getAttribute("style"), "display: none;");

    }

    @Test
    public void testDynamicLoadingElementRenderedAfter() {
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/2");

        By startBtnBy = By.xpath("//button");
        WebElement startBtn = driver.findElement(startBtnBy);


        By helloWorldBy = By.xpath("//div[@id='finish']");
        List<WebElement> helloWorldList = driver.findElements(helloWorldBy);

        Assert.assertTrue(helloWorldList.isEmpty());

        startBtn.click();

        By loadingBy = By.xpath("//div[@id='loading']");
        WebElement loading = driver.findElement(loadingBy);

        wait.until(ExpectedConditions.attributeContains(loading, "style", "display: none;"));

        helloWorldList = driver.findElements(helloWorldBy);

        Assert.assertFalse(helloWorldList.isEmpty());
    }

    @Test
    public void testFloatingMenu() {
        driver.get("https://the-internet.herokuapp.com/floating_menu");

        By menuBy = By.xpath("//div[@id='menu']");
        Assert.assertTrue(driver.findElement(menuBy).isDisplayed());

        executor.executeScript("window.scrollBy(0, 3000)");//0 - to the right; 3000 px -> scroll down
        Assert.assertTrue(driver.findElement(menuBy).isDisplayed());

        //actions.sendKeys(Keys.ARROW_DOWN);

        executor.executeScript("window.scrollBy(0, -2000)");//0 - to the right; 3000 px -> scroll down
        Assert.assertTrue(driver.findElement(menuBy).isDisplayed());
    }

    @Test
    public void testHovers() {
        driver.get("https://the-internet.herokuapp.com/hovers");

        WebElement figure1 = driver.findElement(By.xpath("//div[@class='example']/div[1]"));
        WebElement figure2 = driver.findElement(By.xpath("//div[@class='example']/div[2]"));
        WebElement figure3 = driver.findElement(By.xpath("//div[@class='example']/div[3]"));

        WebElement hovered1 = driver.findElement(By.xpath("//a[@href='/users/1']"));
        WebElement hovered2 = driver.findElement(By.xpath("//a[@href='/users/2']"));
        WebElement hovered3 = driver.findElement(By.xpath("//a[@href='/users/3']"));

        actions.moveToElement(figure1).perform();

        Assert.assertTrue(hovered1.isDisplayed());
        Assert.assertFalse(hovered2.isDisplayed());
        Assert.assertFalse(hovered3.isDisplayed());

        actions.moveToElement(figure2).perform();

        Assert.assertFalse(hovered1.isDisplayed());
        Assert.assertTrue(hovered2.isDisplayed());
        Assert.assertFalse(hovered3.isDisplayed());

        actions.moveToElement(figure3).perform();

        Assert.assertFalse(hovered1.isDisplayed());
        Assert.assertFalse(hovered2.isDisplayed());
        Assert.assertTrue(hovered3.isDisplayed());
    }

    @Test
    public void testMultipleWindows() {
        driver.get("https://the-internet.herokuapp.com/windows");

        WebElement clickHere = driver.findElement(By.xpath("//a[@href='/windows/new']"));

        String originalWindow = driver.getWindowHandle();

        clickHere.click();

        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        } //Така суичваме към последния отворен прозорец

        String newWindow = driver.getWindowHandle();

        WebElement newWindowText = driver.findElement(By.xpath("//h3"));

        Assert.assertTrue(newWindowText.isDisplayed());
        Assert.assertEquals(newWindowText.getText(), "New Window");

        driver.switchTo().window(originalWindow);

        Assert.assertTrue(clickHere.isDisplayed());
        newWindowText = driver.findElement(By.xpath("//h3"));
        Assert.assertEquals(newWindowText.getText(), "Opening a new window");

        driver.switchTo().window(newWindow);
        newWindowText = driver.findElement(By.xpath("//h3"));
        Assert.assertEquals(newWindowText.getText(), "New Window");
    }

    @Test
    public void testRedirectLink() {
        driver.get("https://the-internet.herokuapp.com/redirector");

        WebElement redirectLink = driver.findElement(By.xpath("//a[@id='redirect']"));

        String originalUrl = driver.getCurrentUrl();
        Assert.assertEquals(originalUrl, "https://the-internet.herokuapp.com/redirector");

        redirectLink.click();

        String newURL = driver.getCurrentUrl();

        Assert.assertNotEquals(originalUrl, newURL);
        Assert.assertEquals(newURL, "https://the-internet.herokuapp.com/status_codes");
    }
}


