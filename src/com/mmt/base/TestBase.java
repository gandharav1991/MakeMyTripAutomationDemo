package com.mmt.base;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.google.common.base.Function;
import com.mmt.util.ErrorUtil;
import com.mmt.util.Xls_Reader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;

import java.io.FileOutputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.Document;


public class TestBase {
	public static Properties CONFIG=null;
	public static Properties OR=null;
	public static Xls_Reader suiteXls=null;
	public static Xls_Reader suiteSubmitAddxls=null;
	public static Xls_Reader suiteJoinUsxls=null;
	public static Xls_Reader suiteFlightbookxls=null;
	public static Xls_Reader suiteSearchAddxls=null;
	public static boolean isInitalized=false;
	public static boolean isBrowserOpened=false;
	public static boolean fail=false;
	public static boolean skip=false;
	public static boolean isTestPass=true;
	public static WebDriver driver =null;
	//public static Eyes eyes=null;
	public static XWPFDocument document=null;
	public static FileOutputStream fos= null;
	public static Logger Log = Logger.getLogger("-->");
	
	// initializing the Tests
	public static void initialize() throws Exception{
		// logs
		if(!isInitalized){
		
		DOMConfigurator.configure("log4j.xml");
		// config
		Log.info("Loading Property files");
		CONFIG = new Properties();
		FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"//src//com//mmt//config/config.properties");
		CONFIG.load(ip);
			
		OR = new Properties();
		ip = new FileInputStream(System.getProperty("user.dir")+"//src//com//mmt//config/OR.properties");
		OR.load(ip);
		Log.info("Loaded Property files successfully");
		Log.info("Loading XLS Files");

		// xls file
		
		suiteFlightbookxls = new Xls_Reader(System.getProperty("user.dir")+"//src//com//mmt//xls//FlightBook suite.xlsx");
		suiteXls = new Xls_Reader(System.getProperty("user.dir")+"//src//com//mmt//xls//Suite.xlsx");
		Log.info("Loaded XLS Files successfully");
		isInitalized=true;
		}		
	}

	
