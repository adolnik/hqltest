package com.avvo.avvohivetest;
import java.util.*;
import java.util.List;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Configuration{

    private static String filename = "";
    private static String filePath = "";
    private static Configuration conf = new Configuration();

    public static Configuration ReadConfig(String filename) throws Exception{
        Configuration.filename = filename;
        Configuration conf = new Configuration();
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        File file = new File( filename );
        Configuration.filePath = file.getParent();
        Configuration.conf = objectMapper.readValue(file, Configuration.class);
        return Configuration.conf;
    }

    public static Configuration getCurrentConfiguration(){
        return conf;
    }

    @JsonProperty("version")
    public Long version;
    @JsonProperty("databases")
    public ArrayList<String> databases;
    @JsonProperty("hive_variables")
    public ArrayList<Map<String, String>> hive_variables;
    @JsonProperty("targets")
    public ArrayList<Target> targets;

    private Configuration() {
    }

    public String getFilename(){
        return filename;
    }

    public String getPath(){
        return Configuration.filePath;
    }
}