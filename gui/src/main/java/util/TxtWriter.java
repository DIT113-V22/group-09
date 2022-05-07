package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class TxtWriter {


    public static boolean writeEntry(URL path, String entry) throws IOException,URISyntaxException {
        File file = new File(correctPath(path));
        FileWriter writer = new FileWriter(file,true);
        writer.write("\n"+entry);
        writer.flush();
        writer.close();
        return true;
    }

    private static URI correctPath(URL path) throws URISyntaxException {
        String correctedPath = path.toString();
        correctedPath = correctedPath.replace("/target/classes","/src/main/resources");
        return new URI(correctedPath);
    }
}
