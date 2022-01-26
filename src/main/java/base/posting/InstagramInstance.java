package base.posting;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

import static com.Utils.getDriver;


public class InstagramInstance extends baseInstanceSocial {

    public InstagramInstance(String pattern, InstanceSocial instanceSocialValue) {
        super("instagram", pattern, instanceSocialValue);
    }

    public InstagramInstance(String pattern) {
        super("instagram",pattern);
    }

    @Override
    public String sendPost() throws InterruptedException, IOException {
        if (getLogin() == null || getPassword() == null) return getInstance() + "-F";
        String img = getInstanceSocial().getImage();
        if (img == null) return getInstance() + "-F";

        if (getInstanceSocial().getText(true).isEmpty()) {
            System.out.println("text null");
            return getInstance() + "-F";
        }

        WebDriver webDriver = (getInstanceDriver() == null) ? getDriver(false) : getInstanceDriver();
        // webDriver.manage().window().maximize();
        JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
        WebDriverWait wait1 = new WebDriverWait(webDriver, 600);
        By element;
        if(getInstanceDriver()==null) {
            webDriver.get("https://www.instagram.com/");
            element = By.xpath("//*[@name='username']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));

//            webDriver.findElement(element).clear();
            webDriver.findElement(element).sendKeys(getLogin());
            element = By.xpath("//*[@name='password']");
         //   webDriver.findElement(element).clear();
            webDriver.findElement(element).sendKeys(getPassword());
            element = By.xpath("//*[text()='Log In']");
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        }
        element = By.xpath("//*[text()='Not Now']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));

        element = By.xpath("//button[.//@aria-label='New Post']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));

        element = By.xpath("//button[text()='Select from computer']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
     //   executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        webDriver.findElement(element).click();

        Thread.sleep(2000);
        Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + getInstanceSocial().getImage() + "\"");
    
        element = By.xpath("//button[.//*[@aria-label='Select Crop']]");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));

        element = By.xpath("//button[.//*[@aria-label='Photo Outline Icon']]");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));

        element = By.xpath("//*[text()='Next']");
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        Thread.sleep(2000);
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        element = By.xpath("//*[@aria-label='Write a caption...']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        webDriver.findElement(element).sendKeys(getInstanceSocial().getText(true));
     //   element = By.xpath("//*[@placeholder='Add location']");
     //  wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
     //   webDriver.findElement(element).sendKeys("OC Animal Care");

       // executor1.executeScript("arguments[0].value='OC Animal Care';", element);
        element = By.xpath("//*[text()='Share']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        element = By.xpath("//*[text()='Your post has been shared.']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        element = By.xpath("//button[.//*[@aria-label='Close']]");
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        if (getInstanceDriver() == null) setDriver(webDriver);
        return getInstance() + "-T";
    }
}
