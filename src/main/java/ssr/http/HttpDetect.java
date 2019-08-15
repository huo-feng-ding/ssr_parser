package ssr.http;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class HttpDetect {
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
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
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
    
    public static void detect() throws IOException {
        httpClient.newCall(request).execute().body().string();
    }
}
