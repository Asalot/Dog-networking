package base.posting;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.Utils.getDrive;

public class InstanceSocial {

    private static final String PATH = "D:\\DownloadedFiles\\";

    public void setText(String textValue) {
        textValue = textValue;
    }

    private String text;
    private String id;

    public List<Object> getStatus() {
        return status;
    }

    public List<Object> getStatusString() {
        List<Object> l = new ArrayList<>();
        String x = "";
        for (int i = 0; i < status.size(); i++) {
            if (x.isEmpty()) x = x + status.get(i);
            else x = x + "," + status.get(i);
        }
        l.add(x);
        return l;
    }

    public void setStatus(String text) {
        Object o=text;
        status.add(o);
    }

    public void changeStatus(int index, String text) {
        status.set(index, text);
    }

    private List<Object> status = new ArrayList<>();

    public String getUrl() {
        return url;
    }

    private String url;

    public String getUrlPetharbor() {
        return urlPetharbor;
    }

    private String urlPetharbor;

    public String getText(boolean isRemoveEmogy) {
        String text1 = text;
        if (isRemoveEmogy) {
            text1 = text1.replaceAll("\uD83D\uDCE3", "");
            text1 = text1.replaceAll("\uD83C\uDD98", "");
            text1 = text1.replaceAll("\uD83C\uDFE2", "");
            text1 = text1.replaceAll("\uD83E\uDD0D", "");
            text1 = text1.replaceAll("\uD83D\uDCE7", "");
            text1 = text1.replaceAll("➡", "");
        }
        return text1;
    }

    public String getId() {
        return id;
    }

    public List<String> getTag() {
        return tag;
    }

    public List<String> getTagComma() {
        return tagComma;
    }

    private List<String> tag;
    private List<String> tagComma;


    public List<java.io.File> getFiles() {
        return files;
    }

    private List<java.io.File> files = new ArrayList<>();
    private List<java.io.File> images = new ArrayList<>();

    public List<java.io.File> getImages() {
        return images;
    }

    public List<java.io.File> getVideo() {
        return video;
    }

    private List<java.io.File> video = new ArrayList<>();

    private List<Object> info = new ArrayList<>();



    public InstanceSocial(List<Object> info) throws GeneralSecurityException, IOException {
        this.info = info;
        gettingFiles();
        text = info.get(6).toString();
        text = text.trim();
        id = info.get(1).toString().trim();
        tag = Arrays.stream(info.get(8).toString().split(" ")).filter(el -> !el.isEmpty()).collect(Collectors.toList());
        tagComma = Arrays.stream(info.get(9).toString().split(" ")).filter(el -> !el.isEmpty()).collect(Collectors.toList());
        url = info.get(3).toString().trim();
        urlPetharbor = info.get(2).toString().trim();
        status = new ArrayList<>();
        try {
           if(!info.get(7).toString().trim().isEmpty())
              status = Arrays.asList(info.get(7).toString().split(","));
        } catch (Exception e) {
            
        }

    }

    private void gettingFiles() throws GeneralSecurityException, IOException {
        if (info.size() != 11) return;
        Drive drive = getDrive();
        List<String> filesDrive = Arrays.asList(info.get(10).toString().split(","));
        if (filesDrive.size() == 0) return;

        for (int i = 0; i < filesDrive.size(); i++) {
            try {
                File downloadFile = drive.files().get(filesDrive.get(i).substring((filesDrive.get(i).indexOf("id=") + 3))).execute();
                String extension = downloadFile.getMimeType().substring(downloadFile.getMimeType().indexOf("/") + 1);
                java.io.File f = new java.io.File(PATH + info.get(1).toString().trim() + "." + extension);
                OutputStream outputStream = new FileOutputStream(f.getAbsolutePath());
                drive.files().get(filesDrive.get(i).substring((filesDrive.get(i).indexOf("id=") + 3)))
                        .executeMediaAndDownloadTo(outputStream);
                if (f.exists()) {
                    files.add(f);
                    if (downloadFile.getMimeType().contains("image")) images.add(f);
                    else video.add(f);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImage() {
        if (getImages().size() == 0) return null;
        return getImages().get(0).getAbsolutePath();
    }

}
