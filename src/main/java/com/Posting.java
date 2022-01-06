package com;

import base.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.openqa.selenium.json.JsonOutput;

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
        Sheets service = getService();
        List<String> sheetList = Arrays.asList("dogs", "OCdogs");
        for (int i = 0; i < sheetList.size(); i++) {
            int lastRow = service.spreadsheets().values().get(spreadsheetId, sheetList.get(i) + "!A:A").execute().getValues().size();
            List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId, sheetList.get(i) + "!A2:K" + lastRow).execute().getValues();
            List<List<Object>> val = values.stream().filter(
                    el -> {
                            try{
                                return el.size()>10
                            && el.get(7).equals("") &&
                            !el.get(5).equals("");}
                            catch(Exception e){
                                System.out.println(el.toString());
                                return false;}
                    }).collect(Collectors.toList());
            if (val.size() == 0) continue;
            for (int ii = 0; ii < values.size(); ii++) {
                if(values.get(ii).size()!=11)continue;
                if(!values.get(ii).get(7).toString().isEmpty())continue;
                String status="";
                InstanceSocial instance=new InstanceSocial(values.get(ii));

                TwitterInstance twitter = new TwitterInstance((i == 0) ? "Riverside" : "OC",instance);
                status+=(!status.isEmpty())?" ":"" + twitter.sendPost(" ",instance.getFiles());

                LinkedinInstance linkedin = new LinkedinInstance("",values.get(ii));
                status+=(!status.isEmpty())?" ":"" + linkedin.sendPost((i == 0) ? "Riverside" : "OC", " ");

                TumblrInstance tumblr = new TumblrInstance("",values.get(ii));
                status+=(!status.isEmpty())?" ":"" + tumblr.sendPost((i == 0) ? "Riverside" : "OC", " ");

                YoutubeInstance youtube = new YoutubeInstance("",values.get(ii));
                status+=(!status.isEmpty())?" ":"" + youtube.sendPost((i == 0) ? "Riverside" : "OC", " ");

                if(values.get(ii).get(6).toString().contains("!f")){  //facebook
                    FacebookInstance facebook = new FacebookInstance("",values.get(ii));
                    status+=(!status.isEmpty())?" ":"" + facebook.sendPost((i == 0) ? "Riverside" : "OC", " ");
                 }


                ValueRange body = new ValueRange()
                        .setValues(Arrays.asList(Arrays.asList(status)));
                service.spreadsheets().values().update(spreadsheetId,  sheetList.get(i)+ "!H"+(ii+2), body)
                        .setValueInputOption("USER_ENTERED")
                        .execute();
            }
        }
    }
}
