package org.datacleaner.components.machinelearning;

import org.datacleaner.api.AnalyzerResult;
import org.datacleaner.components.machinelearning.api.MLClassifier;
import org.datacleaner.result.Crosstab;

public class MLAnalyzerResult implements AnalyzerResult {

    private static final long serialVersionUID = 1L;

    private final MLClassifier trainedClassifier;
    private final Crosstab<Integer> trainedRecordsConfusionMatrix;
    private final Crosstab<Integer> crossValidationConfusionMatrix;

    public MLAnalyzerResult(MLClassifier trainedClassifier, Crosstab<Integer> trainedRecordsConfusionMatrix,
            Crosstab<Integer> crossValidationConfusionMatrix) {
        this.trainedClassifier = trainedClassifier;
        this.trainedRecordsConfusionMatrix = trainedRecordsConfusionMatrix;
        this.crossValidationConfusionMatrix = crossValidationConfusionMatrix;
    }

    public MLClassifier getTrainedClassifier() {
        return trainedClassifier;
    }

    public Crosstab<Integer> getCrossValidationConfusionMatrix() {
        return crossValidationConfusionMatrix;
    }

    public Crosstab<Integer> getTrainedRecordsConfusionMatrix() {
        return trainedRecordsConfusionMatrix;
    }
}
