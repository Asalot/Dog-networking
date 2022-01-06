package base;

import org.openqa.selenium.By;

import java.io.IOException;
import java.util.List;

public abstract class baseInstance {

    private String url;
    private String xpathLoadButton;
    private String xpathNumberOfLoadedFiles;

    public By getXpathButtonTweet() {
        return xpathButtonTweet;
    }

    public void setXpathButtonTweet(By xpathButtonTweet) {
        this.xpathButtonTweet = xpathButtonTweet;
    }

    private By xpathButtonTweet;

    public baseInstance() {

    }
    public abstract String sendPost(String pattern, String text, List<String> files) throws IOException, InterruptedException;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getXpathLoadButton() {
        return xpathLoadButton;
    }

    public void setXpathLoadButton(String xpathLoadButton) {
        this.xpathLoadButton = xpathLoadButton;
    }

    public String getXpathNumberOfLoadedFiles() {
        return xpathNumberOfLoadedFiles;
    }

    public void setXpathNumberOfLoadedFiles(String xpathNumberOfLoadedFiles) {
        this.xpathNumberOfLoadedFiles = xpathNumberOfLoadedFiles;
    }




}