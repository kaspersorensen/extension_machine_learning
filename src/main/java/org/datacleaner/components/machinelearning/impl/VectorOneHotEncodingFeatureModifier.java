package org.datacleaner.components.machinelearning.impl;

import java.util.Collection;
import java.util.Map;

import org.datacleaner.components.machinelearning.api.MLFeatureModifier;

import com.google.common.collect.Maps;

public class VectorOneHotEncodingFeatureModifier implements MLFeatureModifier {

    private static final long serialVersionUID = 1L;

    public static String normalize(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString().trim().toLowerCase();
    }

    private final Map<String, Integer> values;

    public VectorOneHotEncodingFeatureModifier(Collection<String> values) {
        this.values = Maps.newHashMapWithExpectedSize(values.size());
        int index = 0;
        for (String value : values) {
            this.values.put(value, index);
            index++;
        }
    }

    @Override
    public double[] generateFeatureValues(Object value) {
        final double[] result = new double[getFeatureCount()];
        final String v = normalize(value);
        final Integer index = values.get(v);
        if (index != null) {
            result[index] = 1;
        }
        return result;
    }

    @Override
    public int getFeatureCount() {
        return values.size();
    }

}
