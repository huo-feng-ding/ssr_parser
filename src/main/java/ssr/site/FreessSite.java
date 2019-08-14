package ssr.site;

import java.util.regex.Matcher;

public class FreessSite extends AbstractSite {
    
    @Override
    public void parse(String line) {
        Matcher matcher = originPattern.matcher(line);
        if (matcher.matches()) {
            String[] split = line.split("\\t");
            SsrInfo ssrInfo = new SsrInfo(split[1].trim(), split[2].trim(), split[4].trim(), split[3].trim(), "free-ss");
            parserSsr(ssrInfo);
        }
    }
}
