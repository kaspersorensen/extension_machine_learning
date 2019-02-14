package org.datacleaner.components.machinelearning.impl;

import org.datacleaner.components.convert.ConvertToBooleanTransformer;
import org.datacleaner.components.machinelearning.api.MLFeatureModifier;

public class DirectBooleanFeatureModifier implements MLFeatureModifier {

    private static final long serialVersionUID = 1L;

    @Override
    public double[] generateFeatureValues(Object value) {
        final Boolean b = ConvertToBooleanTransformer.transformValue(value);
        if (b == null || !b.booleanValue()) {
            return new double[] { 0 };
        }
        return new double[] { 1 };
    }

    @Override
    public int getFeatureCount() {
        return 1;
    }

}
