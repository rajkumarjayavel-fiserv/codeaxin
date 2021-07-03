package com.codeaxin.codeaxin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.support.sniper.SniperJavaPrettyPrinter;

import java.io.File;
import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BotExecutorTestCases {
    @Autowired
    private BotExecutor botExecutor;
    @Test
    public void tryBlockwithLineNumber() throws Exception {
        try {
            URL res= BotExecutorTestCases.class.getClassLoader().getResource("UnreleaseConnection_tryblock.java");
            String output=System.getProperty("user.dir")+File.separator+"generated";
            final String[] args = {"UnReleaseResource:database","40",output,res.getFile()};

            botExecutor.processExecutor(args);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void tryBlockwithoutLineNumber() throws Exception {
        try {
            //if LineNumber is unknown or more than one resources are unclosed please mark it as '0'
            URL res= BotExecutorTestCases.class.getClassLoader().getResource("UnreleaseConnection_tryblockwithoutlinenumber.java");
            String output=System.getProperty("user.dir")+File.separator+"generated";
            final String[] args = {"UnReleaseResource:database","0",output,res.getFile()};

            botExecutor.processExecutor(args);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void tryresourceBlock() throws Exception {
        try {
            URL res= BotExecutorTestCases.class.getClassLoader().getResource("UnreleaseConnection_tryresourceBlock.java");
            String output=System.getProperty("user.dir")+File.separator+"generated";
            final String[] args = {"UnReleaseResource:database","41",output,res.getFile()};

            botExecutor.processExecutor(args);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void notryBlock() throws Exception {
        try {
            URL res= BotExecutorTestCases.class.getClassLoader().getResource("UnreleaseConnection.java");
            String output=System.getProperty("user.dir")+File.separator+"generated";
            final String[] args = {"UnReleaseResource:database","39",output,res.getFile()};

            botExecutor.processExecutor(args);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
