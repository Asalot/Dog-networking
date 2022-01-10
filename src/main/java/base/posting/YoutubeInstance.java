package base.posting;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.Utils.getDriver;


public class YoutubeInstance extends baseInstanceSocial {

    public YoutubeInstance(String pattern, InstanceSocial instanceSocialValue){
        super("youtube", pattern,instanceSocialValue);
    }

    @Override
    public String sendPost()  {
        if (getLogin() == null || getPassword() == null) return getInstance()+"-F";
        String img = "";
        try {
            img = getInstanceSocial().getVideo().get(0).getAbsolutePath();
        } catch (Exception e) {
            System.out.println(e);
            return getInstance()+"-F";
        }
        if(getInstanceSocial().getText(false).isEmpty()){
            System.out.println("text null");return getInstance()+"-F"; }
        WebDriver webDriver = getDriver(false);
        try {
            JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
            WebDriverWait wait1 = new WebDriverWait(webDriver, 600);
            webDriver.get("https://accounts.google.com/ServiceLogin/identifier?service=youtube&uilel=3&passive=true&continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Faction_handle_signin%3Dtrue%26app%3Ddesktop%26hl%3Den%26next%3Dhttps%253A%252F%252Fwww.youtube.com%252F&hl=en&ec=65620&flowName=GlifWebSignIn&flowEntry=ServiceLogin");
            By element =By.xpath("//input[@type='email']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            webDriver.findElement(element).clear();
            webDriver.findElement(element).sendKeys(getLogin());
            element = By.xpath("//button[span[text()='Next']]");
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element =By.xpath("//input[@type='password']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            webDriver.findElement(element).clear();
            webDriver.findElement(element).sendKeys(getPassword());
            element = By.xpath("//button[span[text()='Next']]");
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element = By.xpath("//*[@id='logo']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            webDriver.get("https://studio.youtube.com/channel/UClj3GoZ4qTY4os5sYnGHlBQ/videos/upload?d=ud&filter=%5B%5D&sort=%7B%22columnType%22%3A%22date%22%2C%22sortOrder%22%3A%22DESCENDING%22%7D");
            element = By.xpath("//div[text()='Select files']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            Thread.sleep(2000);
            Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + img + "\"");
            element = By.xpath("//*[@id='textbox'][1]");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            webDriver.findElement(element).sendKeys("📣 🆘 "+getInstanceSocial().getId()+" Needs our help: adopt, rescue, foster, share");
            element = By.xpath("//*[@id='textbox'][2]");
            webDriver.findElement(element).sendKeys(getInstanceSocial().getUrl()+"\\n"+
                    getInstanceSocial().getText(false));
            element = By.xpath("//*[@name='VIDEO_MADE_FOR_KIDS_NOT_MFK']");
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element = By.xpath("//div[text()='Processing video...']");
            wait1.until(ExpectedConditions.invisibilityOfElementLocated(element));
            element = By.xpath("//div[text()='Next']");
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element = By.xpath("//*[@id=\"privacy-radios\"]/tp-yt-paper-radio-button[3]");
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element = By.xpath("//div[text()='Publish']");
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element = By.xpath("//h1[@id='dialog-title']]");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        } catch (Exception e) {
            return getInstance()+"-F";
        } finally {
//            webDriver.close();
//            webDriver.quit();
        }
        return getInstance()+"-T";
    }
}
