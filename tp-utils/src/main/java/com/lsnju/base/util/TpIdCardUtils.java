package com.lsnju.base.util;

import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.lsnju.base.model.BaseMo;

import lombok.Builder;
import lombok.Getter;

/**
 *
 * @author lisong
 * @since 2022/6/29 10:41
 * @version V1.0
 */
public class TpIdCardUtils {

    private static final String EXP_18 = "^[1-9]\\d{5}(18|19|([2-9]\\d))\\d{2}((0[1-9])|(1[0-2]))(([0-2]\\d)|3[0-1])\\d{3}[0-9Xx]$";
    private static final String EXP_15 = "^[1-9]\\d{5}\\d{2}((0[1-9])|(1[0-2]))(([0-2]\\d)|3[0-1])\\d{3}$";
    private static final Pattern PATTERN_18 = Pattern.compile(EXP_18);
    private static final Pattern PATTERN_15 = Pattern.compile(EXP_15);

    private static final int[] FACTOR = {
        7, 9, 10, 5,
        8, 4, 2, 1,
        6, 3, 7, 9,
        10, 5, 8, 4,
        2};
    private static final String[] VERIFY_NUMBER = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    public static boolean validate(String idCard) {
        if (!validatePattern(idCard)) {
            return false;
        }
        if (StringUtils.length(idCard) == 18) {
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                int num = Integer.parseInt(StringUtils.substring(idCard, i, i + 1));
                sum += FACTOR[i] * num;
            }
            return StringUtils.equalsIgnoreCase(StringUtils.substring(idCard, -1), VERIFY_NUMBER[sum % 11]);
        }
        return true;
    }

    public static boolean validatePattern(String idCard) {
        switch (StringUtils.length(idCard)) {
            case 18:
                return PATTERN_18.matcher(idCard).matches();
            case 15:
                return PATTERN_15.matcher(idCard).matches();
            default:
        }
        return false;
    }

    public static IDInfo parse(String idCard) {
        if (StringUtils.length(idCard) == 18) {
            return IDInfo.builder()
                .loc(StringUtils.substring(idCard, 0, 6))
                .birth(StringUtils.substring(idCard, 6, 14))
                .sex(Integer.parseInt(StringUtils.substring(idCard, -2, -1)) % 2 == 0 ? SexEnum.FEMALE : SexEnum.MALE)
                .build();
        }
        if (StringUtils.length(idCard) == 15) {
            return IDInfo.builder()
                .loc(StringUtils.substring(idCard, 0, 6))
                .birth("19" + StringUtils.substring(idCard, 6, 12))
                .sex(Integer.parseInt(StringUtils.substring(idCard, -1)) % 2 == 0 ? SexEnum.FEMALE : SexEnum.MALE)
                .build();
        }
        return IDInfo.builder().build();
    }

    @Builder
    @Getter
    public static class IDInfo extends BaseMo {
        private final String loc;
        private final String birth;
        private final SexEnum sex;

        public Date getBirthDate() {
            return TpDateFormatUtils.parseDateFormat(birth);
        }
    }

    public enum SexEnum {
        MALE, FEMALE
    }

}
