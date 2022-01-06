package base;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;

import static com.Utils.getAccount;
import static com.Utils.getDriver;

public class TwitterInstance extends baseInstance {

    public TwitterInstance() {
        setUrl("https://gifmaker.me/");
        setXpathLoadButton("//div[@id='browse']");
        setXpathNumberOfLoadedFiles("//div[@id='frames']//img");
        setXpathButtonTweet(By.xpath("//span[text()='Tweet']"));
    }

     public String sendPost(String pattern, String text, List<String> files) throws IOException, InterruptedException {
        List<String> account=getAccount("twitter", pattern);
        if ( account== null || files.size()==0) return null;
         WebDriver webDriver = getDriver(false);
         try {
             JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
            WebDriverWait wait1 = new WebDriverWait(webDriver, 600);
            webDriver.get("https://twitter.com/i/flow/login");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='text']")));
            webDriver.findElement(By.xpath("//input[@name='text']")).sendKeys(account.get(1));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//span[contains(text(),'Next')]")));
            wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='password']")));
            webDriver.findElement(By.xpath("//input[@name='password']")).sendKeys(account.get(2));
            //   Thread.sleep(1000);
            executor1.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//span[contains(text(),'Log in')]")));
            By xpathText = By.xpath("//div[@data-offset-key]//div[@data-offset-key]");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(xpathText));
            webDriver.navigate().refresh();
            wait1.until(ExpectedConditions.visibilityOfElementLocated(xpathText));
            webDriver.findElement(xpathText).clear();
            webDriver.findElement(xpathText).sendKeys(text.trim() + " " + files.get(0));
//         executor1.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//div[@aria-label='Add photos or video']")));
//         Thread.sleep(2000);
            // Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + getFileName() + "\"");
            // wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-testid='attachments']")));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(getXpathButtonTweet()));
            Thread.sleep(2000);
        }
         catch(Exception e){return "twitter-F";}
        finally {
            webDriver.close();
            webDriver.quit();
        }
          return "twitter-T";
     }
}
