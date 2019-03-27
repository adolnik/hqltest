package com.avvo.avvohivetest;
import java.util.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Target{
    @JsonProperty("src")
    public String src;
    @JsonProperty("input_tables")
    public ArrayList<TableDefinition> input_tables;
    @JsonProperty("output_tables")
    public ArrayList<TableDefinition> output_tables;

}