//================================================Generic Functions==========================================================\\
	
	
	public static void setUp() throws Exception {
		
	    DesiredCapabilities caps = new DesiredCapabilities();
	    caps.setCapability("browser", "Firefox");
	    caps.setCapability("browser_version", "45.0");
	    caps.setCapability("os", "Windows");
	    caps.setCapability("os_version", "7");
	    caps.setCapability("resolution", "1366x768");
	    driver = new RemoteWebDriver(new URL("http://pss12:1qLdNvZ47aQgJe8TfT4g@hub.browserstack.com/wd/hub"),caps);
	    
	  }	
	
	
	
	public static void openBrowser(String browserType) throws Exception{
	        try{
			if(browserType.equalsIgnoreCase("Firefox")){
				Log.info("Opening Firefox Browser");
				FirefoxProfile profile=new FirefoxProfile(); 
				// This will set the true value
				profile.setAcceptUntrustedCertificates(true); 
				// This will open  firefox browser using above created profile
				driver=new FirefoxDriver();
				
				/*Dimension d =new Dimension(360,640);
				driver.manage().window().setSize(d);*/
				driver.manage().window().maximize();
				Log.info("Firefox browser started");	
				
		    }
			else if (browserType.equalsIgnoreCase("IE")){
				Log.info("Opening IE Browser");
				 File file=new File(System.getProperty("user.dir")+"\\jars\\IEDriverServer.exe");
				 
				 DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				 capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				 capabilities.setCapability("requireWindowFocus", true);
				 System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			     driver = new InternetExplorerDriver(capabilities);
				driver.manage().window().maximize();
				Log.info("IE browser started");
				
			}
				
			else if (browserType.equalsIgnoreCase("Chrome")){
				Log.info("Opening Chrome Browser");
				File file = new File(System.getProperty("user.dir")+"\\jars\\chromedriver.exe");
				 DesiredCapabilities cap=DesiredCapabilities.chrome();
				  
				 // Set ACCEPT_SSL_CERTS  variable to true
				 cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				 // Set the driver path
				 System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
				 // Open browser with capability
				 driver=new ChromeDriver(cap);
				driver.manage().window().maximize();
				Log.info("Chrome browser started");
			}
			else{
				Log.info("Opening Firefox Browser");
				FirefoxProfile profile=new FirefoxProfile(); 
				// This will set the true value
				profile.setAcceptUntrustedCertificates(true); 
				// This will open  firefox browser using above created profile
				driver=new FirefoxDriver();
				driver.manage().window().maximize();
				Log.info("Firefox browser started");
				
			}
	        }catch(Throwable t){
		   // report error
		   ErrorUtil.addVerificationFailure(t);
		   fail=true;
		  // Log.error("Not able to start the browser: -- "+CONFIG.getProperty("browserType") );
		   Log.error("Not able to start the browser: -- "+ t.getMessage() );
		   
			throw new Exception(" Stoping the script....!!!!");
		} 
		  }			
	     
	public static void openMobile(String deviceName) throws Exception{
		// add 20+12 extra in width
        try{
        	FirefoxProfile profile=new FirefoxProfile();
			profile.setAcceptUntrustedCertificates(true);
			driver=new FirefoxDriver();
        	
        	
		if(deviceName.equalsIgnoreCase("Samsung Galaxy S5")){
			Log.info("Opening Samsung Galaxy S5");
			Dimension d =new Dimension(372,640);
			driver.manage().window().setSize(d);
			Log.info("Samsung Galaxy S5 started");	
			
	    }
		else if (deviceName.equalsIgnoreCase("iphone 5")){
			Log.info("Opening iphone 5");
			Dimension d =new Dimension(352,568);
			driver.manage().window().setSize(d);
			Log.info("iphone 5 started");	
			
	    }
		else if (deviceName.equalsIgnoreCase("iphone 6")){
			Log.info("Opening iphone 6");
			Dimension d =new Dimension(407,627);
			driver.manage().window().setSize(d);
			Log.info("iphone 6 started");	
			
	    }
		else if (deviceName.equalsIgnoreCase("Samsung Galaxy Tab 2")){
			Log.info("Opening Samsung Galaxy Tab 2");
			Dimension d =new Dimension(832,1280);
			driver.manage().window().setSize(d);
			Log.info("Samsung Galaxy Tab 2 started");	
			
	    }
		else if (deviceName.equalsIgnoreCase("ipad")){
			Log.info("Opening ipad");
			Dimension d =new Dimension(800,1024);
			driver.manage().window().setSize(d);
			Log.info("ipad started");	
			
	    }
        }catch(Throwable t){
	   // report error
	   ErrorUtil.addVerificationFailure(t);
	   fail=true;
	  // Log.error("Not able to start the browser: -- "+CONFIG.getProperty("browserType") );
	   Log.error("Not able to start Mobile Emulator: -- "+ t.getMessage() );
	   
		throw new Exception(" Stoping the script....!!!!");
	} 
	  }
		public static void navigate(String url) throws Exception{
			
			try{
		    Log.info("Navigating to URL--:"+url);
			driver.get(url);
			
			}catch(Throwable t){
				// report error
				ErrorUtil.addVerificationFailure(t);
				fail=true;
				//Log.error("Not able to open the url -- "+url );	
				driver.quit();
				Log.error("Not able to open the url -- "+t.getMessage() );	
				throw new Exception(" Stoping the script....!!!!");
				}			
		}
		
		
		public static void click(String identifier) throws Exception{
			try{
				
			if(identifier.endsWith("_xpath")) {
				Log.info("Clicking on:-- "+ identifier);
				 
				driver.findElement(By.xpath(OR.getProperty(identifier))).click();
			}
			else if(identifier.endsWith("_id")){
				Log.info("Clicking on:-- "+ identifier);
				driver.findElement(By.id(OR.getProperty(identifier))).click();
			}
			else if(identifier.endsWith("_name")){
				Log.info("Clicking on:-- "+ identifier);
				driver.findElement(By.name(OR.getProperty(identifier))).click();
			}
			else if(identifier.endsWith("_css")){
				Log.info("Clicking on:-- "+ identifier);
				driver.findElement(By.cssSelector(OR.getProperty(identifier))).click();
			}
			else if(identifier.endsWith("_linkText")) {
				Log.info("Clicking on:-- "+ identifier);
				 
				driver.findElement(By.linkText(OR.getProperty(identifier))).click();
			}
			}catch(Throwable t){
				
				ErrorUtil.addVerificationFailure(t);
				fail=true;
				Log.error("Not able to click on -- "+t.getMessage() );
				//captureScreenshot("Fail");
				getScreen(document, fos, "Fail");
				driver.quit();
				throw new Exception(" Stoping the script....!!!!");
				
			}
			}
		public static void clear(String identifier) throws Exception{
			try{
			if(identifier.endsWith("_xpath")){
				Log.info("Clicking on:-- "+ identifier);
				driver.findElement(By.xpath(OR.getProperty(identifier))).clear();
			}
			else if(identifier.endsWith("_id")){
				Log.info("Clicking on:-- "+ identifier);
				driver.findElement(By.id(OR.getProperty(identifier))).clear();
			}
			else if(identifier.endsWith("_name")){
				Log.info("Clicking on:-- "+ identifier);
				driver.findElement(By.name(OR.getProperty(identifier))).clear();
			}
			else if(identifier.endsWith("_css")){
				Log.info("Clicking on:-- "+ identifier);
				driver.findElement(By.cssSelector(OR.getProperty(identifier))).clear();
			}
			}catch(Throwable t){
				
				ErrorUtil.addVerificationFailure(t);
				fail=true;
				Log.error("Not able to clear the input field -- "+t.getMessage() );
				getScreen(document, fos, "Fail");
				driver.quit();
				throw new Exception(" Stoping the script....!!!!");
				
			}
			}
		
		public static void choosePackage(String packageID) throws Exception{
		try{	
			Log.info("Choosing the Plan:--"+packageID);
		driver.findElement(By.id(packageID)).click();
			
		}catch(Throwable t){
			
				ErrorUtil.addVerificationFailure(t);
				fail=true;
				Log.error("Not able to choosePlan:-- "+ t.getMessage() );
				getScreen(document, fos, "Fail");
				driver.quit();
				throw new Exception(" Stoping the script....!!!!");
				
			}
			
		}
		
		public static void selectdropdown_generic(List<WebElement> dropdown, String check) throws Exception {
			// TODO Auto-generated method stub
			
			for(WebElement x : dropdown){
				
				String s=x.getText();
				
				
				if(s.equalsIgnoreCase(check))
				{System.out.println(s);
	               Thread.sleep(2000);
					x.click();
					
					break;
				}
			}
			
		}


		public static void input(String identifier,String data) throws Exception{
			try{	
			if(identifier.endsWith("_xpath")){
				Log.info("Entering the value in:-- " + identifier);
				driver.findElement(By.xpath(OR.getProperty(identifier))).sendKeys(data);
			}
			else if(identifier.endsWith("_id")){
				Log.info("Entering the value in:-- " + identifier);
				driver.findElement(By.id(OR.getProperty(identifier))).sendKeys(data);
			}
			else if(identifier.endsWith("_name")){
				Log.info("Entering the value in:-- " + identifier);
				driver.findElement(By.name(OR.getProperty(identifier))).sendKeys(data);
			}
			else if(identifier.endsWith("_css")){
				Log.info("Entering the value in:-- " + identifier);
				driver.findElement(By.cssSelector(OR.getProperty(identifier))).sendKeys(data);
			}
			}catch(Throwable t){
		
				ErrorUtil.addVerificationFailure(t);
				fail=true;
				Log.error("Not able to enter the value:-- "+ t.getMessage() );
				getScreen(document, fos, "Fail");
				driver.quit();
				throw new Exception(" Stoping the script....!!!!");
				
			}
		}
		
		public static String getText(String identifier) throws Exception{
			String  text = null;
			try{
			if(identifier.endsWith("_xpath")){
				Log.info("Getting the text from:--" + identifier);
				text = driver.findElement(By.xpath(OR.getProperty(identifier))).getText();
			}
			else if(identifier.endsWith("_id")){
				Log.info("Getting the text from:--" + identifier);
				text = driver.findElement(By.id(OR.getProperty(identifier))).getText();
			}
			else if(identifier.endsWith("_name")){
				Log.info("Getting the text from:--" + identifier);
				text = driver.findElement(By.name(OR.getProperty(identifier))).getText();
			}
			else if(identifier.endsWith("_css")){
				Log.info("Getting the text from:--" + identifier);
				driver.findElement(By.cssSelector(OR.getProperty(identifier))).getText();
			}
			
           }catch(Throwable t){
				
				ErrorUtil.addVerificationFailure(t);			
				fail=true;
				Log.error("Not able to get the text:-- "+t.getMessage() );
				getScreen(document, fos, "Fail");
				driver.quit();
				throw new Exception(" Stoping the script....!!!!");	
           }
			return text;
			}

	
        public static void selectDropDown(String identifier, String value) throws Exception{
			
     			try{
     				if(identifier.endsWith("_xpath")){
     					Log.info("selecting "+ value +" from drop Down List");
         				new Select(driver.findElement(By.xpath(OR.getProperty(identifier)))).selectByVisibleText(value);		
     				}
     				else if(identifier.endsWith("_id")){
     					Log.info("selecting "+ value +" from drop Down List");
         				new Select(driver.findElement(By.id(OR.getProperty(identifier)))).selectByVisibleText(value);		
     				}	
     				else if(identifier.endsWith("_name")){
     					Log.info("selecting "+ value +" from drop Down List");
         				new Select(driver.findElement(By.name(OR.getProperty(identifier)))).selectByVisibleText(value);		
     				}
     				else if(identifier.endsWith("_css")){
     					Log.info("selecting "+ value +" from drop Down List");
         				new Select(driver.findElement(By.cssSelector(OR.getProperty(identifier)))).selectByVisibleText(value);		
     				}
     			
     			}catch(Throwable t){
     				ErrorUtil.addVerificationFailure(t);
     				fail=true;
     				Log.error("Not able to select the drop down List -- " +t.getMessage());
     				getScreen(document, fos, "Fail");
     			    driver.quit();
     				throw new Exception(" Stoping the script....!!!!");
     				
     			}
     			
     		}
        public static void captureScreenshot(String filename) throws IOException{
			try{
			Log.info("Taking ScreenShot" );
			DateFormat dateFormat = new SimpleDateFormat("MM-yyyy-dd HH.mm.ss");
			Calendar cal = Calendar.getInstance();
			String dateFormate =dateFormat.format(cal.getTime());
			//System.out.println(dateFormat.format(cal.getTime()));
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		    FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"\\Screenshots\\"+filename+" ,"+dateFormate+".jpg"));
		    Log.info("ScreenShot is captured, to view image please go to "+System.getProperty("user.dir")+"\\Screenshots\\"+filename+""+dateFormate+".jpg" );
			}
		    catch(Throwable t){
 				//ErrorUtil.addVerificationFailure(t);
 				//fail=true;
 				Log.error("Not able to take screenshot :"+ t.getMessage());
 				
 			}
        }
        
        public void waitForPageLoad(int time) throws Exception{
			try {
				Log.info("Waiting for page load");
				Thread.sleep(time);
			} catch(Throwable t){
 				ErrorUtil.addVerificationFailure(t);
 				fail=true;
 				Log.error("Not able to wait :"+ t.getMessage());
 				throw new Exception(" Stoping the script....!!!!");
 				
 			}
		}
        
		// close browser
		public static void closeBrowser(){
			Log.info("Closing the browser now" );
			driver.quit();
		}
		
		// compare titles
		public boolean compareTitle(String expectedVal) throws Exception{
			try{
				Assert.assertEquals(driver.getTitle() , expectedVal);
				
				}catch(Throwable t){
					ErrorUtil.addVerificationFailure(t);
					fail=true;
					Log.error("Titles do not match");
					throw new Exception(" Titles do not match....!!!!");
				//	return false;
				}
			return true;
		}	
		// compaerStrings
		// compare titles
			public boolean compareNumbers(int expectedVal, int actualValue){
				try{
					Assert.assertEquals(actualValue,expectedVal   );
					Log.info("Value matches with URL");
					}catch(Throwable t){
						ErrorUtil.addVerificationFailure(t);			
						Log.error("Values do not match");
						return false;
					}
				return true;
			}
			
			public boolean checkElementPresence(String xpathKey){
				int count =driver.findElements(By.xpath(OR.getProperty(xpathKey))).size();
				
				try{
				Assert.assertTrue(count>0, "No element present");
				}catch(Throwable t){
					ErrorUtil.addVerificationFailure(t);			
					Log.error("No element present");
					return false;
				}
				return true;
			}
			
				 public static void fluentWait(final String identifier, int timeout, int polling) throws IOException{
							try{
							Log.info("Waiting for page load" );
							
							Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
									   .withTimeout(timeout, TimeUnit.SECONDS)//  change here
								       .pollingEvery(polling, TimeUnit.SECONDS) // change here
								       .ignoring(NoSuchElementException.class);
								   WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
								     public WebElement apply(WebDriver driver) {
								       if(identifier.endsWith("_xpath")){
											
								    	   return driver.findElement(By.xpath(OR.getProperty(identifier)));
										}
										else if(identifier.endsWith("_id")){
										
										return driver.findElement(By.id(OR.getProperty(identifier)));
										}
										else if(identifier.endsWith("_name")){
											
											return driver.findElement(By.name(OR.getProperty(identifier)));
										}
										else if(identifier.endsWith("_css")){
											
											return driver.findElement(By.cssSelector(OR.getProperty(identifier)));
										}
									return null;
									
								     }
								   });
							}
						    catch(Throwable t){
				 				ErrorUtil.addVerificationFailure(t);
				 				fail=true;
				 				Log.error(t.getMessage());
				 				
				 			}
				        }
					@SuppressWarnings("deprecation")
					public static void waitForTextToBeDisplayed(String identifier, String text ) throws Exception{
						
						try{
							Log.info("Waiting for the text to display");
							WebDriverWait  wait= new WebDriverWait(driver,60);
							wait.withTimeout(60, TimeUnit.SECONDS);
					        wait.pollingEvery(1, TimeUnit.SECONDS);
					        wait.ignoring(NoSuchElementException.class);
					        if(identifier.endsWith("_xpath")){
					             wait.until(ExpectedConditions.textToBePresentInElement(By.xpath(OR.getProperty(identifier)), text));
					        }
					        else if(identifier.endsWith("_id")){
					        	 wait.until(ExpectedConditions.textToBePresentInElement(By.id(OR.getProperty(identifier)), text));	
					        }
					        else if(identifier.endsWith("_css")){
					        	 wait.until(ExpectedConditions.textToBePresentInElement(By.cssSelector(OR.getProperty(identifier)), text));	
					        }
					        else if(identifier.endsWith("_name")){
					        	 wait.until(ExpectedConditions.textToBePresentInElement(By.name(OR.getProperty(identifier)), text));	
							}
						}catch(Throwable t){
			 				ErrorUtil.addVerificationFailure(t);
			 				fail=true;
			 				Log.error("Not able to verify "+ t.getMessage());
			 				
			 			}
							
						
						
				}
					
					
