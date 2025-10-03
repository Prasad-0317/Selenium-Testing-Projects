package com.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.setup.BaseSteps;

public class BasePage {
	public WebDriver driver;
	WebDriverWait wait;
	Actions action;
	JavascriptExecutor jsexecutor;
	Robot robot;
	
	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.jsexecutor = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
	}
	
	public void loadURL(String url) {
		driver.get(url);
	}
	
	public void waitUntilWebEleVisible(WebElement element) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public WebElement waitWebEleVisibleLocator(By locator) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public void waitUntilWebEleToBeClickable(WebElement element) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	public void scrolltoView(WebElement element) {
		JavascriptExecutor jsexcutor = (JavascriptExecutor) driver;
		jsexcutor.executeScript("arguments[0].scrollIntoView(true)", element);
	}
	
	public void locateInvisibleEle(By locator) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));

	}
	
	public void waitUntilNewWindow() {
		// Wait until a new window is opened
		  wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		  wait.until(driver -> driver.getWindowHandles().size() > 1);
	}
}
