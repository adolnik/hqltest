package com.avvo.avvohivetest;

import java.io.*;
import org.junit.*;
import org.junit.runner.*;

public class TMain
{
    public static void resultReport(Result result) {
        System.out.println("Finished. Result: Failures: " +
                result.getFailureCount() + ". Ignored: " +
                result.getIgnoreCount() + ". Tests run: " +
                result.getRunCount() + ". Time: " +
                result.getRunTime() + "ms.");
    }

    public static void main(String [] args)
    {
        System.out.println("Test project");
        try {
            Configuration conf = Configuration.ReadConfig("/home/adolnik/Develop/rrsoft/AvvoHiveTest/data/test_config.json");
            System.out.println(conf.hive_variables.get(0));
        }catch(Exception e){
            System.out.println(e.toString());
        }

        //org.junit.runner.JUnitCore.main("com.avvo.avvohivetest.HelloHiveRunnerTest");
        JUnitCore junit = new JUnitCore();
        //junit.addListener(new TextListener(System.out));
        Result result = junit.run(HelloHiveRunnerTest.class);
        resultReport(result);
    }

}