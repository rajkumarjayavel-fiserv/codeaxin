package com.codeaxin.codeaxin;

import org.springframework.stereotype.Service;
import spoon.processing.AbstractProcessor;

@Service
public enum ProcessorMapper {


    UNRELEASED_RESOURCES_FILE{
        @Override
        AbstractProcessor getProcessor(BotAttributes botAttributes) {

            DetectResources detectResources=new UnReleasedResourceDetectionImpl();
            ResourceTransformation resourceTransformation=new UnreleasedResourceTransformationImpl();
            UnreleasedResourceProcessorImpl processor=new UnreleasedResourceProcessorImpl(botAttributes,detectResources,resourceTransformation);
            return processor;
        }
    },
    UNRELEASED_RESOURCES_DB{
        @Override
        AbstractProcessor getProcessor(BotAttributes botAttributes) {

            DetectResources detectResources=new UnReleasedResourceDetectionImpl();
            ResourceTransformation resourceTransformation=new UnreleasedDBResourceTransformationImpl();
            UnreleasedResourceProcessorImpl processor=new UnreleasedResourceProcessorImpl(botAttributes,detectResources,resourceTransformation);
            return processor;
        }
    };
   enum VULENARBILITY_TYPE{
       UnReleaseResource,
       }
    enum SUB_VULENARBILITY_TYPE{
        database,
        file,
    }
   // @Autowired UnreleasedResourceProcessorImpl processor;
    abstract AbstractProcessor getProcessor(BotAttributes botAttributes);

}
