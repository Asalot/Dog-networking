package base.posting;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

import static com.Utils.getDriver;

public class TwitterInstance extends baseInstanceSocial {

    public TwitterInstance(String pattern, InstanceSocial instanceSocialValue){
        super("twitter", pattern,instanceSocialValue);
    }

    @Override
    public String sendPost() throws IOException, InterruptedException {
        if (getLogin() == null || getPassword() == null) return getInstance()+"-F";
        String img = getInstanceSocial().getImage();
        if(img==null) return getInstance()+"-F";

        String text=gettingText();
        if(text.isEmpty()){
            System.out.println("text null");return getInstance()+"-F"; }
          WebDriver webDriver = getDriver(false);
      //  WebDriver webDriver = getDriverFirefox(false) ;
        
    //    try {
            JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
            WebDriverWait wait1 = new WebDriverWait(webDriver, 600);
            webDriver.get("https://twitter.com/i/flow/login");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='text']")));
            webDriver.findElement(By.xpath("//input[@name='text']")).sendKeys(getLogin());
            executor1.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//span[contains(text(),'Next')]")));
            wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='password']")));
            webDriver.findElement(By.xpath("//input[@name='password']")).sendKeys(getPassword());
            //   Thread.sleep(1000);
            executor1.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//span[contains(text(),'Log in')]")));
            By xpathText = By.xpath("//div[@data-offset-key]//div[@data-offset-key]");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(xpathText));
            webDriver.navigate().refresh();
            wait1.until(ExpectedConditions.visibilityOfElementLocated(xpathText));
         //   webDriver.findElement(xpathText).clear();
     //   wait1.until(ExpectedConditions.visibilityOfElementLocated(xpathText));

        webDriver.findElement(xpathText).sendKeys(text);
     //       executor1.executeScript("arguments[0].value = arguments[0];", webDriver.findElement(xpathText),text);

           Thread.sleep(2000);
            executor1.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//div[@aria-label='Add photos or video']")));
            Thread.sleep(2000);
            Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + img + "\"");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-testid='attachments']")));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//span[text()='Tweet']")));
            Thread.sleep(2000);
     //   } catch (Exception e) {
     //       return getInstance()+"-F";
      //  } finally {
//            webDriver.close();
//            webDriver.quit();
      //  }
        return getInstance()+"-T";
    }

    private String gettingText() {
        String shortText =getInstanceSocial().getUrl();
        int index = getInstanceSocial().getText(true).indexOf("*");
        shortText += " "+((index != -1) ? getInstanceSocial().getText(true).substring(0, index) :
                getInstanceSocial().getText(true).substring(0, 35));
        shortText += "...";
        for (int i = 0; i < getInstanceSocial().getTag().size(); i++)
            if ((shortText + " " + getInstanceSocial().getTag().get(i)).length() > 280) break;
            else shortText += " " + getInstanceSocial().getTag().get(i);

      return shortText.trim();
    }
}

