package com.talanlabs.configproperties.loader.format;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertiesFormatReader implements IFormatReader {

    @Override
    public boolean accept(URL url) {
        return url.toString().endsWith(".properties");
    }

    @Override
    public Properties read(InputStream in) throws FormatReaderException {
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new FormatReaderException("Failed to load input stream", e);
        }
        return properties;
    }

}
