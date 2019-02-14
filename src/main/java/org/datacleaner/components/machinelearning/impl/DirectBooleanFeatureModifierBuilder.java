package org.datacleaner.components.machinelearning.impl;

import org.datacleaner.components.machinelearning.api.MLFeatureModifier;
import org.datacleaner.components.machinelearning.api.MLFeatureModifierBuilder;

public class DirectBooleanFeatureModifierBuilder implements MLFeatureModifierBuilder {

    @Override
    public void addRecordValue(Object value) {
        assert value instanceof Boolean;
    }

    @Override
    public MLFeatureModifier build() {
        return new DirectBooleanFeatureModifier();
    }

}
