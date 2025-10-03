package com.test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.pages.BasePage;
import com.pages.HomePage;
import com.pages.LoginPage;
import com.pages.PropertyDetails;
import com.pages.SearchPage;
import com.parameter.ExcelReader;
import com.pages.LoginPage;
import com.setup.BaseSteps;
import com.utils.ExtentManager;
import com.utils.Screenshots;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.testng.Assert.ARRAY_MISMATCH_TEMPLATE;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class CommercialTest {
	ExtentManager report = new ExtentManager();
    WebDriver driver;
    BasePage basePage;
    HomePage homePage;
    SearchPage searchPage;
    PropertyDetails propertyDetailsPage;
    LoginPage loginFn;
    
    @DataProvider(name = "FilterDataProvider")
    public Object[][] getFilterData() throws IOException {
        List<Map<String, String>> filterData = ExcelReader.readExcel("C:\\Users\\prsadana\\eclipse-workspace\\Sprint_Implementation\\src\\test\\resources\\Exceldata\\TestData.xlsx", "Filters");
        Object[][] data = new Object[filterData.size()][4];
        for (int i = 0; i < filterData.size(); i++) {
            Map<String, String> rowData = filterData.get(i);
            data[i][0] = i;
            data[i][1] = rowData.get("PropertyType");
            data[i][2] = rowData.get("Furnishing");
            data[i][3] = rowData.get("BuildingType");
        }
        return data;
    }
    
    @BeforeSuite
    public void startReport() {
    	report.reportGeneration();
    }
    
    @BeforeMethod
    @Parameters("browser")
    public void setUp(Method method, @Optional("edge") String browser) {  	
    	if(browser.equalsIgnoreCase("chrome")) {
    		driver = BaseSteps.chromedriver();
    	}
    	else if(browser.equalsIgnoreCase("edge")) {
    		driver = BaseSteps.edgedriver();
    	}
    	else {
    		throw new IllegalArgumentException("Browser not supported: "+ browser);
    	}
    	
    	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    	driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    	
    	basePage = new BasePage(driver);
        homePage = new HomePage(driver);
        searchPage = new SearchPage(driver);
        propertyDetailsPage = new PropertyDetails(driver);
        loginFn = new LoginPage(driver);
        report.setDriver(driver);
        report.reportCreation(method); 
    }
    
  //------------------ TestCase 1 -------------//
    /*
     * Created By: Prasad
     * Reviewed by: 
     * Motive: To validate search functionality with valid data
     */

    
    
    @Test(testName = "Search Functionality Test" ,description = "To validate search functionality",priority = 1, retryAnalyzer = com.listeners.RetryAnalyzer.class)
    public void testSearchProperty() throws IOException {
    	
        homePage.loadURL(BaseSteps.prop.getProperty("url"));
        homePage.searchProperty();
        
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
     
        Assert.assertTrue(homePage.isDisplayed());
    }

  //------------------ TestCase 2 -------------//
    /*
     * Created By: Prasad
     * Reviewed by: 
     * Motive: To validate that data matches as per the filters are applied
     */

    
    @Test(testName = "Filters Functionality Test", description = "To validate filters functionality", dataProvider = "FilterDataProvider", priority = 2, retryAnalyzer = com.listeners.RetryAnalyzer.class)
    public void testFilters(int rowIndex, String propertyType, String furnishing, String buildingType) throws IOException, InterruptedException {
        homePage.loadURL(BaseSteps.prop.getProperty("url"));
        homePage.searchProperty();
        searchPage.applyFiltersFromExcel("C:\\Users\\prsadana\\eclipse-workspace\\Sprint_Implementation\\src\\test\\resources\\Exceldata\\TestData.xlsx", rowIndex);
        report.test.info("Parameter: " + propertyType + ", "+ furnishing+ ", "+ buildingType);
        Assert.assertTrue(searchPage.propertyFiltersAssert(propertyType, furnishing, buildingType), "Filters are not applied.");
    }
    
  //------------------ TestCase 3 -------------//
    /*
     * Created By: Prasad
     * Reviewed by: 
     * Motive: To validate property is being shown under shortlists section after clicking on wishlist
     */

    
    @Test(testName = "ShortList Property Test", description = "To validate shortlist functionality", priority = 3, retryAnalyzer = com.listeners.RetryAnalyzer.class)
    public void propertyShortlistTest(){
    	homePage.loadURL(BaseSteps.prop.getProperty("url"));
        homePage.searchProperty();
        searchPage.applyFilters();
        searchPage.getSpecificProperty();
        String[] results = propertyDetailsPage.propertyShortList();
        Assert.assertEquals( results[0], results[1],"Page titles should match after shortlisting");

    }
    
  //------------------ TestCase 4 -------------//
    /*
     * Created By: Prasad
     * Reviewed by: 
     * Motive: To validate after unshortlist functionality
     */

    
    @Test(testName = "Unshortlist Property Test", description = "To validate unshortlist functionality",priority = 4, retryAnalyzer = com.listeners.RetryAnalyzer.class)
    public void propertyUnShortlistTest() throws InterruptedException {
    	homePage.loadURL(BaseSteps.prop.getProperty("url"));
        homePage.searchProperty();
        searchPage.clickshortListBtn();
        loginFn.loginFunction();
        searchPage.clickshortListBtn();
        
        Assert.assertTrue(searchPage.propertyUnshortList());
    }
    	
  //------------------ TestCase 5 -------------//
    /*
     * Created By: Prasad
     * Reviewed by: 
     * Motive: To validate working of contact button 
     */

    
    @Test(testName = "Contact Button Test" ,priority= 5,description = "To validate contact button functionality", retryAnalyzer = com.listeners.RetryAnalyzer.class)
    public void contactButtonTest() {
    	homePage.loadURL(BaseSteps.prop.getProperty("url"));
        homePage.searchProperty();
        searchPage.getSpecificProperty();
        propertyDetailsPage.checkContactBtn();
        loginFn.loginFunction();
        
        Assert.assertTrue(propertyDetailsPage.takePlanToContact() || propertyDetailsPage.checkContactedBtn());

    }
    
  //------------------ TestCase 6 -------------//
    /*
     * Created By: Prasad
     * Reviewed by: 
     * Motive: To validate working of apply loan button
     */

    
    @Test(testName= "Apply Loan Test" ,priority = 6, description = "To validate apply loan functionality",retryAnalyzer = com.listeners.RetryAnalyzer.class)
    public void applyLoanTest() throws IOException {
    	homePage.loadURL(BaseSteps.prop.getProperty("url"));
        homePage.searchProperty();
        searchPage.getSpecificProperty();
    	propertyDetailsPage.applyLoanBtn();
    	loginFn.loginFunction();
    	Assert.assertTrue(propertyDetailsPage.checkLoanEligVisible());
 
    }
    
  //------------------ TestCase 7 -------------//
    /*
     * Created By: Prasad
     * Reviewed by: 
     * Motive: To validate error message is being shown after clicking on apply loan without entering details 
     */

    
    @Test(testName = "Apply Loan Negative Test" , description = "To validate apply loan  error functionality",priority = 7, retryAnalyzer = com.listeners.RetryAnalyzer.class)
    public void applyLoanTestnegative() throws IOException {
    	homePage.loadURL(BaseSteps.prop.getProperty("url"));
        homePage.searchProperty();
        searchPage.getSpecificProperty();
    	propertyDetailsPage.applyLoanNeg();
    	loginFn.loginFunction();
    	propertyDetailsPage.clickLoanElig();
    	Assert.assertTrue(propertyDetailsPage.checkErrMsgDisplayed());

    }
    
    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        report.reportTestCompletion(result);
    	driver.quit();
    }
    
    
    @AfterSuite
    public void endReport() {
        report.reportCompletion();
    }
}