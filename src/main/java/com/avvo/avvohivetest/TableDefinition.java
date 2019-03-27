package com.avvo.avvohivetest;
import java.util.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TableDefinition{
    @JsonProperty("database")
    public String database;
    @JsonProperty("name")
    public String name;
    @JsonProperty("fields")
    public String fields;
    @JsonProperty("datafile")
    public String datafile;
}