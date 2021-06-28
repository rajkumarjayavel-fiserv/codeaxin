package com.codeaxin.codeaxin;

import lombok.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spoon.reflect.code.CtLocalVariable;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ResourceIdentifier {
   private List<CtLocalVariable> resources = new ArrayList<>();
   private boolean isExecuted=false;
   private List<String> resourcesClosed = new ArrayList<String>();
   private String closeableMethodName="close";
}
