package com;

import base.LinkedinInstance;
import base.TumblrInstance;
import base.TwitterInstance;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.Utils.*;

public class Posting {

    public static void main(String[] args) throws GeneralSecurityException, IOException, InterruptedException {
        final String spreadsheetId = "1jTUJjJOZt5kju6oDavUmMay5-E8UK2it8i-HsY_XMQs";
        WebDriver driver = getDriver(false);
  //      WebDriverWait wait = new WebDriverWait(driver, 600);
        Sheets service = getService();
        List<String> sheetList = Arrays.asList("dogs", "OCdogs");
        for (int i = 0; i < sheetList.size(); i++) {
            int lastRow = service.spreadsheets().values().get(spreadsheetId, sheetList.get(i) + "!A:A").execute().getValues().size();
            List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId, sheetList.get(i) + "!A2:J" + lastRow).execute().getValues();
            List<List<Object>> val = values.stream().filter(el -> el.get(7).equals("")).collect(Collectors.toList());
            if (val.size() == 0) return;
            for (int ii = 0; ii < values.size(); ii++) {
                if(!values.get(ii).get(7).toString().isEmpty())continue;
                String status="";
                List<String> file = new ArrayList<>();
                file.addAll(Arrays.stream(values.get(ii).get(5).toString().split(","))
                        .filter(el -> el.contains(".jpg")).collect(Collectors.toList()));
                if (file.size() == 0) continue;

                TwitterInstance twitter = new TwitterInstance();
                status+=(!status.isEmpty())?" ":"" + twitter.sendPost((i == 0) ? "Riverside" : "OC", " ", file);

                LinkedinInstance linkedin = new LinkedinInstance();
                status+=(!status.isEmpty())?" ":"" + linkedin.sendPost((i == 0) ? "Riverside" : "OC", " ", file);

                TumblrInstance tumblr = new TumblrInstance();
                status+=(!status.isEmpty())?" ":"" + tumblr.sendPost((i == 0) ? "Riverside" : "OC", " ", file);

                ValueRange body = new ValueRange()
                        .setValues(Arrays.asList(Arrays.asList(status)));
                service.spreadsheets().values().update(spreadsheetId,  sheetList.get(i)+ "!H"+(ii+2), body)
                        .setValueInputOption("USER_ENTERED")
                        .execute();
            }
        }
    }
}
