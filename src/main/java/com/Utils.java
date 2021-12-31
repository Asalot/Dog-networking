package com;

import base.PdfLoader;
import base.TwitterLoader;
import base.baseLoader;
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
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class Utils {

    private static int countDogs = 0;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    public List<String> getNewDogs() {
        return newDogs;
    }

    private List<String> newDogs=new ArrayList<>();

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = ScreenShotsOC.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        Collection<String> scopes = new ArrayList<>();
        scopes.add(SheetsScopes.SPREADSHEETS);
        scopes.add(SheetsScopes.DRIVE);
        scopes.add(DriveScopes.DRIVE);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static Sheets getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Dogs")
                .build();
    }

    public static Drive getDrive() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Dogs")
                .build();
    }

    public static WebDriver getDriver(boolean headless) {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--start-maximized");
        if (headless) chromeOptions.addArguments("--headless");

        chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        return new ChromeDriver(chromeOptions);
    }

    public static baseLoader createCombinedFiles(String instance, Date date, String pattern) throws InterruptedException, IOException {
        baseLoader loader = null;
        if (instance.equals("twitter")) {
            TwitterLoader twitter = new TwitterLoader("twitter", date, pattern);
            loader = twitter;
        }
        if (instance.equals("pdf")) {
            PdfLoader pdf = new PdfLoader("pdf", date, pattern);
            loader = pdf;
        }
        System.out.println("load files to " + instance);
        loader.loadFiles();
        loader.setSettingsAndDownload();
        System.out.println("Setting " + instance);

        return loader;
    }

    public static String createCombinedFiles2(baseLoader loader, int dogsNumbers, int urgentDogs) throws InterruptedException, IOException {
        loader.setSettingsAndDownloadAdd();
        System.out.println("Waiting for file to be downloaded for " + loader.getInstance());
        loader.findDownloadedFile();
        loader.closeDriver();

        String text = "";
        if (loader.getInstance().equals("pdf")) {
            System.out.println("Upload to drive");
            text = loader.loadToDrive();
        } else {
            System.out.println("Create file for twitter");
            loader.setUpText(dogsNumbers, urgentDogs);
        }

        return text;
    }

    public static String checkWebLink(WebDriver driver, String url, boolean isFullText) {
        String id = url.substring(url.indexOf(".A") + 1);
        if (url.contains("petharbor")) {
            driver.get(url);
            if (driver.getPageSource().contains("I HAVE BEEN") || driver.getPageSource().contains("I was reclaimed")) {
                String res = driver.findElement(By.xpath("//td[@class=\"DetailDesc\"]//span")).getText();
                return (isFullText) ? id + " " + res + " " + url : res;
            } else if (driver.findElement(By.xpath("//body")).getText().contains("Sorry! This animal is no longer in our online database.")) {
                return (isFullText) ? id + " NLL" : "NLL";
            }
        } else if (url.contains("ocpetinfo")) {
            driver.get(url.substring(0, url.indexOf(".")));
            driver.findElement(By.xpath("//div[@ng-show='ShowDesktop()']//*[@id='searchpet']")).sendKeys(id);
            List<WebElement> listAnimals = driver.findElements(By.xpath("//div[contains(@class,'Box ng-scope')]"));
            if (listAnimals.size() == 0) return (isFullText) ? id + " NLL" : "NLL";
        }
        return "";
    }

    public static List<String> getHistory(String pattern) throws IOException {
        File file1 = new File("./history" + pattern + ".txt");
        file1.createNewFile();
        return Arrays.asList(new String(Files.readAllBytes(Paths.get("./history" + pattern + ".txt"))).split(";"));
    }

    public static void setHistory(String pattern,List<String> listAllId) throws IOException {
        File file1 = new File("./history" + pattern + ".txt");
        file1.createNewFile();
        FileWriter myWriter = new FileWriter(file1.getAbsoluteFile(), true);
        List<String> text = Arrays.asList(new String(Files.readAllBytes(Paths.get("./history" + pattern + ".txt"))).split(";"));
        if (text.size() == 0) {
             myWriter.append(listAllId.stream().collect(Collectors.joining(";")));
         }
        else {
            SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM/dd/yyyy");
            if(dateFormat3.format(new Date()).equals(text.get(0))){
                listAllId.remove(0);
                text.addAll(listAllId);
                myWriter.write(text.stream().collect(Collectors.joining(";")));
            }
            else myWriter.write(listAllId.stream().collect(Collectors.joining(";")));
        }
        myWriter.close();
    }

    public static File screenShot(WebElement el, WebDriver driver, String path){
        File screen ;
        if(driver==null) screen=((TakesScreenshot) el).getScreenshotAs(OutputType.FILE);
        else screen=((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screen, new File(path));
        } catch (Exception e) {
        }
        return screen;
    }

    public static void saveCreateFile(String from, String whereTo) {
        try {
            File file = new File(from);
            FileUtils.copyFile(new File(whereTo), file);
        } catch (Exception e) {
            System.out.println("File isn't created: " + e.getMessage());
        }
    }

}