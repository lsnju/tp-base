package com.lsnju.base.secret;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ls
 * @since 2020/6/5 15:24
 * @version V1.0
 */
@Slf4j
public class AESUtils {

    public static final int DEFAULT_SIZE = 16;

    public static final String AES = "AES";
    public static final String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    public static final String AES_CBC_PKCS7 = "AES/CBC/PKCS7Padding";

    public static final String AES_ECB_PKCS5 = "AES/ECB/PKCS5Padding";
    public static final String AES_ECB_PKCS7 = "AES/ECB/PKCS7Padding";

    public static int getBlockSize(String alg) {
        try {
            return Cipher.getInstance(alg).getBlockSize();
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
            return DEFAULT_SIZE;
        }
    }

    public static byte[] initIV(String alg) {
        byte[] key = new byte[getBlockSize(alg)];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        return key;
    }

    public static byte[] encryptByAesCbcPkcs5(byte[] data, byte[] password, byte[] iv) throws GeneralSecurityException {
        return encrypt(AES_CBC_PKCS5, new SecretKeySpec(password, AES), new IvParameterSpec(iv), data);
    }

    public static byte[] decryptByAesCbcPkcs5(byte[] data, byte[] password, byte[] iv) throws GeneralSecurityException {
        return decrypt(AES_CBC_PKCS5, new SecretKeySpec(password, AES), new IvParameterSpec(iv), data);
    }

    public static byte[] encryptByAesCbcPkcs7(byte[] data, byte[] password, byte[] iv) throws GeneralSecurityException {
        return encrypt(AES_CBC_PKCS7, new SecretKeySpec(password, AES), new IvParameterSpec(iv), data);
    }

    public static byte[] decryptByAesCbcPkcs7(byte[] data, byte[] password, byte[] iv) throws GeneralSecurityException {
        return decrypt(AES_CBC_PKCS7, new SecretKeySpec(password, AES), new IvParameterSpec(iv), data);
    }

    public static byte[] encrypt(String alg, SecretKeySpec key, IvParameterSpec iv, byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(alg);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(String alg, SecretKeySpec key, IvParameterSpec iv, byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(alg);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    public static byte[] encrypt(String alg, SecretKeySpec key, byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(alg);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(String alg, SecretKeySpec key, byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(alg);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

}
