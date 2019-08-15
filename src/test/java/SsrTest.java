import com.stfl.misc.Config;
import com.stfl.network.NioLocalServer;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SsrTest {
    public static void main(String[] args) throws Exception{
        Thread te = null;
        try {
            te= te(args);
        } catch (SSLHandshakeException e) {
            te.interrupt();
        
        }
        te(args);
    }
    public static Thread te(String[] args) throws
                                           IOException,
                                           InvalidAlgorithmParameterException,
                                           InterruptedException,
                                           NoSuchAlgorithmException,
                                           KeyManagementException {
        /*
        new Thread(() -> {
            try {
                Socks5ProxyServer.main(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        */
        
        
        /*
        Socket socket = new Socket("194.156.231.13", 62);
        new Thread(new ClientHandler(socket), "client-handler-" + UUID.randomUUID().toString()).start();
        System.out.println(socket.getLocalAddress().getHostAddress());
        OutputStream outputStream = socket.getOutputStream();
        byte[] ver = {5, 0};
        outputStream.write(ver);
        outputStream.flush();
        System.out.println("output");
        InputStream inputStream = socket.getInputStream();
        System.out.println(inputStream.read());
        socket.close();
        */
        /*Shadowsocks shadowsocks = new Shadowsocks("194.156.231.13", 62, "h4z9hVfhkb0W");
        shadowsocks.start(10086);
        
        shadowsocks.join();*/
        
        //Config config = new Config("45.89.197.7", 62, "127.0.0.1", 10086, "rc4", "lncn.org o9");
        Config config = new Config("194.156.231.13", 62, "127.0.0.1", 10086, "rc4", "lncn.org 5q");
        //Config config = new Config("139.162.61.44", 8097, "127.0.0.1", 10086, "aes-256-cfb", "eIW0Dnk69454e6nSwuspv9DmS201tQ0D");
        NioLocalServer server = new NioLocalServer(config);
        //LocalServer server = new LocalServer(config);
        Thread t = new Thread(server);
        t.start();
        //TimeUnit.SECONDS.sleep(5);
        //t.join();
        
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }
                    
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }
                    
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        
        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        OkHttpClient httpClient = new Builder().proxy(new Proxy(Type.SOCKS, new InetSocketAddress("127.0.0.1", 10086)))
                                               .followRedirects(true)
                                               .followSslRedirects(true)
                                               .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                                               .hostnameVerifier((hostname, session) -> true)
                                               .build();
        Request request = new Request.Builder().url("https://www.google.com/").build();
        try {
        
            System.out.println(httpClient.newCall(request).execute().body().string());
        } catch (Exception e) {
            server.close();
        }
    
        //t.interrupt();
        System.out.println("fffffffffffffff");
        server.close();
        return t;
        
    }
}
