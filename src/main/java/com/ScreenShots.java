package com;

import base.DogInfoPetharbor;
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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
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
//        if ((date.getHours() >= 11 && date.getHours() <= 14) || date.getHours() >= 19) {
//            isCopyToSheet = true;
//            isPDF = true;
//        }
       isPDF=true;
//        isCopyToSheet = true;
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

        List<String> idRed=new ArrayList<>();
        List<String> listAllId=new ArrayList<>();

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

                    executor1.executeScript("arguments[0].click();", list.get(i).findElement(By.xpath(".//a")));

                    String detail= driver1.findElement(By.xpath("//td[@class='DetailDesc']")).getText();
                    int ind=detail.indexOf("I have been at the shelter since")+"I have been at the shelter since".length()+1;
                    dog.setIntakeDate(detail.substring(ind,detail.indexOf(".",ind)));
                    dog.setUrl(driver1.findElement(By.xpath("//a[text()='here']")).getAttribute("href"));

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
                        idRed.add("#"+dog.getId());
                    }
                    listAllId.add(dog.getId());
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

        int totalDogs = numberPerShelter.values()
                .stream().reduce(0, (subtotal, element) -> subtotal + element);

        if (isPDF) {
            baseLoader baseLoader = createCombinedFiles("pdf", date, "Riverside",null);
            int finalRedNumber = idRed.size();
            Thread t1 = new Thread(() -> {
                try {
                    if(idRed.size()>0) {
                        try {
                            createCombinedFiles2(createCombinedFiles("red list", date, "Riverside",idRed),
                                    0, finalRedNumber);
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
        }

//        try {
//            //new
//            Sheets service=getService();
//            List<List<Object>> values=new ArrayList<>();
//            List<String> newDogs=new ArrayList<>();
//            for (int listNum = 0; listNum < lists.length; listNum++) {
//                String sheet = lists[listNum].substring(0, 1).toUpperCase() + lists[listNum].substring(1);
//                int rowCount = service.spreadsheets().values().get(spreadsheetId, sheet + "!C1:C").execute().getValues().size();
//                values.addAll(service.spreadsheets().values().get(spreadsheetId, sheet + "!C2"+ ":L" + rowCount).execute().getValues());
//                String dd=dateFormat.format(
//                        Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                values=values.stream().filter(el-> dateFormat.format(new Date(el.get(el.size()-1).toString())).equals(dd)).collect(Collectors.toList());
//                List<List<Object>> finalValues = values;
//                List<String> t=listAllId.stream().filter(el->!finalValues.contains(el)).collect(Collectors.toList());
//                if(t.size()>0)newDogs.addAll(t);
//            }
//            GifLoader gif=new GifLoader("Riverside",date);
//            gif.loadFiles(newDogs,"new dogs");
//        } catch (Exception e) {
//            System.out.println("gif new dogs error: " + e.getMessage());
//        }

        System.out.println(numberPerShelter + " = " + numberPerShelter.values()
                .stream().reduce(0, (subtotal, element) -> subtotal + element).toString() + "\n");
        System.out.println("Red list: " + idRed.size());
        System.out.println(webLink);
        File file1 = new File(PATH_SCREEN + pattern + "/" +
                dateFormat2.format(date) + "/result.txt");
        file1.createNewFile();
        FileWriter myWriter = new FileWriter(file1.getAbsoluteFile(), true);
        myWriter.append(numberPerShelter + " = " + totalDogs + "\n");
        myWriter.append("Red list: " + idRed.size() + ": "+idRed.toString()+"\n");
        myWriter.append(webLink + "\n");
        myWriter.close();
        //twitter
//        List <List<String>> account=Arrays.asList(new String(Files.readAllBytes(Paths.get("src/main/resources/account.txt"))).split(";"))
//                .stream().map(el->Arrays.asList(el.split(","))).collect(Collectors.toList()).stream()
//                .filter(el->el.get(0).equals("twitter"+pattern+".txt")).collect(Collectors.toList());
//        if(account.size()>0) {
//            driver = getDriver(false);
//            driver.get("https://twitter.com/i/flow/login");
//            driver.findElement(By.xpath("//input[@name='text']")).sendKeys(account.get(0).get(1));
//            Thread.sleep(1000);
//            executor.executeScript("arguments[0].click();", driver.findElement(By.xpath("//span[contains(text(),'Next')]")));
//            driver.findElement(By.xpath("//input[@name='password']")).sendKeys(account.get(0).get(2));
//            Thread.sleep(1000);
//            executor.executeScript("arguments[0].click();", driver.findElement(By.xpath("//span[contains(text(),'Log in')]")));
//            Thread.sleep(1000);
//            driver.findElement(By.xpath("//div[@data-offset-key]//div[@data-offset-key]")).sendKeys("csdsadas");
//            executor.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@aria-label='Add photos or video']")));
//
//            File f = new File("C:\\Users\\Natalia\\Downloads\\red listRiverside-Jan01-16-27.gif");
//            Runtime.getRuntime().exec("C:\\Users\\Natalia\\dialog1.exe" + " " + "\"" +  f.getName() + "\"");
//
//
//
//        }



        /////red list
        //       if (isCopyToSheet) {
//            FileWriter myWriter = new FileWriter(file1.getAbsoluteFile(), true);
//             try {
//                List<List<Object>> valListRed = service.spreadsheets().values().get(spreadsheetId, "RedTable!A8:A").execute().getValues();
//                List<List<Object>> valListYellow = service.spreadsheets().values().get(spreadsheetId, "YellowTable!A9:A").execute().getValues();
//                System.out.println(valListRed.size());
//                System.out.println(valListYellow.size());
//                System.out.println(valListRed.toString());
//                System.out.println(valListYellow.toString());
//                List<List<Object>> l;
//                for (int listNum = 1; listNum < lists.length; listNum++) {
//                    String sheet = lists[listNum].substring(0, 1).toUpperCase() + lists[listNum].substring(1);
//                    List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId, sheet + "Table!C3").execute().getValues();
//                    if (values.size() == 0 || Integer.valueOf(values.get(0).get(0).toString()) == 0) continue;
//                    values = service.spreadsheets().values().get(spreadsheetId, sheet + "Table!A9:D").execute().getValues();
//                    values = values.stream().filter(el -> el.size() > 3).collect(Collectors.toList());
//                    System.out.println(values.toString());
//                    List<List<Object>> valList = service.spreadsheets().values().get(spreadsheetId, sheet + "!A1:C").execute().getValues();
//                    for (int i = 0; i < values.size(); i++) {
//                        //   if (values.get(i).size() < 4) continue;
//                        if (values.get(i).get(0).toString() != "" && values.get(i).get(1).toString() == "" &&
//                                values.get(i).get(2).toString() == "" && values.get(i).get(3).toString() != "") {
//                            l = (listNum == 1) ? valListRed : valListYellow;
//                            String status = "red list";
//                            if (l.toString().indexOf(values.get(i).get(0).toString()) == -1) {
//                                if (listNum == 2) {
//                                    l = valListRed;
//                                    if (l.toString().indexOf(values.get(i).get(0).toString()) == -1) continue;
//                                }
//                                continue;
//                            }
//                            String id = values.get(i).get(0).toString();
//                            System.out.println(id + " " + status);
//                            myWriter.append(id + " " + status + "\n");
//                            List<List<Object>> changedList = new ArrayList<>();
//                            for (int sheetRow = 0; sheetRow < valList.size(); sheetRow++) {
//                                if (!valList.get(sheetRow).get(2).toString().equals(id)) continue;
//                                List<List<Object>> v = new ArrayList<>();
//                                v.add(Arrays.asList(status));
//                                List<ValueRange> data = new ArrayList<>();
//                                data.add(new ValueRange()
//                                        .setRange(sheet + "!A" + (int) (sheetRow + 1))
//                                        .setValues(v));
//                                BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
//                                        .setValueInputOption("USER_ENTERED")
//                                        .setData(data);
//                                service.spreadsheets().values().batchUpdate(spreadsheetId, body).execute();
//                                //  ValueRange body = new ValueRange().setValues(v);
//                                //   service.spreadsheets().values().update(spreadsheetId, sheet + "!A" + (int) (sheetRow + 1), body)
//                                //           .setValueInputOption("USER_ENTERED").execute();
//                                System.out.println((int) (sheetRow + 1) + " is changed");
//                                System.out.println(data.toString());
//                                myWriter.append((int) (sheetRow + 1) + " is changed\n");
//                            }
//                        }
//                    }
//
//                }
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            } finally {
//                myWriter.close();
//            }
//            for (int listNum = 0; listNum < lists.length; listNum++) {
//                String sheet = lists[listNum].substring(0, 1).toUpperCase() + lists[listNum].substring(1);
//                List<List<Object>> values = new ArrayList<>();
//                int firstRow = (listNum == 0) ? 7 : 8;
//                values = service.spreadsheets().values().get(spreadsheetId, sheet + "Table!C" + firstRow).execute().getValues();
//                ValueRange body = new ValueRange()
//                        .setValues(values);
//                //System.out.println(values);
//                service.spreadsheets().values().update(spreadsheetId, sheet + "Table!C1", body)
//                        .setValueInputOption("USER_ENTERED")
//                        .execute();
//            }
//
//        }

        //new dogs
//        if (isCopyToSheet) {
//            List<List<Object>> newDogs = new ArrayList<>();
//            newDogs.add(new ArrayList<>());
//            newDogs.add(new ArrayList<>());
//            for (int listNum = 0; listNum < lists.length; listNum++) {
//                String sheet = lists[listNum].substring(0, 1).toUpperCase() + lists[listNum].substring(1);
//                List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId, sheet + "Table!C4").execute().getValues();
//                if (values.size() == 0 || Integer.valueOf(values.get(0).get(0).toString()) == 0) continue;
//                int firstRow = (listNum == 0) ? 8 : 9;
//                values = service.spreadsheets().values().get(spreadsheetId, sheet + "Table!A" + String.valueOf(firstRow) + ":D").execute().getValues();
//                values = values.stream().filter(el ->
//                {
//                    if (el.size() != 3) return false;
//                    if (!el.get(1).toString().isEmpty()) return false;
//                    return true;
//                }).collect(Collectors.toList());
//                for (int i = 0; i < values.size(); i++) {
//                    String id = values.get(i).get(0).toString();
//                    System.out.println(id + " new");
//                    List<List<Object>> valList = service.spreadsheets().values().get(spreadsheetId, sheet + "!C2:M").execute().getValues();
//                    valList = valList.stream().filter(el -> el.get(0).toString().equals(id)).collect(Collectors.toList());
//                    if (valList.size() == 0) continue;
//                    String text = "";
//                    if (sheet.equals("Green")) {
//                        text = "📣 Status Green - 'Needs Rescue' LIST.🆘 ANY animal listed in the 'Needs Rescue' list is in danger of being euthanized.* \nThose marked status yellow are animals that can no longer be kept in the shelter given their current medical or behavior condition. These animals need a rescue commitment immediately. Contact the rescue coordinator during business hours to facilitate pickup. Those in the yellow category may become red or may be euthanized based on their ongoing condition.";
//                    } else if (sheet.equals("Yellow")) {
//                        text = "📣 Status Yellow - 'Needs Rescue' LIST.🆘 ANY animal listed in the 'Needs Rescue' list is in danger of being euthanized.* \nThose marked status yellow are animals that can no longer be kept in the shelter given their current medical or behavior condition. These animals need a rescue commitment immediately. Contact the rescue coordinator during business hours to facilitate pickup. Those in the yellow category may become red or may be euthanized based on their ongoing condition.";
//                    } else {
//                        text = "📣 🆘 ON EUTHANASIA LIST! *\nRescues if you have additional questions about a dog’s behavior or medical, please do not message this page asking - you need to email (prefer) or call the shelter rescue desk immediately as they will have the most UTD info! ➡️Adopters, PLEASE DO NOT TIE UP THE RESCUE PHONE LINE! Submit an online inquiry or go directly to the shelter.";
//                    }
//                    text += "\n\n🤍MEET: #" + id + ", " + valList.get(0).get(4).toString() + ", " + valList.get(0).get(1).toString() + " " + valList.get(0).get(2).toString() + " " + valList.get(0).get(3).toString() + "\n" + valList.get(0).get(7).toString() + " " + valList.get(0).get(8).toString();
//                    text += "\n\n" + valList.get(0).get(10).toString() + "\n";
//                    text += "\nEmail 📧: adoptions@rivco.org or shelterinfo@rivco.org\nPhone ☎️: 951-358-7387";
//                    text += "\n\nRESCUES:\nRescue desk: 951-358-7302\nEmail 📧: rescue@rivco.org\n";
//                    text += "\nShelter address 🏢: " + valList.get(0).get(6).toString() + "\n";
//                    if (valList.get(0).get(6).toString().indexOf("Riverside") != -1) {
//                        text += "6851 Van Buren Boulevard\nRiverside, California 92509\nMonday - Saturday 10-4pm\nOpen to the public from 1-3:30pm \n";
//                    } else {
//                        text += "72-050 Pet Land Place\nThousand Palms, CA 92276\nPhone Number: (951) 358-7387\n";
//                    }
//                    newDogs.get(0).add(Arrays.asList(valList.get(0).get(10).toString(), text));
//                    newDogs.get(1).add(Arrays.asList("https://www.instagram.com/explore/tags/" + id + "/", "https://www.facebook.com/hashtag/" + id + "/"));
//
//                }
//            }
//            if(newDogs.get(0).size()>0) {
//                ValueRange body = new ValueRange().setValues(newDogs);
//                int count =service.spreadsheets().values().get(spreadsheetId, "New!A:A").execute().getValues().size()+1;
//                service.spreadsheets().values().update(spreadsheetId,   "New!A"+count, body)
//                        .setValueInputOption("USER_ENTERED").execute();
//            }
//        }

    }
}