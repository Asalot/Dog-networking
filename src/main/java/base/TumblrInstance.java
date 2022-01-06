package base;

import org.openqa.selenium.By;

import java.io.IOException;
import java.util.List;

public class TumblrInstance extends baseInstance {

    public TumblrInstance() {
      setXpathButtonTweet(By.xpath("//span[text()='Tweet']"));
    }

    @Override
    public String sendPost(String pattern, String text, List<String> files) throws IOException, InterruptedException {
        return null;
    }
}
