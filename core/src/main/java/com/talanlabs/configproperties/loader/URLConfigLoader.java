package com.talanlabs.configproperties.loader;

import com.google.common.io.Resources;
import com.talanlabs.configproperties.loader.format.IFormatReader;
import com.talanlabs.configproperties.loader.format.PropertiesFormatReader;
import com.talanlabs.configproperties.loader.format.YamlFormatReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class URLConfigLoader implements IConfigLoader {

    private static final Logger LOG = LoggerFactory.getLogger(URLConfigLoader.class);

    private final List<IFormatReader> formatReaders = new ArrayList<>(Arrays.asList(new PropertiesFormatReader(), new YamlFormatReader()));

    private URL url;

    public URLConfigLoader(URL url) {
        super();

        this.url = url;
    }

    protected URLConfigLoader() {
        super();
    }

    protected void setUrl(URL url) {
        this.url = url;
    }

    /**
     * Add format reader
     *
     * @param formatReader new format reader
     */
    public void addFormatReader(IFormatReader formatReader) {
        formatReaders.add(0, formatReader);
    }

    @Override
    public Properties readProperties() {
        LOG.info("Read config file in url {}", url);

        if (url != null) {
            Optional<IFormatReader> ofr = formatReaders.stream().filter(f -> f.accept(url)).findFirst();
            if (!ofr.isPresent()) {
                throw new LoaderReadException("Not read url " + url + " unkwnow format", null);
            }
            try {
                try (InputStream in = Resources.asByteSource(url).openStream()) {
                    return ofr.get().read(in);
                }
            } catch (Exception e) {
                LOG.error("Not read url {}", url, e);
                throw new LoaderReadException("Not read url " + url, e);
            }
        }
        return null;
    }
}
