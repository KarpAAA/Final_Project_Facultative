package com.example.final_project.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
    public static String getUrl(){

        Properties properties = new Properties();

        try(FileInputStream fileInputStream = new FileInputStream("C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\resources\\app.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties.getProperty("url") + "?" +
                "user=" + properties.getProperty("user") +
                "&password=" + properties.get("password");

    }
}
