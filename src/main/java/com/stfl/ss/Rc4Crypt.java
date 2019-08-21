package com.stfl.ss;

import com.stfl.misc.Util;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Rc4Crypt extends CryptBase {
    private Logger logger = Logger.getLogger(Rc4Crypt.class.getName());
    
    public final static String RC4 = "rc4";
    private byte[] encodeIVBytes=null;
    RC4Engine engine = new RC4Engine();
    
    public static Map<String, String> getCiphers() {
        Map<String, String> ciphers = new HashMap<>();
        ciphers.put(RC4, Rc4Crypt.class.getName());
        return ciphers;
    }
    
    public Rc4Crypt(String name, String password) {
        super(name, password);
    }
    
    @Override
    public void encrypt(byte[] data, ByteArrayOutputStream stream) {
        synchronized (encLock) {
            stream.reset();
            byte[] target;
            target = encodeIVBytes == null ? new byte[getIVLength() + data.length] : new byte[data.length];
            int outOff = 0;
    
            if (encodeIVBytes == null) {
                encodeIVBytes = Util.randomBytes(getIVLength());
                System.arraycopy(encodeIVBytes, 0, target, 0, encodeIVBytes.length);
                outOff = getIVLength();
        
                CipherParameters keyParameter = new KeyParameter(getRc4Keys(encodeIVBytes));
                engine.init(true, keyParameter);
            }
    
            engine.processBytes(data, 0, data.length, target, outOff);
        }
    }
    
    @Override
    public void decrypt(byte[] data, ByteArrayOutputStream stream) {
        byte[] temp;
        
        synchronized (decLock) {
            stream.reset();
    
            int offset = 0;
            if (!_decryptIVSet) {
                //parameter
                byte[] ivBytes = new byte[getIVLength()];
                System.arraycopy(data, 0, ivBytes, 0, offset = getIVLength());
        
                CipherParameters keyParameter = new KeyParameter(getRc4Keys(ivBytes));
        
                //init
                engine.init(false, keyParameter);
                _decryptIVSet = true;
            }
    
            byte[] target = new byte[data.length - offset];
            engine.processBytes(data, offset, data.length - offset, target, 0);
        }
    }
    
    /**
     * 获取rc4-md5 parameterKey
     *
     * @param ivBytes ivBytes
     * @return keyParameterBytes
     */
    private byte[] getRc4Keys(byte[] ivBytes) {
        byte[] result = new byte[getKeyLength() + getIVLength()];
        System.arraycopy(_ssKey.getEncoded(), 0, result, 0, getKeyLength());
        System.arraycopy(ivBytes, 0, result, getKeyLength(), ivBytes.length);
        
        /*
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            return messageDigest.digest(result);
        } catch (NoSuchAlgorithmException e) {
            //logger.error(e.getMessage(), e);
        }
        */
        return result;
    }
    
    @Override
    protected StreamBlockCipher getCipher(boolean isEncrypted) throws InvalidAlgorithmParameterException {
        return null;
    }
    
    @Override
    protected SecretKey getKey() {
        return  null;
    }
    
    @Override
    protected void _encrypt(byte[] data, ByteArrayOutputStream stream) {
    }
    
    @Override
    protected void _decrypt(byte[] data, ByteArrayOutputStream stream) {
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
