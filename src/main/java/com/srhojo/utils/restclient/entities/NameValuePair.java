package com.srhojo.utils.restclient.entities;

import java.io.Serializable;

/**
 *
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
public class NameValuePair implements Serializable {

    private static final long serialVersionUID = 2639541362656821845L;

    private final String name;
    private final String value;

    private NameValuePair(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public static NameValuePair of(final String name, final String value) {
        return new NameValuePair(name, value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
