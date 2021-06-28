package com.codeaxin.codeaxin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.support.sniper.SniperJavaPrettyPrinter;
@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"com.codeaxin.codeaxin"})
public class BotExecutor {

    @Autowired
    BotAttributes botAttributes;
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext= SpringApplication.run(BotExecutor.class, args);
       ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(BotExecutor.class);

//        for (String beanName : applicationContext.getBeanDefinitionNames()) {
//            System.out.println(beanName);
//        }

       BotExecutor botExecutor= applicationContext.getBean(BotExecutor.class);
       botExecutor.processExecutor();
       SpringApplication.exit(configurableApplicationContext, () -> 0);

    }

    public void processExecutor(){
        botAttributes.setVulenaribilityType("UnReleaseResource:database");
        botAttributes.setVulnerableFile(BotExecutor.class.getClassLoader().getResource("UnreleaseConnection.java").getPath());
        botAttributes.setTargetDirectory("/Users/fafdmzx/srccode/output/");
        botAttributes.setLineNumber(45);
        try {
            ProcessFinder processFinder=new ProcessFinder();
            Launcher launcher = new Launcher();
            Environment environment = launcher.getEnvironment();
            environment.setPrettyPrinterCreator(() -> new SniperJavaPrettyPrinter(environment));
            environment.disableConsistencyChecks();
            environment.setCopyResources(botAttributes.isCopyResources());
            environment.setAutoImports(botAttributes.isAutoImports());
            launcher.addProcessor(processFinder.findProcessor(botAttributes));
            launcher.addInputResource(botAttributes.getVulnerableFile());
            launcher.setSourceOutputDirectory(botAttributes.getTargetDirectory());
            launcher.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
