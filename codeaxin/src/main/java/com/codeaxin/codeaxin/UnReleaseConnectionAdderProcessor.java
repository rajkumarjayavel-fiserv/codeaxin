package com.codeaxin.codeaxin;

import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.processing.AbstractProcessor;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.support.sniper.SniperJavaPrettyPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnReleaseConnectionAdderProcessor extends AbstractProcessor<CtMethod> {

    List<CtLocalVariable> resources = new ArrayList<>();
    List<String> resourcesClosed = new ArrayList<String>();
    private final List<String> privateApiPackages = Arrays.asList("java.sql.Connection", "java.io.FileInputStream", "java.io.InputStream");
    private final String methodName = "close";
    private AbstractProcessor processor;

    public static void main(String[] args) {

        UnReleaseConnectionAdderProcessor unReleaseConnectionAdderProcessor=new UnReleaseConnectionAdderProcessor();
       simpleCall(unReleaseConnectionAdderProcessor);
    }

    public UnReleaseConnectionAdderProcessor(){
        BotAttributes botAttributes =new BotAttributes();
        DetectResources detectResources=new UnReleasedResourceDetectionImpl();
        ResourceTransformation resourceTransformation=new UnreleasedResourceTransformationImpl();
        UnreleasedResourceProcessorImpl processor=new UnreleasedResourceProcessorImpl(botAttributes,detectResources,resourceTransformation);


    }

    @Override
    public boolean isToBeProcessed(CtMethod element) {

       return processor.isToBeProcessed(element);
    }

    @Override
    public void process(CtMethod element) {
         processor.process(element);
    }

    public static void simpleCall(UnReleaseConnectionAdderProcessor unReleaseConnectionAdderProcessor) {
        try {
            Launcher launcher = new Launcher();
            Environment environment = launcher.getEnvironment();
            environment.setPrettyPrinterCreator(() -> new SniperJavaPrettyPrinter(environment));
            environment.disableConsistencyChecks();
            environment.setCopyResources(false);
            environment.setAutoImports(true);
            launcher.addProcessor(unReleaseConnectionAdderProcessor);
            launcher.addInputResource(UnReleaseConnectionAdderProcessor.class.getClassLoader().getResource("UnreleaseConnection.java").getPath());
            launcher.setSourceOutputDirectory("/Users/fafdmzx/srccode/output/");
            launcher.run();
            CtModel ctModel = launcher.getModel();
            System.out.println("Test:" + ctModel.isBuildModelFinished());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
