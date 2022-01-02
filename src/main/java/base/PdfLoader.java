package base;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.Utils.getDrive;

public class PdfLoader extends baseLoader {

    public PdfLoader(String instanceValue, Date dateValue, String patternValue, List<String> filesValue) {
        super(instanceValue, dateValue, patternValue,filesValue);
    }

    @Override
    public void setSettingsAndDownload() {
        driver.findElement(By.cssSelector("#page_orientation")).sendKeys("Landscape");
        executor.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//li[@class=\"file_queue_element\"][last()]")));
    }

    @Override
    public String loadToDrive() {
//      //drive
        try {
            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
            fileMetadata.setName("OC-" + dateFormat.format(getDate()) + ".pdf");
            fileMetadata.setParents(Collections.singletonList("16BUpNL7S_-eVV6Cqh6bKgKjYMi_NCGpf"));
            FileContent mediaContent = new FileContent("application/pdf", new File(getFileName()));
            Drive  drive=getDrive();
            com.google.api.services.drive.model.File file = drive.files().create(fileMetadata, mediaContent)
                    .execute();
            Permission permission = new Permission();
            permission.setRole("reader");
            permission.setType("anyone");
            drive.permissions().create(file.getId(), permission).execute();
            return "https://drive.google.com/file/d/" + file.getId() + "/view";
        } catch (
                Exception e) {
            System.out.println("error with DRIVE");
            System.out.println(
                    e.getMessage());
            return "";
        }
    }

    @Override
    protected void additionalSetUp() {
        executor.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//h3[text()='JPG to convert to PDF']")));

    }

    @Override
    public void setUpText(int dogsNumbers,int urgentDogs) throws IOException {

    }

    @Override
    public void setSettingsAndDownloadAdd() {
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//li[.//img[@class='picture_thumbnail']]"), getNumberFiles()));
        driver.findElement(By.cssSelector("#convert_to_pdf_button")).click();
        wait.until(ExpectedConditions.textToBe(By.cssSelector("#download_link_container p"), "Your PDF is ready!"));
        WebElement pdfButton = driver.findElement(By.cssSelector("#download_pdf_button"));
        pdfButton.click();

       setFileName(pdfButton.getAttribute("href").substring(
               pdfButton.getAttribute("href").lastIndexOf("/") + 1));

    }
}
