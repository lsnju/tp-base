package com.lsnju.tpbase.test.vo;

import java.util.List;

import com.lsnju.base.model.BaseRo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lisong
 * @since 2025/11/15 11:40
 * @version V1.0
 */
@Getter
@Setter
public class PerfResult extends BaseRo {

    private String name;
    private int totalCount;
    private long totalCost;
    private List<PerfResult> subList;

    public double avg() {
        if (subList == null) {
            return totalCount * 1000.0 / totalCost;
        }
        return subList.stream().filter(BaseRo::isSuccess).mapToDouble(PerfResult::avg).sum();
    }

}
