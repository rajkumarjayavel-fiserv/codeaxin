package com.codeaxin.codeaxin;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

public interface DetectResources<E extends CtElement> {
    public ResourceIdentifier identifyResources(CtMethod element, BotAttributes attributes);
}
