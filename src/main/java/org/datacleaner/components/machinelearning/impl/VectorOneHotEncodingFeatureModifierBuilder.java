package org.datacleaner.components.machinelearning.impl;

import java.util.Set;
import java.util.TreeSet;

import org.datacleaner.components.machinelearning.api.MLFeatureModifier;
import org.datacleaner.components.machinelearning.api.MLFeatureModifierBuilder;

public class VectorOneHotEncodingFeatureModifierBuilder implements MLFeatureModifierBuilder {

    private final Set<String> values = new TreeSet<>();

    @Override
    public void addRecordValue(Object value) {
        final String v = VectorOneHotEncodingFeatureModifier.normalize(value);
        synchronized (this) {
            values.add(v);
        }
    }

    @Override
    public MLFeatureModifier build() {
        return new VectorOneHotEncodingFeatureModifier(values);
    }

}
