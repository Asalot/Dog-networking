package base;

import base.posting.FacebookInstance;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.Utils.PATH_DOWNLOAD;
import static com.Utils.PATH_SCREEN;

public class FacebookLoader extends baseLoader  {

    private String file;

    public FacebookLoader(String instanceValue, Date dateValue, String patternValue, List<DogInfoPetharbor> filesValue, String gifFile) {
        super(instanceValue, dateValue, patternValue, filesValue);
        file=gifFile;
    }
    private String text;

    @Override
    public void setSettingsAndDownload() {

    }

    @Override
    public String loadToDrive() {
        return null;
    }

    @Override
    protected void additionalSetUp() {

    }

    @Override
    public void setUpText( int dogsNumbers, int urgentDogs){}

    public void setUpText(int dogsNumbers, int urgentDogs, String weblink) throws IOException {
        String textTwitter = new String(Files.readAllBytes(Paths.get("src/main/resources/"+getInstance() + getPattern() + ".txt")));

        File file = new File("./"+getInstance() + getPattern() + ".txt");
        file.createNewFile();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hhaa");
        textTwitter = textTwitter.replaceAll("<date>", dateFormat.format(new Date()));
        textTwitter = textTwitter.replace("<dogs>", String.valueOf(dogsNumbers));
        textTwitter = textTwitter.replace("<urgent>", String.valueOf(urgentDogs));
        textTwitter = textTwitter.replace("<weblink>", weblink);
        textTwitter = textTwitter.replace("<list>", getFiles().stream().map(el->(el.getId().contains("#")?el.getId():"#"+el.getId()).concat(" - "+el.getMainInfoGAB())).collect(Collectors.joining("\r\n")));

        FileWriter myWriter = new FileWriter(file.getAbsoluteFile(), true);
        myWriter.append(textTwitter);
        text=textTwitter;
        myWriter.close();
    }

    @Override
    public void setSettingsAndDownloadAdd() {

    }

    @Override
    public void sendPost() throws IOException, InterruptedException {
        loadFiles();
        if(driver==null)return;
        JavascriptExecutor executor1 = (JavascriptExecutor) driver;
        WebDriverWait wait1 = new WebDriverWait(driver, 600);
        By  element = By.xpath("//div[@data-offset-key]//div[@data-offset-key]");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(element));

        driver.findElement(element).sendKeys(text);
//        element = By.xpath("//*[text()='Post']");
//        executor1.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(element));
//        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
//        executor1.executeScript("arguments[0].click();", driver.findElement(element));
//        Thread.sleep(2000);
    }

    @Override
    public void loadFiles() throws InterruptedException, IOException {
        FacebookInstance facebook = new FacebookInstance("", null);
        driver = facebook.loginGetPage();
        if (driver == null) return;

        JavascriptExecutor executor1 = (JavascriptExecutor) driver;
        WebDriverWait wait1 = new WebDriverWait(driver, 600);
        By element = By.xpath("//*[contains(text(),'on your mind')]");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        driver.get("https://www.facebook.com/DogsMatterNetwork/");

        element = By.xpath("//*[text()='Create post']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(element));
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        executor1.executeScript("arguments[0].click();", driver.findElement(element));
        element = By.xpath("//*[contains(text(),'Write something')]");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
        element = By.xpath("//*[@aria-label='Photo/Video']");
        executor1.executeScript("arguments[0].click();", driver.findElement(element));
        element = By.xpath("//*[text()='Add Photos/Videos']");
        wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
//        if (!file.isEmpty()) {
//            driver.findElement(element).click();
//            Thread.sleep(2000);
//            Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + file + "\"");
//            element = By.xpath("//img[@alt='" + file.substring(file.lastIndexOf("\\") + 1) + "']");
//            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
//        }

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy HH-mm");
        String path = PATH_SCREEN + getPattern() + "\\" + dateFormat2.format(getDate()) + "\\";
        File dir = new File(path);
        int count = 1;
        String fileFirst = "";

         for (File f : dir.listFiles()) {
            if (f.isDirectory() || f.getName().contains(".txt")) continue;
            if (getFiles() != null && getFiles().stream().filter(el -> f.getName().contains(el.getId())).collect(Collectors.toList()).size() == 0)
                continue;
            fileFirst = f.getName();
            element = By.xpath("//*[text()='Add Photos/Videos']");
            Thread.sleep(2000);
            driver.findElement(element).click();
            Thread.sleep(2000);
            Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + path + f.getName() + "\"");
            element = By.xpath("//img[@alt='"+f.getName()+"']");
            wait1.until(ExpectedConditions.visibilityOfElementLocated(element));
            break;
        }
        List<List<String>> listF = new ArrayList<>();
        listF.add(new ArrayList<>());
        int ii = 0;
        for (File f : dir.listFiles()) {
            if (f.isDirectory() || f.getName().contains(".txt")) continue;
            if (getFiles() != null && getFiles().stream().filter(el -> f.getName().contains(el.getId())).collect(Collectors.toList()).size() == 0)
                continue;
            if (fileFirst.equals(f.getName())) continue;
            if (listF.get(ii).size() == 15) {
                listF.add(new ArrayList<>());
                ii++;
            }
            listF.get(ii).add("\"" + f.getName() + "\" ");
        }
        for (List<String> ll : listF) {
            String xx = StringUtils.join(ll, "").trim();
            count += ll.size();
            if (xx.isEmpty() || xx.trim().equals("")) break;
            element = By.xpath("//*[text()='Add Photos/Videos']");
            driver.findElement(element).click();
            Thread.sleep(3000);
            Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + xx);
            wait1.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//*[@aria-label='Attached media']//img"),(count>5)?5:count));
        }
        Thread.sleep(4000);
    }
}
