package com.nopCommerce.Group;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class NopCommerceForEachAssertPractice extends Utils
{
    LoadProps loadProps = new LoadProps();

    @BeforeMethod
    public void openBrowser()
    {
        System.setProperty("webdriver.chrome.driver", "src\\main\\Resources\\BrowserDrivers\\chromedriver.exe");
        //open the browser
        driver = new ChromeDriver();
        //maximise the window
        driver.manage().window().maximize();
        //set the implicit wait time on driver level
        implicitWaitTime(10);
        //open the website
        getUrl("Url");
    }
    @AfterMethod
    public void tearDown(ITestResult result)
    {
        if(ITestResult.FAILURE==result.getStatus())
        {
            captureScreenShot(driver, result.getName());
        }
        driver.quit();
    }
    @Test
    public void compareTwoProductsFromHomepageAndClearItFromComparePage()
    {
        SoftAssert softAssert1 = new SoftAssert();
        //click on first product to compare
        ////input[@type='button'][@value='Add to cart'][@onclick='return AjaxCart.addproducttocart_catalog("/addproducttocart/catalog/1/1/1"),!1']
        clickOnElement(By.xpath("//div[1]/div/div[2]/div[3]/div[2]/input[2]"));
        //check the green line on top of the page to make sure product has been added
        Assert.assertEquals((driver.findElement(By.xpath("//p[@class='content']")).getText()),"The product has been added to your product comparison");
        //explicit wait for invisible the green line
        waitForAlertInvisible((By.id("bar-notification")), 5);
        //click on second product to compare
        clickOnElement(By.xpath("//div[2]/div/div[2]/div[3]/div[2]/input[2]"));
        //check the green line on top of the page to make sure product has been added
        Assert.assertEquals((driver.findElement(By.xpath("//p[@class='content']")).getText()),"The product has been added to your product comparison");
        //click on compare product in green line
        clickOnElement(By.xpath("//a[contains(text(),'product comparison')]"));
        //assert the both added items is displayed in page
        WebElement firstItem = driver.findElement(By.xpath("//a[contains(text(),'Apple MacBook Pro 13-inch')]"));
        Assert.assertTrue(firstItem.isDisplayed());
        WebElement secondItem = driver.findElement(By.xpath("//a[contains(text(),'Build your own computer')]"));
        Assert.assertTrue(secondItem.isDisplayed());
        //screenshot method
        captureScreenShot(driver, "Check Both Items Are in Basket");
        //click on clear button
        clickOnElement(By.xpath("//a[@class='clear-list']"));
        //make sure both products are cleared from the page
        assertMethod(By.xpath("//div[@class='no-data']"),"You have no items to compare.");
        //softAssert will keep checking all the asserts even though one of them is failed
        softAssert1.assertAll();
    }
    @Test
    public void userCanAddCommentForOnlineShoppingOnHomePage()
    {
        SoftAssert softAssert2 = new SoftAssert();
        //click on 'Details'to leave the review
        clickOnElement(By.xpath("//div[@class='buttons']//a[@href='/new-online-store-is-open']"));
        //assert to check we are on right page
        assertMethod(By.xpath("//h1[contains(text(),'New online store is open!')]"),"New online store is open!");
        //write title of the comment
        enterText(By.id("AddNewComment_CommentTitle"),"nop commerce");
        //write comments
        enterText(By.id("AddNewComment_CommentText"),"wonderful website for shopping");
        //click on New Comment
        clickOnElement(By.xpath("//input[@value='New comment']"));
        //assert to check comment has been successfully submitted
        assertMethod(By.xpath("//div[@class='result']"),"News comment is successfully added.");
        //assert to check written comment is posted
        assertMethod(By.xpath("//p[contains(text(),'website for shopping')]"),"wonderful website for shopping");
        //to check comment has been post in the bottom of the page
        List<WebElement> commentList = driver.findElements(By.xpath("//p[contains(text(),'website for shopping')]"));
        //it will obtain last comment
        WebElement last_Comment = commentList.get(commentList.size()-1);
        //it will get text of last comment
        String my_Last_Comment = last_Comment.getText();
        System.out.println("wonderful website for shopping");
        System.out.println(my_Last_Comment);
        //compare the actual with expected
        softAssert2.assertEquals(my_Last_Comment, "wonderful website for shopping");

        softAssert2.assertAll();
    }
    @Test
    public void userShouldAbleToSearchDesiredProductFromSearchStoreBar()
    {
        SoftAssert softAssert3 = new SoftAssert();
        //enter the product name in search store
        enterText(By.xpath("//input[@name='q']"),loadProps.getProperty("SearchStore"));
        //click on search bar
        clickOnElement(By.xpath("//input[@type='submit']"));
        //it will give list of items which includes Nike word in description
        List<WebElement> products = driver.findElements(By.xpath("//div[@class='item-grid']//a[contains(text(),'Nike')]"));
        //will give numbers of products
        System.out.println(products.size());
        int count=0;
        for (WebElement searchProduct : products)
        {
            if (searchProduct.getText().contains(loadProps.getProperty("SearchStore")))
            {
                count++;
                System.out.println(searchProduct.getText());
                //assert of all products have product(e.g.: nike) name in it.
                Assert.assertTrue((searchProduct.getText()).contains(loadProps.getProperty("SearchStore")));
            }
            else
            {
                System.out.println("Not a described Product "+ searchProduct.getText());
            }
        }
        //total nike products
        System.out.println(count);
        //assert will compare the result of total nike products with products numbers with products.size()
        Assert.assertEquals(products.size(),count);
        softAssert3.assertAll();
    }
}