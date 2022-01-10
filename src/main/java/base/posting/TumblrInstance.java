package base.posting;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.Utils.getDriver;

public class TumblrInstance extends baseInstanceSocial {

    public TumblrInstance(String pattern, InstanceSocial instanceSocialValue) throws IOException {
        super("tumblr", pattern,instanceSocialValue);
    }

  


    @Override
    public String sendPost() {
        if (getLogin() == null || getPassword() == null) return getInstance() + "-F";
        List<File> img = getInstanceSocial().getImages();
        if (img.size() == 0) return getInstance() + "-F";
        if (getInstanceSocial().getText(false).isEmpty()) {
            System.out.println("text null");
            return getInstance() + "-F";
        }
        WebDriver webDriver = getDriver(false);
        try {
            JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
            WebDriverWait wait1 = new WebDriverWait(webDriver, 600);
            webDriver.get("https://www.tumblr.com/login");
            By element = By.xpath("//*[@name='email']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            webDriver.findElement(element).clear();
            webDriver.findElement(element).sendKeys(getLogin());
            element = By.xpath("//*[@name='password']");
            webDriver.findElement(element).clear();
            webDriver.findElement(element).sendKeys(getPassword());
            //   Thread.sleep(1000);
            executor1.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//*[text()='Log in']")));
            element = By.xpath("//a[@aria-label='New post']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            webDriver.get("https://www.tumblr.com/new/photo");
            element = By.xpath("//input[@type='file']");
//            Thread.sleep(1000);
//                        Robot robot=new Robot();
//            robot.keyPress(KeyEvent.VK_ENTER);
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            for (int i = 0; i < img.size(); i++) {
                executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
                Thread.sleep(2000);
                Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + img.get(i) + "\"");
                wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            }
            element = By.xpath("//*[@aria-label='Post title']");
            webDriver.findElement(element).sendKeys(getInstanceSocial().getUrl() + "\\n" +
                    getInstanceSocial().getText(true));
            element = By.xpath("//*[@aria-label='Post tags']");
            for (int i = 0; i < getInstanceSocial().getTagComma().size(); i++) {
                webDriver.findElement(element).sendKeys(getInstanceSocial().getTagComma().get(i));
            }
            element = By.xpath("//*[text()='Post']");
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            Thread.sleep(1000);
        } catch (Exception e) {
            return getInstance() + "-F";
        } finally {
//            webDriver.close();
//            webDriver.quit();
        }
        return getInstance() + "-T";
    }
}
