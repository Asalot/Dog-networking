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
import java.util.stream.Collectors;

import static com.Utils.PATH_SCREEN;
import static com.Utils.getDriver;

public abstract class baseLoader{

    private String instance;
    public WebDriver driver;
    public WebDriverWait wait;
    public JavascriptExecutor executor;
    private List<String> files=new ArrayList<>();

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

    public baseLoader(String instanceValue,Date dateValue,String patternValue, List<String> filesValue) {
        instance= instanceValue;
        driver = getDriver(false);
        wait = new WebDriverWait(driver, 600);
        executor = (JavascriptExecutor) driver;
        if(instance.equals("pdf")) {baseInstance =new PDFInstance(); extension="pdf";}
        else { baseInstance =new TwitterInstance();  extension="gif" ;}
        date=dateValue;
        pattern=patternValue;
        files=filesValue;
    }

    public void loadFiles() throws InterruptedException, IOException{
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy HH-mm");
        String path = PATH_SCREEN + getPattern() + "\\" + dateFormat2.format(getDate()) + "\\";

        driver.get(baseInstance.getUrl());
        additionalSetUp();
        File dir = new File(path);
        int count = 1;
        String fileFirst="";
        for (File f : dir.listFiles()) {
//            if (f.isDirectory() || f.getName().contains(".txt") || (redList!=null && redList.size()>0 && redList.toString()
//                    .indexOf(f.getName().substring(f.getName().indexOf("#")))==-1)) continue;
            if (f.isDirectory() || f.getName().contains(".txt"))continue;
            if(files!=null && files.stream().filter(el->f.getName().contains(el)).collect(Collectors.toList()).size()==0)continue;
            fileFirst=f.getName();
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
            if (f.isDirectory() || f.getName().contains(".txt"))continue;
            if(files!=null && files.stream().filter(el->f.getName().contains(el)).collect(Collectors.toList()).size()==0)continue;
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

    public void findDownloadedFile() throws InterruptedException{
        File f = new File("C:\\Users\\Natalia\\Downloads\\" + getFileName());
        while (!f.exists()) {
            Thread.sleep(1000);
        }
        File newFile=new File("C:\\Users\\Natalia\\Downloads\\" + getInstance()+getPattern() + "-" + dateFormat.format(getDate()) + "."+getExtension());
        f.renameTo(newFile);
        setFileName(newFile.getAbsolutePath());
     //   return newFile;
    }

    public abstract void setUpText(int dogsNumbers, int urgentDogs) throws IOException;

    public abstract void setSettingsAndDownloadAdd();

    public void closeDriver(){
        driver.close();
        driver.quit();
    }
}
