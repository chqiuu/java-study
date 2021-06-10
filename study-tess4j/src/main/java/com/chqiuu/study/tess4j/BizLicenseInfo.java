package com.chqiuu.study.tess4j;

import lombok.Data;

@Data
public class BizLicenseInfo {
    /**
     * 统一社会信用代码
     */
    private String creditCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String bizType;
    /**
     * 法人信息
     */
    private String juridical;
    /**
     * 经营范围
     */
    private String bizScope;
    /**
     * 注册资本
     */
    private String capital;
    /**
     * 成立日期
     */
    private String buildOn;
    /**
     * 营业期限
     */
    private String bizLimit;
    /**
     * 住所
     */
    private String address;
}
