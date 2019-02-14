package org.datacleaner.components.machinelearning.impl;

import java.util.Collection;
import java.util.List;

import org.datacleaner.components.machinelearning.api.MLClassificationRecord;
import org.datacleaner.components.machinelearning.api.MLFeatureModifier;

public class MLFeatureUtils {

    /**
     * Ensures that a feature value is in the valid range (from 0 to 1).
     * 
     * @param scaled
     * @return
     */
    public static double ensureFeatureInRange(double v) {
        return Math.max(0d, Math.min(1d, v));
    }

    public static double[] generateFeatureValues(MLClassificationRecord record,
            List<MLFeatureModifier> featureModifiers) {
        final Object[] recordValues = record.getRecordValues();
        assert featureModifiers.size() == recordValues.length;

        final double[] featureValues = new double[getFeatureCount(featureModifiers)];

        int offset = 0;
        for (int i = 0; i < recordValues.length; i++) {
            final Object value = recordValues[i];
            final MLFeatureModifier featureModifier = featureModifiers.get(i);
            final double[] vector = featureModifier.generateFeatureValues(value);
            System.arraycopy(vector, 0, featureValues, offset, vector.length);
            offset += vector.length;
        }
        return featureValues;
    }

    private static int getFeatureCount(Collection<MLFeatureModifier> featureModifiers) {
        return featureModifiers.stream().mapToInt(f -> f.getFeatureCount()).sum();
    }
}
