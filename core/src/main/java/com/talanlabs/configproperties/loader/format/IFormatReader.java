package com.talanlabs.configproperties.loader.format;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public interface IFormatReader {

    /**
     * @param url url to read
     * @return true if format reader read
     */
    boolean accept(URL url);

    /**
     * Read input stream
     *
     * @param in stream
     * @return a properties
     */
    Properties read(InputStream in) throws FormatReaderException;

}
