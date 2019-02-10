package org.datacleaner.components.machinelearning.impl;

import org.datacleaner.components.machinelearning.api.MLClassification;

public class MLSimpleClassification implements MLClassification {

    private final int prediction;

    public MLSimpleClassification(int prediction) {
        this.prediction = prediction;
    }

    @Override
    public int getBestClassificationIndex() {
        return prediction;
    }

    @Override
    public double getConfidence(int classIndex) throws IndexOutOfBoundsException {
        if (classIndex == prediction) {
            return 1;
        }
        return 0;
    }

}
