package org.datacleaner.components.machinelearning.api;

public interface MLFeatureModifierBuilder {

    void addRecordValue(Object value);
    
    MLFeatureModifier build();
}
