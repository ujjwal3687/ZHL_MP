package com.test.automation.Pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.test.automation.TestBase.BaseClass;

public class Login_Page extends BaseClass
{
	@FindBy(xpath = "//select[@id='ddlShift']")WebElement ele_drop_down_value;
	@FindBy(id = "txtUserId")WebElement uname;
	@FindBy(id = "txtPassword")WebElement pwd;
	@FindBy(xpath = "//input[@type='submit']")WebElement login_button;
	
	public Login_Page(WebDriver driver)
	{
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void Login_To_App(String drop_down_value, String UNAME, String PWD, String XPath, String Expected_Msg) throws InterruptedException
	{
		test = reports.createTest("Login_To_App");
		logger = Logger.getLogger("com.test.automation.Pages.Login_Page");
		logger.info("<<<<<<<<<<<<<<<<<<<<Selecting Drop Down Values from the List>>>>>>>>>>>>>>>>>");
		drop_down_selection(ele_drop_down_value,drop_down_value);
		logger.info("<<<<<<<<<<<<<<<<<<<<Entering the username>>>>>>>>>>>>>>>>>");
		input(uname,UNAME);
		logger.info("<<<<<<<<<<<<<<<<<<<<Entering the Password>>>>>>>>>>>>>>>>>");
		input(pwd,PWD);
		logger.info("<<<<<<<<<<<<<<<<<<<<Clicking the submit button>>>>>>>>>>>>>>>>>");
		click(login_button);
		if((UNAME.equalsIgnoreCase("")) || (PWD.equalsIgnoreCase("")))
		 { 
			handling_Alert_clicking_OK(Expected_Msg); 
		 } 
		else 
		{
			verify_error_message(XPath,Expected_Msg);
		} 
	}	
}
