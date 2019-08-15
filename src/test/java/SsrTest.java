import ssr.http.ShadowsocksDetect;

public class SsrTest {
    public static void main(String[] args) throws Exception{
        String ssr = "ssr://NDUuMTEuMi40NTo2MjpvcmlnaW46cmM0OnBsYWluOmJHNWpiaTV2Y21jZ05YRS8_b2Jmc3BhcmFtPSZyZW1hcmtzPTVMaWM1THFzV1EmZ3JvdXA9VEc1amJpNXZjbWM";
        ShadowsocksDetect.parseSsr(ssr);
    }
}
