package org.datacleaner.components.machinelearning;

import org.datacleaner.api.AnalyzerResult;
import org.datacleaner.components.machinelearning.api.MLClassifier;
import org.datacleaner.result.Crosstab;

public class MLTrainingResult implements AnalyzerResult {

    private static final long serialVersionUID = 1L;

    private final MLClassifier classifier;
    private final Crosstab<Integer> trainedRecordsConfusionMatrix;
    private final Crosstab<Integer> crossValidationConfusionMatrix;

    public MLTrainingResult(MLClassifier classifier, Crosstab<Integer> trainedRecordsConfusionMatrix,
            Crosstab<Integer> crossValidationConfusionMatrix) {
        this.classifier = classifier;
        this.trainedRecordsConfusionMatrix = trainedRecordsConfusionMatrix;
        this.crossValidationConfusionMatrix = crossValidationConfusionMatrix;
    }

    public MLClassifier getClassifier() {
        return classifier;
    }

    public Crosstab<Integer> getCrossValidationConfusionMatrix() {
        return crossValidationConfusionMatrix;
    }

    public Crosstab<Integer> getTrainedRecordsConfusionMatrix() {
        return trainedRecordsConfusionMatrix;
    }
}
