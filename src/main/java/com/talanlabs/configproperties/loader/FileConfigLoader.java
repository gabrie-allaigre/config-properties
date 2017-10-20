package com.talanlabs.configproperties.loader;

import java.io.File;
import java.net.MalformedURLException;

public class FileConfigLoader extends URLConfigLoader {

    public FileConfigLoader(File file) {
        super();

        try {
            setUrl(file != null ? file.toURI().toURL() : null);
        } catch (MalformedURLException e) {
            throw new LoaderReadException("Failed to read file " + file, e);
        }
    }
}
