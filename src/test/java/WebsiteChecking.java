
import com.ScreenShots;
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
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.io.File;
import java.io.*;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.support.ui.WebDriverWait;

public class WebsiteChecking {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = ScreenShots.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String[] args) throws GeneralSecurityException, IOException, InterruptedException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1n9nr1IA6IX0CTTvaEXybZI-3oMD2W56JP7MJEc0NalY";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Dogs")
                .build();
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, 10);

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        String[] lists = {"Red", "Yellow", "Green"};

        File file = new File("./Screens/website.txt");
        file.createNewFile();

        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM/dd/yyyy");
        for (int listNum = 0; listNum < lists.length; listNum++) {
            List<List<Object>> listPages = service.spreadsheets().values().get(spreadsheetId, lists[listNum] + "!L2:M").execute().getValues();
            Date date=new Date(listPages.get(listPages.size()-1).get(0).toString());
            String date1 = dateFormat3.format(date);
            listPages = listPages.stream().filter(el -> dateFormat3.format(new Date(el.get(0).toString())).equals(date1)).collect(Collectors.toList());
            List<String> idList=new ArrayList<>();
            for (List<Object> l : listPages) {
                String id=l.get(1).toString().substring(l.get(1).toString().indexOf(".A")+ 1);
                if(idList.indexOf(id)==-1)idList.add(id);
                else continue;
                driver.get(l.get(1).toString());
                // Thread.sleep(200);
                if (driver.getPageSource().contains("I HAVE BEEN")) {
                    String res=driver.findElement(By.xpath("//td[@class=\"DetailDesc\"]//span")).getText();
                    System.out.println(id + " " +res+" "+ l.get(1).toString());
                    FileWriter myWriter = new FileWriter(file.getAbsoluteFile(),true);
                    myWriter.write(date1+": "+id + " " +res+" "+ l.get(1).toString()+"\n");
                    myWriter.close();
                }  else  if (driver.findElement(By.xpath("//body")).getText().contains("Sorry! This animal is no longer in our online database.")){
                    System.out.println(id + " NLL");
                    FileWriter myWriter = new FileWriter(file.getAbsoluteFile(), true);
                    myWriter.write(date1+": "+id + " NLL"+"\n");
                    myWriter.close();
                }
            }
        }
        driver.close();
        driver.quit();
    }
}