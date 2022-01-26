package com;

import base.posting.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.Utils.*;

public class Posting {

    public static void main(String[] args) throws GeneralSecurityException, IOException, InterruptedException {
        final String spreadsheetId = "1jTUJjJOZt5kju6oDavUmMay5-E8UK2it8i-HsY_XMQs";
        Sheets service = getService();
        List<String> sheetList = Arrays.asList("dogs", "OCdogs");
        //      List<baseInstanceSocial> listInstances = Arrays.asList(
//                        new TwitterInstance((i == 0) ? "Riverside" : "OC", instance),
//                        new LinkedinInstance("", instance),
//                        new TumblrInstance("", instance),
//                new PinterestInstance(),
//                new InstagramInstance());
//                        new YoutubeInstance("", instance),
//                        new FacebookInstance("", instance));

        List<String> listDates = new ArrayList<>();
        Date d1 = new Date();
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM/dd/yyyy");
        listDates.add(dateFormat3.format(d1));
        listDates.add(dateFormat3.format(DateUtils.addDays(d1, -1)));

        for (int i = 0; i < sheetList.size(); i++) {
            int lastRow = service.spreadsheets().values().get(spreadsheetId, sheetList.get(i) + "!A:A").execute().getValues().size();
            List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId, sheetList.get(i) + "!A2:K" + lastRow).execute().getValues();

            List<List<Object>> val = values.stream().filter(
                    el -> {
                        try {
                            return el.size() > 10
                                    //               && !el.get(6).equals("");
                                    && listDates.indexOf(dateFormat3.format(new Date(el.get(0).toString()))) != -1;
                        } catch (Exception e) {
                            System.out.println(el.toString());
                            return false;
                        }
                    }).collect(Collectors.toList());
            if (val.size() == 0) continue;

            List<baseInstanceSocial> listInstances = Arrays.asList(
                    new PinterestInstance(""),
                    new InstagramInstance((i == 0) ? "" : "OC"));

            for (int ii = values.size() - 1; ii >= 0; ii--) {
                if (values.get(ii).size() != 11) continue;
                if (values.get(ii).get(6).toString().isEmpty()) continue;
                if (listDates.indexOf(dateFormat3.format(new Date(values.get(ii).get(0).toString()))) == -1) break;

                InstanceSocial instance = new InstanceSocial(values.get(ii));
                boolean isStatusChanged = false;
                for (int instanceNum = 0; instanceNum < listInstances.size(); instanceNum++) {
                    if (listInstances.get(instanceNum).getInstance().equals("instagram") && i == 0) continue;

                    if (listInstances.get(instanceNum).getInstance().equals("facebook") &&
                            !instance.getText(false).contains("!f")) continue;

                    listInstances.get(instanceNum).setInstanceSocial(instance);

                    int ind = -1;
                    for (int stNum = 0; stNum < instance.getStatus().size(); stNum++)
                        if (instance.getStatus().get(stNum).toString().contains(listInstances.get(instanceNum).getInstance())) {
                            ind = stNum;
                            break;
                        }

                    //            int ind = instance.getStatus().indexOf(listInstances.get(instanceNum).getInstance());
                    if (ind == -1) instance.setStatus(listInstances.get(instanceNum).sendPost());
                    else if (ind != -1 && instance.getStatus().get(ind).toString().contains("-F"))
                        instance.changeStatus(ind, listInstances.get(instanceNum).sendPost());
                    else continue;
                    isStatusChanged = true;
                }
                if (isStatusChanged) {
                    ValueRange body = new ValueRange()
                            .setValues(Arrays.asList(instance.getStatusString()));
                    service.spreadsheets().values().update(spreadsheetId, sheetList.get(i) + "!H" + (ii + 2), body)
                            .setValueInputOption("USER_ENTERED")
                            .execute();
                }

            }
            for (int instanceNum = 0; instanceNum < listInstances.size(); instanceNum++) {
               if(listInstances.get(instanceNum).getInstanceDriver()!=null) {
                   listInstances.get(instanceNum).getInstanceDriver().close();
                   listInstances.get(instanceNum).getInstanceDriver().quit();
               }
            }
        }
    }
}
