package com.ean.usercenter.model.request;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @description:TODO
 * @author:Povlean
 */
@Data
public class LoginRequest implements Serializable {

    private String userAccount;

    private String userPassword;
}
