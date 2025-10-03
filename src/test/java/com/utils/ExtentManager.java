package com.utils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.github.dockerjava.api.model.Driver;
import com.setup.BaseSteps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class ExtentManager {
	public static ExtentReports extent;
	public ExtentSparkReporter sparkReport;
	public ExtentTest test;
	WebDriver driver;
	Properties props;
	
	public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
	
	public void reportGeneration() {
		sparkReport = new ExtentSparkReporter("target/Reports/TestReport.html");
		sparkReport.config().setTheme(Theme.DARK);
		sparkReport.config().setDocumentTitle("Automation Report");
		sparkReport.config().setReportName("Functional Testing");
		extent = new ExtentReports();
		extent.attachReporter(sparkReport);
	}

	
	public void reportCreation(java.lang.reflect.Method method) {
	    Test testAnnotation = method.getAnnotation(Test.class);
	    String testName = testAnnotation.testName();
	    if (testName.isEmpty()) {
	        testName = method.getName(); // fallback to method name
	    }
	    test = extent.createTest("Test Name: " + testName);

	}
	
	public void reportTestCompletion(ITestResult result) throws IOException{
		  String screenShotPath = Screenshots.capture(driver, result.getMethod().getMethodName());
		  String testName = result.getMethod().getConstructorOrMethod().getMethod()
                  .getAnnotation(org.testng.annotations.Test.class).testName();

		  // Fallback to method name if testName is empty
		    if (testName == null || testName.trim().isEmpty()) {
		        testName = result.getMethod().getMethodName();
		    }

		  
		   if(result.getStatus() == ITestResult.SUCCESS) {
			   test.addScreenCaptureFromPath(screenShotPath);
			   test.pass(testName + " Passed");
//			   test.pass("Parameters: " + result.getParameters().toString());
		   }
		   else if(result.getStatus() == ITestResult.FAILURE) {
			   test.addScreenCaptureFromPath(screenShotPath);
			   test.fail(testName +" Failed: " +result.getThrowable().toString());
		   }
		   else if(result.getStatus() == ITestResult.SKIP) {
			   test.addScreenCaptureFromPath(screenShotPath);
			   test.skip(testName + " Skipped: " +result.getThrowable().toString());
		   }
	  }
	  
	
	  public void cleanUp() {
		 BaseSteps.tearDown();
	  }
	  

	  public void reportCompletion() {
		  extent.flush();
	  }
}
