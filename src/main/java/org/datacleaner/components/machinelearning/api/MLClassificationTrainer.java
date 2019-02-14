package org.datacleaner.components.machinelearning.api;

import java.util.List;

public interface MLClassificationTrainer {

    MLClassifier train(Iterable<MLClassificationRecord> data, List<MLFeatureModifier> featureModifiers,
            MLClassificationTrainerCallback callback);
}
