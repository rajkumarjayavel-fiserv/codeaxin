package com.codeaxin.codeaxin;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

public interface ResourceTransformation<E extends CtElement>{
    public void  executeUnreleasedResourceTransfrom(E element, ResourceIdentifier resourceIdentifier);
}
