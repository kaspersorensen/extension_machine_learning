package org.datacleaner.components.machinelearning.impl;

import org.datacleaner.components.convert.ConvertToNumberTransformer;
import org.datacleaner.components.machinelearning.api.MLFeatureModifier;
import org.datacleaner.components.machinelearning.api.MLFeatureModifierType;

public class DirectNumericFeatureModifier implements MLFeatureModifier {

    private static final long serialVersionUID = 1L;

    @Override
    public double[] generateFeatureValues(Object value) {
        final Number n = ConvertToNumberTransformer.transformValue(value);
        if (n == null) {
            return new double[] { 0 };
        }
        final double inRange = MLFeatureUtils.ensureFeatureInRange(n.doubleValue());
        return new double[] { inRange };
    }

    @Override
    public int getFeatureCount() {
        return 1;
    }

    @Override
    public MLFeatureModifierType getType() {
        return MLFeatureModifierType.DIRECT_NUMERIC;
    }
}
