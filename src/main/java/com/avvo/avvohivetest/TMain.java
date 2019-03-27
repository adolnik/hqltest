package com.avvo.avvohivetest;

import java.io.*;

public class TMain
{
    public static void main(String [] args)
    {
        System.out.println("Test project");
        try {
            Configuration conf = Configuration.ReadConfig("/home/adolnik/Develop/rrsoft/AvvoHiveTest/data/test_config.json");
            System.out.println(conf.hive_variables.get(0));
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

}