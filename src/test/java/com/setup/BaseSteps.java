package com.setup;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.parameter.ExcelReader;
import com.parameter.PropertyReader;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseSteps {
	public static WebDriver driver;
	public static ChromeOptions chromeoptions;
	public static EdgeOptions edgeoptions;
	public static FirefoxOptions firefoxoptions;
	public static Properties prop;
	public static ExcelReader excel;
	
	public static WebDriver chromedriver() {
//		WebDriverManager.chromedriver().setup();
		loadConfig();
		chromeoptions = new ChromeOptions();
		chromeoptions.addArguments("--start-maximized");
		chromeoptions.addArguments("incognito");
		driver = new ChromeDriver(chromeoptions);
		
		return driver;
	}
	public static WebDriver edgedriver() {
//		WebDriverManager.edgedriver().setup();
		loadConfig();
		edgeoptions = new EdgeOptions();
		edgeoptions.addArguments("--start-maximized","Incongnito","disable-notifications");
		driver = new EdgeDriver(edgeoptions);
		
		return driver;
	}
	public static WebDriver firefoxdriver() {
//		WebDriverManager.firefoxdriver().setup();
		firefoxoptions.addArguments("--start-maximized","--incongnito","disable-notifications");
		driver = new FirefoxDriver(firefoxoptions);
		
		return driver;
	}

	public static void loadConfig(){
		if(prop == null) {
			prop = PropertyReader.initProperties();
//			System.out.println("Config file loaded..");
		}
		ExcelReader.init("C:\\Users\\prsadana\\eclipse-workspace\\Sprint_Implementation\\src\\test\\resources\\Exceldata\\TestData.xlsx", "Locators");
	}
	
	public static void tearDown(){
//		driver.close();
		if(driver != null) {
			driver.quit();
		}
	}
}

