package base;

import base.baseLoader;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TwitterLoader extends baseLoader {

    public TwitterLoader(String instanceValue, Date dateValue, String patternValue,List<String> filesValue) {
        super(instanceValue, dateValue, patternValue,filesValue);
    }

    @Override
    public void setSettingsAndDownload() {
        executor.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.cssSelector("#gif_speed")));
        driver.findElement(By.cssSelector("#gif_speed")).clear();
        driver.findElement(By.cssSelector("#gif_speed")).sendKeys("700");
        int count = getNumberFiles();
        wait.until(ExpectedConditions.textToBe(By.xpath("//div[@id='filelist']"), count + " images uploaded successfully"));
        driver.findElement(By.xpath("//div[text()='Create GIF Animation']")).click();
        wait.until(ExpectedConditions.textToBe(By.xpath("//div[@id='output_file']/a[2]"), "Download the GIF"));

        setFileName(driver.findElement(By.xpath("//div[@id='output_file']//a[text()='Download the GIF']")).getAttribute("download"));

        driver.findElement(By.xpath("//div[@id='output_file']//a[text()='Download the GIF']")).click();
    }

    @Override
    public String loadToDrive() {
     return null;
    }

    @Override
    protected void additionalSetUp() {

    }

//    @Override
//    public File findDownloadedFile() throws InterruptedException {
//        File f;// = new File("C:\\Users\\Natalia\\Downloads\\" + nameFile);
//        File dir = new File("C:\\Users\\Natalia\\Downloads\\");
//        int count = 0;
//        String fName = getFileName().substring(0, getFileName().indexOf(".gif"));
//        for (File ff : dir.listFiles())
//            if (ff.getName().contains(fName)) count++;
//        if (count == 0) f = new File("C:\\Users\\Natalia\\Downloads\\" + getFileName());
//        else f = new File("C:\\Users\\Natalia\\Downloads\\" + fName + " (" + count + ").gif");
//        while (!f.exists()) {
//            Thread.sleep(1000);
//        }
//        return f;
//    }

    @Override
    public void setUpText(int dogsNumbers, int urgentDogs) throws IOException {
        String textTwitter = new String(Files.readAllBytes(Paths.get("src/main/resources/"+getInstance() + getPattern() + ".txt")));

        File file = new File("./"+getInstance() + getPattern() + ".txt");
        file.createNewFile();

        SimpleDateFormat dateFormat = new SimpleDateFormat("\n\ndd MMM yyyy hhaa");
        textTwitter = textTwitter.replace("<date>", dateFormat.format(new Date()));
        textTwitter = textTwitter.replace("<dogs>", String.valueOf(dogsNumbers));
        textTwitter = textTwitter.replace("<urgent>", String.valueOf(urgentDogs));

        int prevDogs = 0;
        String d = "yest";
        try {
            List<String> stream = Files.lines(file.toPath()).collect(Collectors.toList());
            for (int i = stream.size() - 1; i >= 0; i--) {
                if (stream.get(i).contains("dogs in Needs urgent list") && prevDogs == 0) {
                    prevDogs = Integer.parseInt(stream.get(i).substring(0, stream.get(i).indexOf("dogs in") - 2));
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        textTwitter = textTwitter.replace("<dif>", String.valueOf(prevDogs));

        FileWriter myWriter = new FileWriter(file.getAbsoluteFile(), true);
        myWriter.append(textTwitter);
        myWriter.close();
    }

    @Override
    public void setSettingsAndDownloadAdd() {

    }
}
