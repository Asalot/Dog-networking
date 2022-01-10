package base.posting;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.Utils.getDriver;

public class LinkedinInstance extends baseInstanceSocial {

    public LinkedinInstance(String pattern, InstanceSocial instanceSocialValue) {
        super("linkedin", pattern,instanceSocialValue);
    }

    @Override
    public String sendPost() {
        if (getLogin() == null || getPassword() == null) return getInstance()+"-F";
        String img = getInstanceSocial().getImage();
        if(img==null) return getInstance()+"-F";

        if(getInstanceSocial().getText(false).isEmpty()){
            System.out.println("text null");return getInstance()+"-F"; }
        WebDriver webDriver = getDriver(false);
        try {
            JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
            WebDriverWait wait1 = new WebDriverWait(webDriver, 600);
            webDriver.get("https://www.linkedin.com/checkpoint/rm/sign-in-another-account?fromSignIn=true&trk=guest_homepage-basic_nav-header-signin");
            By element =By.xpath("//input[@id='username']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            webDriver.findElement(element).clear();
            webDriver.findElement(element).sendKeys(getLogin());
            webDriver.findElement(By.xpath("//input[@id='password']")).clear();
            webDriver.findElement(By.xpath("//input[@id='password']")).sendKeys(getPassword());
            //   Thread.sleep(1000);
            executor1.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//button[text()='Sign in']")));
            element = By.xpath("//*[text()='Start a post']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element= By.xpath("//div[contains(@data-placeholder,'What do you')]");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            WebElement text=webDriver.findElement(element);
            String text1=getInstanceSocial().getId()+" Needs our help: #adopt, #rescue, #foster, #share\r\n"+
                    getInstanceSocial().getUrl().substring(0,getInstanceSocial().getUrl().length()-1)+
                    "\r\n"+getInstanceSocial().getText(true);

             text.sendKeys(text1);
     //       text.sendKeys(getInstanceSocial().getId()+" Needs our help: #adopt, #rescue, #foster, #share\r\n");

       //     text.sendKeys(getInstanceSocial().getUrl().substring(0,getInstanceSocial().getUrl().length()-1));
            Thread.sleep(2000);
//            Robot robot=new Robot();
//            robot.keyPress(KeyEvent.VK_ENTER);
            //    webDriver.findElement(element).sendKeys(getInstanceSocial().getUrl());
          //  Thread.sleep(500);
 //           text.clear();
//            text.sendKeys(getInstanceSocial().getId()+" Needs our help: #adopt, #rescue, #foster, #share\r\n");
//            text.sendKeys(getInstanceSocial().getUrl().substring(0,getInstanceSocial().getUrl().length()-1));
//            text.sendKeys("\r\n"+getInstanceSocial().getText());

   //         element = By.xpath("//button[*[text()='Add to your post']]");
       //     executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
      //      wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
//            element = By.xpath("//*[text()='Add a photo']");
//            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
//
//            Thread.sleep(2000);
//            Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + img + "\"");
//                        element = By.xpath("//*[text()='Done']");
//            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
//            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));


  //          wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Saving dogs on Instagram')]")));


 //           Thread.sleep(2000);
//            element = By.xpath("//*[text()='Done']");
//            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
//            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));
            element = By.xpath("//*[text()='Post']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(element));

        } catch (Exception e) {
            return getInstance()+"-F";
        } finally {
//            webDriver.close();
//            webDriver.quit();
        }
        return getInstance()+"-T";
    }
}
