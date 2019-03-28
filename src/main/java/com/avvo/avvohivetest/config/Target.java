package com.avvo.avvohivetest.config;
import java.util.*;
import java.util.List;

import com.avvo.avvohivetest.config.processor.TableField;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Target{
    @JsonProperty("src")
    public String src;
    @JsonProperty("name")
    public String name;
    @JsonProperty("input_tables")
    public ArrayList<TableDefinition> input_tables;
    @JsonProperty("output_tables")
    public ArrayList<TableDefinition> output_tables;

}