package com.avvo.avvohivetest;

import org.junit.runner.*;
import org.junit.internal.TextListener;
import com.avvo.avvohivetest.config.Configuration;
import org.junit.runner.notification.Failure;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class TMain
{
    public static void resultReport(Result result) {
        int succeeded = result.getRunCount() - result.getFailureCount() -
                        result.getIgnoreCount();

        System.out.println(result.getRunCount() + " test(s) in " +
            result.getRunTime() + "ms. " + succeeded + " passed, " + 
            result.getFailureCount() + " failed, " +
            result.getIgnoreCount() + " ignored.");

        for (Failure fail:result.getFailures() ) {
            System.out.println(fail.getMessage() + " " + fail.getDescription());
        }

        if (!result.wasSuccessful()) {
          System.exit(1);
        }

    }

    public static void main(String [] args)
    {
        PrintStream original = new PrintStream(System.out);

        String configFile = args[0];
        Configuration conf = null;
        try {
            System.out.println("Configuration file: " + configFile);
            conf = Configuration.ReadConfig(configFile);
            conf.targets.get(0).input_tables.get(0).getFields();
        }catch(Exception e){
            System.out.println(e.toString());
        }

        while(conf != null && conf.getCurrentTarget() != null){
            JUnitCore junit = new JUnitCore();
            Result result = junit.run(TableHiveRunnerTest.class);

            // replace the System.out, here I redirect to NUL
            try
            {
                FileOutputStream fos=new FileOutputStream("test_report.txt");
                PrintStream printStream = new PrintStream(fos);
                System.setOut(printStream);
                resultReport(result);
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }

            conf.nextTarget();
        }
    }
}
