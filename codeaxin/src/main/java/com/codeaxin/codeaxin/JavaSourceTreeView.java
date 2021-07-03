package com.codeaxin.codeaxin;
import spoon.Launcher;

public class JavaSourceTreeView extends Launcher {
    public static void main(String[] args) {
        JavaSourceTreeView obj = new JavaSourceTreeView();
        String fullJavaFilePath="";
        if(args.length<1)
            fullJavaFilePath = JavaSourceTreeView.class.getClassLoader().getResource("UnreleaseConnection_tryblock.java").getPath();
        else
            args[0]=fullJavaFilePath;
        obj.runGuiLauncher(fullJavaFilePath);
    }
    public void runGuiLauncher(String fullJavaFilePath){
        String[] args = {"--gui","--input",fullJavaFilePath};
        super.run(args);
    }

}
