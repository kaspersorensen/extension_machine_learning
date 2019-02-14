package org.datacleaner.components.machinelearning.impl;

import org.datacleaner.components.machinelearning.api.MLFeatureModifier;
import org.datacleaner.components.machinelearning.api.MLFeatureModifierBuilder;

public class DirectNumericFeatureModifierBuilder implements MLFeatureModifierBuilder {

    @Override
    public void addRecordValue(Object value) {
        assert value instanceof Number;
    }

    @Override
    public MLFeatureModifier build() {
        return new DirectNumericFeatureModifier();
    }

}
