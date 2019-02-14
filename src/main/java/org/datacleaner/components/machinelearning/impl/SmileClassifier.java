package org.datacleaner.components.machinelearning.impl;

import org.apache.metamodel.util.SerializableRef;
import org.datacleaner.components.machinelearning.api.MLClassification;
import org.datacleaner.components.machinelearning.api.MLClassificationMetadata;

import smile.classification.Classifier;
import smile.classification.SoftClassifier;

public class SmileClassifier extends AbstractClassifier {

    private static final long serialVersionUID = 1L;
    
    private final SerializableRef<Classifier<double[]>> smileClassifierRef;

    public SmileClassifier(final Classifier<double[]> smileClassifier,
            MLClassificationMetadata classificationMetadata) {
        super(classificationMetadata);
        this.smileClassifierRef = new SerializableRef<>(smileClassifier);
    }

    @Override
    public MLClassification classify(double[] featureValues) {
        final Classifier<double[]> classifier = smileClassifierRef.get();
        if (classifier instanceof SoftClassifier) {
            final SoftClassifier<double[]> softClassifier = (SoftClassifier<double[]>) classifier;

            final double[] posteriori = new double[getMetadata().getClassCount()];
            softClassifier.predict(featureValues, posteriori);
            return new MLConfidenceClassification(posteriori);
        }

        final int prediction = classifier.predict(featureValues);
        return new MLSimpleClassification(prediction);
    }
}
