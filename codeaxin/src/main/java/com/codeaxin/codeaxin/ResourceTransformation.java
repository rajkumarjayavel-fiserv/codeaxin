package com.codeaxin.codeaxin;

import spoon.reflect.declaration.CtElement;

public interface ResourceTransformation<E extends CtElement>{
    public void  executeUnreleasedResourceTransfrom(E element, ResourceIdentifier resourceIdentifier);
}
