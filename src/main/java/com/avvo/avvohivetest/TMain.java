package com.avvo.avvohivetest;

import java.io.*;
import org.junit.*;
import org.junit.runner.*;
import com.avvo.avvohivetest.config.Configuration;

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
        String configFile = "/home/adolnik/Develop/rrsoft/AvvoHiveTest/data/test_config.json";
        Configuration conf = null;
        try {
            System.out.println("Test project...");
            conf = Configuration.ReadConfig(configFile);
            conf.targets.get(0).input_tables.get(0).getFields();
            System.out.println(conf.targets.get(0).input_tables.get(0));
        }catch(Exception e){
            System.out.println(e.toString());
        }

        while(conf != null && conf.getCurrentTarget() != null){
            //org.junit.runner.JUnitCore.main("com.avvo.avvohivetest.HelloHiveRunnerTest");
            JUnitCore junit = new JUnitCore();
            //junit.addListener(new TextListener(System.out));
            Result result = junit.run(TableHiveRunnerTest.class);
            resultReport(result);
            conf.nextTarget();
        }
    }

}