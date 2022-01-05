package com;
import base.DogInfo;
import base.GifLoader;
import base.PetPardonSite;
import base.baseLoader;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.openqa.selenium.*;

import java.io.*;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.Utils.*;


public class ScreenShotsOC {

    public static void main(String[] args) throws GeneralSecurityException, IOException, InterruptedException {
        boolean isPDF = true;
        boolean isCopyToSheet = true;
        String pattern = "OC";

        Date date = new Date();
        if (date.getHours() >= 19) {
            isCopyToSheet = true;
            isPDF = true;
        }
//        isPDF = false;
       isCopyToSheet = false;

        final String spreadsheetId = "1r67mBrmeovXN5nBKLmgZ3owswoRcg7ASraiWyERZLj4";

        WebDriver driver = getDriver(true);
        WebDriver driverUrl = getDriver(true);

        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy HH-mm");

        JavascriptExecutor executor = (JavascriptExecutor) driver;

        //petpardon
        List<DogInfo> urgentList = new PetPardonSite("http://petadoption.ocpetinfo.com/rescuetrackportal/#/speciallist/DOG")
                .getUrgentDogsId();

        String[] lists = {"Adopt", "LostFound", "Lostocac", "foundocac"};
        //       String[] listsScope = {"adoptBox ng-scope", "lostBox ng-scope", "lostBox ng-scope", "lostBox ng-scope"};

        List<String> idRed = new ArrayList<>();

        int totalNumber = 0;
        for (int listNum = 0; listNum < lists.length; listNum++) {
            List<List<Object>> values = new ArrayList<>();
            driver.get("http://petadoption.ocpetinfo.com/" + lists[listNum] + "/#/list/DOG");
            Thread.sleep(2000);
            List<WebElement> listAnimals = driver.findElements(By.xpath("//div[contains(@class,'Box ng-scope')]"));

            for (int i = 0; i < listAnimals.size(); i++) {
                DogInfo dog = new DogInfo(Arrays.stream(listAnimals.get(i).getText().split("\\n")).filter(el -> !el.isEmpty()).map(el ->
                {
                    int ind = el.indexOf(":") + 1;
                    if (ind >= el.length()) return "";
                    if (el.substring(ind).trim().isEmpty()) return "";
                    else return el.substring(ind).trim();
                }).collect(Collectors.toList()), listNum);
                if (dog.getId().isEmpty()) continue;

                dog.setTypeDog(lists[listNum]);

                dog.setDate(dateFormat3.format(date));

                for (int ind = 0; ind < urgentList.size(); ind++) {
                    if (urgentList.get(ind).getId().equals(dog.getId())) {
                        dog.setStatus(urgentList.get(ind).getStatus());
                        dog.setKennel(urgentList.get(ind).getKennel());
                        dog.setComments(urgentList.get(ind).getComments());
                        if (dog.getGender() == null) dog.setGender(urgentList.get(ind).getGender());
                        if (dog.getName().isEmpty()) dog.setName(urgentList.get(ind).getName());
                        idRed.add(dog.getId());
                        break;
                    }
                }

                driverUrl.get(dog.getUrl());
                if (driverUrl.findElement(By.xpath("//body")).getText().contains("Sorry! This animal is no longer in our online database.")) {
//                    driver.switchTo().parentFrame();
//                    driver.switchTo().defaultContent();
                    executor.executeScript("document.body.style.zoom = '70%'");
                    executor.executeScript("arguments[0].click();", listAnimals.get(i).findElement(By.xpath(".//img")));
                    Thread.sleep(2000);
                    WebElement el = driver.findElement(By.xpath("//*[@id='petzoom']//div[@class='modal-content']"));
                    screenShot(el, driver, pattern + "/" +
                            dateFormat2.format(date) + "/" +
                            dog.getId() + ".png");
                    executor.executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id='petzoom']//button[@class='close']")));
                    Thread.sleep(2000);
                    dog.setUrl(driver.getCurrentUrl());
                } else {
                    screenShot(null, driverUrl, pattern + "/" +
                            dateFormat2.format(date) + "/" +
                            dog.getId() + ".png");
                    if (dog.getGender() == null || dog.getName().isEmpty()) {
                        String text = driverUrl.findElement(By.xpath("//td[@class='DetailDesc']")).getText();
                        if (dog.getGender() == null)
                            if (text.indexOf(" male") == -1)
                                dog.setGender(DogInfo.Gender.F);
                            else dog.setGender(DogInfo.Gender.M);
                        if (dog.getName().equals("") && text.indexOf("My name is") != -1)
                            dog.setName(text.substring(text.indexOf("My name is") + 10, text.indexOf("and I am")).trim());
                    }
                }
                values.add(dog.getInfoDog());
            }
            if (isCopyToSheet) {
                Sheets service = getService();
                int rowCount = service.spreadsheets().values().get(spreadsheetId, "list!C1:C").execute().getValues().size() + 1;
                ValueRange body = new ValueRange()
                        .setValues(values);
                service.spreadsheets().values().append(spreadsheetId, "list!A" + rowCount, body)
                        .setValueInputOption("USER_ENTERED")
                        .execute();
            }
            totalNumber += values.size();
        }
        if (isCopyToSheet) {
            //  for (int listNum = 0; listNum < lists.length; listNum++) {
            String sheet = "urgent";
            Sheets service = getService();
            List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId, sheet + "!C6").execute().getValues();
            ValueRange body = new ValueRange()
                    .setValues(values);
            service.spreadsheets().values().update(spreadsheetId, sheet + "!C1", body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
            //      }
        }
        driver.close();
        driver.quit();
        driverUrl.close();
        driverUrl.quit();
        System.out.println("completed making screenshots");
        String webLink = "";

        if (isPDF) {
            baseLoader baseLoader = createCombinedFiles("pdf", date, "OC", null);
            int finalTotalNumber = totalNumber;
            Thread t1 = new Thread(() -> {
                if (idRed.size() > 0) {
                    try {
                        createCombinedFiles2(createCombinedFiles("red list", date, "OC", idRed),
                                0, idRed.size());
                    } catch (Exception e) {
                        System.out.println("gif red list error: " + e.getMessage());
                    }
                }
                try {
                    createCombinedFiles2(createCombinedFiles("twitter", date, "OC",null),
                            finalTotalNumber, 0);
                } catch (Exception e) {
                    System.out.println("twitter error: " + e.getMessage());
                }
            });
            t1.start();
            webLink = createCombinedFiles2(baseLoader, 0, 0);
        }

        System.out.println("Total: " + totalNumber);
        System.out.println(webLink);
        File file1 = new File(PATH_SCREEN + pattern + "/" +
                dateFormat2.format(date) + "/result.txt");
        file1.createNewFile();
        FileWriter myWriter = new FileWriter(file1.getAbsoluteFile(), true);
        myWriter.append("Total: " + totalNumber + "\n");
        myWriter.append(webLink + "\n");
        myWriter.close();
    }
}