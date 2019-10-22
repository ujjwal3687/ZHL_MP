package com.test.automation.TestCases;

import java.io.IOException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.test.automation.Pages.Login_Page;
import com.test.automation.TestBase.BaseClass;

public class Login_Page_Test_Cases extends BaseClass
{
	public Login_Page LP;
	
	@BeforeMethod
	@Parameters("browser")
	public void login(String Browser_Name) throws IOException
	{
		Launch_Browser(Browser_Name);
	}
	
	@DataProvider
	public Object[][] get_Excel_Data() throws Exception
	{
		Object[][] data = read_excel("Login_Page_Test_Data");
		return data;
	}
	
	@Test(dataProvider = "get_Excel_Data")
	public void Login(String drop_down_value, String UNAME, String PWD, String XPath, String Expected_Msg) throws InterruptedException
	{
		LP = new Login_Page(driver);
		LP.Login_To_App(drop_down_value, UNAME, PWD, XPath, Expected_Msg);
	}
	
	@AfterMethod
	public void tear_down()
	{
		driver.quit();
	}
}
