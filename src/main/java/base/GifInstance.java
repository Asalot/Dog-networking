package base;

public class GifInstance extends baseInstance {

    public GifInstance() {
        setUrl("https://gifmaker.me/");
        setXpathLoadButton("//div[@id='browse']");
        setXpathNumberOfLoadedFiles("//div[@id='frames']//img");
        }
}
