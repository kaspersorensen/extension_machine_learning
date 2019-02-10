package org.datacleaner.components.machinelearning.impl;

import java.util.ArrayList;
import java.util.List;

import org.datacleaner.components.machinelearning.api.MLClassificationMetadata;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainer;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainerRecord;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainingOptions;
import org.datacleaner.components.machinelearning.api.MLClassifier;

import smile.classification.SVM;
import smile.math.kernel.GaussianKernel;

public class SvmClasificationTrainer implements MLClassificationTrainer {

    private final MLClassificationTrainingOptions trainingOptions;

    public SvmClasificationTrainer(MLClassificationTrainingOptions trainingOptions) {
        this.trainingOptions = trainingOptions;
    }

    @Override
    public MLClassifier train(Iterable<MLClassificationTrainerRecord> data) {
        final int numClasses = trainingOptions.getEpochs();
        final int epochs = trainingOptions.getEpochs();

        // TODO: Consider hyper parameters
        final SVM<double[]> svm =
                new SVM<double[]>(new GaussianKernel(8.0), 5.0, numClasses, SVM.Multiclass.ONE_VS_ONE);

        final List<double[]> trainingInstances = new ArrayList<>();
        final List<Integer> responseVariables = new ArrayList<>();
        final List<Object> classifications = new ArrayList<>();

        for (MLClassificationTrainerRecord record : data) {
            final Object classification = record.getClassification();

            int classificationIndex = classifications.indexOf(classification);
            if (classificationIndex == -1) {
                classifications.add(classification);
                classificationIndex = classifications.size() - 1;
            }

            final double[] features = record.getFeatureValues();

            trainingInstances.add(features);
            responseVariables.add(classificationIndex);
        }

        final double[][] x = trainingInstances.toArray(new double[trainingInstances.size()][]);
        final int[] y = responseVariables.stream().mapToInt(i -> i).toArray();

        for (int j = 0; j < epochs; j++) {
            svm.learn(x, y);
        }
        svm.finish();
        svm.trainPlattScaling(x, y);

        final MLClassificationMetadata metadata = new MLClassificationMetadata(classifications);
        return new SmileClassifier(svm, metadata);
    }

}
