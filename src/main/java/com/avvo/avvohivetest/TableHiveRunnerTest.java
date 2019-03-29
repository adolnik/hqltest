package com.avvo.avvohivetest;

import com.klarna.hiverunner.HiveShell;
import com.klarna.hiverunner.StandaloneHiveRunner;
import com.klarna.hiverunner.annotations.HiveSQL;
import com.klarna.hiverunner.data.TsvFileParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.rules.TestName;

import java.io.File;
import java.util.*;

import java.io.StringWriter;
import java.io.PrintWriter;

import com.avvo.avvohivetest.config.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A basic Hive Runner example showing how to setup the test source database and target database, execute the query
 * and then validate the result.
 *
 * <p/>
 * All HiveRunner tests should run with the StandaloneHiveRunner and have a reference to HiveShell.
 */
@RunWith(StandaloneHiveRunner.class)
public class TableHiveRunnerTest {
    @Rule
    public TestName name = new TestName();

    @HiveSQL(files = {}, encoding = "UTF-8", autoStart = false)
    private HiveShell shell;

    private Configuration configurator = null;

    private void filterHQLFile(String filename){
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if(!(line.trim().startsWith("set mapred.output.compression.codec=org.apache.hadoop.io.compress.SnappyCodec") ||
                        line.trim().startsWith("set hive.parquet.compression=SNAPPY;")))
                    System.out.println(line);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setupSourceDatabase() {
        configurator = Configuration.getCurrentConfiguration();

        // Set Hive variables before start
        for(String key : configurator.hive_variables.keySet()){
            shell.setHiveVarValue(key, configurator.hive_variables.get(key));
        }

        System.out.println("Starting hive server");
        shell.start();

        System.out.println("Prepare to run tests. Create database");
        for(String db_name : configurator.databases) {
            shell.execute("CREATE DATABASE " + db_name);
        }

        Target trg = configurator.getCurrentTarget();
        for(TableDefinition tbl_def : trg.input_tables){

            if(tbl_def.has_to_be_created) {
                System.out.println(tbl_def.getCreateStatement());
                shell.execute(new StringBuilder()
                        .append(tbl_def.getCreateStatement()).toString());
            }

            String filePath = configurator.getPath() + tbl_def.datafile;
            File dataFile = new File(filePath);
            System.out.println("Data file is " + filePath);
            TsvFileParser tsvFileParser = new TsvFileParser().withHeader().withDelimiter("\t").withNullValue("__NULL__");

            String state = "select * from " + tbl_def.database + "." + tbl_def.name;
            System.out.println(state);

            try {
                //https://github.com/klarna/HiveRunner/blob/master/src/test/java/com/klarna/hiverunner/examples/InsertTestData.java
                shell.insertInto(tbl_def.database, tbl_def.name).withAllColumns().addRowsFrom(dataFile, tsvFileParser).commit();
                printResult(shell.executeStatement(state));
            }catch (Exception e){
                System.out.println("ERROR: Incorrect file");
                System.out.println(e);
            }

        }

    }

    public void printResult(List<Object[]> result) {
        System.out.println(String.format("Result from %s:",name.getMethodName()));
        for (Object[] row : result) {
            System.out.println(Arrays.asList(row));
        }
    }

    @Test
    public void testHQLScript() {
        Target trg = configurator.getCurrentTarget();
        String filePath = configurator.getPath() + trg.src;

        File hql_script_file = new File(filePath);
        System.out.println("Starting the test, HQL query");
        // Move clean HQL query
        this.filterHQLFile(filePath);

        String phase = "Execute HQL statement";
        try {
            shell.execute(hql_script_file);

            phase = "Get tests results";
            for(TableDefinition tbl_def : trg.output_tables) {
                String sql_statement = tbl_def.getSelectStatement();
                System.out.println(sql_statement);
                List<Object[]> actual = shell.executeStatement(sql_statement);
                printResult(actual);
                compareToRefData(actual, tbl_def.datafile);
            }
            Assert.assertEquals(0,1);
        }catch(Exception ex){
            System.out.println("Errors detected on phase: " + phase);
            System.out.println(ex);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            System.out.println(sStackTrace);

        }
    }

    private void compareToRefData(List<Object[]> actual, String datafile) throws Exception {
        File ref_file = new File(configurator.getPath() + datafile);
        ArrayList<String> result = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new FileReader(ref_file));
        while (br.ready()) {
            result.add(br.readLine());
        }

        System.out.println("Check table rows count");
        // Check the size of outputArray
        Assert.assertEquals(result.size(),actual.size());
    }

}
