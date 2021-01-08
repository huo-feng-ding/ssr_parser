package ssr.site;

import java.util.regex.Matcher;

/**
 * 页面上已经有ip，端口等各种信息，需要对ssr://协议进行编码
 */
public class FreessSite extends AbstractSite {
    
    @Override
    public void parse(String line) {
        Matcher matcher = ORIGIN_PATTERN.matcher(line);
        if (matcher.matches()) {
            String[] split = line.split("\\t");
            SsrInfo ssrInfo = new SsrInfo(split[1].trim(), split[2].trim(), split[4].trim(), split[3].trim(), "free-ss");
            parserSsr(ssrInfo);
        }
    }
}
