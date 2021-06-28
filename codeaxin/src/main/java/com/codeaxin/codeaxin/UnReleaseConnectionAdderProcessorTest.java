package com.codeaxin.codeaxin;

import org.junit.Test;
import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.support.sniper.SniperJavaPrettyPrinter;

public class UnReleaseConnectionAdderProcessorTest {
    @Test
    public void testUnreleaseConnection() throws Exception {
        final String[] args = {
                "-i", "/Users/fafdmzx/Downloads/gyft-parent/gyft-core/src/main/java/com/gyft",
                "-o", "target/spooned/unrelease",
                "-p", "com.codeaxin.codeaxin.UnReleaseConnectionAdderProcessor",
                "--compile"
        };

        try {
            final Launcher launcher = new Launcher();
            launcher.setArgs(args);
            Environment environment = launcher.getEnvironment();
            environment.setPrettyPrinterCreator(() -> new SniperJavaPrettyPrinter(environment));
            environment.disableConsistencyChecks();
            environment.setCopyResources(true);
            environment.setAutoImports(false);

            launcher.run();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
