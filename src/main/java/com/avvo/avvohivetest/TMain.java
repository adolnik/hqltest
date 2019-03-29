package com.avvo.avvohivetest;

import org.junit.runner.*;
import org.junit.internal.TextListener;
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
        String configFile = args[0];
        Configuration conf = null;
        try {
            System.out.println("Test project... " + configFile);
            conf = Configuration.ReadConfig(configFile);
            conf.targets.get(0).input_tables.get(0).getFields();
        }catch(Exception e){
            System.out.println(e.toString());
        }

        while(conf != null && conf.getCurrentTarget() != null){
            System.out.println("Start the first test");
            //org.junit.runner.JUnitCore.main("com.avvo.avvohivetest.HelloHiveRunnerTest");
            JUnitCore junit = new JUnitCore();
            junit.addListener(new TextListener(System.out));
            Result result = junit.run(TableHiveRunnerTest.class);

            System.out.println("Finish the first test");
            resultReport(result);
           conf.nextTarget();
        }
    }
}