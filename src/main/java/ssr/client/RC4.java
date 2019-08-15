package ssr.client;

public class RC4 {
    
    public static void main(String[] args) {
        RC4 rc4 = new RC4();
        
        String plaintext = "helloworld";
        String key = "key";
        
        String ciphertext = rc4.encrypt(plaintext, key);
        String decryptText = rc4.encrypt(ciphertext, key);
        
        System.out.print(
                "明文为：" + plaintext + "\n" + "密钥为：" + key + "\n\n" + "密文为：" + ciphertext + "\n" + "解密为：" + decryptText);
        byte[] bytes = RC4Base(plaintext.getBytes(), key);
        byte[] bytes1 = RC4Base(bytes, key);
        System.out.println(new String(bytes1));
    }
    
    // 1 加密
    public String encrypt(final String plaintext, final String key) {
        Integer[] S = new Integer[256]; // S盒
        Character[] keySchedul = new Character[plaintext.length()]; // 生成的密钥流
        StringBuffer ciphertext = new StringBuffer();
        
        ksa(S, key);
        rpga(S, keySchedul, plaintext.length());
        
        for (int i = 0; i < plaintext.length(); ++i) {
            ciphertext.append((char) (plaintext.charAt(i) ^ keySchedul[i]));
        }
        
        return ciphertext.toString();
    }
    
    public static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte key[] = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];
        
        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
                     xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }
    
    private static byte[] initKey(String aKey) {
        byte[] b_key = aKey.getBytes();
        byte state[] = new byte[256];
        
        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (b_key == null || b_key.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
                            index1 = (index1 + 1) % b_key.length;
        }
        return state;
    }
    
    // 1.1 KSA--密钥调度算法--利用key来对S盒做一个置换，也就是对S盒重新排列
    public void ksa(Integer[] s, String key) {
        for (int i = 0; i < 256; ++i) {
            s[i] = i;
        }
        
        int j = 0;
        for (int i = 0; i < 256; ++i) {
            j = (j + s[i] + key.charAt(i % key.length())) % 256;
            swap(s, i, j);
        }
    }
    
    // 1.2 RPGA--伪随机生成算法--利用上面重新排列的S盒来产生任意长度的密钥流
    public void rpga(Integer[] s, Character[] keySchedul, int plaintextLength) {
        int i = 0, j = 0;
        for (int k = 0; k < plaintextLength; ++k) {
            i = (i + 1) % 256;
            j = (j + s[i]) % 256;
            swap(s, i, j);
            keySchedul[k] = (char) (s[(s[i] + s[j]) % 256]).intValue();
        }
    }
    
    // 1.3 置换
    public void swap(Integer[] s, int i, int j) {
        Integer mTemp = s[i];
        s[i] = s[j];
        s[j] = mTemp;
    }
}

