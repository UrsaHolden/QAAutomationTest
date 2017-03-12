package com.ursatester.functions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**********************************************
 * CORE FUNCTIONS LIBRARY
 * SOME DRY ASPECTS COULD BE REFINED FURTHER
 *
 * ********************************************/
public class CoreFunctions {

    WebDriver _driver;
    public CoreFunctions(WebDriver driver){_driver = driver;}

    /**********************************************
     * SearchForComputer
     * FILTER BY EXACT OR SHORT NAME TO RETRIEVE
     * SINGLE OR MULTIPLE ENTRIES
     * ********************************************/
    public void SearchForComputer( String name, String intro, String discon, String company, boolean multi){
        //
        WebElement searchbox = _driver.findElement(By.id("searchbox"));

        searchbox.sendKeys(name);

        _driver.findElement(By.id("searchsubmit")).click();

        String heading = multi ? " computers found" : " computer found";

        Assert.assertTrue("Should say X computers found", CheckPageHeading(heading));

        WebElement firstLink = _driver.findElement(By.xpath(".//*[@id='main']/table/tbody/tr[1]/td[1]/a"));
        Assert.assertTrue("First link name not found",firstLink.getText().contains(name));

        if (!multi) {
            WebElement link = _driver.findElement(By.linkText(name));
            Assert.assertTrue("name of computer not found", link.getText().contains(name));

            if (intro != null)
                Assert.assertTrue("Introduced date not found",
                        _driver.findElement(By.xpath(".//*[@id='main']/table/tbody/tr/td[2]")).getText().contains(intro));

            if (discon != null)
                Assert.assertTrue("Discontinued date not found",
                        _driver.findElement(By.xpath(".//*[@id='main']/table/tbody/tr/td[3]")).getText().contains(discon));

            if (company != null)
                Assert.assertTrue("Company name not found",
                        _driver.findElement(By.xpath(".//*[@id='main']/table/tbody/tr/td[4]")).getText().contains(company));
        }
    }

    /**********************************************
     *
     *
     *
     * ********************************************/
    public void AddNewComputer( String name, String company, String intro, String discon){

        _driver.findElement(By.id("add")).click();

        Assert.assertTrue("Should be Add a computer page",CheckPageHeading("Add a computer"));

        WebElement nameBox = _driver.findElement(By.id("name"));
        nameBox.sendKeys(name);

        if (intro != null) {
            WebElement introduceDate = _driver.findElement(By.id("introduced"));
            introduceDate.sendKeys(intro);
        }
        if(discon != null) {
            WebElement discontinuedDate = _driver.findElement(By.id("discontinued"));
            discontinuedDate.sendKeys(discon);
        }
        if(company != null) {
            Select ddMenu = new Select(_driver.findElement(By.id("company")));
            ddMenu.selectByVisibleText(company);
        }

        WebElement button = _driver.findElement(By.tagName("input"));
        button.submit();

        StringBuilder searchText = new StringBuilder().append("Computer ").append(name).append(" has been created");

        Assert.assertTrue("Should say Done! etc.",
                _driver.findElement(By.className("alert-message")).getText().contains(searchText));

    }

    /**********************************************
     *
     *
     *
     * ********************************************/
    public void OpenComputerDatabaseSite(){

        _driver.navigate().to("http://computer-database.herokuapp.com/computers");

        Assert.assertTrue("title should be Computers database",
                _driver.getTitle().contains("Computers database"));

        Assert.assertTrue("Should say X computers found", CheckPageHeading(" computers found"));
    }

    /**********************************************
     *
     *
     *
     * ********************************************/
    public void UpdateComputerDetails(String name, String newName,
                                      String company, String newCompany,
                                      String intro, String newIntro,
                                      String discon, String newDiscon) throws ParseException {

        String introDateFormat = newIntro;
        String disconDateFormat = newDiscon;
        WebElement link = _driver.findElement(By.linkText(name));
        link.click();

        Assert.assertTrue("Should be on Edit page", CheckPageHeading("Edit computer"));

        WebElement nameBox = _driver.findElement((By.id("name")));
        nameBox.clear();

        if (newName != null)
            nameBox.sendKeys(newName);

        if (newIntro != null) {
            _driver.findElement(By.id("introduced")).sendKeys(newIntro);
            introDateFormat = SearchDateFormat(newIntro);
        }
        if(newDiscon != null) {
            _driver.findElement(By.id("discontinued")).sendKeys(newDiscon);
            disconDateFormat = SearchDateFormat(newDiscon);
        }
        if(newCompany != null) {
            Select ddMenu = new Select(_driver.findElement(By.id("company")));
            ddMenu.selectByVisibleText(newCompany);
        }

        WebElement button = _driver.findElement(By.className("primary"));
        button.submit();

        StringBuilder searchText = new StringBuilder().append("Computer ").append(newName).append(" has been updated");

        Assert.assertTrue("Should say Done! etc.",
                _driver.findElement(By.className("alert-message")).getText().contains(searchText));

        //BELT AND BRACES
        SearchForComputer(newName,introDateFormat,disconDateFormat,newCompany,false);

    }

