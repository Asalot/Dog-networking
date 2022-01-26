package com;

import base.DogInfoPetharbor;
import base.FacebookLoader;
import base.baseLoader;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.openqa.selenium.*;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.*;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.Utils.*;
import static com.Utils.createCombinedFiles2;

public class ScreenShots {

    public static void main(String[] args) throws GeneralSecurityException, IOException, InterruptedException{
        boolean isPDF = false;
        boolean isCopyToSheet = false;
        Date date = new Date();
        if ((date.getHours() >= 11 && date.getHours() <= 14) || date.getHours() >= 19) {
            isCopyToSheet = true;
            isPDF = true;
        }
//         isPDF=false;
//        isCopyToSheet = false;
        int totalCount = 1;

        final String spreadsheetId = "1n9nr1IA6IX0CTTvaEXybZI-3oMD2W56JP7MJEc0NalY";
        String pattern = "Riverside";
        WebDriver driver = getDriver(false);
        WebDriver driver1 = getDriver(true);
        try {
            driver1.manage().window().maximize();
        } catch (Exception e) {
        }

        WebDriverWait wait = new WebDriverWait(driver, 600);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        JavascriptExecutor executor1 = (JavascriptExecutor) driver1;

        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy HH-mm");

        String[] lists = {"red", "yellow", "green"};
        String[] color = {"f99494", "fff77d", "a0e7a2"};
        Map<String, Integer> numberPerShelter = new HashMap<>();

        List<DogInfoPetharbor> idRed=new ArrayList<>();
    //   List<String> listAllId=new ArrayList<>();

        saveCreateFile( "./asa.jpg",pattern + "/" +
                dateFormat2.format(date) + "/1-#0.png");

        for (int listNum = 0; listNum < lists.length; listNum++) {
            driver.get("https://petharbor.com/results.asp?searchtype=ALL&start=4&friends=1&samaritans=1&nosuccess=0&rows=200&imght=120&imgres=thumb&tWidth=200&view=sysadm.v_rvsd_rescue_" + lists[listNum] + "&bgcolor=e5e5e5&text=572700&link=572700&alink=e5e5e5&vlink=00b5cc&fontface=verdana&fontsize=10&col_hdr_bg=97694F&col_bg=" + color[listNum] + "&SBG=00b5cc&zip=92503&miles=100&shelterlist=%27RVSD%27,%27RVSD4%27,%27RVSD5%27,%27RVSD6%27,%27RVSD7%27,%27RVSD1%27&atype=&where=type_DOG&NewOrderBy=Located%20At&PAGE=1");
            while (true) {
                List<WebElement> list = driver.findElements(By.cssSelector(".ResultsTable tr:not(:first-child)"));
                int count = 0;
                while (true) {
                    executor.executeScript("document.body.style.zoom = '78%'");
                    try {
                        driver.manage().window().maximize();
                    } catch (Exception e) {
                    }
                    if (count > 0)
                        executor.executeScript("arguments[0].scrollIntoView(true);", list.get(count));
                    wait.until(ExpectedConditions.elementToBeClickable(list.get(count).findElement(By.xpath(".//a"))));
                    Thread.sleep(1000);
                    try {

                        Rectangle screenRectangle = new Rectangle(0, 0, driver.manage().window().getSize().getWidth()-15, driver.manage().window().getSize().getHeight()+27);
                        Robot robot = new Robot();
                        BufferedImage image = robot.createScreenCapture(screenRectangle);
                        File directory = new File(PATH_SCREEN + pattern + "/" +
                                dateFormat2.format(date));
                        if (!directory.exists()) directory.mkdir();
                        String order = "1-" + ((listNum == 0) ? "#1" : (listNum == 1) ? "#2" : "#3");
                        File file = new File(
                                directory + "/" + order + lists[listNum] + String.format("%03d", count) + ".png");
                        ImageIO.write(image, "png", file);
                    } catch (Exception ex) {
                        System.out.println("don't create full screenshot");
                    }
                    count += (count == 0) ? 5 : 6;
                    if (count >= list.size()) break;
                }
                if (driver.getPageSource().indexOf("Next Page") != -1) {
                    driver.findElement(By.xpath("//a[contains(text(),\"Next Page\")]")).click();
                } else break;
            }
        }
        driver.close();
        driver.quit();
        for (int listNum = 0; listNum < lists.length; listNum++) {
            List<List<Object>> values = new ArrayList<>();
            driver1.get("https://petharbor.com/results.asp?searchtype=ALL&start=4&friends=1&samaritans=1&nosuccess=0&rows=200&imght=120&imgres=thumb&tWidth=200&view=sysadm.v_rvsd_rescue_" + lists[listNum] + "&bgcolor=e5e5e5&text=572700&link=572700&alink=e5e5e5&vlink=00b5cc&fontface=verdana&fontsize=10&col_hdr_bg=97694F&col_bg=" + color[listNum] + "&SBG=00b5cc&zip=92503&miles=100&shelterlist=%27RVSD%27,%27RVSD4%27,%27RVSD5%27,%27RVSD6%27,%27RVSD7%27,%27RVSD1%27&atype=&where=type_DOG&NewOrderBy=Located%20At&PAGE=1");
            try {
                driver1.manage().window().maximize();
            } catch (Exception e) {
            }
            while (true) {
                List<WebElement> list = driver1.findElements(By.cssSelector(".ResultsTable tr:not(:first-child)"));
                executor1.executeScript("arguments[0].scrollIntoView();", driver1.findElement(By.cssSelector(".TableTitle tr td")));
                for (int i = 0; i < list.size(); i++) {
                    list = driver1.findElements(By.cssSelector(".ResultsTable tr:not(:first-child)"));
                    DogInfoPetharbor dog=new DogInfoPetharbor(list.get(i).findElements(By.cssSelector(" td:not(:nth-child(2)):not(:first-child)")).stream().map(WebElement::getText).collect(Collectors.toList()));
                    dog.setDate(dateFormat3.format(date));

                    //executor1.executeScript("arguments[0].click();", list.get(i).findElement(By.xpath(".//a")));
                    dog.setUrl("https://petharbor.com/pet.asp?uaid=RVSD"+((dog.getShelter().contains("Riverside"))?"":"1")+
                                    "."+dog.getId());
                    driver1.get(dog.getUrl());

                    String detail= driver1.findElement(By.xpath("//td[@class='DetailDesc']")).getText();
                    int ind=detail.indexOf("I have been at the shelter since")+"I have been at the shelter since".length()+1;
                    dog.setIntakeDate(detail.substring(ind,detail.indexOf(".",ind)));
                   // dog.setUrl(driver1.findElement(By.xpath("//a[text()='here']")).getAttribute("href"));

                    File screen=screenShot(null,driver1,pattern + "/" +
                            dateFormat2.format(date) + "/" +
                            lists[listNum] + "/" +
                            dog.getShelter() + "/#" +
                            dog.getId() + ".png");

                    boolean isShelter = false;
                    for (Map.Entry<String, Integer> shelt : numberPerShelter.entrySet())
                        if (shelt.getKey().contains(dog.getShelter())) {
                            shelt.setValue(shelt.getValue() + 1);
                            isShelter = true;
                            break;
                        }
                    if (!isShelter)
                        numberPerShelter.put(dog.getShelter(), 1);

                  saveCreateFile(screen.getAbsolutePath(), pattern + "/" +
                          dateFormat2.format(date) + "/2-" + String.format("%03d", totalCount) + "-#" +
                          dog.getId() + ".png");

                    totalCount++;
                    driver1.navigate().back();
                    if(listNum==0) {//red  for red gif
                        idRed.add(dog);
                    }
          //          listAllId.add(dog.getId());
                    if (isCopyToSheet) values.add(dog.getInfoDog());
                }
                if (isCopyToSheet) {
                    String sheet = lists[listNum].substring(0, 1).toUpperCase() + lists[listNum].substring(1);
                    Sheets service = getService();
                    int rowCount = service.spreadsheets().values().get(spreadsheetId, sheet + "!C1:C").execute().getValues().size() + 1;
                    ValueRange body = new ValueRange()
                            .setValues(values);
                    service.spreadsheets().values().append(spreadsheetId, sheet + "!C" + rowCount, body)
                            .setValueInputOption("USER_ENTERED")
                            .execute();
                }
                if (driver1.getPageSource().indexOf("Next Page") != -1) {
                    driver1.findElement(By.xpath("//a[contains(text(),\"Next Page\")]")).click();
                } else break;
            }
        }
        driver1.close();
        driver1.quit();
        
        if (isCopyToSheet) {
             for (int listNum = 0; listNum < lists.length; listNum++) {
                String sheet = lists[listNum].substring(0, 1).toUpperCase() + lists[listNum].substring(1);
                int firstRow = (listNum == 0) ? 7 : 8;
                Sheets service=getService();
                List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId, sheet + "Table!C" + firstRow).execute().getValues();
                ValueRange body = new ValueRange()
                        .setValues(values);
                service.spreadsheets().values().update(spreadsheetId, sheet + "Table!C1", body)
                        .setValueInputOption("USER_ENTERED")
                        .execute();
            }
        }
        String webLink = "";
        AtomicReference<String> path_red= new AtomicReference<>("");
        int totalDogs = numberPerShelter.values()
                .stream().reduce(0, (subtotal, element) -> subtotal + element);

