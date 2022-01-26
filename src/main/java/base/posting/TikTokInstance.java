package base.posting;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.stream.Collectors;

import static com.Utils.getDriver;


public class TikTokInstance extends baseInstanceSocial {

    public TikTokInstance(String pattern, InstanceSocial instanceSocialValue) {
        super("tiktok", pattern, instanceSocialValue);
    }
    public TikTokInstance(String pattern) {
        super("tiktok", pattern);
    }

    @Override
    public String sendPost() throws InterruptedException, IOException {
        if (getLogin() == null || getPassword() == null) return getInstance() + "-F";
        String video = getInstanceSocial().getVideo().get(0).getAbsolutePath();
        if (video == null) return getInstance() + "-F";

        if (getInstanceSocial().getText(false).isEmpty()) {
            System.out.println("text null");
            return getInstance() + "-F";
        }

        WebDriver webDriver=(getInstanceDriver()==null)?getDriver(false):getInstanceDriver();
        // webDriver.manage().window().maximize();
        JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
        WebDriverWait wait1 = new WebDriverWait(webDriver, 600);
        By element;
        if(getInstanceDriver()==null) {
            webDriver.get("https://www.tiktok.com/");

            element = By.xpath("//*[text()='Log in']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element = By.xpath("//*[text()='Use phone / email / username']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element = By.xpath("//*[text()='Log in with email or username']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element = By.xpath("//*[@name='email']");
            webDriver.findElement(element).clear();
            webDriver.findElement(element).sendKeys(getLogin());
            element = By.xpath("//*[@name='password']");
            webDriver.findElement(element).clear();
            webDriver.findElement(element).sendKeys(getPassword());
            element = By.xpath("//button[contains(@class,'login-button')]");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));

            element = By.xpath("//button[contains(@class,'red SignupButton')]");
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            // Thread.sleep(1000);
        }
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(" //input[@placeholder='Search']")));
        webDriver.get("https://www.pinterest.com/pin-builder/");
        element = By.xpath("//div[@data-test-id='media-empty-view']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        element = By.xpath("//*[@placeholder='Add your title']");
        webDriver.findElement(element).sendKeys(getInstanceSocial().getId() + " Needs our help: adopt, rescue, foster, share");
        element = By.xpath("//*[contains(@id,'pin-draft-description')]//span");
        executor1.executeScript("arguments[0].scrollIntoView(true);", webDriver.findElement(element));
        webDriver.findElement(element).sendKeys(gettingText());
        element = By.xpath("//*[@placeholder='Add a destination link']");
        webDriver.findElement(element).sendKeys(getInstanceSocial().getUrl());
        element = By.xpath("//*[contains(@id,'media-upload-input')]");
        executor1.executeScript("arguments[0].scrollIntoView(true);", webDriver.findElement(element));

        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        Thread.sleep(2000);
        Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + getInstanceSocial().getImage() + "\"");
        element = By.xpath("//*[@data-test-id='media-image-view']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));

        element = By.xpath("//*[text()='Save']");
        executor1.executeScript("arguments[0].scrollIntoView(true);", webDriver.findElement(element));
        executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
        element = By.xpath("//*[contains(text(),'it now')]");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        //       webDriver.manage().window().setPosition(new Point(0, -1000));
        if(getInstanceDriver()==null)setDriver(webDriver);
        return getInstance() + "-T";
    }

    private String gettingText() {
        String tag = getInstanceSocial().getTag().stream().collect(Collectors.joining(" "));
        String shortText = getInstanceSocial().getText(true).substring(0, 490 - tag.length());
        if(!shortText.substring(shortText.length()-1).equals(".") &&
                !shortText.substring(shortText.length()-1).equals(",") && shortText.substring(shortText.length()-1).equals(" ")) {
            int last = -1;
            for (int i = shortText.length() - 1; i >= 0; i--) {
                if (shortText.charAt(i) == '.' || shortText.charAt(i) == ',' || shortText.charAt(i) == ' ') {
                    last = i;
                    break;
                }
            }
            shortText=shortText.substring(0,last);
        }
        return shortText.trim()+"...\r\n"+tag;
    }
}
