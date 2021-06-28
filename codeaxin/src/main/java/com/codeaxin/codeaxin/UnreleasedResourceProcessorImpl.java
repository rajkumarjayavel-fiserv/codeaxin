package com.codeaxin.codeaxin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtMethod;

@Component
@Conditional(BotExecutor.class)
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UnreleasedResourceProcessorImpl extends AbstractProcessor<CtMethod> {

    @Autowired
    private  ResourceIdentifier resourceIdentifier;
    @Autowired
    private BotAttributes botAttributes;
    @Autowired
    private DetectResources detectResources;
    @Autowired
    private ResourceTransformation resourceTransformation;

    UnreleasedResourceProcessorImpl(BotAttributes botAttributes, DetectResources resources, ResourceTransformation resourceTransformation){
      this.botAttributes = botAttributes;
      this.detectResources=resources;
      this.resourceTransformation=resourceTransformation;
    }


    @Override
    public boolean isToBeProcessed(CtMethod element) {
        resourceIdentifier=detectResources.identifyResources(element, botAttributes);
        return resourceIdentifier.isExecuted();
    }

    @Override
    public void process(CtMethod element) {
            resourceTransformation.executeUnreleasedResourceTransfrom(element, resourceIdentifier);

    }


}
