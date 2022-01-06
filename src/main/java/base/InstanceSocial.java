package base;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.Utils.getDrive;

public class InstanceSocial {

    private static final String PATH="D:\\DownloadedFiles\\";

    public List<java.io.File> getFiles() {
        return files;
    }

    private List<java.io.File> files =new ArrayList<>();
    private List<Object> info=new ArrayList<>() ;

    public InstanceSocial(List<Object> info) throws GeneralSecurityException, IOException {
        this.info = info;
        gettingFiles();
    }

    private void gettingFiles() throws GeneralSecurityException, IOException {
        if (info.size() != 11) return;
        Drive drive = getDrive();
        List<String> filesDrive = Arrays.asList(info.get(10).toString().split(","));
        if (filesDrive.size() == 0) return;

        for (int i = 0; i < filesDrive.size(); i++) {
            try {
                File downloadFile = drive.files().get(filesDrive.get(i).substring((filesDrive.get(i).indexOf("id=") + 3))).execute();
                String extension = downloadFile.getMimeType().substring(downloadFile.getMimeType().indexOf("/")+1);
                java.io.File f = new java.io.File(PATH + info.get(1).toString().trim() + "." + extension);
                OutputStream outputStream =new FileOutputStream(f.getAbsolutePath());
                drive.files().get(filesDrive.get(i).substring((filesDrive.get(i).indexOf("id=") + 3)))
                        .executeMediaAndDownloadTo(outputStream);
                if (f.exists()) files.add(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
