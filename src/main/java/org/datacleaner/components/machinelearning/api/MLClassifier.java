package org.datacleaner.components.machinelearning.api;

public interface MLClassifier {
    
    MLClassificationMetadata getMetadata();

    MLClassification classify(double[] featureValues);
}
