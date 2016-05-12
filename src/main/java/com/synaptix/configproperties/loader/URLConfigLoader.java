package com.synaptix.configproperties.loader;

import com.google.common.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class URLConfigLoader implements IConfigLoader {

    private static final Logger LOG = LogManager.getLogger(URLConfigLoader.class);

    private final URL url;

    public URLConfigLoader(URL url) {
        super();

        this.url = url;
    }

    @Override
    public Properties readProperties() {
        LOG.info("Read config file in url {}", url);

        if (url != null) {
            try {
                try (InputStream in = Resources.asByteSource(url).openStream()) {
                    Properties properties = new Properties();
                    properties.load(in);
                    return properties;
                }
            } catch (Exception e) {
                LOG.error("Not read url {}", url, e);
                throw new LoaderReadException("Not read url " + url, e);
            }
        }
        return null;
    }
}
