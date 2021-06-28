package com.codeaxin.codeaxin;

import org.springframework.beans.factory.annotation.Autowired;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtMethod;

public class UnreleasedResourceProcessorImpl extends AbstractProcessor<CtMethod> {

    @Autowired
    private  ResourceIdentifier resourceIdentifier;
    @Autowired
    private BotAttributes botAttributes;
    private DetectResources detectResources;
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
