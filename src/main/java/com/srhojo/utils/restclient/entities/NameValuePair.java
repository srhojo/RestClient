package com.srhojo.utils.restclient.entities;

import java.io.Serializable;

/**
 * Author: srhojo
 * URL: https://github.com/srhojo
 */
public class NameValuePair implements Serializable {

    private static final long serialVersionUID = 2639541362656821845L;

    private final String name;
    private final String value;

    private NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static NameValuePair of(String name, String value) {
        return new NameValuePair(name, value);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
