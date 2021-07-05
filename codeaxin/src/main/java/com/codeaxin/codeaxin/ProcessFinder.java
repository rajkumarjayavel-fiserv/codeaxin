package com.codeaxin.codeaxin;

import spoon.processing.AbstractProcessor;

public class ProcessFinder {

    public AbstractProcessor findProcessor(BotAttributes botAttributes){
        if((ProcessorMapper.VULENARBILITY_TYPE.UnReleaseResource.name()+":"+ProcessorMapper.SUB_VULENARBILITY_TYPE.file.name()).equalsIgnoreCase(botAttributes.getVulenaribilityType())){
          return  ProcessorMapper.UNRELEASED_RESOURCES_FILE.getProcessor(botAttributes);

        }
        else if((ProcessorMapper.VULENARBILITY_TYPE.UnReleaseResource.name()+":"+ProcessorMapper.SUB_VULENARBILITY_TYPE.database.name()).equalsIgnoreCase(botAttributes.getVulenaribilityType())){
            return  ProcessorMapper.UNRELEASED_RESOURCES_DB.getProcessor(botAttributes);

        }
        return null;
    }
}
