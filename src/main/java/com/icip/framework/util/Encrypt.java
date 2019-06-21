package com.icip.framework.util;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 *	加解密工具<br>
 *	DES加密双方约定秘钥,加密后使用base64转换<br>
 */
public class Encrypt {


	private static final String DES_ALGORITHM = "DES";

	private static final String secretKey = "yzksecr0";

	/**
	 * DES加密
	 * @param plainData
	 * @return
	 * @throws Exception
	 */
	public static String encryption(String plainData){
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(DES_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, generateKey());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		String result = null;
		try {
			// FIXME 为了防止解密时报javax.crypto.IllegalBlockSizeException: Input
			// length must be multiple of 8 when decrypting with padded cipher异常，
			// 不能把加密后的字节数组直接转换成字符串
			byte[] buf = cipher.doFinal(plainData.getBytes("UTF-8"));
			result = new BASE64Encoder().encode(buf);
			return result;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * DES解密
	 * @param secretData
	 * @return
	 * @throws Exception
	 */
	public static String decryption(String secretData)throws Exception {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(DES_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, generateKey());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("NoSuchAlgorithmException", e);
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new Exception("NoSuchPaddingException", e);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new Exception("InvalidKeyException", e);
		}

		BASE64Decoder decoder = new BASE64Decoder();
		byte[] decryptFrom = decoder.decodeBuffer(secretData);
		try {
			byte[] buf = cipher.doFinal(decryptFrom);
			String result = new String(buf,"UTF-8");
			return result;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new Exception("IllegalBlockSizeException", e);
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw new Exception("BadPaddingException", e);
		}
	}

	/**
	 * 获得秘密密钥
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 */
	//FIXME SecureRandom 实现完全随操作系统本身的內部状态，除非调用方在调用 getInstance方法，然后调用 setSeed 方法
	//	该实现在 windows 上每次生成的 key 都相同，但是在 solaris 或部分 linux 系统上则不同
	private static SecretKey generateKey()throws NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidKeyException {
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
		DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
		keyFactory.generateSecret(keySpec);
		return keyFactory.generateSecret(keySpec);
	}

	/**将16进制转换为二进制
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length()/2];
		for (int i = 0;i< hexStr.length()/2; i++) {
			int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
			int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**将二进制转换成16进制
	 * @param buf
	 * @return
	 */
	public final static String MD5(String s) {
    char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       

    try {
        byte[] btInput = s.getBytes();
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest mdInst = MessageDigest.getInstance("MD5");
        // 使用指定的字节更新摘要
        mdInst.update(btInput);
        // 获得密文
        byte[] md = mdInst.digest();
        // 把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

}
