package com.avvo.avvohivetest.config;
import java.util.*;

import com.avvo.avvohivetest.config.processor.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TableDefinition{
    @JsonProperty("has_to_be_created")
    public boolean has_to_be_created = true;
    @JsonProperty("database")
    public String database;
    @JsonProperty("name")
    public String name;
    @JsonProperty("fields")
    private String fieldsString;

    private ArrayList<TableField> fields = null;
    public ArrayList<TableField> getFields(){
        if(fields != null){
            return fields;
        }

        fields = new ArrayList<TableField>();

        for(String field : fieldsString.split(",")){
            String[] fld = field.trim().split("\\s+");
            TableField tf = new TableField(fld[0].trim(), fld[1].trim());
            fields.add(tf);
        }

        return fields;
    }

    @JsonProperty("datafile")
    public String datafile;

    public String getCreateStatement(){
        return "CREATE TABLE "+ database+"."+name+" (" + fieldsString + ")";
    }

    private String getListOfFields(){
        String res = "";
        if(fields == null){
            getFields();
        }

        for(TableField tf : fields) {
            res = res + tf.name + ",";
        }
        res = res.substring(0,res.length()-1);
        return res;
    }

    public String getSelectStatement(){
        return "SELECT " + getListOfFields() + " FROM "+ database+"."+name;
    }
}