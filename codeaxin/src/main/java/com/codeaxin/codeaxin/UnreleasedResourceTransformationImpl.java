package com.codeaxin.codeaxin;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.path.CtRole;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtCodeSnippetStatementImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UnreleasedResourceTransformationImpl implements ResourceTransformation<CtMethod>{
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
          //  resources= resources.stream().filter(e->e.getSimpleName().equals("input")).collect(Collectors.toList());
            for (CtLocalVariable ctLocalVariable : resources) {
                CtCodeSnippetStatement snippet = element.getFactory().Core().createCodeSnippetStatement();
                System.out.println("ctLocalVariable:"+ctLocalVariable);
//                CtCatchVariable<Throwable> newCatchVariable = this.getFactory().Core().<Throwable>createCatchVariable()
//                        .<CtCatchVariable<Throwable>>setType(this.getFactory().Code().<Throwable>createCtTypeReference(java.lang.Exception.class))
//                        .setSimpleName("e");
//                newCatch.setParameter(newCatchVariable);
//                newCatch.setBody(new CtCodeSnippetStatementImpl().setValue("e.printStackTrace()"));
//                newTryNode.setCatchers(Arrays.asList(new CtCatch[]{newCatch}));
                snippet.setValue("if (" + ctLocalVariable.getSimpleName() + " != null ) " + ctLocalVariable.getSimpleName() + "." + resourceIdentifier.getCloseableMethodName() + "()");
                List<CtTry> ctBlocks= element.getBody().getElements(new TypeFilter<>(CtTry.class));
                CtExpression expression=ctLocalVariable.getAssignment();
                for(CtTry ctBlock:ctBlocks){
                    if(ctBlock.getBody().getStatements().stream().filter(e->e.equals(ctLocalVariable)).findFirst().isPresent()) {
                        newTryNode = ctBlock;
                        newTryNode.getBody().getStatements().stream().filter(e->e.equals(ctLocalVariable)).findFirst().get().delete();
                        break;
                    }
                }
                if(ctLocalVariable.getAssignment()!=null) {
                    CtCodeSnippetStatement resourceDeclaration = element.getFactory().Core().createCodeSnippetStatement();
                    resourceDeclaration.setValue(ctLocalVariable.getType() + " " + ctLocalVariable.getSimpleName() + " = null");
                    newTryNode.insertBefore(resourceDeclaration);
                    CtCodeSnippetStatement resourceAssignment = element.getFactory().Core().createCodeSnippetStatement();
                    resourceAssignment.setValue(ctLocalVariable.getSimpleName() + " = " + expression);
                    newTryNode.getBody().insertBegin(resourceAssignment);
                }
                else if(!newTryNode.isParentInitialized()){
                    element.getBody().getStatements().stream().filter(e->e.equals(ctLocalVariable)).findFirst().get().delete();
                    newTryNode.setBody(element.getBody().clone());
                    newTryNode.setParent(element.getBody().getParent());
                    element.setBody(newTryNode);
                }
                newTryNode.insertBefore(ctLocalVariable);
               finallyBlock.insertBegin(snippet);
               newTryNode.setFinalizer(finallyBlock);
            }
          //  System.out.println("Resources:" + resources);
            System.out.println("Matched:" + element.getBody());
            resources.clear();
            resourcesClosed.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
