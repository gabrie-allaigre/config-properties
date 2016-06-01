package com.synaptix.configproperties.loader;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Properties;

public class ArgsConfigLoader implements IConfigLoader {

    private static final String START_ARG = "--";
    private static final String SEPARATOR = "=";

    private final IGetArgs getArgs;

    public ArgsConfigLoader() {
        this(StaticGetArgs.getINSTANCE());
    }

    public ArgsConfigLoader(IGetArgs getArgs) {
        super();

        this.getArgs = getArgs;
    }

    @Override
    public Properties readProperties() {
        String[] args = getArgs != null ? getArgs.getArgs() : null;
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

    public interface IGetArgs {

        /**
         * Use only args with format --server.mail=test@test.com
         */
        String[] getArgs();

    }

    public static class StaticGetArgs implements IGetArgs {

        private static StaticGetArgs INSTANCE;
        private String[] args;

        private StaticGetArgs() {
            super();
        }

        public static synchronized StaticGetArgs getINSTANCE() {
            if (INSTANCE == null) {
                INSTANCE = new StaticGetArgs();
            }
            return INSTANCE;
        }

        /**
         * Set args, Use only args with format --server.mail=test@test.com
         */
        public void setArgs(String[] args) {
            this.args = args;
        }

        @Override
        public String[] getArgs() {
            return args;
        }
    }
}
