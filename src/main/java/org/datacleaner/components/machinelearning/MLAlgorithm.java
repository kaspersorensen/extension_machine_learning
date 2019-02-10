package org.datacleaner.components.machinelearning;

import org.apache.metamodel.util.HasName;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainer;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainingOptions;
import org.datacleaner.components.machinelearning.impl.RandomForestClassificationTrainer;
import org.datacleaner.components.machinelearning.impl.SvmClasificationTrainer;

public enum MLAlgorithm implements HasName {

    RANDOM_FOREST("Random Forest"),

    SVM("Support Vector Machine")

    ;

    private final String name;

    private MLAlgorithm(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    MLClassificationTrainer createTrainer(MLClassificationTrainingOptions trainingOptions) {
        switch (this) {
        case RANDOM_FOREST:
            return new RandomForestClassificationTrainer(trainingOptions);
        case SVM:
            return new SvmClasificationTrainer(trainingOptions);
        default:
            throw new UnsupportedOperationException();
        }
    }
}
