package base;

import base.baseInstance;

import java.io.IOException;
import java.util.List;

public class PDFInstance extends baseInstance {
    public PDFInstance() {
        setUrl("https://www.convert-jpg-to-pdf.net/");
        setXpathLoadButton("//input[@id='select_file_button']");
        setXpathNumberOfLoadedFiles("//li[@class='file_queue_element']");
    }

    @Override
    public String sendPost(String pattern, String text, List<String> files) throws IOException, InterruptedException {
        return null;
    }
}
