package com.ean.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:TODO
 * @author:Povlean
 */
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = -2038884913144640407L;

    /**
     * id
     */
    private Long teamId;

}
