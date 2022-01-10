package base.posting;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.Utils.getDriver;


public class PinterestInstance extends baseInstanceSocial {

    public PinterestInstance(String pattern, InstanceSocial instanceSocialValue){
        super("pinterest", pattern,instanceSocialValue);
    }

    @Override
    public String sendPost() throws InterruptedException {
        if (getLogin() == null || getPassword() == null) return getInstance()+"-F";
        String img = getInstanceSocial().getImage();
        if(img==null) return getInstance()+"-F";
        
        if(getInstanceSocial().getText(false).isEmpty()){
            System.out.println("text null");return getInstance()+"-F"; }
        WebDriver webDriver = getDriver(false);
   //     try {
            JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
            WebDriverWait wait1 = new WebDriverWait(webDriver, 600);
            webDriver.get("https://www.pinterest.com/");

            By element =By.xpath("//div[text()='Log in']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element =By.xpath("//*[@id='email']");
            webDriver.findElement(element).clear();
            webDriver.findElement(element).sendKeys(getLogin());
            element = By.xpath("//*[@id='password']");
            webDriver.findElement(element).clear();
            webDriver.findElement(element).sendKeys(getPassword());
            element =By.xpath("//button[contains(@class,'red SignupButton')]");
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            Thread.sleep(2000);
            webDriver.get("https://www.pinterest.com/pin-builder/");
            element =By.xpath("//div[@data-test-id='media-empty-view']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            element =By.xpath("//*[@placeholder='Add your title']");
            webDriver.findElement(element).sendKeys(getInstanceSocial().getId()+" Needs our #help: #adopt, #rescue, #foster, #share");
            element =By.xpath("/*[contains(@id,'pin-draft-description')]//span");
            int index = (getInstanceSocial().getUrlPetharbor().contains("petharbor"))?
                    144:190;
            String text = getInstanceSocial().getText(false).substring(0, index)+"\\n"+getInstanceSocial().getTag();
            webDriver.findElement(element).sendKeys(text);
            element =By.xpath("//*[@placeholder='Add a destination link']");
            webDriver.findElement(element).sendKeys(getInstanceSocial().getUrl());
            element =By.xpath("//*[text()='Save']");
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element =By.xpath("//*[text()='Get it now']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
//        } catch (Exception e) {
//            return getInstance()+"-F";
//        } finally {
//            webDriver.close();
//            webDriver.quit();
  //      }
        return getInstance()+"-T";
    }
}
