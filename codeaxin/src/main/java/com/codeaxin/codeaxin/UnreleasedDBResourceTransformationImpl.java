package com.codeaxin.codeaxin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;
import java.util.stream.Collectors;
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UnreleasedDBResourceTransformationImpl implements ResourceTransformation<CtMethod>{
    private static final Logger LOGGER = LogManager.getLogger(UnreleasedResourceTransformationImpl.class);

    public void executeUnreleasedResourceTransfrom( CtMethod element,ResourceIdentifier resourceIdentifier){
        List<CtLocalVariable>  resources=resourceIdentifier.getResources();
        List<String> resourcesClosed=resourceIdentifier.getResourcesClosed();
        resources = resources.stream().filter(e -> !resourcesClosed.contains(e.getSimpleName())).distinct().collect(Collectors.toList());

        //CtCodeSnippetStatement snippet = getFactory().Core().createCodeSnippetStatement();
        try {
            CtTry newTryNode = element.getFactory().createTry(); // Create New TRY Node data model
            CtCatch newCatch = element.getFactory().createCatch(); //Create New CATCH Node data model
            CtTryWithResource ctTryWithResource=element.getFactory().createTryWithResource();
            CtBlock finallyBlock = element.getFactory().Core().createBlock();
            CtCodeSnippetStatement resourceDeclaration = element.getFactory().Core().createCodeSnippetStatement();
            //  resources= resources.stream().filter(e->e.getSimpleName().equals("input")).collect(Collectors.toList());
            for (CtLocalVariable ctLocalVariable : resources) {
                CtCodeSnippetStatement snippet = element.getFactory().Core().createCodeSnippetStatement();
                LOGGER.debug("ctLocalVariable:"+ctLocalVariable);
                snippet.setValue("if (" + ctLocalVariable.getSimpleName() + " != null ) " + ctLocalVariable.getSimpleName() + "." + resourceIdentifier.getCloseableMethodName() + "()");
                List<CtTry> ctBlocks= element.getBody().getElements(new TypeFilter<>(CtTry.class));
                CtExpression expression=ctLocalVariable.getAssignment();
                //Find the Nearest Parent Try Block
                for(CtTry ctBlock:ctBlocks){
                    if(ctBlock.getBody().getStatements().stream().filter(e->e.equals(ctLocalVariable)).findFirst().isPresent()) {
                        newTryNode = ctBlock;
                        newTryNode.getBody().getStatements().stream().filter(e->e.equals(ctLocalVariable)).findFirst().get().delete();
                        break;
                    }
                }
                //Without Try Block - wrap with new try block
                if(!newTryNode.isParentInitialized()){
                    element.getBody().getStatements().stream().filter(e->e.equals(ctLocalVariable)).findFirst().get().delete();
                    newTryNode.setBody(element.getBody().clone());
                    newTryNode.setParent(element.getBody().getParent());
                    element.setBody(newTryNode);
                }
                else if(ctLocalVariable.getAssignment()!=null) {
                    resourceDeclaration.setValue(ctLocalVariable.getType() + " " + ctLocalVariable.getSimpleName() + " = null");
                    newTryNode.insertBefore(resourceDeclaration);
                    CtCodeSnippetStatement resourceAssignment = element.getFactory().Core().createCodeSnippetStatement();
                    resourceAssignment.setValue(ctLocalVariable.getSimpleName() + " = " + expression);
                    newTryNode.getBody().insertBegin(resourceAssignment);
                }
                //Add the Resource Declaration precedence of the try block
                if(resourceDeclaration.getValue()==null) {
                    newTryNode.insertBefore(ctLocalVariable);
                }

                finallyBlock.insertBegin(snippet);
                newTryNode.setFinalizer(finallyBlock);
                System.out.println("ctLocalVariable:" + ctLocalVariable);
                resourceDeclaration = element.getFactory().Core().createCodeSnippetStatement();
            }

            LOGGER.debug("Matched:" + element.getBody());
            resources.clear();
            resourcesClosed.clear();
        } catch (Exception e) {
            LOGGER.error("Exception IN UNreleased Response",e);
            e.printStackTrace();
        }

    }
}
