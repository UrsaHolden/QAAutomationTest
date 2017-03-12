package com.ursatester.seleniumtests;

import com.ursatester.functions.CoreFunctions;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ComputersDatabaseRegressionTests {


    /*******************************************************
     * THIS TEST SEARCHES FOR "Nokia" COMPUTERS
     * VERIFYING THAT THE SEARCH RETURNED MULTIPLE ENTRIES
     * AND THE FIRST ITEM CONTAINED THE SEARCHED FOR NAME
     * *****************************************************/
    @Test
    public void SearchForComputerTest(){
        //INITIALIZE THE WEB-DRIVERR
        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //INITIALIZE THE CORE FUNCTIONS
        CoreFunctions siteActions = new CoreFunctions(driver);

        //OPEN THE APPLICATION
        siteActions.OpenComputerDatabaseSite();
        //FILTER BY SHORT NAME
        siteActions.SearchForComputer("Nokia",null,null,null, true);

        //CLOSE BROWSER AND DRIVER
        driver.close();
        driver.quit();
    }

    /**********************************************
     * THIS TEST ADDS A NEW COMPUTER TO THE DB WITH
     * DATE AND TIME APPENDED TO THE NAME
     * VERIFIES IF NEW NAME IS IN THE SUCCESS MESSAGE
     * COULD BE EXTENDED TO SEARCH FOR NEW NAME AS
     * FURTHER VERIFICATION
     * ********************************************/
    @Test
    public void AddNewComputerTest(){
        //CREATE ADD DATA
        Date today = new Date();
        SimpleDateFormat frmt = new SimpleDateFormat("'NewAceAuto Pooter-'yyyyMMddhhmmss");
        String computerName = frmt.format(today);

        //INITIALIZE THE WEB-DRIVERR
        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //INITIALIZE THE CORE FUNCTIONS
        CoreFunctions siteActions = new CoreFunctions(driver);

        //OPEN THE APPLICATION
        siteActions.OpenComputerDatabaseSite();
        siteActions.AddNewComputer(computerName, "IBM", null, "2011-01-13");


        //CLOSE BROWSER AND DRIVER
        driver.close();
        driver.quit();
    }

    /**********************************************
     * THIS TEST ADDS A NEW COMPUTER WITH AN UNIQUE
     * NAME, FINDS THAT NEW ENTRY AND UPDATES THE NAME
     * AND DISCONTINUED DATE.  IT THEN SEARCHES FOR
     * THE RENAMED ENTRY AND CHECKS THAT UPDATES ARE
     * CORRECT
     * ********************************************/
    @Test
    public void UpdateComputerDetailsTest() throws ParseException {
        //CREATE ADD DATA
        Date today = new Date();
        SimpleDateFormat frmt = new SimpleDateFormat("'NewAceAuto Pooter-'yyyyMMddhhmmss");
        String computerName = frmt.format(today);
        //CREATE UPDATE DATA
        String newName = "Very" + computerName;

        //INITIALIZE THE WEB-DRIVERR
        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //INITIALIZE THE CORE FUNCTIONS
        CoreFunctions siteActions = new CoreFunctions(driver);

        //OPEN THE APPLICATION
        siteActions.OpenComputerDatabaseSite();

        //ADD A NEW ENTRY
        siteActions.AddNewComputer(computerName, "IBM", null, "2011-01-13");
        //FIND NEW ENTRY
        siteActions.SearchForComputer(computerName,null,null,null, false);
        //UPDATE NAME AND INTRODUCED DATE AND CHECK UPDATES SUCCESSFUL ON SEARCH
        siteActions.UpdateComputerDetails(computerName, newName, null,null,
                null, "1999-08-14", null, null);

        //CLOSE BROWSER AND DRIVER
        driver.close();
        driver.quit();

    }
    /**********************************************
     * THIS TEST ADDS A NEW COMPUTER WITH AN UNIQUE
     * NAME, SEARCHES FOR THAT NEW ENTRY AND THEN
     * DELETES IT.  IT THEN SEARCHES AGAIN TO MAKE
     * SURE IT HAS BEEN DELETED
     * ********************************************/
    @Test
    public void DeleteComputerTest() throws ParseException {
        //CREATE ADD DATA
        Date today = new Date();
        SimpleDateFormat frmt = new SimpleDateFormat("'NewAceAuto Pooter-'yyyyMMddhhmmss");
        String computerName = frmt.format(today);

        //INITIALIZE THE WEB-DRIVERR
        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //INITIALIZE THE CORE FUNCTIONS
        CoreFunctions siteActions = new CoreFunctions(driver);

        //OPEN THE APPLICATION
        siteActions.OpenComputerDatabaseSite();

        //ADD A NEW COMPUTER
        siteActions.AddNewComputer(computerName, "IBM", null, "2011-01-13");
        //FILTER ON NEW ENTRY
        siteActions.SearchForComputer(computerName,null,null,null, false);
        //DELETE NEW ENTRY AND CHECK ON FILTER
        siteActions.DeleteComputer(computerName);

        //CLOSE BROWSER AND DRIVER
        driver.close();
        driver.quit();

    }

    /**********************************************
     * THIS TEST OPENS AN EXISTING ENTRY FOR EDITING
     * AND THEN VERIFIES THAT EACH FIELD THROWS AN
     * ERROR WHEN INCORRECT DATA IS ADDED
     * THE TEST IS THEN REPEATED FOR A NEW ADDITION
     * ********************************************/
    @Test
    public void CheckErrorHandling(){
        //INITIALIZE THE WEB-DRIVERR
        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //INITIALIZE THE CORE FUNCTIONS
        CoreFunctions siteActions = new CoreFunctions(driver);

        //OPEN THE APPLICATION
        siteActions.OpenComputerDatabaseSite();

        //FILTER BY SHORT NAME
        siteActions.SearchForComputer("Nokia",null,null,null, true);
        //CHECK ERRORS ON EDIT FUNCTION
        siteActions.CheckErrorHandling("01-22-1978","2001.01.01", "Edit");

        //CHECK ERRORS ON ADD FUNCTION
        siteActions.CheckErrorHandling("2001-02-333", "0/2/2015", "Add");

        //CLOSE BROWSER AND DRIVER
        driver.close();
        driver.quit();
    }
}

