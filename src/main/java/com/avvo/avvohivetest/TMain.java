package com.avvo.avvohivetest;

import org.junit.runner.*;
import org.junit.internal.TextListener;
import com.avvo.avvohivetest.config.Configuration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

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
        PrintStream original = new PrintStream(System.out);

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
            //org.junit.runner.JUnitCore.main("com.avvo.avvohivetest.TableHiveRunnerTest");
            JUnitCore junit = new JUnitCore();
            //junit.addListener(new TextListener(System.out));
            Result result = junit.run(TableHiveRunnerTest.class);

            // replace the System.out, here I redirect to NUL
            try
            {
                FileOutputStream fos=new FileOutputStream("test_report.txt");
                PrintStream printStream = new PrintStream(fos);
                System.setOut(printStream);
                System.out.println("Finish the first test");
                resultReport(result);
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }
            conf.nextTarget();

        }
    }
}