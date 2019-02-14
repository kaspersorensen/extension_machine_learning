package org.datacleaner.components.machinelearning.impl;

import org.datacleaner.components.convert.ConvertToNumberTransformer;
import org.datacleaner.components.machinelearning.api.MLFeatureModifier;

public class ScaledMinMaxFeatureModifier implements MLFeatureModifier {

    private static final long serialVersionUID = 1L;

    private final double min;
    private final double max;

    public ScaledMinMaxFeatureModifier(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public double[] generateFeatureValues(Object value) {
        final Number v = ConvertToNumberTransformer.transformValue(value);
        if (v == null) {
            return new double[] { 0 };
        }
        final double scaled = (v.doubleValue() - min) / (max - min);
        final double inRange = MLFeatureUtils.ensureFeatureInRange(scaled);
        return new double[] { inRange };
    }

    @Override
    public int getFeatureCount() {
        return 1;
    }

}
