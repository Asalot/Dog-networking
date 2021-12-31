package base;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GifLoader extends TwitterLoader {

    public GifLoader(String patternValue, Date dateValue) {
        super("twitter", dateValue, patternValue);
    }

    public void loadFiles(List<String> redList, String type) throws InterruptedException, IOException {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy HH-mm");

        path = path.substring(0, path.length() - 1) + "Screens" + getPattern() + "\\" + dateFormat2.format(getDate()) + "\\";

        driver.get(getBaseInstance().getUrl());
        File dir = new File(path);
        int count = 1;
        for (File f : dir.listFiles()) {
            if (f.getName().contains(redList.get(0))) {
                Thread.sleep(2000);
                driver.findElement(By.xpath(getBaseInstance().getXpathLoadButton())).click();
                Thread.sleep(2000);
                Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" + path + f.getName() + "\"");
                wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(getBaseInstance().getXpathNumberOfLoadedFiles()), count));
                //  wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("#frames img"), count));
                break;
            }
        }
        if (redList.size() > 1) {
            List<List<String>> listF = new ArrayList<>();
            listF.add(new ArrayList<>());
            int ii = 0;
            for (File f : dir.listFiles())
                for (int i = 1; i < redList.size(); i++) {
                    if (!f.getName().contains(redList.get(i))) continue;
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
                driver.findElement(By.xpath(getBaseInstance().getXpathLoadButton())).click();
                Thread.sleep(2000);
                Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + xx);
                wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(getBaseInstance().getXpathNumberOfLoadedFiles()), count));
            }
        }
        setNumberFiles(count);
        System.out.println("Waiting for file to be downloaded for Gif: " + type);
        setSettingsAndDownload();
        File file = findDownloadedFile();
        file.renameTo(new File(file.getParent() + "\\" +
                type + "-" + dateFormat2.format(getDate()) + ".gif"));
      
         setUpText(redList.size(),0);
        System.out.println("Completed: " + type);
        closeDriver();
    }

}
