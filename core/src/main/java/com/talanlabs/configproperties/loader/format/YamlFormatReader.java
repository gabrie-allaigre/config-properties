package com.talanlabs.configproperties.loader.format;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class YamlFormatReader implements IFormatReader {

    private final Yaml yaml;

    public YamlFormatReader() {
        super();

        this.yaml = new Yaml(new Constructor(), new Representer(), new DumperOptions(),
                new CustomResolver());
    }

    @Override
    public boolean accept(URL url) {
        return url.toString().endsWith(".yml") || url.toString().endsWith(".yaml");
    }

    @Override
    public Properties read(InputStream in) throws FormatReaderException {
        Map<String, Object> map = yaml.load(in);
        Properties properties = new Properties();
        fill("", properties, map);
        return properties;
    }

    @SuppressWarnings("unchecked")
    private void fill(String prefix, Properties properties, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = StringUtils.isNotEmpty(prefix) ? prefix + "." + entry.getKey() : entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                properties.put(key, value);
            } else if (value instanceof Map) {
                fill(key, properties, (Map<String, Object>) value);
            } else if (value instanceof List) {
                List<?> ls = (List<?>) value;
                int s = String.valueOf(ls.size()).length();
                for (int i = 0; i < ls.size(); i++) {
                    properties.put(key + "." + StringUtils.leftPad(String.valueOf(i), s, "0"), ls.get(i) != null ? ls.get(i).toString() : null);
                }
            }
        }
    }

    private static class CustomResolver extends Resolver {

        @Override
        protected void addImplicitResolvers() {
            // Nothing resolver
        }
    }
}
