package base;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.Utils.*;

public abstract class baseLoader {

    private String instance;
    public WebDriver driver;
    public WebDriverWait wait;
    public JavascriptExecutor executor1;

    public List<DogInfoPetharbor> getFiles() {
        return files;
    }

    private List<DogInfoPetharbor> files;

    public String getExtension() {
        return extension;
    }

    private String extension;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("MMMdd-HH-mm");

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

    public baseLoader(String instanceValue, Date dateValue, String patternValue, List<DogInfoPetharbor> filesValue) {
        instance = instanceValue;
        driver = (instance.equals("facebook"))?null:getDriver(false);
        wait = (instance.equals("facebook"))?null:new WebDriverWait(driver, 600);
        executor1 = (instance.equals("facebook"))?null:(JavascriptExecutor) driver;
        if (instance.equals("pdf")) {
            baseInstance = new PDFInstance();
            extension = "pdf";
        }else {
            baseInstance = new GifInstance();
            extension = "gif";
        }
        date = dateValue;
        pattern = patternValue;
        files = filesValue;
    }

    public void loadFiles() throws InterruptedException, IOException {
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy HH-mm");
        String path = PATH_SCREEN + getPattern() + "\\" + dateFormat2.format(getDate()) + "\\";

        driver.get(baseInstance.getUrl());
        additionalSetUp();
        File dir = new File(path);
        int count = 1;
        String fileFirst = "";
        for (File f : dir.listFiles()) {
            if (f.isDirectory() || f.getName().contains(".txt")) continue;
            if (files != null && files.stream().filter(el -> f.getName().contains(el.getId())).collect(Collectors.toList()).size() == 0)
                continue;
            fileFirst = f.getName();
            Thread.sleep(2000);
            driver.findElement(By.xpath(baseInstance.getXpathLoadButton())).click();
            Thread.sleep(2000);
            Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + path + f.getName() + "\"");
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(baseInstance.getXpathNumberOfLoadedFiles()), count));
            //  wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("#frames img"), count));
            break;
        }
        List<List<String>> listF = new ArrayList<>();
        listF.add(new ArrayList<>());
        int ii = 0;
        for (File f : dir.listFiles()) {
            if (f.isDirectory() || f.getName().contains(".txt")) continue;
            if (files != null && files.stream().filter(el -> f.getName().contains(el.getId())).collect(Collectors.toList()).size() == 0)
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

    public void findDownloadedFile() throws InterruptedException, IOException {
        List<File> downloadinFilePresence = new ArrayList<>();
        while (downloadinFilePresence.size() == 0) {
            downloadinFilePresence = Arrays.stream(new File(PATH_DOWNLOAD).listFiles())
                    .filter(el -> el.getName().contains(".crdownload") && el.getName().contains(getFileName().substring(0, getFileName().indexOf("." + getExtension()))))
                    .collect(Collectors.toList());
        }

        File f = new File(downloadinFilePresence.get(0).toString().substring(0, downloadinFilePresence.get(0).toString().indexOf(".crdownload")));

        while (!f.exists()) {
            Thread.sleep(1000);
        }

        File newFile = new File(PATH_DOWNLOAD + getInstance() + getPattern() + "-" + dateFormat.format(getDate()) + "." + getExtension());
        if (!instance.equals("pdf")) {
            long bytes = Files.size(Path.of(f.getAbsolutePath()));
            if ((bytes / 1024) / 1024 > 14) {
                driver.get("https://ezgif.com/optimize");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='new-image']")));
                //  Thread.sleep(2000);
                driver.findElement(By.xpath("//*[@id='new-image']")).sendKeys(f.getAbsolutePath());
                driver.findElement(By.xpath("//*[@value='Upload!']")).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@value='Optimize GIF!']")));
                driver.findElement(By.xpath("//*[@value='Optimize GIF!']")).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='output']//img[@alt='save']")));
                driver.findElement(By.xpath("//*[@id='output']//img[@alt='save']")).click();
             //   Thread.sleep(4000);
                f=new File(PATH_DOWNLOAD+"ezgif.com-gif-maker.gif.crdownload");
                while (!f.exists()) {
                    Thread.sleep(1000);
                }
                f = new File(f.getName().substring(0,f.getName().indexOf(".crdownload")));
//                for (File ff : new File(PATH_DOWNLOAD).listFiles())
//                    if (ff.getName().contains("ezgif.com-gif-maker")) {
//                        f = new File(ff.getName().substring(0,ff.getName().indexOf(".crdownload")));
//                        break;
//                    }
                while (!f.exists()) {
                    Thread.sleep(1000);
                }
            }
        }
        f.renameTo(newFile);
        setFileName(newFile.getAbsolutePath());
    }

    public abstract void setUpText(int dogsNumbers, int urgentDogs) throws IOException;

    public abstract void setSettingsAndDownloadAdd();

    public void closeDriver() {
        driver.close();
        driver.quit();
    }

    public abstract void sendPost() throws IOException, InterruptedException;

}
