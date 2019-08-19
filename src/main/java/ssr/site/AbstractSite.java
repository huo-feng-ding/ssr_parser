package ssr.site;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class AbstractSite implements ISite {
    
    protected final static Pattern originPattern = Pattern.compile("([^\\t]+\\t){5,}.+");
    //ssr://server:port:protocol:method:obfs:password_base64/?obfsparam=obfsparam_base64&protoparam=protoparam_base64&remarks=remarks_base64
    // &group=group_base64
    protected final static String ssrFormat = "%s:%s:origin:%s:plain:%s/?obfsparam=&remarks=&group=%s";
    protected final static Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();
    
    private final static HashSet<String> ssrSet = new HashSet<>();
    
    protected void parserSsr(SsrInfo ssrInfo) {
        String format = String.format(ssrFormat,
                                      ssrInfo.ip,
                                      ssrInfo.port,
                                      ssrInfo.method,
                                      base64Encoder.encodeToString(ssrInfo.pwd.getBytes(StandardCharsets.UTF_8)),
                                      base64Encoder.encodeToString(ssrInfo.domain.getBytes(StandardCharsets.UTF_8)));
        addSsrSet("ssr://" + base64Encoder.encodeToString(format.getBytes(StandardCharsets.UTF_8)));
    }
    
    protected void addSsrSet(String ssr) {
        if (ssr != null && !"".equals(ssr)) {
            ssrSet.add(ssr);
        }
    }
    
    @Override
    public Set<String > getSsr() {
        return ssrSet;
    }
    
    protected static class SsrInfo {
        String ip, port, method, pwd, domain;
        
        SsrInfo(String ip, String port, String method, String pwd, String domain) {
            this.ip     = ip;
            this.port   = port;
            this.method = method;
            this.pwd    = pwd;
            this.domain = domain;
        }
    }
    
}
