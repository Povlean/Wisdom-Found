package com.ean.usercenter.model.request;

import lombok.Data;

/**
 * @description:TODO
 * @author:Povlean
 */
@Data
public class TeamJoinRequest {
    /**
     * id
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;
}
