package ssr.site;

import java.util.Set;

public interface ISite {
    void parse(String line);
    
    Set<String> getSsr();
}
