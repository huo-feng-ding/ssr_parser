import ssr.http.ShadowsocksDetect;

import java.util.HashSet;

public class SsrTest {
    public static void main(String[] args) throws Exception{
        String ssr = "ssr://NDUuODkuMTk3Ljc6OTc6b3JpZ2luOnJjNDpwbGFpbjpiRzVqYmk1dmNtY2djakkvP29iZnNwYXJhbT0mcmVtYXJrcz01clNiNXAySjU1LTJRUSZncm91cD1URzVqYmk1dmNtYw";
        //String ssr =
        //        "ssr://MTcyLjEwNC42Mi4xNDQ6ODA5OTpvcmlnaW46YWVzLTI1Ni1jZmI6cGxhaW46WlVsWE1FUnVhelk1TkRVMFpUWnVVM2QxYzNCMk9VUnRVekl3TVhSUk1FUS8_b2Jmc3BhcmFtPQ";
        HashSet<String > set = new HashSet<>();
        set.add(ssr);
        //set.add("ssr://MTcyLjEwNS43MS44Mjo4MDk5Om9yaWdpbjphZXMtMjU2LWNmYjpwbGFpbjpaVWxYTUVSdWF6WTVORFUwWlRadVUzZDFjM0IyT1VSdFV6SXdNWFJSTUVRLz9vYmZzcGFyYW09JnJlbWFya3M9NWI2MzVadTlMVWhsYzNObCZncm91cD1WMWRYTGxsUFZVNUZSVVF1VjBsTw");
        ShadowsocksDetect.detectShadowsocks(set);
        //TimeUnit.SECONDS.sleep(10);
    }
}