    /**********************************************
     *
     *
     *
     * ********************************************/
    public void DeleteComputer(String name){
        WebElement link = _driver.findElement(By.linkText(name));
        link.click();

        Assert.assertTrue("Should be on Edit page", CheckPageHeading("Edit computer"));

        WebElement button = _driver.findElement(By.className("danger"));
        button.submit();

        Assert.assertTrue("Should say Done! Computer has been deleted.",
                _driver.findElement(By.className("alert-message")).getText().contains("Computer has been deleted"));

        _driver.findElement(By.id("searchbox")).sendKeys(name);
        _driver.findElement(By.id("searchsubmit")).click();

        Assert.assertTrue("Should say X computers found", CheckPageHeading("No computers found"));

    }

    public void CheckErrorHandling(String introDate, String DisconDate, String pageToCheck){
        String heading = "Edit computer";
        boolean isEdit = true;
        if(pageToCheck.toLowerCase().contains("edit")) {
            WebElement link = _driver.findElement(By.xpath(".//*[@id='main']/table/tbody/tr/td[1]/a"));
            link.click();
        }
        else {
            heading = "Add a computer";
            _driver.findElement(By.id("add")).click();
            isEdit = false;
        }
        Assert.assertTrue("Should be on Edit page", CheckPageHeading(heading));

        WebElement nameBox = _driver.findElement(By.id("name"));


        String name = isEdit ? nameBox.getText() : "Any old name";

        //if(isEdit)
        nameBox.clear();
        _driver.findElement(By.className("primary")).click();
        Assert.assertTrue("Should be on Edit page", CheckPageHeading(heading));
        WebElement nameErrorDiv =  _driver.findElement(By.xpath(".//*[@id='main']/form[1]/fieldset/div[1]"));
        Assert.assertTrue("Name text bar should error",nameErrorDiv.getAttribute("class").contains("error"));

        nameBox = _driver.findElement(By.id("name"));
        nameBox.sendKeys(name);
        WebElement introBox = _driver.findElement(By.id("introduced"));
        introBox.clear();
        introBox.sendKeys(introDate);
        _driver.findElement(By.className("primary")).click();
        Assert.assertTrue("Should be on Edit page", CheckPageHeading(heading));
        WebElement introDateErrorDiv =  _driver.findElement(By.xpath(".//*[@id='main']/form[1]/fieldset/div[2]"));
        Assert.assertTrue("Introduced date text bar should error",introDateErrorDiv.getAttribute("class").contains("error"));
        introBox = _driver.findElement(By.id("introduced"));
        introBox.clear();

        WebElement disconBox = _driver.findElement(By.id("discontinued"));
        disconBox.clear();
        disconBox.sendKeys(DisconDate);
        _driver.findElement(By.className("primary")).click();
        Assert.assertTrue("Should be on Edit page", CheckPageHeading(heading));
        WebElement disconDateErrorDiv =  _driver.findElement(By.xpath(".//*[@id='main']/form[1]/fieldset/div[3]"));
        Assert.assertTrue("Discontinued date text bar should error",disconDateErrorDiv.getAttribute("class").contains("error"));

        _driver.findElement(By.linkText("Cancel")).click();

    }
    /**********************************************
     *
     *
     *
     * ********************************************/
    private boolean CheckPageHeading(String heading){
        return _driver.findElement(By.xpath("//section[contains(@id,'main')]/h1")).getText().contains(heading);
    }

    /**********************************************
     *
     *
     *
     * ********************************************/
    private String SearchDateFormat(String date) throws ParseException {
        Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return new SimpleDateFormat("dd MMM yyyy").format(newDate);
    }
}