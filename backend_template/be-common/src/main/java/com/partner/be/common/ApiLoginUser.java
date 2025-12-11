package com.partner.be.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * Login user data class
 * Contains user information and permissions for API authentication
 *
 * Created by ${author} on 14-10-14.
 */

public class ApiLoginUser implements Serializable {

    private static final long serialVersionUID = 7946100926368211990L;

    /**
     * User ID
     */
    @Setter
    @Getter
    protected String  userId;

    /**
     * User name
     */
    @Setter
    @Getter
    protected String  name;

    /**
     * Menu permissions
     */
    @Setter
    @Getter
    protected Set<String> menuRight;

    /**
     * Constructor
     */
    public ApiLoginUser() {
    }
}
