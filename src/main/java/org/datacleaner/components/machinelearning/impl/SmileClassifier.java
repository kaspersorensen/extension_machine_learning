package org.datacleaner.components.machinelearning.impl;

import org.datacleaner.components.machinelearning.api.MLClassification;
import org.datacleaner.components.machinelearning.api.MLClassificationMetadata;
import org.datacleaner.components.machinelearning.api.MLClassifier;

import smile.classification.Classifier;
import smile.classification.SoftClassifier;

public class SmileClassifier implements MLClassifier {

    private final Classifier<double[]> smileClassifier;
    private final MLClassificationMetadata metadata;

    public SmileClassifier(final Classifier<double[]> smileClassifier,
            MLClassificationMetadata classificationMetadata) {
        this.smileClassifier = smileClassifier;
        this.metadata = classificationMetadata;
    }

    @Override
    public MLClassification classify(double[] featureValues) {
        if (smileClassifier instanceof SoftClassifier) {
            final double[] posteriori = new double[metadata.getClassCount()];
            final SoftClassifier<double[]> softClassifier = (SoftClassifier<double[]>) smileClassifier;
            softClassifier.predict(featureValues, posteriori);

            return new MLConfidenceClassification(posteriori);
        }

        final int prediction = smileClassifier.predict(featureValues);
        return new MLSimpleClassification(prediction);
    }

    @Override
    public MLClassificationMetadata getMetadata() {
        return metadata;
    }

}
