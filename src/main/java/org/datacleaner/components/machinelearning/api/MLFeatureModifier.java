package org.datacleaner.components.machinelearning.api;

import java.io.Serializable;

public interface MLFeatureModifier extends Serializable {

    double[] generateFeatureValues(Object value);

    int getFeatureCount();

}
