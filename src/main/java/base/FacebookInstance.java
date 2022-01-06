package base;

import org.openqa.selenium.By;

import java.io.IOException;
import java.util.List;

public class FacebookInstance extends baseInstanceSocial{

    public FacebookInstance(String pattern,List<Object> infoValue) throws IOException {
        super("youtube",pattern, infoValue);
        setXpathButtonTweet(By.xpath("//span[text()='Tweet']"));
    }


    @Override
    public String sendPost(String pattern, String text) throws IOException, InterruptedException {
        return null;
    }
}
