package com.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.setup.BaseSteps;

public class HomePage extends BasePage{
	
//	Actions action = new Actions(driver);
	
	@FindBy(xpath = "//div[text()='Commercial']") WebElement commercialEle;
	@FindBy(id = "react-select-5-option-0") WebElement location;
	@FindBy(xpath = "//input[contains(@placeholder,'localities')]") WebElement localitySearch;
	@FindBy(className = "nb-select__single-value") WebElement  dropdown;
	@FindBy(id = "react-select-5-option-0") WebElement locationContact;
	@FindBy(xpath = "//span[@title='Thane West']") WebElement inputValSearch;
	@FindBy(xpath = "//h1[contains(@class, 'heading-5') and contains(@class, 'truncate ')]") WebElement heading;
	@FindBy(xpath = "//div[text()='Log in']/parent::div[@class='m-auto py-0.8p']") WebElement loginBtn;
	@FindBy(id = "signUp-phoneNumber") WebElement signinMobileNo;
	@FindBy(id = "signUpSubmit")  WebElement continueBtn;
	
	public HomePage(WebDriver driver) {
		super(driver);
	}
	
	public void searchProperty() {
		
		try {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		waitUntilWebEleToBeClickable(commercialEle);
		commercialEle.click();
		dropdown.click();
		location.click();
		localitySearch.click();
		Thread.sleep(2000);
		localitySearch.sendKeys(BaseSteps.prop.getProperty("locationSearchProp"));
		Thread.sleep(2000);
		
		Robot robot = new Robot();
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyPress(KeyEvent.VK_ENTER);
		Thread.sleep(2000);
		robot.keyPress(KeyEvent.VK_ENTER);
//	
//
		
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isDisplayed() {
		String searchinput = inputValSearch.getText();
		String headingVal = heading.getText();
		return headingVal.contains(searchinput); 
	}
	
	
	public void loginClick() {
		try {
		Actions action = new Actions(driver);
		waitUntilWebEleToBeClickable(loginBtn);
		loginBtn.click();
		signinMobileNo.sendKeys(BaseSteps.prop.getProperty("mobileNo"));
		Thread.sleep(15000);
		action.moveToElement(continueBtn).click().build().perform();
		Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isLogin() {
		return driver.getCurrentUrl().contains("signup-login");
	}
}
