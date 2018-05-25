/**
 * Copyright 2018 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.shared.util;

import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import sun.misc.BASE64Decoder;

/**
 * AES Encryption and Decryption
 */
public class Aes {
	
    //Key (must be 16-bit, requires front-end and back-end consistency)
    private static final String KEY = "bdaictvisualabcd";  
    
    //Algorithm
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    
    /** 
     * Aes Decryption 
     * 
     * @param encrypt   content 
     * @return 
     * @throws Exception 
     */  
    public static String aesDecrypt(String encrypt) {  
        try {
            return aesDecrypt(encrypt, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }  
    }  
      
    /** 
     * Aes encryption
     *  
     * @param content 
     * @return 
     * @throws Exception 
     */  
    public static String aesEncrypt(String content) {  
        try {
            return aesEncrypt(content, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }  
    }  
  
    /** 
     * Convert byte[] to a variety of hexadecimal strings
     *  
     * @param bytes byte[] 
     * @param radix Convertible range，from Character.MIN_RADIX to Character.MAX_RADIX， out of range is changed to decimal
     * @return converted string 
     */  
    public static String binary(byte[] bytes, int radix){  
        return new BigInteger(1, bytes).toString(radix);
    }  
  
    /** 
     * Base 64 encode 
     * 
     * @param bytes  
     * @return 
     */  
    public static String base64Encode(byte[] bytes){  
        return new String(Base64.encodeBase64(bytes));  
    }  
  
    /** 
     * Base 64 decode 
     * 
     * @param base64Code 
     * @return 
     * @throws Exception 
     */  
    public static byte[] base64Decode(String base64Code) throws Exception{  
        return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);  
    }  
  
      
    /** 
     * Aes Encryption 
     * @param content content to be encrypted
     * @param encryptKey encryption key
     * @return 
     * @throws Exception 
     */  
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128);  
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));  
  
        return cipher.doFinal(content.getBytes("utf-8"));  
    }  
  
  
    /** 
     * Aes Encryption for base 64 code
     * 
     * @param content content to be encrypted 
     * @param encryptKey encryption key 
     * @return  
     * @throws Exception 
     */  
    public static String aesEncrypt(String content, String encryptKey) throws Exception {  
        return base64Encode(aesEncryptToBytes(content, encryptKey));  
    }  
  
    /** 
     * Aes Decryption
     * 
     * @param encryptBytes byte[] to be decrypted
     * @param decryptKey decryption key 
     * @return 
     * @throws Exception 
     */  
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128);  
  
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));  
        byte[] decryptBytes = cipher.doFinal(encryptBytes);  
        return new String(decryptBytes);  
    }  
  
  
    /** 
     * Aes Decryption for base 64 code
     * 
     * @param encryptStr base 64 code to be decrypted 
     * @param decryptKey decryption key 
     * @return 
     * @throws Exception 
     */  
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {  
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);  
    }  
}