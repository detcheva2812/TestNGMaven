package Lecture10;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Calculator {

    @DataProvider
    public static Object[][] dataProvider() {
        return new Object[][]{{1, 2}, {3, 4}};
    }

    @Test(dataProvider = "dataProvider", groups = "group1")
    public void sum(int a, int b) {
        int sum = a + b;
        Assert.assertEquals(a + b, sum);
    }

    @Test(dataProvider = "dataProvider", groups = "group2")
    public void subtract(int a, int b) {
        int subtraction = a - b;
        Assert.assertEquals(a - b, subtraction);
    }

    @Test(dataProvider = "dataProvider", groups = "group3")
    public void multiply(int a, int b) {
        int multiplication = a * b;
        Assert.assertEquals(a * b, multiplication);
    }

    @Test(dataProvider = "dataProvider", groups = "group4")
    public void divide(int a, int b) {
        int division = a / b;
        Assert.assertEquals(a / b, division);
    }


    @Test(dataProvider = "dataProvider", groups = "group5")
    public void myTest5(int a, int b) {
        int modulus = a % b;
        Assert.assertEquals(a % b, modulus);
    }

}
