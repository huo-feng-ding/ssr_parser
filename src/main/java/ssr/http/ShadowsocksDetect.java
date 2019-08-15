package ssr.http;

import com.stfl.misc.Config;
import com.stfl.network.NioLocalServer;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.util.Base64;

public class ShadowsocksDetect {
    
    public static boolean detect(Config config) throws IOException, InvalidAlgorithmParameterException {
        NioLocalServer server = new NioLocalServer(config);
        //LocalServer server = new LocalServer(config);
        Thread t = new Thread(server);
        t.start();
        try {
            HttpDetect.detect();
        } catch (SSLHandshakeException e) {
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            server.close();
        }
        return true;
    }
    
    public static Config parseSsr(String ssr) {
        ssr = ssr.replace("ssr://", "");
        String ssrInfo = new String(Base64.getUrlDecoder().decode(ssr), StandardCharsets.UTF_8);
        System.out.println(ssrInfo);
    
        return null;
    }
}
