package com.avvo.avvohivetest.config;
import java.util.*;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

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
    public Map<String, String> hive_variables;
    @JsonProperty("targets")
    public ArrayList<Target> targets;
    private int current_target_pos = 0;

    private Configuration() {
    }

    public String getFilename(){
        return filename;
    }

    public String getPath(){
        return Configuration.filePath + "/";
    }

    public Target getCurrentTarget() {
        Target trg = null;
        if(targets != null && targets.size() > 0 && current_target_pos < targets.size()){
            trg = targets.get(current_target_pos);
        }
        return trg;
    }

    public void nextTarget(){
        current_target_pos++;
    }
}