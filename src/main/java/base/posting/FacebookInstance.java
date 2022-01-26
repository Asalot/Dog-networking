package base.posting;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

import static com.Utils.getDriver;

public class FacebookInstance extends baseInstanceSocial {

    public FacebookInstance(String pattern, InstanceSocial instanceSocialValue) throws IOException {
        super("facebook", pattern,instanceSocialValue);

    }

    @Override
    public String sendPost() throws IOException, InterruptedException {
        if (getLogin() == null || getPassword() == null) return getInstance() + "-F";
        String img = getInstanceSocial().getImage();
        if (img == null) return getInstance() + "-F";

        if (getInstanceSocial().getText(false).isEmpty()) {
            System.out.println("text null");
            return getInstance() + "-F";
        }

        WebDriver webDriver;
        if(getInstanceDriver()==null) {
            webDriver=loginGetPage();
            if(webDriver==null)return getInstance() + "-F";
        }else webDriver=getInstanceDriver();
        // webDriver.manage().window().maximize();
        JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
        WebDriverWait wait1 = new WebDriverWait(webDriver, 600);
        By element = By.xpath("//*[contains(text(),'on your mind')]");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        webDriver.get("https://www.facebook.com/DogsMatterNetwork/");
        element = By.xpath("//*[text()='Create post']");
        executor1.executeScript("arguments[0].scrollIntoView(true);", webDriver.findElement(element));
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        element = By.xpath("//*[contains(text(),'Write something')]");
        webDriver.findElement(element).sendKeys(getInstanceSocial().getText(true));
        element = By.xpath("//*[@aria-label='Photo/Video']");
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        element = By.xpath("//*[text()='Add Photos/Videos']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
   //     String files=getInstanceSocial().getFiles().stream().map(el->"'"+el.getAbsolutePath()+"'").collect(Collectors.joining(" "));

        Thread.sleep(2000);
        Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + getInstanceSocial().getImage() + "\"");

        element = By.xpath("//img[@alt='"+getInstanceSocial().getImage()
                .substring(getInstanceSocial().getImage().lastIndexOf("\\"))+"']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        element = By.xpath("//div[@role='button'][.//span[text()='Post']]");
        executor1.executeScript("arguments[0].scrollIntoView(true);", webDriver.findElement(element));
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
      //  executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        webDriver.findElement(element).click();
        Thread.sleep(2000);

        if(getInstanceDriver()==null)setDriver(webDriver);
        return getInstance() + "-T";
    }

    public String sendMultyImagePost() throws IOException, InterruptedException {
        if (getLogin() == null || getPassword() == null) return getInstance() + "-F";
        WebDriver webDriver;
        if(getInstanceDriver()==null) {
            webDriver=loginGetPage();
            if(webDriver==null)return getInstance() + "-F";
        }else webDriver=getInstanceDriver();
        // webDriver.manage().window().maximize();
        JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
        WebDriverWait wait1 = new WebDriverWait(webDriver, 600);
        By element = By.xpath("//*[contains(text(),'on your mind')]");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        webDriver.get("https://www.facebook.com/DogsMatterNetwork/");
        element = By.xpath("//*[text()='Create post']");
        executor1.executeScript("arguments[0].scrollIntoView(true);", webDriver.findElement(element));
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        element = By.xpath("//*[contains(text(),'Write something')]");
        webDriver.findElement(element).sendKeys(getInstanceSocial().getText(true));
        element = By.xpath("//*[@aria-label='Photo/Video']");
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        element = By.xpath("//*[text()='Add Photos/Videos']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        //     String files=getInstanceSocial().getFiles().stream().map(el->"'"+el.getAbsolutePath()+"'").collect(Collectors.joining(" "));

        Thread.sleep(2000);
        Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + getInstanceSocial().getImage() + "\"");

        element = By.xpath("//img[@alt='"+getInstanceSocial().getImage()
                .substring(getInstanceSocial().getImage().lastIndexOf("\\"))+"']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        element = By.xpath("//*[text()='Post']");
        executor1.executeScript("arguments[0].scrollIntoView(true);", webDriver.findElement(element));
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        Thread.sleep(2000);

        if(getInstanceDriver()==null)setDriver(webDriver);
        return getInstance() + "-T";
    }

    public WebDriver loginGetPage() {
        if (getLogin() == null || getPassword() == null) return null;
        WebDriver webDriver = getDriver(false);
        JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
        WebDriverWait wait1 = new WebDriverWait(webDriver, 600);
        webDriver.get("https://www.facebook.com/login");
        By element = By.xpath("//*[@id='email']");
        webDriver.findElement(element).clear();
        webDriver.findElement(element).sendKeys(getLogin());
        element = By.xpath("//*[@id='pass']");
        webDriver.findElement(element).clear();
        webDriver.findElement(element).sendKeys(getPassword());
        element = By.xpath("//button[text()='Log In']");
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        element = By.xpath("//*[contains(text(),'on your mind')]");
        try {
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        } catch (TimeoutException e) {
            return null;
        }
        return webDriver;
    }
}
