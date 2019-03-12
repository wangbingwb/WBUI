package xyz.wbsite.wbui.base.utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class DESUtil {

	public static final String ALGORITHM_DES = "DES";

	public static Key generateKey(String algorithm) throws NoSuchAlgorithmException {
		KeyGenerator generator = KeyGenerator.getInstance(algorithm);
		return generator.generateKey();
	}

	public static Key getKey(byte[] key, String algorithm) throws NoSuchAlgorithmException {
		return new SecretKeySpec(key, algorithm);
	}

	public static byte[] encrypt(String transformation, Key key, String content)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(transformation);
		// 加密模式
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(content.getBytes());
	}

	public static byte[] decrypt(String transformation, Key key, byte[] content)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(transformation);
		// 解密模式
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(content);
	}

}
