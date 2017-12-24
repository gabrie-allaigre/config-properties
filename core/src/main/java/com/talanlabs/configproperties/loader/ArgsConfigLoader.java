package com.talanlabs.configproperties.loader;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Properties;

public class ArgsConfigLoader implements IConfigLoader {

    private static final String START_ARG = "--";
    private static final String SEPARATOR = "=";

    private final String[] args;

    public ArgsConfigLoader(String[] args) {
        super();

        this.args = args;
    }


    @Override
    public Properties readProperties() {
        if (args != null) {
            Properties properties = new Properties();

            Arrays.stream(args).filter(arg -> arg != null && arg.startsWith(START_ARG) && arg.contains(SEPARATOR)).map(this::split).forEach(p -> properties.put(p.getKey(), p.getValue()));

            return properties;
        }
        return null;
    }

    private Pair<String, String> split(String arg) {
        int i = arg.indexOf(SEPARATOR);
        String key = arg.substring(START_ARG.length(), i);
        String value = arg.substring(i + 1);
        return Pair.of(key, value);
    }
}
