package base.posting;

import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.Utils.getAccount;

public abstract class baseInstanceSocial {
    private String login;
    private String password;
    private String instance;

    public WebDriver getInstanceDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    private WebDriver driver;

    public void setInstanceSocial(InstanceSocial instanceSocialValue) {
        instanceSocial = instanceSocialValue;
    }

    public void setPattern(String patternValue) {
        patternValue = patternValue;
    }
    
    public InstanceSocial getInstanceSocial() {
        return instanceSocial;
    }

    private InstanceSocial instanceSocial;

    public String getInstance() {
        return instance;
    }

    public String getPattern() {
        return pattern;
    }

    private String pattern;

    public baseInstanceSocial(String instanceValue){
        instance=instanceValue;
        instanceSocial=null;
        getCredentials();
    }

    public baseInstanceSocial(String instanceValue,String patternValue,InstanceSocial instanceSocialValue){
        instance=instanceValue;
        pattern=patternValue;
        instanceSocial=instanceSocialValue;
        getCredentials();
    }

    private void getCredentials(){
        try {
            List<String> account = getAccount(getInstance(), getPattern());
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

    public abstract String sendPost() throws IOException, InterruptedException, GeneralSecurityException;
}