//================================== your own functions===============================================\\
			
			
			 public static void verifyPhoneNumber(String identifier, String data) throws Exception{
		    		String strng = null;
		    			Log.info("verifying the presence of "+ data);
		    			
		    			try{
		    				if(identifier.endsWith("_xpath")){
		    					strng = driver.findElement(By.xpath(OR.getProperty(identifier))).getText();
		    				}
		    				else if(identifier.endsWith("_id")){
		    					strng = driver.findElement(By.id(OR.getProperty(identifier))).getText();
		    				}
		    				else if(identifier.endsWith("_name")){
		    					strng = driver.findElement(By.name(OR.getProperty(identifier))).getText();
		    				}
		    				else if(identifier.endsWith("_css")){
		    					strng = driver.findElement(By.cssSelector(OR.getProperty(identifier))).getText();
		    				}
		    			 
		    			}catch(Throwable t){
		     				ErrorUtil.addVerificationFailure(t);
		     				fail=true;
		     				Log.error("Element Not Present ---  "+ t.getMessage());
		     				throw new Exception(" Stoping the script....!!!!");	
		     			}
		    			try{
		    			Assert.assertEquals(strng, data);
		    			Log.info(data+" is verified successfully");
		    			}catch(Throwable t){
		     				ErrorUtil.addVerificationFailure(t);
		     				fail=true;
		     				Log.error("Not able to verify "+ t.getMessage());
		     				throw new Exception(" Stoping the script....!!!!");	
		    			}
		    		 }
					 
			 public static void verifyURL(String expectedURL) throws Exception{
		    		String actualURL = null;
		    			Log.info("verifying the url "+ expectedURL);
		    			
		    			try{
		    				
		    			actualURL = driver.getCurrentUrl();
		    			Assert.assertEquals(actualURL,expectedURL);
		    			Log.info(expectedURL +" is verified successfully");
		    			}catch(Throwable t){
		     				ErrorUtil.addVerificationFailure(t);
		     				fail=true;
		     				Log.error("Not able to verify "+ t.getMessage());
		     				throw new Exception(" Stoping the script....!!!!");	
		    			}
		    		 }
			 
			 
			 public static void selectdate(String date1) throws Exception{
				    String selectdatecheck=date1;
				       Date d= new Date(selectdatecheck);
				       SimpleDateFormat simpledateformat = new SimpleDateFormat("MMMM/dd/YYYY");
				       String date = simpledateformat.format(d);
				       System.out.println(date);
				       String[] split = date.split("/");
				       String month = split[0];
				       String year = split[2];
				       while (true)
				       {
				    	   
				    	   try{
				    		   WebElement x = driver.findElement(By.xpath("//*[@class='ui-datepicker-group ui-datepicker-group-first']//span[contains(.,'"+month+"')]"));
				    		   WebElement y= driver.findElement(By.xpath("//span[contains(.,'"+year+"')][1]"));
				    		 
				    		   if(x.isDisplayed() && y.isDisplayed())
				    		   {
				    			  // (//a[contains(.,'19')])[1]
				    					 String dateselect = split[1];
				    					 driver.findElement(By.xpath(("//td[@class='ui-datepicker-week-end']//a[contains(.,'"+dateselect+"')])[1]"))).click();
				    					   
				    					   break;
				    		   }
				    		   
				    	   }
				    	   catch(Exception e)
				    	   {
				    		   driver.findElement(By.xpath("//span[contains(.,'Next')]")).click();
				    		   Thread.sleep(2000);
				    	   }
				       }
				       
						
						
						
			 }
			
			public static void wait1() throws Exception{

				    Log.info("Waiting for page load");
					Thread.sleep(2000);
					WebDriverWait  explicitwait= new WebDriverWait(driver,120);
					explicitwait.withTimeout(120, TimeUnit.SECONDS);
					explicitwait.pollingEvery(1, TimeUnit.SECONDS);
					explicitwait.ignoring(NoSuchElementException.class);
					explicitwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//img[@src='resources/layout/images/loader.gif']")));
					//explicitwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@src='/resources/images/residential/buyflow/loading-time.gif']")));
					//explicitwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@alt='Charter Spectrum Loading Timer']")));
					Thread.sleep(5000);
					Log.info("Waiting Done");
			    //css=img.timer.center-block
				//css=.loading-panel
				//css=p.text-center
				//css=img.large-logo.center-block	
			}
			
		/*	public static void bhund() throws Exception{
                
				List<WebElement> lst= new ArrayList();
				lst.add(arg0)
			    Log.info("Waiting for page load");
				Thread.sleep(2000);
				WebDriverWait  explicitwait= new WebDriverWait(driver,120);
				explicitwait.withTimeout(120, TimeUnit.SECONDS);
				explicitwait.pollingEvery(1, TimeUnit.SECONDS);
				explicitwait.ignoring(NoSuchElementException.class);
				explicitwait.until(ExpectedConditions.invisibilityOfAllElements(lst));
				Thread.sleep(3000);
				Log.info("Waiting Done");
		 
		}*/
			
			public static void wait2() throws Exception{
				Log.info("Waiting for page load");
				Thread.sleep(2000);
				WebDriverWait  wait= new WebDriverWait(driver,120);
				wait.withTimeout(120, TimeUnit.SECONDS);
		        wait.pollingEvery(4, TimeUnit.SECONDS);
		        wait.ignoring(NoSuchElementException.class);
		        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("img.timer.center-block")));
				Thread.sleep(6000);
				//css=div.timer
		        //css=p.text
		        //css=img[alt="loading"]
		}
			public static void wait3() throws Exception{
				Log.info("Waiting for page load");
				Thread.sleep(2000);
				WebDriverWait  wait= new WebDriverWait(driver,120);
				wait.withTimeout(120, TimeUnit.SECONDS);
		        wait.pollingEvery(4, TimeUnit.SECONDS);
		        wait.ignoring(NoSuchElementException.class);
		        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.timer")));
				Thread.sleep(6000);
				//css=div.timer
		        //css=p.text
		        //css=img[alt="loading"]
		}	
			public static void getScreen(XWPFDocument document, FileOutputStream fos, String filename) throws InvalidFormatException, IOException {
				
				DateFormat dateFormat = new SimpleDateFormat("MM-yyyy-dd HH.mm.ss");
				Calendar cal = Calendar.getInstance();
				String dateFormate =dateFormat.format(cal.getTime());
				//System.out.println(dateFormat.format(cal.getTime()));
				File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			    FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"\\Screenshots\\"+filename+" ,"+dateFormate+".jpg"));			   
			    Log.info("ScreenShot is captured and stored at "+System.getProperty("user.dir")+"\\Screenshots\\"+filename+""+dateFormate+".jpg" );
			    String filepath=FindLatestFile();
				String blipId = document.addPictureData(new FileInputStream(new File(filepath)), Document.PICTURE_TYPE_JPEG);
			   // System.out.println(document.getNextPicNameNumber(Document.PICTURE_TYPE_JPEG));
			    //System.out.println(document.getNextPicNameNumber(Document.PICTURE_TYPE_JPEG));
			    createPicture(document, blipId,document.getNextPicNameNumber(Document.PICTURE_TYPE_JPEG), 620, 380);    

			}
            
			 private static String FindLatestFile() 
			    {
					File file=new File(System.getProperty("user.dir")+"\\Screenshots");
					File[] listofFile = file.listFiles();
					long data=0,data1;
					File latest = null;
					for (File file2 : listofFile) {
						data1 = file2.lastModified();
						if(data1>data)
						{				
							long temp = data;
							data=data1;
							data1=temp;	
							latest=file2;
						}
					}
					return latest.getAbsolutePath();
				}
			public static void createPicture(XWPFDocument document, String blipId,int id, int width, int height) {
				    
				final int EMU = 9525;
			    width *= EMU;
			    height *= EMU;
			    //String blipId = getAllPictures().get(id).getPackageRelationship().getId();
			
			
			    CTInline inline = document.createParagraph().createRun().getCTR().addNewDrawing().addNewInline();
			
			    String picXml = "" +
			            "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
			            "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
			            "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
			            "         <pic:nvPicPr>" +
			            "            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>" +
			            "            <pic:cNvPicPr/>" +
			            "         </pic:nvPicPr>" +
			            "         <pic:blipFill>" +
			            "            <a:blip r:embed=\"" + blipId + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
			            "            <a:stretch>" +
			            "               <a:fillRect/>" +
			            "            </a:stretch>" +
			            "         </pic:blipFill>" +
			            "         <pic:spPr>" +
			            "            <a:xfrm>" +
			            "               <a:off x=\"0\" y=\"0\"/>" +
			            "               <a:ext cx=\"" + width + "\" cy=\"" + height + "\"/>" +
			            "            </a:xfrm>" +
			            "            <a:prstGeom prst=\"rect\">" +
			            "               <a:avLst/>" +
			            "            </a:prstGeom>" +
			            "         </pic:spPr>" +
			            "      </pic:pic>" +
			            "   </a:graphicData>" +
			            "</a:graphic>";
			
			    //CTGraphicalObjectData graphicData = inline.addNewGraphic().addNewGraphicData();
			    XmlToken xmlToken = null;
			    try
			    {
			        xmlToken = XmlToken.Factory.parse(picXml);
			    }
			    catch(XmlException xe)
			    {
			        xe.printStackTrace();
			    }
			    inline.set(xmlToken);
			    //graphicData.set(xmlToken);
			
			    inline.setDistT(0);
			    inline.setDistB(0);
			    inline.setDistL(0);
			    inline.setDistR(0);
			
			    CTPositiveSize2D extent = inline.addNewExtent();
			    extent.setCx(width);
			    extent.setCy(height);
			
			    CTNonVisualDrawingProps docPr = inline.addNewDocPr();
			    docPr.setId(id);
			    docPr.setName("Picture " + id);
			    docPr.setDescr("Generated");
			}
			
			public static void createDoc(String streetAddress, String zipCode ) throws Exception{
				XWPFDocument document = new XWPFDocument();
				 FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.dir")+"\\Screenshots\\"+streetAddress+" ,"+zipCode+".docx"));
				
			}
			
			 public static void verifyTagInHTML(String Url, String tagName) throws IOException, Exception{
				 URL url;
					InputStream is = null;
					BufferedReader br;
					String line;
					boolean flag = false;
				 
					 TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
				            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
				            public void checkServerTrusted(X509Certificate[] certs, String authType) { }

				        } };

				        SSLContext sc = SSLContext.getInstance("SSL");
				        sc.init(null, trustAllCerts, new java.security.SecureRandom());
				        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

				        // Create all-trusting host name verifier
				        HostnameVerifier allHostsValid = new HostnameVerifier() {
				            public boolean verify(String hostname, SSLSession session) { return true; }
				        };
				        // Install the all-trusting host verifier
				        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
				        /* End of the fix*/
				      
				        
				    try{
				    	
				    Xls_Reader datatable=new Xls_Reader(System.getProperty("user.dir")+"\\Confirmation Number\\Url Response Code.xlsx");
	     			int rowCount =datatable.getRowCount("(855)757-7328");
	     				
	     			datatable.setCellData("(855)757-7328", "Url", rowCount+1, Url);
	     			
	     			Log.info("Verifying the tag "+tagName +" in HTML");
				    url = new URL(Url);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					int i =conn.getResponseCode();
					String str = Integer.toString(i);
					Log.info(conn.getResponseCode());
					if(conn.getResponseCode()!=404)
					{
					is = url.openStream(); // throws an IOException
					br = new BufferedReader(new InputStreamReader(is));

					while ((line = br.readLine()) != null) {
						if (line.contains(tagName)) {
							flag = true;
							datatable.setCellData("(855)757-7328", "Results", rowCount+1, "Pass");
							Log.info("Pass");
							break;
						}
					}
					if (!flag) {
						datatable.setCellData("(855)757-7328", "Results", rowCount+1, "Fail");
						Log.info("Fail");
					}
					}
					else
					{
						datatable.setCellData("(855)757-7328", "Response", rowCount+1, str);
						Log.info("Page not found.");
					}

				}
				   /* catch (HttpStatusException t) {
		            	ErrorUtil.addVerificationFailure(t);
		     			fail=true;
		     			int response= t.getStatusCode();
		     			Log.error("Page not found so could not verify -- " +response);
		                   
		             }*/catch(Throwable t){
	     				ErrorUtil.addVerificationFailure(t);
	     				fail=true;
	     				Log.error("Tag is not present in HTML -- " +t.getMessage());
	     							
	     			} 
				 finally{
					 try {
							if (is != null)
								is.close();
						} catch (IOException ioe) {
							// nothing to see here
						}
	     			}	
			 }
			/* public static void verifyTagInHTML(String tagName) throws Exception{
					
	     			try{
	     				String s =driver.getPageSource();
	     				//Log.info(s);
	     				Log.info("Verifying the tag "+tagName +" in HTML");
	     				Assert.assertTrue(s.contains(tagName));
	     			}catch(Throwable t){
	     				ErrorUtil.addVerificationFailure(t);
	     				fail=true;
	     				Log.error("Tag is not present in HTML -- " +t.getMessage());
	     			   driver.quit();
	     				throw new Exception(" Tag is not present in HTML....!!!!");			
	     			}		
			 }*/
			 
			 public static void scrollPage() throws Exception{
					
	     			try{
	     				Log.info("Scrolling Page");
	     				JavascriptExecutor jse = (JavascriptExecutor)driver;
	     				jse.executeScript("window.scrollBy(0,250)", "");
	     				//driver.findElement(By.tagName("html")).sendKeys(Keys.PAGE_DOWN);
	     				//driver.findElement(By.tagName("html")).sendKeys(Keys.PAGE_DOWN);
	     			}catch(Throwable t){
	     				ErrorUtil.addVerificationFailure(t);
	     				fail=true;
	     				Log.error("Not able to Scroll Page -- " +t.getMessage());
	     				getScreen(document, fos, "Fail");
	     			    driver.quit();
	     				throw new Exception(" Not able to Scroll Page....!!!!");	
	     			}	
			 }
			//================================================Broken Link Function=========================================			 
				//	 http://download.java.net/jdk7/archive/b123/docs/api/java/net/HttpURLConnection.html	
							public static List findAllLinks(WebDriver driver){
								List<WebElement> elementList = new ArrayList();
								  elementList = driver.findElements(By.tagName("a"));
								  elementList.addAll(driver.findElements(By.tagName("img")));
								  List finalList = new ArrayList(); 
								  for (WebElement element : elementList)
								  {
							        if(element.getAttribute("href") != null)
									  {
									  finalList.add(element);
									  }		  
								  }	
								  return finalList;
							  }
								public static String isLinkBroken(URL url) throws Exception{
									String response = "";
									HttpURLConnection connection = (HttpURLConnection) url.openConnection();
									try
									{
									    connection.connect();
									    response = connection.getResponseMessage();	        
									    connection.disconnect();
									    return response;
									}
									catch(Exception exp)
									{
									return exp.getMessage();
									}  				
								}
							public static void checkForBrokenLinks(){
								
								Log.info("Checking for Broken Link");
								List<WebElement> allImages = findAllLinks(driver);    
								Log.info("Total number of Links found " + allImages.size());
							    for( WebElement element : allImages){
							    	try
							    	{
							    		Log.info("URL: " + element.getAttribute("href")+ " returned " + isLinkBroken(new URL(element.getAttribute("href"))));
							    	Log.info("URL: " + element.getAttribute("outerhtml")+ " returned " + isLinkBroken(new URL(element.getAttribute("href"))));
							    	}
							    	catch(Exception exp)
							    	{
							    		Log.info("At " + element.getAttribute("innerHTML") + " Exception occured at; " + exp.getMessage());
							    	}
							    }
						    }
							
							public static String getCurrentDateTime(){
								
								DateFormat dateFormat = new SimpleDateFormat("MM-yyyy-dd HH.mm.ss");
								Calendar cal = Calendar.getInstance();
								String dateFormate =dateFormat.format(cal.getTime());
								return dateFormate;
							}
							
							public static void handleCustomizationResi(String TV, String Internet, String Phone) throws Exception{
								
								try{
									if(!TV.isEmpty()){
										Log.info("Clicking on:-- "+ TV);
										driver.findElement(By.id(TV)).click();	
									}
						            if(!Internet.isEmpty()){
						            	Log.info("Clicking on:-- "+ Internet);
						            	driver.findElement(By.id(Internet)).click();
						            	Thread.sleep(10000);
									}
						            if(!Phone.isEmpty()){
						            	Log.info("Clicking on:-- "+ Phone);
						            	driver.findElement(By.id(Phone)).click();	
										//Thread.sleep(16000);
										//input("txtbx_enterYourNo_id","3146589053");
										//selectDropDown("phoneProvider_id", "AT&T");	
									}
						            /*if(!phoneProvider.isEmpty()){
						            	Log.info("Selecting :-- "+ phoneProvider);
										selectDropDown("phoneProvider_id",phoneProvider );	
									}*/
				     			}catch(Throwable t){
				     				ErrorUtil.addVerificationFailure(t);
				     				fail=true;
				     				Log.error(" -- " +t.getMessage());
				     				getScreen(document, fos, "Fail");
				     			    driver.quit();
				     				throw new Exception(" Stoping the script....!!!!");	
				     			}		
							}
							
                          public static void handleCustomizationBusiness(String TV, String Internet, String Phone  ) throws Exception{
								
								try{
									if(!TV.isEmpty()){
										Log.info("Clicking on:-- "+ TV);
										driver.findElement(By.id(TV)).click();	
									}
						            if(!Internet.isEmpty()){
						            	Log.info("Clicking on:-- "+ Internet);
						            	driver.findElement(By.id(Internet)).click();		
									}
						            if(!Phone.isEmpty()){
						            	Log.info("Clicking on:-- "+ Phone);
						            	driver.findElement(By.id(Phone)).click();	
										Thread.sleep(10000);
										input("txtbx_enterYourNo_id","2019271146");
										selectDropDown("phoneProvider_id", "AT&T");	
									}
				     			}catch(Throwable t){
				     				ErrorUtil.addVerificationFailure(t);
				     				fail=true;
				     				Log.error(" -- " +t.getMessage());
				     				getScreen(document, fos, "Fail");
				     			    driver.quit();
				     				throw new Exception(" Stoping the script....!!!!");	
				     			}		
							}
                          
                          public static String getSourceCode(String Url) throws IOException, Exception{
             				 URL url;
             					InputStream is = null;
             					BufferedReader br;
             					String line = null;
             					boolean flag = false;
             				 
             					 TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
             				            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
             				            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
             				            public void checkServerTrusted(X509Certificate[] certs, String authType) { }

             				        } };

             				        SSLContext sc = SSLContext.getInstance("SSL");
             				        sc.init(null, trustAllCerts, new java.security.SecureRandom());
             				        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

             				        // Create all-trusting host name verifier
             				        HostnameVerifier allHostsValid = new HostnameVerifier() {
             				            public boolean verify(String hostname, SSLSession session) { return true; }
             				        };
             				        // Install the all-trusting host verifier
             				        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
             				        /* End of the fix*/
             				      
             				        
             				    
             				    url = new URL(Url);
             					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
             					int i =conn.getResponseCode();
             					String str = Integer.toString(i);
             					Log.info(conn.getResponseCode());
             					if(conn.getResponseCode()!=404)
             					{
             					is = url.openStream(); // throws an IOException
             					br = new BufferedReader(new InputStreamReader(is));

             					while ((line = br.readLine()) != null) {
             						System.out.println(line);
             					}
            
             					}
             					else
             					{
             						/*datatable.setCellData("(855)757-7328", "Response", rowCount+1, str);
             						Log.info("Page not found.");*/
             					}

             				
             				    
         	                      return line;
             			 }
                          
                  public static void compare2URLs(String URL1, String URL2,String TestName) throws Exception{
          					
          	     		try{/*
          	     			Log.info("Comparing UI");
          	     			driver = new FirefoxDriver();
          	     		    eyes = new Eyes();
          	     			// This is your api key, make sure you use it in all your tests.
          	     			eyes.setApiKey("eWFrfwRig1mlVHxT9UscWJ9SuDXs3jgXMrWqAWMcbJA110");
          	     			eyes.setBaselineName("Comparison");
          	     			// Makes sure that the 1st URL is saved as baseline
          	     			eyes.setSaveFailedTests(true);
          	     			driver = eyes.open(driver, "App Name", TestName, new RectangleSize(1024, 768));
          	     			driver.get(URL1);
          	     			// Visual validation point #1
          	     			eyes.checkWindow("PageName");
          	     			// End visual testing. Validate visual correctness.
          	     			eyes.close(false);
          	     			// Makes sure that the second URL does not override the baseline.
          	     			eyes.setSaveFailedTests(false);
          	     			driver = eyes.open(driver, "App Name", TestName, new RectangleSize(1024, 768));
          	     			driver.get(URL2);
          	     			// Visual validation point #1
          	     			eyes.checkWindow("PageName");
          	     			// End visual testing. Validate visual correctness.
          	     			eyes.close();*/
          	     			
          	     			}catch(Throwable t){
          	     				ErrorUtil.addVerificationFailure(t);
          	     				fail=true;
          	     				Log.error("UI is not matched -- " +t.getMessage());
          	     				//throw new Exception("UI is not matched....!!!!");	
          	     			}
          	     		finally{
          	     		driver.quit();
          	     		//eyes.abortIfNotClosed();
          	     		}
          			 }    
                          
                          
			}		


