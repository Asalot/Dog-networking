package base;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.Utils.getDriver;

public class TwitterInstance extends baseInstanceSocial {

    private InstanceSocial instanceSocial;
    private String text;

    public TwitterInstance(String pattern, InstanceSocial instanceSocialValue) throws IOException {
        super("twitter",pattern);
        setXpathButtonTweet(By.xpath("//span[text()='Tweet']"));
        instanceSocial=instanceSocialValue;
    }

    @Override
    public String sendPost(String text, List<File> files)  {
        if (getLogin() == null || getPassword() == null) return null;
        String img = "";
        try {
            img = files.stream().filter(el -> el.getName().contains(".jpeg")).collect(Collectors.toList()).get(0).getAbsolutePath();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }





        WebDriver webDriver = getDriver(false);
        try {
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
            webDriver.findElement(xpathText).clear();
            webDriver.findElement(xpathText).sendKeys(text.trim()+" ");
            Thread.sleep(2000);
            executor1.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//div[@aria-label='Add photos or video']")));
            Thread.sleep(2000);
            Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + img + "\"");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-testid='attachments']")));
            executor1.executeScript("arguments[0].click();", webDriver.findElement(getXpathButtonClick()));
            Thread.sleep(2000);
        } catch (Exception e) {
            return "twitter-F";
        } finally {
//            webDriver.close();
//            webDriver.quit();
        }
        return "twitter-T";
    }

    private void gettingText() throws IOException {
            String textTwitter = new String(Files.readAllBytes(Paths.get("src/main/resources/"+getInstance() + ".txt")));

            SimpleDateFormat dateFormat = new SimpleDateFormat("\n\ndd MMM yyyy hhaa");
            textTwitter = textTwitter.replace("<url>", );
            textTwitter = textTwitter.replace("<dogs>", String.valueOf(dogsNumbers));
            textTwitter = textTwitter.replace("<urgent>", String.valueOf(urgentDogs));

            int prevDogs = 0;
            String d = "yest";
            try {
                List<String> stream = Files.lines(file.toPath()).collect(Collectors.toList());
                for (int i = stream.size() - 1; i >= 0; i--) {
                    if (stream.get(i).contains("dogs in Needs") && prevDogs == 0) {
                        prevDogs = Integer.parseInt(stream.get(i).substring(0, stream.get(i).indexOf("dogs in") - 1));
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            textTwitter = textTwitter.replace("<dif>", String.valueOf(dogsNumbers-prevDogs));

            FileWriter myWriter = new FileWriter(file.getAbsoluteFile(), true);
            myWriter.append(textTwitter);
            text=textTwitter;
            myWriter.close();
        }
    }
}
