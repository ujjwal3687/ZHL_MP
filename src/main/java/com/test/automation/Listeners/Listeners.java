package com.test.automation.Listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.test.automation.TestBase.BaseClass;

public class Listeners extends BaseClass implements IRetryAnalyzer, ITestListener, IAnnotationTransformer
{
	int Counter = 0, retry_limit = 2;
	
	@Override
	public void onTestSuccess(ITestResult result) 
	{
		try 
		{
			Object current_browser_instance = result.getInstance();
			driver = ((BaseClass)current_browser_instance).getDriver();
			String path = Capture_Screenshot(driver, "Passed_Test_Cases",result.getName());
			logger = Logger.getLogger("com.test.automation.Listeners.Listeners");
			logger.info("<<<<<<<<<<<<<<<<<<<<Marking Pass and highlighting the label to Green Color in Reports>>>>>>>>>>>>>>>>");
			test = test.pass(MarkupHelper.createLabel(result.getName(), ExtentColor.GREEN));
			logger.info("<<<<<<<<<<<<<<<<<<<<Capturing Screenshot from " + path + " and integrating to Extent Report>>>>>>>>>>>>>>>>");
			test = test.pass(result.getName(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void onTestFailure(ITestResult result) 
	{
		try 
		{
			Object current_browser_instance = result.getInstance();
			driver = ((BaseClass)current_browser_instance).getDriver();
			String path = Capture_Screenshot(driver, "Failed_Test_Cases",result.getName());
			logger = Logger.getLogger("com.test.automation.Listeners.Listeners");
			logger.info("<<<<<<<<<<<<<<<<<<<<Marking Fail and highlighting the label to Red Color in Reports>>>>>>>>>>>>>>>>");
			test = test.fail(MarkupHelper.createLabel(result.getThrowable().getMessage(), ExtentColor.RED));
			logger.info("<<<<<<<<<<<<<<<<<<<<Captruing Screenshot from" + path + " and integrating to Extent Report>>>>>>>>>>>>>>>>");
			test = test.fail(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) 
	{
		try 
		{
			Object current_browser_instance = result.getInstance();
			driver = ((BaseClass)current_browser_instance).getDriver();
			String path = Capture_Screenshot(driver, "Skipped_Test_Cases",result.getName());
			logger = Logger.getLogger("com.test.automation.Listeners.Listeners");
			logger.info("<<<<<<<<<<<<<<<<<<<<Marking Skip and highlighting the label to blue Color in Reports>>>>>>>>>>>>>>>>");
			test = test.skip(MarkupHelper.createLabel(result.getThrowable().getMessage(), ExtentColor.BLUE));
			logger.info("<<<<<<<<<<<<<<<<<<<<Captruing Screenshot from" + path + " and integrating to Extent Report>>>>>>>>>>>>>>>>");
			test = test.skip(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onTestStart(ITestResult result) 
	{
	}

	@Override
	public boolean retry(ITestResult result) 
	{
		if(Counter < retry_limit)
		{
			Counter++;
			return true;
		}
		else
		{
			Counter = 0;
			return false;
		}	
	}

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) 
	{
		annotation.setRetryAnalyzer(com.test.automation.Listeners.Listeners.class);
		
	}
}
