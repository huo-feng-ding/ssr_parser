package com.stfl.ss;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Rc4Crypt implements ICrypt {
    public final static String RC4 = "rc4";
    private String name;
    private String password;
    
    
    public Rc4Crypt(String name, String password) {
        this.name     = name;
        this.password = password;
    }
    
    public static Map<String, String> getCiphers() {
        Map<String, String> ciphers = new HashMap<>();
        ciphers.put(RC4, Rc4Crypt.class.getName());
        return ciphers;
    }
    
    
    @Override
    public void encrypt(byte[] data, ByteArrayOutputStream stream) {
        try {
            SecureRandom sr = new SecureRandom(password.getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("RC4");
            kg.init(sr);
            SecretKey sk = kg.generateKey();
            
            // do the decryption with that key
            Cipher cipher = Cipher.getInstance("RC4");
            cipher.init(Cipher.DECRYPT_MODE, sk);
            byte[] decrypted = cipher.doFinal(data);
            stream.write(decrypted, 0, decrypted.length);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void encrypt(byte[] data, int length, ByteArrayOutputStream stream) {
        byte[] d = new byte[length];
        System.arraycopy(data, 0, d, 0, length);
        encrypt(d, stream);
    }
    
    @Override
    public void decrypt(byte[] data, ByteArrayOutputStream stream) {
        encrypt(data, stream);
    }
    
    @Override
    public void decrypt(byte[] data, int length, ByteArrayOutputStream stream) {
        byte[] d = new byte[length];
        System.arraycopy(data, 0, d, 0, length);
        decrypt(d, stream);
        
    }
    
    @Override
    public int getIVLength() {
        return 0;
    }
    
    @Override
    public int getKeyLength() {
        return 0;
    }
    
    
}
