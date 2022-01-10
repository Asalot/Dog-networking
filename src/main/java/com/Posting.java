package com;

import base.posting.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.Utils.*;

public class Posting {

    public static void main(String[] args) throws GeneralSecurityException, IOException, InterruptedException {
        final String spreadsheetId = "1jTUJjJOZt5kju6oDavUmMay5-E8UK2it8i-HsY_XMQs";
        Sheets service = getService();
        List<String> sheetList = Arrays.asList("dogs", "OCdogs");

        for (int i = 0; i < sheetList.size(); i++) {
            int lastRow = service.spreadsheets().values().get(spreadsheetId, sheetList.get(i) + "!A:A").execute().getValues().size();
            List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId, sheetList.get(i) + "!A2:K" + lastRow).execute().getValues();
            List<List<Object>> val = values.stream().filter(
                    el -> {
                        try {
                            return el.size() > 10
                                    && !el.get(6).equals("");
                        } catch (Exception e) {
                            System.out.println(el.toString());
                            return false;
                        }
                    }).collect(Collectors.toList());
            if (val.size() == 0) continue;
            for (int ii = 0; ii < values.size(); ii++) {
                if (values.get(ii).size() != 11) continue;
                if (!values.get(ii).get(7).toString().isEmpty()) continue;
                String status = "";
                InstanceSocial instance = new InstanceSocial(values.get(ii));
                List<baseInstanceSocial> listInstances = Arrays.asList(
//                        new TwitterInstance((i == 0) ? "Riverside" : "OC", instance),
//                        new LinkedinInstance("", instance),
//                        new TumblrInstance("", instance),
                        new PinterestInstance("", instance));
//                        new YoutubeInstance("", instance),
//                        new FacebookInstance("", instance));
                for (int instanceNum = 0; instanceNum < listInstances.size(); instanceNum++) {
                    if (listInstances.get(instanceNum).getInstance().equals("facebook") &&
                            !instance.getText(false).contains("!f")) continue;
           //                  if (instance.getStatus().size()>0) {
                        int ind = instance.getStatus().indexOf(listInstances.get(instanceNum).getInstance());
                        if (ind == -1) instance.setStatus(listInstances.get(instanceNum).sendPost());
                        else if (ind != -1 && instance.getStatus().get(ind).toString().contains("-F"))
                            instance.changeStatus(ind, listInstances.get(instanceNum).sendPost());
                        else continue;
  //                  }
                }

                ValueRange body = new ValueRange()
                        .setValues(Arrays.asList(instance.getStatusString()));
                service.spreadsheets().values().update(spreadsheetId, sheetList.get(i) + "!H" + (ii + 2), body)
                        .setValueInputOption("USER_ENTERED")
                        .execute();
            }
        }
    }
}
