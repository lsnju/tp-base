package com.lsnju.base.model.rs;

import org.hibernate.validator.constraints.Range;

import com.lsnju.base.model.BaseMo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ls
 * @since 2020/5/26 19:17
 * @version V1.0
 */
@Getter
@Setter
public class BasePageQryVo extends BaseMo {

    @ApiParam(name = "page", value = "分页页码", required = true, example = "1")
    @Parameter(name = "page", description = "分页页码", required = true, example = "1")
    @ApiModelProperty(notes = "分页页码")
    @Positive
    private int page = 1;

    @ApiParam(name = "size", value = "分页大小", required = true, example = "10")
    @Parameter(name = "size", description = "分页大小", required = true, example = "10")
    @ApiModelProperty(notes = "分页大小")
    @Range(min = 1, max = 1024)
    private int size = 10;
}