        if (isPDF) {
            baseLoader baseLoader = createCombinedFiles("pdf", date, "Riverside",null);
            int finalRedNumber = idRed.size();
            Thread t1 = new Thread(() -> {
                try {
                    if(idRed.size()>0) {
                        try {
                            path_red.set(createCombinedFiles2(createCombinedFiles("red list", date, "Riverside", idRed),
                                    0, finalRedNumber));
                        } catch (Exception e) {
                            System.out.println("gif red list error: " + e.getMessage());
                        }
                    }

                    createCombinedFiles2(createCombinedFiles("twitter", date, "Riverside", null),
                            totalDogs, finalRedNumber);
                } catch (Exception e) {
                    System.out.println("twitter error: " + e.getMessage());
                }
            });
            t1.start();
            webLink = createCombinedFiles2(baseLoader, totalDogs, 0);

//            Sheets service=getService();
//            List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId,  "RedTable!C7:D7").execute().getValues();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//            if(!dateFormat.format(new Date(values.get(0).get(0).toString())).equals(dateFormat.format(new Date(values.get(0).get(1).toString())))) {
//                try {
//                    FacebookLoader facebook = new FacebookLoader("facebook", date, "Riverside", idRed, path_red.get());
//                    facebook.setUpText(totalDogs, idRed.size(), webLink);
//                    facebook.sendPost();
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
//            }
//
//            t1 = new Thread(() -> {
//                try{
//                 createCombinedFiles2(createCombinedFiles("twitter", date, "Riverside", null),
//                            totalDogs, finalRedNumber);
//                } catch (Exception e) {
//                    System.out.println("twitter error: " + e.getMessage());
//                }
//            });
//            t1.start();
        }

        System.out.println(numberPerShelter + " = " + numberPerShelter.values()
                .stream().reduce(0, (subtotal, element) -> subtotal + element).toString() + "\n");
        System.out.println("Red list: " + idRed.size());
        System.out.println(webLink);
        File file1 = new File(PATH_SCREEN + pattern + "/" +
                dateFormat2.format(date) + "/result.txt");
        file1.createNewFile();
        FileWriter myWriter = new FileWriter(file1.getAbsoluteFile(), true);
        myWriter.append(numberPerShelter + " = " + totalDogs + "\n");
        myWriter.append("Red list: " + idRed.size() + ": "+idRed.stream().map(el->el.getId()).collect(Collectors.joining(","))+"\n");
        myWriter.append(webLink + "\n");
        myWriter.close();

        Sheets service=getService();
        List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId,  "RedTable!C7:D7").execute().getValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        if(!dateFormat.format(new Date(values.get(0).get(0).toString())).equals(dateFormat.format(new Date(values.get(0).get(1).toString())))) {
            try {
                FacebookLoader facebook = new FacebookLoader("facebook", date, "Riverside", idRed, path_red.get());
                facebook.setUpText(totalDogs, idRed.size(), webLink);
                facebook.sendPost();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
   }
}