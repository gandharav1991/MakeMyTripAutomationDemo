package com.mmt.flightBooking;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mmt.util.TestUtil;
import com.thoughtworks.selenium.webdriven.commands.Click;

public class TestCase_Flightbook extends TestSuiteBase{
	
	
	String runmodes[]=null;
	static int count=-1;

	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip(){
		
		if(!TestUtil.isTestCaseRunnable(suiteFlightbookxls,this.getClass().getSimpleName())){
			Log.info("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		// load the runmodes off the tests
		runmodes=TestUtil.getDataSetRunmodes(suiteFlightbookxls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void nonAutomatedBuyFlow(
			            String browserType,
						String Url,
						String Fromcity,
						String Tocity,
						String FromDate,
						String ToDate
							) throws Exception{
		// test the runmode of current dataset
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")){
			skip=true;
			throw new SkipException("Runmode for test set data set to no "+count);
		}
		
	
		   Log.info("================================ Executing TestCase_mobileAdd for Test Data: "+browserType+"======================================================");
		   Log.info("==================================================================================================================================================");
		
		  
		
//======================================WebDriver Code ================================================================

		
	    openBrowser(browserType);
		navigate(Url);
		click("Flight_tab_id");
	    Thread.sleep(2000);
	    click("roundtrip_option_xpath");
	    Thread.sleep(2000);
	    //Dropdown handling
	    click("from_city_id");
	    Thread.sleep(2000);
		List <WebElement> dropdown = driver.findElements(By.xpath(OR.getProperty("all_fromcity_dropdown_xpath")));
		selectdropdown_generic(dropdown, Fromcity);
		Thread.sleep(2000);
		click("to_city_id");
		Thread.sleep(5000);
		dropdown =driver.findElements(By.xpath(OR.getProperty("all_tocity_dropdown_xpath")));
		selectdropdown_generic(dropdown, Tocity);
		Thread.sleep(5000);
		//calender handling
		click("Depart_xpath");
		Thread.sleep(2000);
		selectdate(FromDate);
		Thread.sleep(2000);
		click("Switch_to_close_btn_xpath");
		Thread.sleep(2000);
		click("Return_id");
		Thread.sleep(2000);
		selectdate(ToDate);
		Thread.sleep(2000);
		click("Passenger_xpath");
		Thread.sleep(2000);
		click("class_xpath");
		Thread.sleep(2000);
		click("Search_btn_xpath");
		
		
		closeBrowser();
			
//===============================================================================================================================================		
	
	}
	
	@AfterMethod
	public void reportDataSetResult() throws IOException{

		if(skip)
			TestUtil.reportDataSetResult(suiteFlightbookxls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(fail){
			isTestPass=false;
			
			TestUtil.reportDataSetResult(suiteFlightbookxls, this.getClass().getSimpleName(), count+2, "FAIL");
		}
		else
			TestUtil.reportDataSetResult(suiteFlightbookxls, this.getClass().getSimpleName(), count+2, "PASS");
		
		skip=false;
		fail=false;	
	}
	@AfterTest
	public void reportTestResult(){
		if(isTestPass)
			TestUtil.reportDataSetResult(suiteFlightbookxls, "Test Cases", TestUtil.getRowNum(suiteFlightbookxls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(suiteFlightbookxls, "Test Cases", TestUtil.getRowNum(suiteFlightbookxls,this.getClass().getSimpleName()), "FAIL");
	}
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(suiteFlightbookxls, this.getClass().getSimpleName()) ;
	}
	
	
	
}
