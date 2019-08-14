package ssr.site;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Freefq extends AbstractSite {
    private boolean needConstruct = true;//是否需要
    
    private final static Pattern ssrPattern = Pattern.compile("ssr://[\\w=]+");
    
    public Freefq(String html) {
        if (html != null && "html".equalsIgnoreCase(html.trim())) {//html类型的文件，可以直接从文件中提取ssr链接地址，不需要构造
            needConstruct = false;
        }
    }
    
    
    @Override
    public void parse(String line) {
        if (needConstruct) {
            Matcher matcher = originPattern.matcher(line);
            if (matcher.matches()) {
                String[] split = line.split("\\t");
                SsrInfo ssrInfo = new SsrInfo(split[0].trim(), split[1].trim(), split[3].trim(), split[2].trim(), "freefq");
                parserSsr(ssrInfo);
            }
            return;
        }
    
        Matcher matcher = ssrPattern.matcher(line);
        if (matcher.find()) {
            addSsrSet(matcher.group());
        }
    }
}
