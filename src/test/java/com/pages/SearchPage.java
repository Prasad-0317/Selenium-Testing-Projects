package com.pages;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.parameter.ExcelReader;

public class SearchPage extends BasePage{
	
	Actions actions = new Actions(driver);
	
	public SearchPage(WebDriver driver) {
		super(driver);
		this.action = new Actions(driver);
	}
	
	@FindBy(id = "propertyTypeoffice_space") WebElement propTypeOffice;
	@FindBy(className = "rc-slider-handle") List<WebElement> sliderHandles;
	@FindBy(css = "input[value='FULLY_FURNISHED']") WebElement furnishing;
	@FindBy(xpath = "//span[text()='Skip']") WebElement skipButton;
	@FindBy(xpath = "//div[text()='Got it']") WebElement gotitBtn;
	@FindBy(css = "input[value='SHOWROOM']") WebElement showroom;
	@FindBy(id="article_0") WebElement propertyLink;
	@FindBy(xpath = "//img[@alt='shortlist']") WebElement shortListBtn;
	@FindBy(className = "w-1/2") List<WebElement> shortListedProp;
	@FindBy(id = "shortlistProperty") WebElement unShortListBtn;
	
	public void dragSlider(WebElement sliderHandle, int xOffset) {
	    actions.dragAndDropBy(sliderHandle, xOffset, 0).perform();
	}

	public void applyFiltersFromExcel(String excelFilePath, int rowIndex) {
        // Read filter data from Excel
        List<Map<String, String>> filterData;
		try {
			filterData = ExcelReader.readExcel(excelFilePath, "Filters");
		
        if (rowIndex >= filterData.size()) {
            throw new IllegalArgumentException("Row index " + rowIndex + " exceeds available data in Excel.");
        }
        Map<String, String> rowData = filterData.get(rowIndex);

        String propertyType = rowData.get("PropertyType");
        String furnishingType = rowData.get("Furnishing");
        String buildingType = rowData.get("BuildingType");

        // Locate the filter container
        WebElement filterContainer = driver.findElement(By.className("nb__2Z1fi"));

        // Apply Property Type filter
        if (propertyType != null && !propertyType.isEmpty()) {
            String propertyTypeXPath = String.format("//input[@value='%s']", propertyType);
            WebElement propertyTypeElement = filterContainer.findElement(By.xpath(propertyTypeXPath));
            waitUntilWebEleToBeClickable(propertyTypeElement);
            propertyTypeElement.click();
        }

        // Apply Furnishing filter
        if (furnishingType != null && !furnishingType.isEmpty()) {
            String furnishingXPath = String.format("//input[@value='%s']", furnishingType);
            WebElement furnishingElement = filterContainer.findElement(By.xpath(furnishingXPath));
            waitUntilWebEleToBeClickable(furnishingElement);
            furnishingElement.click();
        }

        // Apply Building Type filter
        if (buildingType != null && !buildingType.isEmpty()) {
            String buildingTypeXPath = String.format("//input[@value='%s']", buildingType);
            WebElement buildingTypeElement = filterContainer.findElement(By.xpath(buildingTypeXPath));
            waitUntilWebEleToBeClickable(buildingTypeElement);
            buildingTypeElement.click();
        }

        // Handle sliders for rent (keeping existing logic)
        waitUntilWebEleVisible(driver.findElement(By.className(ExcelReader.get("slider_class"))));
        WebElement sliderRent = sliderHandles.get(0);
        dragSlider(sliderRent, 100);
        action.moveToElement(sliderRent);
        JavascriptExecutor javascriptexec = (JavascriptExecutor) driver;
        javascriptexec.executeScript("arguments[0].scrollIntoView(true)", sliderHandles.get(1));
        waitUntilWebEleToBeClickable(sliderHandles.get(1));
        dragSlider(sliderHandles.get(3), -200);

        // Click Skip and Got It buttons
        waitUntilWebEleToBeClickable(skipButton);
        skipButton.click();
        waitUntilWebEleToBeClickable(gotitBtn);
        gotitBtn.click();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	
	public void applyFilters() {
		waitUntilWebEleToBeClickable(propTypeOffice);
		propTypeOffice.click();
		showroom.click();
		waitUntilWebEleVisible(driver.findElement(By.className(ExcelReader.get("slider_class"))));
		WebElement sliderRent = sliderHandles.get(0);
		
		dragSlider(sliderRent, 100);
	
		action.moveToElement(sliderRent);
		JavascriptExecutor javascriptexec = (JavascriptExecutor)driver;
		javascriptexec.executeScript("arguments[0].scrollIntoView(true)",sliderHandles.get(1)); // H,V
		
		waitUntilWebEleToBeClickable(sliderHandles.get(1));
		
		dragSlider(sliderHandles.get(3), -200);
		
		// 19k to 1cr
		// 0-4000
		waitUntilWebEleToBeClickable(furnishing);
		furnishing.click();
		skipButton.click();
		gotitBtn.click();
	}
	
	public void getSpecificProperty() {
		waitUntilWebEleToBeClickable(propertyLink);
		propertyLink.click();
		waitUntilNewWindow();
		List<String> windowids = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(windowids.get(1));
	}
	
	public void clickshortListBtn() {
		waitUntilWebEleToBeClickable(shortListBtn);
		shortListBtn.click();
	}
	
	public boolean propertyUnshortList() throws InterruptedException {
	    Actions action = new Actions(driver);

	    if (shortListedProp != null && !shortListedProp.isEmpty()) {
	        waitUntilWebEleToBeClickable(unShortListBtn);
	        action.moveToElement(unShortListBtn).click().build().perform();
	    }

	    WebElement unShortlistMessage = driver.findElement(By.xpath(ExcelReader.get("unshortlist_xpath")));
	    waitUntilWebEleVisible(unShortlistMessage);
	    return unShortlistMessage.isDisplayed();
	}

	public boolean propertyFiltersAssert(String expectedPropertyType, String expectedFurnishing, String expectedBuildingType) {
        List<WebElement> articlesList = driver.findElements(By.tagName("article"));
        boolean isPropertyTypeApplied = false;
        boolean isFurnishingApplied = false;
        boolean isBuildingTypeApplied = false;

        for (WebElement element : articlesList) {
            String elementText = element.getText().toLowerCase();
            if (expectedPropertyType != null && !expectedPropertyType.isEmpty() &&
                elementText.contains(expectedPropertyType.toLowerCase().replace("_", " "))) {
                isPropertyTypeApplied = true;
            }
            if (expectedFurnishing != null && !expectedFurnishing.isEmpty() &&
                elementText.contains(expectedFurnishing.toLowerCase().replace("_", " "))) {
                isFurnishingApplied = true;
            }
            if (expectedBuildingType != null && !expectedBuildingType.isEmpty() &&
                elementText.contains(expectedBuildingType.toLowerCase().replace("_", " "))) {
                isBuildingTypeApplied = true;
            }
        }

        return isPropertyTypeApplied && isFurnishingApplied;
    }
}
