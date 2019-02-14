package org.datacleaner.components.machinelearning.api;

import java.io.Serializable;

public interface MLClassifier extends Serializable {

    MLClassificationMetadata getMetadata();

    MLClassification classify(MLClassificationRecord record);

    MLClassification classify(double[] featureValues);
}
