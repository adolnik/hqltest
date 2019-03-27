package com.avvo.avvohivetest;
import java.util.*;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HiveVariables{
    @JsonProperty("srcMgdDatabase")
    public String srcMgdDatabase;
    @JsonProperty("dmDatabase")
    public String dmDatabase;
}