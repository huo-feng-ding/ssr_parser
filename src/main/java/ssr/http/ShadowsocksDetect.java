package ssr.http;

import com.stfl.misc.Config;
import com.stfl.network.NioLocalServer;
import com.zkdcloud.shadowsocks.client.socks5.ClientDetecter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import static com.zkdcloud.shadowsocks.client.socks5.config.ClientConfig.clientConfig;

public class ShadowsocksDetect {
    private static final Logger LOGGER = Logger.getLogger(ShadowsocksDetect.class.getName());
    private static Decoder base64Decoder = Base64.getUrlDecoder();
    
    public static boolean detect(Config config) throws IOException, InvalidAlgorithmParameterException {
        NioLocalServer server = new NioLocalServer(config);
        //LocalServer server = new LocalServer(config);
        Thread t = new Thread(server);
        t.start();
        try {
            return HttpDetect.detect(config.getMethod());
        } finally {
            server.close();
        }
    }
    
    public static Config parseSsr(String ssr) {
        ssr = ssr.replace("ssr://", "");
        String ssrInfo = new String(base64Decoder.decode(ssr), StandardCharsets.UTF_8);
        ssrInfo = ssrInfo.substring(0, ssrInfo.indexOf('/'));
        String[] split = ssrInfo.split(":");
        Config config = new Config(split[0],
                                   Integer.parseInt(split[1]),
                                   "127.0.0.1",
                                   10086,
                                   split[3],
                                   new String(base64Decoder.decode(split[5]), StandardCharsets.UTF_8));
        return config;
    }
    
    public static void detect(Set<String> ssrSet) {
        Iterator<String> iterator = ssrSet.iterator();
        while (iterator.hasNext()) {
            String ssr = iterator.next();
            try {
                Config config = parseSsr(ssr);
                boolean b = detect(config);
                if (!b) {
                    iterator.remove();
                    LOGGER.info("removed:" + ssr);
                }
            } catch (Exception e) {
                LOGGER.info("detect error:" + ssr + "   " + e.getMessage());
                iterator.remove();
            }
        }
        
        ssrSet.forEach(System.out::println);
    }
    
    /****************下边的zkdcloud使用*/
    public static void detectShadowsocks(Set<String> ssrSet) {
        Iterator<String> iterator = ssrSet.iterator();
        while (iterator.hasNext()) {
            String ssr = iterator.next();
            ClientDetecter clientDetecter = null;
            try {
                Config config = parseSsr(ssr);
                // remote ip
                clientConfig.setServer(config.getRemoteIpAddress());
                // remote port
                clientConfig.setServer_port(config.getRemotePort());
                // remote secret key
                clientConfig.setPassword(config.getPassword());
                // encrypt key
                clientConfig.setMethod(config.getMethod());
                // method
                clientConfig.setLocal_port(config.getLocalPort());
                clientDetecter = new ClientDetecter();
                boolean b = HttpDetect.detect(config.getMethod());
                if (!b) {
                    iterator.remove();
                    LOGGER.info("removed:" + ssr);
                }
            } catch (Exception e) {
                LOGGER.info("detect error:" + ssr + "   " + e.getMessage());
                iterator.remove();
            } finally {
                if (clientDetecter != null) { clientDetecter.shutdown(); }
            }
        }
        
        ssrSet.forEach(System.out::println);
        
    }
}
