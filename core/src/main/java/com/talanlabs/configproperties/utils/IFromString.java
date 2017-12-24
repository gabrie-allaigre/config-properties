package com.talanlabs.configproperties.utils;

public interface IFromString<E> {

    /**
     * Convert String to E
     *
     * @param value string value
     * @return object
     */
    E fromString(String value);

}