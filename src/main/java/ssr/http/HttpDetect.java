package ssr.http;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class HttpDetect {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpDetect.class);
    
    private static final TrustManager[] trustAllCerts = new TrustManager[]{
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
    private static SSLContext sslContext;
    
    static {
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, trustAllCerts, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
    
    // Create an ssl socket factory with our all-trusting manager
    private static final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
    private static OkHttpClient httpClient = new Builder().proxy(new Proxy(Type.SOCKS, new InetSocketAddress("127.0.0.1", 10086)))
                                                          .followRedirects(true)
                                                          .followSslRedirects(true)
                                                          .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                                                          .hostnameVerifier((hostname, session) -> true)
                                                          .build();
    private static Request request = new Request.Builder().url("https://www.google.com/").build();
    
    public static boolean detect(String ssrMethod) throws IOException {
        try {
            String html = httpClient.newCall(request).execute().body().string();
            System.out.println(html);
            return html.contains("html");
        } catch (SSLHandshakeException e) {
            LOGGER.info("http detect ssl hand shake exception error " + e.getMessage());
            return ssrMethod.equalsIgnoreCase("rc4");
        } catch (Exception e) {
            LOGGER.info("http detect error" + e.getMessage());
            return false;
        }
    }
}
