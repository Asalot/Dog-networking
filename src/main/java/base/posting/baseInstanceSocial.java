package base.posting;

import base.posting.InstanceSocial;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.Utils.getAccount;

public abstract class baseInstanceSocial {
    private String login;
    private String password;
    private String instance;

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

    public baseInstanceSocial(String instanceValue,String patternValue,InstanceSocial instanceSocialValue){
        instance=instanceValue;
        pattern=patternValue;
        instanceSocial=instanceSocialValue;
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

    public abstract String sendPost() throws IOException, InterruptedException, GeneralSecurityException;
}
