import ssr.http.ShadowsocksDetect;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class SsrTest {
    public static void main(String[] args) throws Exception{
        String ssr = "ssr://NDUuODkuMTk3Ljc6OTc6b3JpZ2luOnJjNDpwbGFpbjpiRzVqYmk1dmNtY2djakkvP29iZnNwYXJhbT0mcmVtYXJrcz01clNiNXAySjU1LTJRUSZncm91cD1URzVqYmk1dmNtYw";
        HashSet<String > set = new HashSet<>();
        set.add(ssr);
        ShadowsocksDetect.detect(set);
        TimeUnit.SECONDS.sleep(10);
    }
}
