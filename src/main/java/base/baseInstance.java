package base;

public abstract class baseInstance {

    private String url;
    private String xpathLoadButton;
    private String xpathNumberOfLoadedFiles;

    public baseInstance() {

    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getXpathLoadButton() {
        return xpathLoadButton;
    }

    public void setXpathLoadButton(String xpathLoadButton) {
        this.xpathLoadButton = xpathLoadButton;
    }

    public String getXpathNumberOfLoadedFiles() {
        return xpathNumberOfLoadedFiles;
    }

    public void setXpathNumberOfLoadedFiles(String xpathNumberOfLoadedFiles) {
        this.xpathNumberOfLoadedFiles = xpathNumberOfLoadedFiles;
    }
}