package com.lsnju.tpbase.log.boolex;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lisong
 * @since 2025/12/20 18:48
 * @version V1.0
 */
public class IsPropertyNotBlankCondition extends AbstractPropertyConditionBase {

    @Getter
    @Setter
    private String key;

    public void start() {
        if (this.key == null) {
            this.addError("In IsPropertyDefinedEvaluator 'key' parameter cannot be null");
        } else {
            super.start();
        }
    }

    @Override
    public boolean evaluate() {
        String value = this.p(this.key);
        if (isDebugMode()) {
            System.err.printf("IsPropertyNotBlankCondition key=%s, value=%s \n", this.key, value);
        }
        return StringUtils.isNotBlank(value);
    }

}
