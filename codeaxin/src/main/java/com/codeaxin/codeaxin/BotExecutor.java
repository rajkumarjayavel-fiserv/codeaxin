package com.codeaxin.codeaxin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.processing.AbstractProcessor;
import spoon.support.sniper.SniperJavaPrettyPrinter;

import java.util.Optional;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"com.codeaxin.codeaxin"})
public class BotExecutor implements Condition {
    private static final Logger LOGGER = LogManager.getLogger(BotExecutor.class);
    @Override
    public boolean matches(ConditionContext context,
                           AnnotatedTypeMetadata metadata) {
        return Optional.ofNullable(botAttributes).isPresent();

    }

    @Autowired
    BotAttributes botAttributes;
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext= SpringApplication.run(BotExecutor.class, args);
       ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(BotExecutor.class);

        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            LOGGER.debug(beanName);
        }

       BotExecutor botExecutor= applicationContext.getBean(BotExecutor.class);
       botExecutor.processExecutor(args);
       SpringApplication.exit(configurableApplicationContext, () -> 0);
        LOGGER.info("Info level log message");
        LOGGER.debug("Debug level log message");
        LOGGER.error("Error level log message");

    }
    public BotExecutor(){

    }

    public void processExecutor(String[] args){
        if(args.length>=3){
            botAttributes.setVulenaribilityType(args[0]);
            botAttributes.setLineNumber(Integer.parseInt(args[1]));
            botAttributes.setTargetDirectory(args[2]);
            botAttributes.setVulnerableFile(args[3]);


        }else {
            botAttributes.setVulenaribilityType("UnReleaseResource:file");
            botAttributes.setVulnerableFile(BotExecutor.class.getClassLoader().getResource("UnreleaseConnection_tryblock.java").getPath());
            botAttributes.setTargetDirectory("/Users/fafdmzx/srccode/output/");
           botAttributes.setLineNumber(40);
        }
        try {
            AbstractProcessor processor=null;
            ProcessFinder processFinder=new ProcessFinder();
            processor=processFinder.findProcessor(botAttributes);
            if(processor!=null) {
                Launcher launcher = new Launcher();
                Environment environment = launcher.getEnvironment();
                environment.setPrettyPrinterCreator(() -> new SniperJavaPrettyPrinter(environment));
                environment.disableConsistencyChecks();
                environment.setCopyResources(botAttributes.isCopyResources());
                environment.setAutoImports(botAttributes.isAutoImports());
                launcher.addProcessor(processor);
                launcher.addInputResource(botAttributes.getVulnerableFile());
                launcher.setSourceOutputDirectory(botAttributes.getTargetDirectory());
                launcher.run();
            }
            else{
                throw new RuntimeException("Vulnerability type is not FOUND.");
            }

        } catch (Exception e) {
            LOGGER.error("Exception In Processor",e);
           // e.printStackTrace();
        }
    }
}
