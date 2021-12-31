package com;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.Utils.checkWebLink;
import static java.util.stream.Collectors.*;

public class UnknownStatus {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = ScreenShots.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        Collection<String> scopes = new ArrayList<String>();
        scopes.add(SheetsScopes.SPREADSHEETS);
        scopes.add(SheetsScopes.DRIVE);
        scopes.add(DriveScopes.DRIVE);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public static void main(String[] args) throws GeneralSecurityException, IOException {


        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--headless");
        chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();

        Date date = new Date();
  //      SimpleDateFormat format = new SimpleDateFormat("W");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
         calendar.setFirstDayOfWeek( Calendar.MONDAY );
        int weekOfYear=calendar.get(Calendar.WEEK_OF_YEAR);
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1n9nr1IA6IX0CTTvaEXybZI-3oMD2W56JP7MJEc0NalY";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Dogs")
                .build();
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy");

        String[] lists = {"Red", "Yellow", "Green"};
        for (int listNum = 0; listNum < lists.length; listNum++) {
            String sheet = lists[listNum] + "Table";
            String range = (listNum == 0) ? "7" : "8";
            List<List<Object>> values = service.spreadsheets().values().get(spreadsheetId, sheet + "!C" + range + ":" + range).execute().getValues();
            List<Object> values_=new ArrayList<>();
            boolean isFirst=false;
            for(int i=0;i<values.get(0).size();i++) {
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
                if(el.get(1).toString().equals("status"))return true;
                if(!el.get(2).toString().isEmpty()) return false;
                return el.get(1).toString().isEmpty();
            }).collect(toList());
            if(values.size()<2) continue;
            int rowCount = service.spreadsheets().values().get(spreadsheetId, "Unknown!A1:A").execute().getValues().size() + 1;
            List<List<Object>> unknownList = new ArrayList<>();
            for (int i = 1; i < values.size(); i++)
                for (int ii = 2; ii < values.get(i).size(); ii++)
                    if (!values.get(i).get(ii).toString().isEmpty()) {
                        String url = "https://petharbor.com/pet.asp?uaid=RVSD." + values.get(i).get(0).toString();
                        String result=checkWebLink(driver,url,false);
                        String url1="";
                        if (result.contains("NLL")) {
                            url1 = url.replace("RVSD.", "RVSD1.") ;
                            result = checkWebLink(driver,url,false);
                        }
                        unknownList.add(Arrays.asList(dateFormat3.format(date),
                                weekOfYear, dateFormat2.format(new Date(values.get(0).get((ii - 1)).toString())),lists[listNum],values.get(i).get(0).toString(),result,"","","","",url,url1));
                        break;
                    }

            ValueRange body = new ValueRange()
                    .setValues(unknownList);
            AppendValuesResponse result =
                    service.spreadsheets().values().append(spreadsheetId, "Unknown!A" + rowCount, body)
                            .setValueInputOption("USER_ENTERED")
                            .execute();
        }

    driver.close();
    driver.quit();
    }
}
