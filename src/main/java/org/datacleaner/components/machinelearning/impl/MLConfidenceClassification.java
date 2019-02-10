package org.datacleaner.components.machinelearning.impl;

import org.datacleaner.components.machinelearning.api.MLClassification;

public class MLConfidenceClassification implements MLClassification {

    private final double[] scores;

    public MLConfidenceClassification(double[] scores) {
        this.scores = scores;
    }

    @Override
    public int getBestClassificationIndex() {
        int winnerIndex = -1;
        double winnerScore = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > winnerScore) {
                winnerScore = scores[i];
                winnerIndex = i;
            }
        }
        return winnerIndex;
    }

    @Override
    public double getConfidence(int classIndex) throws IndexOutOfBoundsException {
        return scores[classIndex];
    }

}
