package base.posting;

import java.io.IOException;

public class FacebookInstance extends baseInstanceSocial {

    public FacebookInstance(String pattern, InstanceSocial instanceSocialValue) throws IOException {
        super("facebook", pattern,instanceSocialValue);

    }


    @Override
    public String sendPost() throws IOException {
        return null;
    }
}
