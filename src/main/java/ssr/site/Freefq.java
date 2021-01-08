package ssr.site;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Freefq extends AbstractSite {
    /**是否需要将ip，port等信息进行ssr://协议编码*/
    private boolean needConstruct = true;
    
    private final static Pattern SSR_PATTERN = Pattern.compile("ssr://[\\w=]+");
    
    public Freefq(String html) {
        //html类型的文件，可以直接从文件中提取ssr链接地址，不需要构造
        if (html != null && "html".equalsIgnoreCase(html.trim())) {
            needConstruct = false;
        }
    }
    
    
    @Override
    public void parse(String line) {
        if (needConstruct) {
            Matcher matcher = ORIGIN_PATTERN.matcher(line);
            if (matcher.matches()) {
                String[] split = line.split("\\t");
                SsrInfo ssrInfo = new SsrInfo(split[0].trim(), split[1].trim(), split[3].trim(), split[2].trim(), "freefq");
                parserSsr(ssrInfo);
            }
            return;
        }
    
        Matcher matcher = SSR_PATTERN.matcher(line);
        if (matcher.find()) {
            addSsrSet(matcher.group());
        }
    }
}
