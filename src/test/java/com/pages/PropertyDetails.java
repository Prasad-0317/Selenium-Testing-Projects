package com.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.parameter.ExcelReader;
import com.setup.BaseSteps;

public class PropertyDetails extends BasePage{

	public PropertyDetails(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(xpath = "//span[text()='Shortlist']") WebElement shortlistBtn;
	@FindBy(id = "signUp-phoneNumber") WebElement signinMobileNo;
	@FindBy(xpath = "//div[contains(text(),'OTP here')]") WebElement otp;
	@FindBy(id = "signUpSubmit")  WebElement shortlistPropBtn;
	@FindBy(xpath = "//img[@alt='shortlist']") WebElement shortListMenu;
	@FindBy(xpath = "//div[contains(@class,'nb__MCj8k')]") List<WebElement> shortlistedProp;
	@FindBy(xpath = "//button[text()='Apply Loan']") WebElement applyloan;
	@FindBy(xpath = "//button[contains(text(),'Loan Eligibility')]") WebElement checkLoanEleg;
	@FindBy(className = "nbform-err-msg") List<WebElement> errorMsg;
	
	public String[] propertyShortList() {
		String titleOfPage ="";
		String titleOfPage1="";
		try {
		Actions action = new Actions(driver); 
		titleOfPage = driver.getTitle();
		
		waitUntilWebEleToBeClickable(shortlistBtn);
		shortlistBtn.click();
		waitUntilWebEleVisible(signinMobileNo);
		signinMobileNo.sendKeys(BaseSteps.prop.getProperty("mobileNo"));
		
		Thread.sleep(15000);
//		if we use direct click -> then ElementClickinterceptedException 
		action.moveToElement(shortlistPropBtn).click().build().perform();
		Thread.sleep(10000);
		driver.navigate().refresh();
		
		shortListMenu.click();
		waitUntilWebEleVisible(shortlistedProp.get(0));
		shortlistedProp.get(0).click();
		
		List<String> windowid1 = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(windowid1.get(2));
		titleOfPage1 = driver.getTitle(); 
		}
		catch(InterruptedException e) {
			System.out.println("Error..");
		}
	return new String[]{titleOfPage,titleOfPage1};
}
	
	public void checkContactBtn() {
		WebElement contactOwner = driver.findElement(By.id(ExcelReader.get("contact_id")));
		waitUntilWebEleToBeClickable(contactOwner);
		contactOwner.click();
	}
	public void applyLoanBtn() {
		waitUntilWebEleVisible(applyloan);
		applyloan.click();
	}
	
	public boolean checkLoanEligVisible() {
		return checkLoanEleg.isDisplayed();
	}
	public void applyLoanNeg() {
		waitUntilWebEleVisible(applyloan);
		applyloan.click();
	}
	public void clickLoanElig() {
		waitUntilWebEleToBeClickable(checkLoanEleg);
		checkLoanEleg.click();
	}
	public boolean checkErrMsgDisplayed(){
		return !errorMsg.isEmpty();
	}
	
	public boolean checkContacted() {
		WebElement feedback =  waitWebEleVisibleLocator(By.id(ExcelReader.get("feedback_id")));
		return feedback.isDisplayed();
	}
	public boolean takePlanToContact() {
		WebElement popup = driver.findElement(By.className(ExcelReader.get("popup_class")));
		waitUntilWebEleVisible(popup);
		return popup.isDisplayed();
	}
	
	public boolean checkDisplayPopup() {
		WebElement model = driver.findElement(By.className(" modal-body "));
		waitUntilWebEleVisible(model);
		return model.isDisplayed();
	}
	
	public boolean checkContactedBtn() {
		WebElement contactbtn =  waitWebEleVisibleLocator(By.id("getOwnerDetails"));
		return !contactbtn.isEnabled();
	}
}
