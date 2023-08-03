package com.lsnju.base.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author ls
 * @since 2023-07-20 10:10:37
 * @version V1.0
 */
public class UUIDGenerator {

    // java.util.Base64.Encoder.toBase64URL
    private static final char[] BASE_CHAR = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };
    private static final int LENGTH = BASE_CHAR.length;

    public static String getUUID() {
        return StringUtils.remove(UUID.randomUUID().toString(), '-');
    }

    public static String getSUID() {
        return Long.toHexString(nextTraceIdHigh(ThreadLocalRandom.current().nextInt()));
    }

    static long nextTraceIdHigh(int random) {
        long epochSeconds = System.currentTimeMillis() / 1000;
        return (epochSeconds & 0xffffffffL) << 32 | (random & 0xffffffffL);
    }

    public static String randomString(int length) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(BASE_CHAR[random.nextInt(LENGTH)]);
        }
        return sb.toString();
    }

    public static String randomString(int length, char[] source) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(source[random.nextInt(source.length)]);
        }
        return sb.toString();
    }
}
