package ssr.site;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class AbstractSite implements ISite {
    
    protected final static Pattern         ORIGIN_PATTERN  = Pattern.compile("([^\\t]+\\t){5,}.+");
    //ssr://server:port:protocol:method:obfs:password_base64/?obfsparam=obfsparam_base64&protoparam=protoparam_base64&remarks=remarks_base64
    // &group=group_base64
    protected final static String          SSR_FORMAT      = "%s:%s:origin:%s:plain:%s/?obfsparam=&remarks=&group=%s";
    protected final static Encoder         BASE_64_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private final static   HashSet<String> SSR_SET         = new HashSet<>();
    
    /**
     * 将ip，端口等信息进行编辑成ssr://协议
     */
    protected void parserSsr(SsrInfo ssrInfo) {
        String format = String.format(SSR_FORMAT,
                                      ssrInfo.ip,
                                      ssrInfo.port,
                                      ssrInfo.method,
                                      BASE_64_ENCODER.encodeToString(ssrInfo.pwd.getBytes(StandardCharsets.UTF_8)),
                                      BASE_64_ENCODER.encodeToString(ssrInfo.domain.getBytes(StandardCharsets.UTF_8)));
        addSsrSet("ssr://" + BASE_64_ENCODER.encodeToString(format.getBytes(StandardCharsets.UTF_8)));
    }
    
    protected void addSsrSet(String ssr) {
        if (ssr != null && !"".equals(ssr)) {
            SSR_SET.add(ssr);
        }
    }
    
    @Override
    public Set<String> getSsr() {
        return SSR_SET;
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
