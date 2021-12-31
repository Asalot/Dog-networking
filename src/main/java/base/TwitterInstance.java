package base;

import base.baseInstance;

public class TwitterInstance extends baseInstance {
    public TwitterInstance() {
        setUrl("https://gifmaker.me/");
        setXpathLoadButton("//div[@id='browse']");
        setXpathNumberOfLoadedFiles("//div[@id='frames']//img");
    }
}
