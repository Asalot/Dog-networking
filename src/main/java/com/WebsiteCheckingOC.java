package com;

import com.google.api.services.sheets.v4.Sheets;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.*;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.Utils.*;


public class WebsiteCheckingOC {

    public static void main(String[] args) throws GeneralSecurityException, IOException, InterruptedException {
        final String spreadsheetId = "1r67mBrmeovXN5nBKLmgZ3owswoRcg7ASraiWyERZLj4";
        Sheets service = getService();
        WebDriver driver = getDriver(true);

        String[] lists = {"list"};
        List<String> listID = new ArrayList<>() ;

        File file = new File("./websiteOC.txt");
        file.createNewFile();
        Date dateNow = new Date();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM/dd/yyyy");
        FileWriter myWriter = new FileWriter(file.getAbsoluteFile(), true);

        for (int listNum = 0; listNum < lists.length; listNum++) {
            List<List<Object>> listOfDateAndLinks = service.spreadsheets().values().get(spreadsheetId, lists[listNum] + "!L2:M").execute().getValues();
            List<String> listDates = new ArrayList<>();
            Date d1 = new Date(listOfDateAndLinks.get(listOfDateAndLinks.size() - 1).get(0).toString());
            listDates.add(dateFormat3.format(d1));
            listDates.add(dateFormat3.format(DateUtils.addDays(d1, -1)));
            listOfDateAndLinks = listOfDateAndLinks.stream().filter(el -> listDates.indexOf(dateFormat3.format(new Date(el.get(0).toString()))) != -1).collect(Collectors.toList());
            myWriter.write(lists[listNum] + "\n");
            for (List<Object> l : listOfDateAndLinks) {
                if(l.size()<2 || l.get(1).toString().isEmpty() || l.get(1).toString().equals(""))continue;
                String id = l.get(1).toString().substring(l.get(1).toString().indexOf(".A") + 1);
                if(listID.indexOf(id)==-1) {listID.add(id);}
                else continue;
                String result = checkWebLink(driver,l.get(1).toString(),true);
                if (!result.isEmpty()) {
                    System.out.println(result);
                    myWriter.write(dateFormat2.format(dateNow) + " - " + dateFormat3.format(new Date(l.get(0).toString())) + ": " + result + "\n");
                }
            }
        }
        myWriter.close();
        driver.close();
        driver.quit();
    }
}