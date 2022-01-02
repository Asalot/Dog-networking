package base;

public class TwitterInstance extends baseInstance {
    public TwitterInstance() {
        setUrl("https://gifmaker.me/");
        setXpathLoadButton("//div[@id='browse']");
        setXpathNumberOfLoadedFiles("//div[@id='frames']//img");
    }
}
