package com.avvo.avvohivetest;

import com.klarna.hiverunner.HiveShell;
import com.klarna.hiverunner.StandaloneHiveRunner;
import com.klarna.hiverunner.annotations.HiveSQL;
import com.klarna.hiverunner.data.TsvFileParser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.rules.TestName;

import java.nio.file.Paths;
import java.io.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.avvo.avvohivetest.config.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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

    @HiveSQL(files = {})
    private HiveShell shell;

    private Configuration configurator = null;

    @Before
    public void setupSourceDatabase() {
        configurator = Configuration.getCurrentConfiguration();

        for(String db_name : configurator.databases) {
            shell.execute("CREATE DATABASE " + db_name);
        }

        Target trg = configurator.getCurrentTarget();
        for(TableDefinition tbl_def : trg.input_tables){
            shell.execute(new StringBuilder()
                    .append(tbl_def.getCreateStatement()).toString());
        }
    }

    public void printResult(List<Object[]> result) {
        System.out.println(String.format("Result from %s:",name.getMethodName()));
        for (Object[] row : result) {
            System.out.println(Arrays.asList(row));
        }
    }

    @Test
    public void insertRowsIntoPartitionedTableStoredAsSequencefileWithCustomDelimiterAndNullValue() {
        File dataFile = new File("/home/adolnik/Develop/rrsoft/AvvoHiveTest/data/data2.tsv");
        shell.execute(new StringBuilder()
                .append("CREATE TABLE dm.test_table2 (")
                .append("col_a STRING, col_b BIGINT")
                .append(")")
                .append("partitioned by (col_c string)")
                .append("stored as SEQUENCEFILE")
                .toString());

        shell.insertInto("dm", "test_table2")
                .withAllColumns()
                .addRowsFrom(dataFile, new TsvFileParser().withDelimiter(":").withNullValue("__NULL__"))
                .commit();

        printResult(shell.executeStatement("select * from dm.test_table2"));
    }

    @Test
    public void insertRowsFromCode() {
        Target trg = configurator.getCurrentTarget();
        for(TableDefinition tbl_def : trg.input_tables){
            String filePath = configurator.getPath() + "/" + tbl_def.datafile;
            File dataFile = new File(filePath);
            System.out.println("Data file is " + filePath);
            TsvFileParser tsvFileParser = new TsvFileParser().withDelimiter(",").withNullValue("");
            shell.insertInto(tbl_def.database, tbl_def.name).withAllColumns().addRowsFrom(dataFile, tsvFileParser).commit();

            printResult(shell.executeStatement("select count(*) from " + tbl_def.database + "." + tbl_def.name));
        }

    }
}
