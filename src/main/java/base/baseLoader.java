package base;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.Utils.getDriver;

public abstract class baseLoader{

    private String instance;
    public WebDriver driver;
    public WebDriverWait wait;
    public JavascriptExecutor executor;

    public String getInstance() {
        return instance;
    }

    public Date getDate() {
        return date;
    }

    public String getPattern() {
        return pattern;
    }

    private Date date;
    private String pattern;

    public base.baseInstance getBaseInstance() {
        return baseInstance;
    }

    private base.baseInstance baseInstance;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String fileName;

    public int getNumberFiles() {
        return numberFiles;
    }

    public void setNumberFiles(int numberFiles) {
        this.numberFiles = numberFiles;
    }

    private int numberFiles;

    public baseLoader(String instanceValue,Date dateValue,String patternValue) {
        instance= instanceValue;
        driver = getDriver(false);
        wait = new WebDriverWait(driver, 600);
        executor = (JavascriptExecutor) driver;
        if(instance.equals("twitter")) baseInstance =new TwitterInstance();
        else   baseInstance =new PDFInstance();
        date=dateValue;
        pattern=patternValue;
    }

    public void loadFiles() throws InterruptedException, IOException{
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy HH-mm");
        path = path.substring(0, path.length() - 1) + "Screens" + getPattern() + "\\" + dateFormat2.format(getDate()) + "\\";

        driver.get(baseInstance.getUrl());
        additionalSetUp();
        File dir = new File(path);
        int count = 1;
        for (File f : dir.listFiles()) {
//            if (f.isDirectory() || f.getName().contains(".txt") || (redList!=null && redList.size()>0 && redList.toString()
//                    .indexOf(f.getName().substring(f.getName().indexOf("#")))==-1)) continue;
            if (f.isDirectory() || f.getName().contains(".txt"))continue;
            Thread.sleep(2000);
            driver.findElement(By.xpath(baseInstance.getXpathLoadButton())).click();
            Thread.sleep(2000);
            Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + path + f.getName() + "\"");
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(baseInstance.getXpathNumberOfLoadedFiles()), count));
             //  wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("#frames img"), count));
            break;
        }
        boolean isFirst = true;
        List<List<String>> listF = new ArrayList<>();
  //      String F = "";
        listF.add(new ArrayList<>());
        int ii = 0;
        for (File f : dir.listFiles()) {
            if (f.isDirectory() || f.getName().contains(".txt"))continue;
            if (isFirst) {
                isFirst = false;
                continue;
            }
            if (listF.get(ii).size() == 15) {
                listF.add(new ArrayList<>());
                ii++;
            }
            listF.get(ii).add("\"" + f.getName() + "\" ");
     //       F += "\"" + path + f.getName() + "\" ";
        }
        for (List<String> ll : listF) {
            String xx = StringUtils.join(ll, "").trim();
            count += ll.size();
            if (xx.isEmpty()) break;
            driver.findElement(By.xpath(baseInstance.getXpathLoadButton())).click();
            Thread.sleep(2000);
            Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + xx);
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(baseInstance.getXpathNumberOfLoadedFiles()), count));
        }
        setNumberFiles(count);
    }

    public abstract void setSettingsAndDownload();
    public abstract String loadToDrive();
    protected abstract void additionalSetUp();

    public abstract File findDownloadedFile() throws InterruptedException;

    public abstract void setUpText(int dogsNumbers, int urgentDogs) throws IOException;

    public abstract void setSettingsAndDownloadAdd();

    public void closeDriver(){
        driver.close();
        driver.quit();
    }
}
