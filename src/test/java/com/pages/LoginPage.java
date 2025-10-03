package com.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.setup.BaseSteps;

public class LoginPage extends BasePage{

	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(id = "signUp-phoneNumber") WebElement signinMobileNo;
	@FindBy(id = "signUpSubmit")  WebElement shortlistPropBtn;
	
	public void loginFunction() {
		try {
			Actions action = new Actions(driver);
			waitUntilWebEleVisible(signinMobileNo);
			signinMobileNo.sendKeys(BaseSteps.prop.getProperty("mobileNo"));
			Thread.sleep(15000);
			action.moveToElement(shortlistPropBtn).click().build().perform();
			Thread.sleep(10000);
//			driver.navigate().refresh();
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
