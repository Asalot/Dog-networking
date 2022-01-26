package base.posting;

import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.Utils.PATH_SECURITY;

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
      //  getCredentials();
    }

    public void setPatternSocial(String patternValue) {
        patternSocial = patternValue;
    }
    
    public InstanceSocial getInstanceSocial() {
        return instanceSocial;
    }

    private InstanceSocial instanceSocial;

    public String getInstance() {
        return instance;
    }

    public String getPatternSocial() {
        return patternSocial;
    }

    private String patternSocial;

    public baseInstanceSocial(String instanceValue,String patternValue){
        instance=instanceValue;
        instanceSocial=null;
        patternSocial =patternValue;
        getCredentials();
    }

    public baseInstanceSocial(String instanceValue,String patternValue,InstanceSocial instanceSocialValue){
        instance=instanceValue;
        patternSocial =patternValue;
        instanceSocial=instanceSocialValue;
        getCredentials();
    }

    private void getCredentials(){
        try {
            List<String> account = getAccount();
            login = account.get(1);
            password = account.get(2);
        } catch (Exception e) {
        }
    }

    public List<String> getAccount() throws IOException {
        List <List<String>> account= Arrays.asList(new String(Files.readAllBytes(Paths.get(PATH_SECURITY+"accounts.txt"))).split(";"))
                .stream().map(el->Arrays.asList(el.split(","))).collect(Collectors.toList()).stream()
                .filter(el->el.get(0).trim().equals(instance+patternSocial)).collect(Collectors.toList());
        if(account.size()==0) return null;
        else return account.get(0);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public abstract String sendPost() throws IOException, InterruptedException, GeneralSecurityException;
}
