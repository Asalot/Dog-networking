package com;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import static com.Utils.getDriver;

public class Instagram {

    public static void main(String[] args) throws InterruptedException {

        WebDriver driver=getDriver(false);
        driver.get("https://www.instagram.com/");
        driver.findElement(By.name("username")).sendKeys("dogsmatter_network");
        driver.findElement(By.name("password")).sendKeys("Nata171277#");
        driver.findElement(By.xpath("//button[div[contains(text(),'Log In')]]")).click();
        Thread.sleep(1000);
      //  driver.findElement(By.xpath("//img[contains(@alt,'dogsmatter_network')]")).click();
        driver.get("https://www.instagram.com/dogsmatter_network/");
        Thread.sleep(1000);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView(true);",
                driver.findElement(By.xpath("//div[contains(text(),'2021 Instagram from Meta')]")));
        




    }
}
