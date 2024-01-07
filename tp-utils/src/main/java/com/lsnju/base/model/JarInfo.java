package com.lsnju.base.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lis614
 * @since 2024/1/2 15:28
 * @version V1.0
 */
@Getter
@Setter
public class JarInfo extends BaseMo {

    private String mfName;
    private String mfVersion;
    private String jarFullName;
    private String jarName;
    private String jarVersion;
    private String path;

}
