package WebConnector;

import java.io.FileInputStream;
import org.testng.Assert;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.*;
import io.cucumber.core.api.Scenario;

public class webconnector<V> {

    public static WebDriver driver = null;
    public SessionId session = null;
    public static Properties prop = new Properties();

    public webconnector() {
        try {
            prop.load(new FileInputStream("./src/test/config/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WebDriver getDriver() {
        return this.getDriver();
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void setUpDriver() {
        String browser = prop.getProperty("browser");
        if (browser == null) {
            browser = "chrome";
        }
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("start-maximized");
                //session = ((ChromeDriver)driver).getSessionId();
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                driver.manage().window().maximize();
                //session = ((FirefoxDriver)driver).getSessionId();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                driver.manage().window().maximize();
                //session = ((FirefoxDriver)driver).getSessionId();
                break;
            default:
                throw new IllegalArgumentException("Browser \"" + browser + "\" isn't supported.");
        }
    }

    public void closeDriver(Scenario scenario) {
        if (scenario.isFailed()) {
            saveScreenshotsForScenario(scenario);
        }
        driver.close();
    }

    private void saveScreenshotsForScenario(final Scenario scenario) {
        final byte[] screenshot = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenshot, "image/png");
    }

    public void waitForPageLoad(int timeout) {
        ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";");
    }

    public String getSpecificColumnData(String FilePath, String SheetName, String ColumnName) throws InvalidFormatException, IOException {
        FileInputStream fis = new FileInputStream(FilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheet(SheetName);
        XSSFRow row = sheet.getRow(0);
        int col_num = -1;
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (row.getCell(i).getStringCellValue().trim().equals(ColumnName))
                col_num = i;
        }
        row = sheet.getRow(1);
        XSSFCell cell = row.getCell(col_num);
        String value = cell.getStringCellValue();
        fis.close();
        System.out.println("Value of the Excel Cell is - " + value);
        return value;
    }

    public void setSpecificColumnData(String FilePath, String SheetName, String ColumnName) throws IOException {
        FileInputStream fis;
        fis = new FileInputStream(FilePath);
        FileOutputStream fos = null;
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheet(SheetName);
        XSSFRow row = null;
        XSSFCell cell = null;
        XSSFFont font = workbook.createFont();
        XSSFCellStyle style = workbook.createCellStyle();
        int col_Num = -1;
        row = sheet.getRow(0);
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (row.getCell(i).getStringCellValue().trim().equals(ColumnName)) {
                col_Num = i;
            }
        }
        row = sheet.getRow(1);
        if (row == null)
            row = sheet.createRow(1);
        cell = row.getCell(col_Num);
        if (cell == null)
            cell = row.createCell(col_Num);
        font.setFontName("Comic Sans MS");
        font.setFontHeight(14.0);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREEN.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);
        cell.setCellValue("PASS");
        fos = new FileOutputStream(FilePath);
        workbook.write(fos);
        fos.close();
    }

    public By getElementWithLocator(String WebElement) throws Exception {
    	System.out.println("WebElement "+WebElement);
        String locatorTypeAndValue = prop.getProperty(WebElement); 
        String[] locatorTypeAndValueArray = locatorTypeAndValue.split(",",2);
        String locatorType = locatorTypeAndValueArray[0].trim();
        String locatorValue = locatorTypeAndValueArray[1].trim();
        System.out.println("locatorType "+locatorType);
        System.out.println("locatorValue "+locatorValue);
        
        switch (locatorType.toUpperCase()) {
            case "ID":
                return By.id(locatorValue);
            case "NAME":
                return By.name(locatorValue);
            case "TAGNAME":
                return By.tagName(locatorValue);
            case "LINKTEXT":
                return By.linkText(locatorValue);
            case "PARTIALLINKTEXT":
                return By.partialLinkText(locatorValue);
            case "XPATH":
                return By.xpath(locatorValue);
            case "CSS":
                return By.cssSelector(locatorValue);
            case "CLASSNAME":
                return By.className(locatorValue);
            default:
                return null;
        }
    }

    public WebElement FindAnElement(String WebElement) throws Exception {
      //  Thread.sleep(1000);
        WebDriverWait wait=new WebDriverWait(driver,100);
        wait.until(ExpectedConditions.presenceOfElementLocated(getElementWithLocator(WebElement)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(getElementWithLocator(WebElement)));
     //   wait.until(ExpectedConditions.elementToBeClickable(getElementWithLocator(WebElement)));
        return driver.findElement(getElementWithLocator(WebElement));
    }

    public String getPageTitle() {
        String title = driver.getTitle();
        return title;
    }
    
    public void PerformActionOnWindow() throws InterruptedException {
    	
    	String CurrentWindow=driver.getWindowHandle();
    	Set<String> WindowIds = driver.getWindowHandles();
    	Iterator itr= WindowIds.iterator();
    	while(itr.hasNext()) {
    		String targtWindow= (String) itr.next();
    		if(!CurrentWindow.equalsIgnoreCase(targtWindow)) {
    			driver.switchTo().window(targtWindow);
    		}
    	}

    	}
 public void PerformActionOnElement(String WebElement, String Action, String Text) throws Exception {
        switch (Action) {
        case "SwitchWindow":
                   PerformActionOnWindow();
            break;
        case "verifyElementPresent":
        	boolean flag = FindAnElement(WebElement).isDisplayed();
        	Assert.assertTrue(flag,"Element Not Exists");
        	
        	 break;
            
        case "getText":
        	  System.out.println(FindAnElement(WebElement).getText());
        	break;
        
            case "Click":
            	Thread.sleep(2000);
            	WebDriverWait wait=new WebDriverWait(driver,100);
                wait.until(ExpectedConditions.elementToBeClickable(getElementWithLocator(WebElement)));
                
                FindAnElement(WebElement).click();
           //   WindowHandling(driver.getWindowHandle());
                break;
            case "ClickIfExists":
                try {
                    if(FindAnElement(WebElement).isDisplayed()){
                    	
                       FindAnElement(WebElement).click();
                    }
                }catch(NoSuchFieldException e){

                }
                break;
            case "Type":
            	
                FindAnElement(WebElement).sendKeys(Text);
                FindAnElement(WebElement).sendKeys(Keys.TAB);
                break;
            case "Clear":
                FindAnElement(WebElement).clear();
                break;
            case "Select":
                Select s;
				new Select(FindAnElement(WebElement)).selectByVisibleText(Text);
				break;
            case "WaitForElementDisplay":
                waitForCondition("Presence", WebElement, 60);
                break;
            case "WaitForElementClickable":
                waitForCondition("Clickable", WebElement, 60);
                break;
            case "ElementNotDisplayed":
                waitForCondition("NotPresent", WebElement, 60);
                break;
            default:
                throw new IllegalArgumentException("Action \"" + Action + "\" isn't supported.");
        }
    }

    public void waitForCondition(String TypeOfWait, String WebElement, int Time) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Time, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS).ignoring(Exception.class);
            switch (TypeOfWait) {
                case "PageLoad":
                    wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
                    break;
                case "Clickable":
                    wait.until(ExpectedConditions.elementToBeClickable(FindAnElement(WebElement)));
                    break;
                case "Presence":
                    wait.until(ExpectedConditions.presenceOfElementLocated(getElementWithLocator(WebElement)));
                    break;
                case "Visibility":
                    wait.until(ExpectedConditions.visibilityOfElementLocated(getElementWithLocator(WebElement)));
                    break;
                case "NotPresent":
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(getElementWithLocator(WebElement)));
                    break;
                default:
                    Thread.sleep(Time * 1000);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("wait For Condition \"" + TypeOfWait + "\" isn't supported.");
        }
    }
}
