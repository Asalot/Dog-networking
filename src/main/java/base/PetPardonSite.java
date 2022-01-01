package base;

import base.DogInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.Utils.getDriver;

public class PetPardonSite {

    private String url;
    private List<DogInfo> allDogDetail;


    public PetPardonSite(String urlValue) {
        url = urlValue;
        allDogDetail = new ArrayList<>();
    }

    public boolean contains(String id) {
        if (allDogDetail == null || allDogDetail.size() == 0) return false;
        return allDogDetail.stream().filter(el -> el.getId().equals(id)).collect(Collectors.toList()).size() > 0;
    }

    public List<DogInfo> getUrgentDogsId() throws InterruptedException {
        WebDriver driver = getDriver(true);
        driver.get(url);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//img[@alt='Orange County Logo']")));
        Thread.sleep(5000);
        List<WebElement> list = driver.findElements(By.xpath("//div[@ng-show='ShowDesktop()']//div[@class='well well-sm']//tbody/tr"));
        for (int i = 0; i < list.size(); i++) {
            executor.executeScript("arguments[0].scrollIntoView();", list.get(i));
            String id = list.get(i).findElement(By.xpath(".//a[2]")).getAttribute("href").substring(list.get(i).findElement(By.xpath(".//a[2]")).getAttribute("href").indexOf("/A") + 1);
            if (id.isEmpty()) continue;
            if (contains(id)) continue;
            DogInfo info = new DogInfo(id);
            List<String> array = Arrays.asList(list.get(i).getText().split("\n"));
            String bound = "";
            String intakeType = "";
            for (int ii = 0; ii < array.size(); ii++) {
                int ind = array.get(ii).indexOf(":") + 2;
                if (ind >= array.get(ii).length()) continue;
                if (array.get(ii).contains("Sex") && info.getGender() == null)
                    info.setGender((array.get(ii).contains("Female")) ? DogInfo.Gender.F : DogInfo.Gender.M);
                else if (array.get(ii).contains("Name") && info.getName().isEmpty())
                    info.setName(array.get(ii).substring(array.get(ii).indexOf(":") + 2));
                else if (array.get(ii).contains("Intake Type"))
                    intakeType = array.get(ii).substring(array.get(ii).indexOf(":") + 2).trim();
                else if (array.get(ii).contains("Kennel:"))
                    info.setKennel(array.get(ii).substring(array.get(ii).indexOf(":") + 2));
            }
            if (list.get(i).findElement(By.xpath(".//div")).getAttribute("ng-show").equals("true")) {
                driver.switchTo().parentFrame();
                driver.switchTo().defaultContent();
                executor.executeScript("arguments[0].scrollIntoView(true);", list.get(i).findElement(By.xpath(".//div//input")));
                executor.executeScript("arguments[0].click();", list.get(i).findElement(By.xpath(".//div//input")));
                Thread.sleep(2000);
                List<String> boundPair = driver.findElements(By.xpath("//*[@id='familyModal']//tbody/tr//b[1]")).stream().map(el -> "#" + el.getText()).collect(Collectors.toList());
                bound = boundPair.stream().filter(el -> !el.contains(id)).collect(Collectors.joining(","));
            }
            array = Arrays.asList(list.get(i).findElement(By.xpath(".//td[3]")).getText().split("\n"));
            String comments = "";
            for (int ii = 0; ii < array.size(); ii++) {
                if (ii + 1 >= array.size()) break;
                if (array.get(ii).contains("Status")) comments += array.get(ii + 1).trim();
                else if (array.get(ii).contains("Comments")) comments = comments + ": " + array.get(ii + 1).trim();
            }
            if (!bound.isEmpty()) comments = "Bonded pair and should go together with " + bound + "\n" + comments;
            if (!intakeType.isEmpty()) comments = intakeType + ", " + comments;
            info.setComments(comments);
            try {
                info.setStatus(list.get(i).findElement(By.xpath(".//h3")).getText());
            } catch (Exception e) {
            }
            allDogDetail.add(info);
        }
        driver.close();
        driver.quit();
        return allDogDetail;
    }
}
