package com.avvo.avvohivetest;

import java.io.*;

public class TMain
{
    public static void main(String [] args)
    {
        System.out.println("Test project");
        try {
            Configuration conf = Configuration.ReadConfig("/home/adolnik/Develop/rrsoft/AvvoHiveTest/data/test_config.json");
            System.out.println(conf.databases);
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

}