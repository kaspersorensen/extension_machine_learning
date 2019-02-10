package org.datacleaner.components.machinelearning.impl;

import org.datacleaner.components.machinelearning.api.MLClassificationTrainerRecord;

public final class MLClassificationTrainerRecordImpl implements MLClassificationTrainerRecord {

    private final Object classification;
    private final double[] featureValues;

    public MLClassificationTrainerRecordImpl(Object classification, double[] featureValues) {
        this.classification = classification;
        this.featureValues = featureValues;
    }

    @Override
    public Object getClassification() {
        return classification;
    }

    @Override
    public double[] getFeatureValues() {
        return featureValues;
    }
}
