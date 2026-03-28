package com.lsnju.base.secret;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link AESUtils}.
 *
 * @author ls
 */
class AESUtilsTest {

    private static boolean cipherAvailable(String transformation) {
        try {
            Cipher.getInstance(transformation);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] key16(String s) {
        byte[] raw = s.getBytes(StandardCharsets.UTF_8);
        byte[] key = new byte[16];
        System.arraycopy(raw, 0, key, 0, Math.min(raw.length, 16));
        return key;
    }

    @Test
    void getBlockSize_aesCbc_returns16() {
        assertEquals(16, AESUtils.getBlockSize(AESUtils.AES_CBC_PKCS5));
    }

    //    @Test
    void getBlockSize_unknownAlgorithm_returnsDefault() {
        assertEquals(AESUtils.DEFAULT_SIZE, AESUtils.getBlockSize("NoSuchCipher/XXX/YYY"));
    }

    @Test
    void initIV_lengthMatchesBlockSize() {
        byte[] iv = AESUtils.initIV(AESUtils.AES_CBC_PKCS5);
        assertEquals(AESUtils.getBlockSize(AESUtils.AES_CBC_PKCS5), iv.length);
    }

    @Test
    void encryptDecrypt_cbcPkcs5_helpers_roundTrip() throws GeneralSecurityException {
        byte[] key = key16("unit-test-key-16");
        byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 7);
        byte[] plain = "hello aes cbc".getBytes(StandardCharsets.UTF_8);

        byte[] enc = AESUtils.encryptByAesCbcPkcs5(plain, key, iv);
        assertNotNull(enc);
        byte[] dec = AESUtils.decryptByAesCbcPkcs5(enc, key, iv);
        assertArrayEquals(plain, dec);
    }

    @Test
    void encryptDecrypt_cbcPkcs5_lowLevel_roundTrip() throws GeneralSecurityException {
        SecretKeySpec keySpec = new SecretKeySpec(key16("k"), AESUtils.AES);
        IvParameterSpec ivSpec = new IvParameterSpec(AESUtils.initIV(AESUtils.AES_CBC_PKCS5));
        byte[] plain = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        byte[] enc = AESUtils.encrypt(AESUtils.AES_CBC_PKCS5, keySpec, ivSpec, plain);
        byte[] dec = AESUtils.decrypt(AESUtils.AES_CBC_PKCS5, keySpec, ivSpec, enc);
        assertArrayEquals(plain, dec);
    }

    @Test
    void encryptDecrypt_ecbPkcs5_roundTrip() throws GeneralSecurityException {
        assumeTrue(cipherAvailable(AESUtils.AES_ECB_PKCS5));
        SecretKeySpec keySpec = new SecretKeySpec(key16("ecb-test-key!!"), AESUtils.AES);
        byte[] plain = "ecb no iv".getBytes(StandardCharsets.UTF_8);

        byte[] enc = AESUtils.encrypt(AESUtils.AES_ECB_PKCS5, keySpec, plain);
        byte[] dec = AESUtils.decrypt(AESUtils.AES_ECB_PKCS5, keySpec, enc);
        assertArrayEquals(plain, dec);
    }

    @Test
    void cbcPkcs7_roundTrip_whenProviderSupports() throws GeneralSecurityException {
        if (cipherAvailable(AESUtils.AES_CBC_PKCS7)) {
            assumeTrue(cipherAvailable(AESUtils.AES_CBC_PKCS7));
            byte[] key = key16("pkcs7-key-16b!");
            byte[] iv = AESUtils.initIV(AESUtils.AES_CBC_PKCS7);
            byte[] plain = "pkcs7 payload".getBytes(StandardCharsets.UTF_8);

            byte[] enc = AESUtils.encryptByAesCbcPkcs7(plain, key, iv);
            byte[] dec = AESUtils.decryptByAesCbcPkcs7(enc, key, iv);
            assertArrayEquals(plain, dec);
        }
    }

    @Test
    void decrypt_wrongKey_fails() throws GeneralSecurityException {
        byte[] key = key16("correct-key-16");
        byte[] wrong = key16("wrong-key-16!!");
        byte[] iv = new byte[16];
        byte[] plain = "secret".getBytes(StandardCharsets.UTF_8);
        byte[] enc = AESUtils.encryptByAesCbcPkcs5(plain, key, iv);

        assertThrows(GeneralSecurityException.class, () -> AESUtils.decryptByAesCbcPkcs5(enc, wrong, iv));
    }
}
