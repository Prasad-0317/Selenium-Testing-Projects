package com.listeners;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{
	int retryCount = 0;
	int maxRetryCount = 3; // Retry 3 times

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            System.out.println("Retrying test: " + result.getName() + ", Attempt " + (retryCount + 1));
            return true; // Retry
        }
        return false; // Stop retrying
    }
}
