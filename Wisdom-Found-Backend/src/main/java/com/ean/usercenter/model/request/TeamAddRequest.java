package com.ean.usercenter.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @description:TODO
 * @author:Povlean
 */
@Data
public class TeamAddRequest {

    private static final long serialVersionUID = -4162304142710323660L;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;

}
