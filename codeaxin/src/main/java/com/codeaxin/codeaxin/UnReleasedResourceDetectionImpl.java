package com.codeaxin.codeaxin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.logging.LoggerGroup;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtTryWithResource;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UnReleasedResourceDetectionImpl implements DetectResources<CtMethod>{
    private static final Logger LOGGER = LogManager.getLogger(UnReleasedResourceDetectionImpl.class);

    private final List<String> privateApiPackages = Arrays.asList("java.sql.Connection", "java.io.FileInputStream", "java.io.InputStream","java.io.OutputStream");

    @Override
    public ResourceIdentifier identifyResources(CtMethod element, BotAttributes attributes) {
        LOGGER.debug("Attr:"+attributes);
        ResourceIdentifier resourceIdentifier = new ResourceIdentifier();
        List<CtLocalVariable> resources = new ArrayList<>();
        List<String> resourcesClosed = new ArrayList<String>();
        List<CtElement> lineElement=new ArrayList<>();
        if(attributes.getLineNumber()>0){
            try {
               // resources= lineElement.stream().map(e->e.getElements(new TypeFilter(CtLocalVariable.class))).findAny().orElseThrow(() -> new Exception("Line Number is not matched"));
                for(CtLocalVariable ctLocalVariable1:element.getElements(new TypeFilter<>(CtLocalVariable.class))){
                    System.out.println(ctLocalVariable1);
                    System.out.println(ctLocalVariable1.getPosition().getLine());
                }
                CtLocalVariable ctLocalVariable=element.getElements(new TypeFilter<>(CtLocalVariable.class)).stream().filter(e->e.getPosition().getLine()== attributes.getLineNumber()).findAny().orElseThrow(() -> new Exception("Line Number is not matched"));
                 resources.add(ctLocalVariable);
            } catch (Exception e) {
                LOGGER.error("Exceptiom in detect Resources",e);
                e.printStackTrace();
            }
        }
        else{
            resources = defaultTracer(element, attributes, resourceIdentifier, resources, resourcesClosed);
        }
        resourceIdentifier.setExecuted(resources.size() != resourcesClosed.size() && resources.size() > 0);
        resourceIdentifier.setResources(resources);
        resourceIdentifier.setResourcesClosed(resourcesClosed);
        return  resourceIdentifier;
    }

    private List<CtLocalVariable> defaultTracer(CtMethod element, BotAttributes attributes, ResourceIdentifier resourceIdentifier, List<CtLocalVariable> resources, List<String> resourcesClosed) {
        if (Optional.ofNullable(element.getBody()).isPresent() && element.getBody().getElements(new TypeFilter(CtLocalVariable.class)).size() > 0) {
            resources = element.getBody().getElements(new TypeFilter(CtLocalVariable.class));
            resources.removeIf(e -> e.getParent(new TypeFilter(CtTryWithResource.class)) != null);
            resources = element.getBody().getElements(new TypeFilter(CtLocalVariable.class));
            if (Optional.ofNullable(resources).isPresent()) {
                resources = resources.stream().filter(e -> privateApiPackages.contains(e.getType().getQualifiedName())).distinct().
                        collect(Collectors.toList());
                //Filter the CtVariableRead
                List<CtInvocation> CtInvocations = element.getBody().getElements(new TypeFilter(CtInvocation.class));
                CtInvocations = CtInvocations.stream().filter(e -> e.getTarget() instanceof CtVariableRead).collect(Collectors.toList());
                List<CtTryWithResource> ctTryWithResources = element.getElements(new TypeFilter<>(CtTryWithResource.class));
                //Auto-closeable resources
                for (CtTryWithResource ctLocalVariable : ctTryWithResources) {
                    resourcesClosed.addAll(ctLocalVariable.getResources().stream().map(e -> e.getSimpleName()).collect(Collectors.toList()));
                }
                boolean isClosed = false;
                boolean isAvailable = false;

                //Scan the statement
                for (CtInvocation ctInvocation : CtInvocations) {
                    //  System.out.println("parent:"+ctInvocation.getParent());
                    CtVariableRead read = (CtVariableRead) ctInvocation.getTarget();
                    for (CtLocalVariable variable : resources) {
                        isAvailable = read.getVariable().getSimpleName().matches(variable.getSimpleName());
                        if (isAvailable) {
                            isClosed = ctInvocation.getExecutable().getSimpleName().matches(attributes.getCloseableMethod());
                        }
                        if (isClosed && isAvailable)
                            resourcesClosed.add(variable.getSimpleName());

                    }
                    resourceIdentifier.setExecuted(resources.size() != resourcesClosed.size() && resources.size() > 0);
                    resourceIdentifier.setResources(resources);
                    resourceIdentifier.setResourcesClosed(resourcesClosed);
                    read = null;
                    isAvailable = false;
                    isClosed = false;
                }

            }
        }
        return resources;
    }
}
