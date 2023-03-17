package com.ean.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:TODO
 * @author:Povlean
 */
@Data
public class RegisterRequest implements Serializable {

    private String userAccount;

    private String userPassword;

    private String checkPassword;

}
