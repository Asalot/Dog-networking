package base;

import base.baseInstance;

public class PDFInstance extends baseInstance {
    public PDFInstance() {
        setUrl("https://www.convert-jpg-to-pdf.net/");
        setXpathLoadButton("//input[@id='select_file_button']");
        setXpathNumberOfLoadedFiles("//li[@class='file_queue_element']");
    }
}
