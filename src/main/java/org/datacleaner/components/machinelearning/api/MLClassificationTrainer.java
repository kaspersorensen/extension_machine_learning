package org.datacleaner.components.machinelearning.api;

public interface MLClassificationTrainer {

    MLClassifier train(Iterable<MLClassificationTrainerRecord> data, MLClassificationTrainerCallback callback);
}
