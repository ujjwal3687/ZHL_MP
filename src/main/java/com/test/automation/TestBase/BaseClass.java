package com.test.automation.TestBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.test.automation.Utility.Util;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass 
{
	public Properties PRO;
	public WebDriver driver;
	public String url;
	public static ExtentHtmlReporter reporter;
	public static ExtentReports reports;
	public static ExtentTest test;
	public Logger logger;
	
	public WebDriver getDriver()
	{
		return driver;
	}
	
	public void Load_Config_File() throws IOException
	{
		File f = new File("./src/main/java/com/test/automation/Configuration/Config.Properties");
		FileInputStream FIS = new FileInputStream(f);
		PRO = new Properties();
		PRO.load(FIS);
	}

	public void Launch_Browser(String BrowserName) throws IOException
	{
		String path = "./src/main/java/com/test/automation/Configuration/Config.Properties";
		FileInputStream FIS = new FileInputStream(path);
		PropertyConfigurator.configure(FIS);
		logger = Logger.getLogger("com.test.automation.TestBase.BaseClass");
		if(BrowserName.equalsIgnoreCase("Chrome"))
		{
			logger.info("<<<<<<<<<<<<<<<<<<<<Lanunching the Chrome Browser>>>>>>>>>>>>>>>>>");
			WebDriverManager.chromedriver().setup();
			ChromeOptions opt = new ChromeOptions();
			opt.addArguments("disable-notifications");
			opt.addArguments("incogito");
			opt.addArguments("start-maximized");
			driver = new ChromeDriver(opt);
		}
		else if(BrowserName.equalsIgnoreCase("Firefox"))
		{
			logger.info("<<<<<<<<<<<<<<<<<<<<Lanunching the Firefox Browser>>>>>>>>>>>>>>>>>");
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
		}
		
		else if(BrowserName.equalsIgnoreCase("IE"))
		{
			logger.info("<<<<<<<<<<<<<<<<<<<<Lanunching the IE Browser>>>>>>>>>>>>>>>>>");
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
		}
		
		Load_Config_File();
		url = PRO.getProperty("URL");
		driver.get(url);
		logger.info("<<<<<<<<<<<<<<<<<<<<Waiting to load the page>>>>>>>>>>>>>>>>>");
		driver.manage().timeouts().pageLoadTimeout(Util.pageLoadTimeout, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(Util.implicitlyWait, TimeUnit.SECONDS);
	}
	
	public void input(WebElement ele, String keysToSend)
	{
		ele.sendKeys(keysToSend);
	}
	
	public void click(WebElement ele)
	{
		ele.click();
	}
	
	public void drop_down_selection(WebElement ele, String text)
	{
		Select list = new Select(ele);
		list.selectByVisibleText(text);
	}
	
	public void handling_Alert_clicking_OK(String expected_text)
	{
		Alert alert = driver.switchTo().alert();
		String actual_text = alert.getText(); 
		Assert.assertEquals(actual_text, expected_text);
		alert.accept();
	}
	
	public void handling_Alert_clicking_Cancel(String expected_text)
	{
		Alert alert = driver.switchTo().alert();
		String actual_text = alert.getText(); 
		Assert.assertEquals(actual_text, expected_text);
		alert.dismiss();
	}
	
	public void handling_mouse_over(String xpathExpression, String target)
	{
		Actions act = new Actions(driver);
		WebElement ele = driver.findElement(By.xpath(xpathExpression));
		act.moveToElement(ele);
	}
	
	public String date_Time_Stmap()
	{
		SimpleDateFormat SDF = new SimpleDateFormat("dd-MMM-yyyy hh-mm-ss a");
		String date = SDF.format(new Date());
		return date;
	}
	
	public String Capture_Screenshot(String Sub_Folder_Name, String image_name) throws Exception
	{
		TakesScreenshot TS = (TakesScreenshot) driver;
		File src = TS.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir")+ "/Screenshot/" +Sub_Folder_Name +"/" + image_name + "_" + date_Time_Stmap() + ".png";
		File dest = new File(destination);
		FileUtils.copyFile(src, dest);
		return destination;
	}
	
	public Object[][] read_excel(String Sheet_Name) throws Exception
	{
		Load_Config_File();
		File f = new File(PRO.getProperty("Test_Data_File_Path"));
		FileInputStream FIS = new FileInputStream(f);
		XSSFWorkbook wb = new XSSFWorkbook(FIS);
		XSSFSheet sheet = wb.getSheet(Sheet_Name);
		int rows = sheet.getLastRowNum();
		int columns  = sheet.getRow(0).getLastCellNum();
		Object data[][] = new Object[rows][columns];
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				data[i][j] = sheet.getRow(i + 1).getCell(j).toString();
			}
		}
		wb.close();
		return data;
	}
	
	public void verify_error_message(String xpathExpression, String expected_msg)
	{
		String actual_msg = driver.findElement(By.xpath(xpathExpression)).getText();
		Assert.assertEquals(actual_msg, expected_msg);
	}
	
	//Test rteter        
	@BeforeTest
	public void Extent_Report_Generation() throws IOException
	{
		Load_Config_File();
		String report_path = PRO.getProperty("Report_Path")+ "/Report_" + date_Time_Stmap() +".html";
		File f = new File(report_path);
		reporter = new ExtentHtmlReporter(f);
		reports = new ExtentReports();
		reports.attachReporter(reporter);
	}
	
	@AfterTest
	public void report_flush()
	{
		reports.flush();
	}
}
