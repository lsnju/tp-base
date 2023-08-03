package com.lsnju.base.secret;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ls
 * @since 2020/6/5 17:02
 * @version V1.0
 */
public class RSAUtils {

    public static final String RSA = "RSA";
    private static final String RSA_ECB_PKCS1 = "RSA/ECB/PKCS1Padding";
    public static final String SHA_256_WITH_RSA = "SHA256WithRSA";
    public static final String SHA_128_WITH_RSA = "SHA1WithRSA";

    public static boolean isValidPrivate(String privateKey) {
        try {
            getPrivateKey(privateKey);
            return true;
        } catch (GeneralSecurityException e) {
            return false;
        }
    }

    public static boolean isValidPublic(String publicKeyStr) {
        try {
            getPublicKey(publicKeyStr);
            return true;
        } catch (GeneralSecurityException e) {
            return false;
        }
    }

    public static RSAKeyPair generate(int keySize) throws GeneralSecurityException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return new RSAKeyPair((RSAPrivateKey) keyPair.getPrivate(), (RSAPublicKey) keyPair.getPublic());
    }

    public static PrivateKey getPrivateKey(String privateKey) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
    }

    public static PublicKey getPublicKey(String publicKeyStr) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr)));
    }

    public static PublicKey getPublicKeyFromPrivateKey(PrivateKey privateKey) throws GeneralSecurityException {
        RSAPrivateCrtKey key = (RSAPrivateCrtKey) privateKey;
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(publicKeySpec);
    }

    public static byte[] encryptEcbPKCS1Padding(byte[] content, PublicKey publicKey) throws GeneralSecurityException {
        return encrypt(content, publicKey, RSA_ECB_PKCS1);
    }

    public static byte[] decryptEcbPKCS1Padding(byte[] content, PrivateKey privateKey) throws GeneralSecurityException {
        return decrypt(content, privateKey, RSA_ECB_PKCS1);
    }

    public static byte[] encrypt(byte[] content, PublicKey publicKey, String cipherAlg) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(cipherAlg);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    public static byte[] decrypt(byte[] content, PrivateKey privateKey, String cipherAlg) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(cipherAlg);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    public static String sign(byte[] content, PrivateKey privateKey, String algorithm) throws GeneralSecurityException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(content);
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    public static boolean verify(byte[] content, String sign, PublicKey publicKey, String algorithm) throws GeneralSecurityException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(content);
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    public static String signWithSha256(byte[] content, PrivateKey privateKey) throws GeneralSecurityException {
        return sign(content, privateKey, SHA_256_WITH_RSA);
    }

    public static boolean verifyWithSha256(byte[] content, String sign, PublicKey publicKey) throws GeneralSecurityException {
        return verify(content, sign, publicKey, SHA_256_WITH_RSA);
    }

    public static String signWithSha1(byte[] content, PrivateKey privateKey) throws GeneralSecurityException {
        return sign(content, privateKey, SHA_128_WITH_RSA);
    }

    public static boolean verifyWithSha1(byte[] content, String sign, PublicKey publicKey) throws GeneralSecurityException {
        return verify(content, sign, publicKey, SHA_128_WITH_RSA);
    }

    @Getter
    @AllArgsConstructor
    public static class RSAKeyPair {
        RSAPrivateKey privateKey;
        RSAPublicKey publicKey;
    }
}
