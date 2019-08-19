package com.stfl.ss;

import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Rc4Crypt extends CryptBase {
    private Logger logger = Logger.getLogger(Rc4Crypt.class.getName());
    
    public final static String RC4 = "rc4";
    RC4Engine engine = new RC4Engine();
    
    protected void setIV(byte[] iv, boolean isEncrypt) {
        if (_ivLength == 0) {
            return;
        }
        
        if (isEncrypt) {
            _encryptIV = new byte[_ivLength];
            System.arraycopy(iv, 0, _encryptIV, 0, _ivLength);
            engine.init(isEncrypt, new KeyParameter(_ssKey.getEncoded()));
        } else {
            _decryptIV = new byte[_ivLength];
            System.arraycopy(iv, 0, _decryptIV, 0, _ivLength);
            engine.init(isEncrypt, new KeyParameter(_ssKey.getEncoded()));
        }
    }
    
    @Override
    protected StreamBlockCipher getCipher(boolean isEncrypted) throws InvalidAlgorithmParameterException {
        return null;
    }
    
    @Override
    protected SecretKey getKey() {
        return  new SecretKeySpec(_ssKey.getEncoded(), "RC4");
    }
    
    @Override
    protected void _encrypt(byte[] data, ByteArrayOutputStream stream) {
        int noBytesProcessed;
        byte[] buffer = new byte[data.length];
    
        noBytesProcessed = engine.processBytes(data, 0, data.length, buffer, 0);
        stream.write(buffer, 0, noBytesProcessed);
    }
    
    @Override
    protected void _decrypt(byte[] data, ByteArrayOutputStream stream) {
        int noBytesProcessed;
        byte[] buffer = new byte[data.length];
    
        noBytesProcessed = engine.processBytes(data, 0, data.length, buffer, 0);
        stream.write(buffer, 0, noBytesProcessed);
    }
    
    public Rc4Crypt(String name, String password) {
        super(name, password);
    }
    
    public static Map<String, String> getCiphers() {
        Map<String, String> ciphers = new HashMap<>();
        ciphers.put(RC4, Rc4Crypt.class.getName());
        return ciphers;
    }
    
    
    /*
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
    
            System.out.println(new String(decrypted, "utf-8"));
            stream.reset();
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
    */
    
    @Override
    public int getIVLength() {
        return 16;
    }
    
    @Override
    public int getKeyLength() {
        return 16;
    }
    
    
}
