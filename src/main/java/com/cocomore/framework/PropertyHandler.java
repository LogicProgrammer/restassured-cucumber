package com.cocomore.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyHandler {

    String file_path = null;
    Properties properties = new Properties();

    public PropertyHandler(String file_path) {
        this.file_path = file_path;
        loadProperties();
    }

    public void loadProperties(){
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(new File(file_path));
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }

}
