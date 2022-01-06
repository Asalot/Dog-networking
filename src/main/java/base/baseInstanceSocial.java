package base;

import org.openqa.selenium.By;
import java.io.IOException;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.Utils.getAccount;

public abstract class baseInstanceSocial {
    private String login;
    private By xpathButtonClick;
    private String password;
    private String instance;

    public String getInstance() {
        return instance;
    }

    public String getPattern() {
        return pattern;
    }

    private String pattern;

    public baseInstanceSocial(String instanceValue,String patternValue){
        instance=instanceValue;
        pattern=patternValue;
        try {
            List<String> account = getAccount(instanceValue, patternValue);
            login = account.get(1);
            password = account.get(2);
        } catch (Exception e) {

        }
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public By getXpathButtonClick() {
        return xpathButtonClick;
    }

    public void setXpathButtonTweet(By xpathButton) {
        this.xpathButtonClick = xpathButton;
    }

    public abstract String sendPost(String text,List<java.io.File> files) throws IOException, InterruptedException, GeneralSecurityException;
}
