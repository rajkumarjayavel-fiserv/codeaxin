package com.codeaxin.codeaxin;

import lombok.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spoon.reflect.reference.CtTypeReference;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BotAttributes {
    private String vulnerableFile;
    private String methodName;
    private String vulenaribilityType;

    @Override
    public String toString() {
        return "BotAttributes{" +
                "vulnerableFile='" + vulnerableFile + '\'' +
                ", methodName='" + methodName + '\'' +
                ", vulenaribilityType='" + vulenaribilityType + '\'' +
                ", vulenaribilityName='" + vulenaribilityName + '\'' +
                ", lineNumber=" + lineNumber +
                '}';
    }

    private String vulenaribilityName;
    private int lineNumber;
    private String closeableMethod="close";
    private String targetDirectory;

    private boolean copyResources;
    private boolean autoImports=true;

}
