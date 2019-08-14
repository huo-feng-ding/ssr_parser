package ssr.parser;

import ssr.site.Freefq;
import ssr.site.FreessSite;
import ssr.site.ISite;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SsrParser {
    
    /**
     * @param args 文件路径 站点名 文件是否是html类型的文件
     */
    public static void main(String[] args) throws IOException {
        if (args == null || args.length < 2) {
            System.out.println("请输入文件路径和站点域名");
            System.exit(1);
        }
        
        String siteArg = args[1].trim().toLowerCase();
        ISite site = null;
        if (siteArg.contains("freefq")) {
            String html = args.length > 2 ? args[2] : null;
            site = new Freefq(html);
        }
        if (siteArg.contains("free-ss.site")) {
            site = new FreessSite();
        }
        
        if (site == null) {
            String html = args.length > 2 ? args[2] : null;
            site = new Freefq(html);
        }
        
        List<String> fileLines = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
        fileLines.forEach(site::parse);
        site.printSsr();
    }
    
}
