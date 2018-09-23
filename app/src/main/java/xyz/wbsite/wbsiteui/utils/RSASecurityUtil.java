package xyz.wbsite.wbsiteui.utils;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSASecurityUtil {

    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";
    private static final String KEY_ALGORITHM = "RSA";
    private static int KEY_SIZE = 512;

    private static PublicKey MOBILE_PUBLIC_KEY = null;
    private static PrivateKey MOBILE_PRIVATE_KEY = null;

    static {
        try {

            // 初始化公钥
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64Util.decode("MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJChCOcEFsKdG9Ibwi23b3UreERc1D5+GhEjvByxmFZ2Q+mqco8V9CDsIZUfpb6pLQA8x32G17YHEyj34etpmBUCAwEAAQ=="));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            MOBILE_PUBLIC_KEY = keyFactory.generatePublic(spec);

            // 初始化私钥
            PKCS8EncodedKeySpec specPri = new PKCS8EncodedKeySpec(Base64Util.decode("MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAkKEI5wQWwp0b0hvCLbdvdSt4RFzUPn4aESO8HLGYVnZD6apyjxX0IOwhlR+lvqktADzHfYbXtgcTKPfh62mYFQIDAQABAkBhuZ4lUxr592TEDOOhNnCGkI/cSYlUjKqaaDYEgW/5Azom4VLya/JXFQVMVeMKQoiwH8aXn8od6+l5O7m+mYVlAiEA5yPFXhM4GXY0Oxe7l0uHKRlLkHIz44kvnd4Fj2xNfK8CIQCgL0SljzvL+iilHb+5PKk1wmpLPaDKajZqInSOhkhQewIgFpGAkOnxfVL0UJzFnUUrolCs9yKffGUFuDVYd6OMgVMCICh2CBbxqR8K3z1l2EnH4s3rf8HlnTnDvl7suRhPHvEFAiAxvjwQl6yHoDH6IGB6jeE9+hYK/xvuGT2TAyztZhfVRA=="));

            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            MOBILE_PRIVATE_KEY = keyFactory.generatePrivate(specPri);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> generateKeys() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        generator.initialize(KEY_SIZE);
        KeyPair keyPair = generator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put(PUBLIC_KEY, publicKey);
        keys.put(PRIVATE_KEY, privateKey);
        return keys;
    }

    public static PublicKey getPublicKey(byte[] content) throws Exception {
        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(content);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            publicKey = keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw e;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw e;
        }

        return publicKey;
    }

    public static PrivateKey getPrivateKey(byte[] content) throws Exception {
        PrivateKey privateKey = null;
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(content);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            privateKey = keyFactory.generatePrivate(spec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw e;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw e;
        }

        return privateKey;
    }

    public static byte[] encryptWithPublicKey(byte[] data, PublicKey key) throws Exception {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw e;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw e;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw e;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static byte[] decryptWithPrivateKey(byte[] data, PrivateKey key) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw e;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw e;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw e;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static byte[] encryptMobile(byte[] data) throws Exception {
        return encryptWithPublicKey(data, MOBILE_PUBLIC_KEY);
    }

    public static byte[] decryptMobile(byte[] data) throws Exception {
        return decryptWithPrivateKey(data, MOBILE_PRIVATE_KEY);
    }


}
