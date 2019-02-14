package org.datacleaner.components.machinelearning.impl;

import java.util.List;

import org.datacleaner.components.machinelearning.api.MLClassification;
import org.datacleaner.components.machinelearning.api.MLClassificationMetadata;
import org.datacleaner.components.machinelearning.api.MLClassificationRecord;
import org.datacleaner.components.machinelearning.api.MLClassifier;
import org.datacleaner.components.machinelearning.api.MLFeatureModifier;

public abstract class AbstractClassifier implements MLClassifier {

    private static final long serialVersionUID = 1L;

    private final MLClassificationMetadata metadata;

    public AbstractClassifier(MLClassificationMetadata classificationMetadata) {
        this.metadata = classificationMetadata;
    }

    @Override
    public MLClassification classify(MLClassificationRecord record) {
        final List<MLFeatureModifier> featureModifiers = metadata.getFeatureModifiers();
        final double[] featureValues = MLFeatureUtils.generateFeatureValues(record, featureModifiers);
        return classify(featureValues);
    }

    @Override
    public MLClassificationMetadata getMetadata() {
        return metadata;
    }

}
