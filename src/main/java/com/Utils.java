package com;

import base.*;
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

import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Utils {

    private static int countDogs = 0;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    public static final String PATH_SCREEN = "D:\\Backup Screen\\Screen";
    public static final String PATH_SECURITY = "C:\\Users\\Natalia\\Dogs\\";
    public static final String PATH_DOWNLOAD = "C:\\Users\\Natalia\\Downloads\\";

    private List<String> newDogs = new ArrayList<>();

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = Files.newInputStream(Paths.get(PATH_SECURITY + "credentials.json"));
//                ScreenShotsOC.class.getResourceAsStream(PATH_SECURITY+"credentials.json");
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + PATH_SECURITY + "credentials.json");
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

    public static WebDriver getDriverFirefox(boolean headless) {
        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");
//        FirefoxOptions chromeOptions = new FirefoxOptions();
//        chromeOptions.addArguments("disable-infobars");
//        chromeOptions.addArguments("--disable-notifications");
//        chromeOptions.addArguments("--start-maximized");
//        if (headless) chromeOptions.addArguments("--headless");

        //  chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        return new FirefoxDriver();
    }

    public static baseLoader createCombinedFiles(String instance, Date date, String pattern, List<DogInfoPetharbor> files) throws InterruptedException, IOException {
        baseLoader loader = null;
        if (instance.equals("twitter")) {
            TwitterLoader twitter = new TwitterLoader("twitter", date, pattern, null);
            loader = twitter;
        } else if (instance.equals("pdf")) {
            PdfLoader pdf = new PdfLoader("pdf", date, pattern, null);
            loader = pdf;
        } else if (instance.equals("red list")) {
            GifLoader gif = new GifLoader("red list", date, pattern, files);
            loader = gif;
        } //else if (instance.equals("facebook")) {
//            FacebookLoader fac = new FacebookLoader("facebook", date, pattern, files);
//            loader = fac;
//        }
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
            loader.sendPost();
            text=loader.getFileName();
        }
        return text;
    }

    public static String checkWebLink(WebDriver driver, String url, boolean isFullText) {
        String id = url.substring(url.indexOf(".A") + 1);
        if (url.contains("petharbor")) {
            driver.get(url);
            if (driver.getPageSource().contains("I HAVE BEEN") || driver.getPageSource().contains("I was reclaimed")) {
                String res = driver.findElement(By.xpath("//td[@class='DetailDesc']//span")).getText();
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

    public static void setHistory(String pattern, List<String> listAllId) throws IOException {
        File file1 = new File("./history" + pattern + ".txt");
        file1.createNewFile();
        FileWriter myWriter = new FileWriter(file1.getAbsoluteFile(), true);
        List<String> text = Arrays.asList(new String(Files.readAllBytes(Paths.get("./history" + pattern + ".txt"))).split(";"));
        if (text.size() == 0) {
            myWriter.append(listAllId.stream().collect(Collectors.joining(";")));
        } else {
            SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM/dd/yyyy");
            if (dateFormat3.format(new Date()).equals(text.get(0))) {
                listAllId.remove(0);
                text.addAll(listAllId);
                myWriter.write(text.stream().collect(Collectors.joining(";")));
            } else myWriter.write(listAllId.stream().collect(Collectors.joining(";")));
        }
        myWriter.close();
    }

    public static boolean getSamplePicture(WebDriver driver, Dimension size) throws IOException {
        Color myGray = new Color(209, 209, 209);
        Color myGray1= new Color(227, 228, 223);
        File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage fullScreen = ImageIO.read(screen);
        BufferedImage logoImage = fullScreen.getSubimage(0, 0, 800, 410);
        if(driver.getCurrentUrl().contains("petharbor"))return logoImage.getRGB(size.getWidth(), size.getHeight()) == myGray.getRGB();
        else return logoImage.getRGB(size.getWidth(), size.getHeight()) == myGray1.getRGB();
    }


    public static File screenShot(WebDriver driverPetHarbor, WebDriver driver, String path) throws IOException {
        File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage fullScreen = ImageIO.read(screen);
        if (driverPetHarbor == null || driverPetHarbor == driver) {
            BufferedImage logoImage = fullScreen.getSubimage(0, 0, 800, 410);
            ImageIO.write(logoImage, "png", screen);
        } else {
            int y = 322;
            Color myWhite = new Color(255, 255, 255);
            Color mycolor = new Color(fullScreen.getRGB(225, y));
            if (mycolor.getRGB() != myWhite.getRGB()) {
                for (int i = (y + 1); i < fullScreen.getHeight(); i++) {
                    mycolor = new Color(fullScreen.getRGB(225, i));
                    if (mycolor.getRGB() == myWhite.getRGB()) {
                        y = i;
                        break;
                    }
                }
            }
            int height = 276;
            if (y + height > fullScreen.getHeight()) height = fullScreen.getHeight() - y;
            BufferedImage logoImage1 = fullScreen.getSubimage(215, 27, 340, 295);
            BufferedImage logoImage2 = fullScreen.getSubimage(215, y, 340, height);
            BufferedImage combined = new BufferedImage(571, 340, BufferedImage.TYPE_INT_ARGB);
            Graphics g = combined.getGraphics();
            g.drawImage(logoImage1, 0, 0, null);
            g.drawImage(logoImage2, 340, 10, null);
            g.dispose();
            ImageIO.write(combined, "png", screen);
        }
        FileUtils.copyFile(screen, new File(PATH_SCREEN + path));
        return screen;
    }

    public static void saveCreateFile(String from, String whereTo) {
        try {
            File file = new File(PATH_SCREEN + whereTo);
            FileUtils.copyFile(new File(from), file);
        } catch (Exception e) {
            System.out.println("File isn't created: " + e.getMessage());
        }
    }
}