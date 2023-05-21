//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package chordvers.lanstard.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class IString {
    public String code = "§";

    public IString() {
    }

    public static String addColor(String s) {
        return s.replace("&", "§");
    }

    public static String getStringFromURL(String s) {
        try {
            URL url = new URL(s);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(500);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = conn.getInputStream();
            return readInputStream(inputStream);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int len;
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }

        bos.close();
        return bos.toString(String.valueOf(StandardCharsets.UTF_8));
    }
}
