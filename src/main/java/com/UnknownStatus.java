package com;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.Utils.*;
import static java.util.stream.Collectors.*;

public class UnknownStatus {

    public static void main(String[] args) throws GeneralSecurityException, IOException {

        WebDriver driver = getDriver(true);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1n9nr1IA6IX0CTTvaEXybZI-3oMD2W56JP7MJEc0NalY";
        Sheets service = getService();
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy");

        String[] lists = {"Red", "Yellow", "Green"};
        for (int listNum = 0; listNum < lists.length; listNum++) {
            String sheet = lists[listNum] + "Table";
            String range = (listNum == 0) ? "7" : "8";
            List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId, sheet + "!C" + range + ":" + range).execute().getValues();
            List<Object> values_ = new ArrayList<>();
            boolean isFirst = false;
            for (int i = 0; i < values.get(0).size(); i++) {
                calendar.setTime(new Date(values.get(0).get(i).toString()));
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                int week = calendar.get(Calendar.WEEK_OF_YEAR);
                if (week == weekOfYear) values_.add(values.get(0).get(i));
                else if (isFirst) break;
                else {
                    isFirst = true;
                    values_.add(values.get(0).get(i));
                }
            }
            char letter = (char) (values_.size() + 65 + 1);
            values = service.spreadsheets().values().get(spreadsheetId, sheet + "!A" + range + ":" + letter).execute().getValues();
            values = values.stream().filter(el -> {
                if (el.size() < 3) return false;
                if (el.get(1).toString().equals("status")) return true;
                if (!el.get(2).toString().isEmpty()) return false;
                return el.get(1).toString().isEmpty();
            }).collect(toList());
            if (values.size() < 2) continue;
            int rowCount = service.spreadsheets().values().get(spreadsheetId, "Unknown!A1:A").execute().getValues().size() + 1;
            List<List<Object>> unknownList = new ArrayList<>();
            for (int i = 1; i < values.size(); i++)
                for (int ii = 2; ii < values.get(i).size(); ii++)
                    if (!values.get(i).get(ii).toString().isEmpty()) {
                        String url = "https://petharbor.com/pet.asp?uaid=RVSD." + values.get(i).get(0).toString();
                        String result = checkWebLink(driver, url, false);
                        String url1 = "";
                        if (result.contains("NLL")) {
                            url1 = url.replace("RVSD.", "RVSD1.");
                            result = checkWebLink(driver, url, false);
                        }
                        unknownList.add(Arrays.asList(dateFormat3.format(date),
                                weekOfYear, dateFormat2.format(new Date(values.get(0).get((ii - 1)).toString())), lists[listNum], values.get(i).get(0).toString(), result, "", "", "", "", url, url1));
                        break;
                    }

            ValueRange body = new ValueRange()
                    .setValues(unknownList);
            service.spreadsheets().values().append(spreadsheetId, "Unknown!A" + rowCount, body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
        }

        driver.close();
        driver.quit();
    }
}
