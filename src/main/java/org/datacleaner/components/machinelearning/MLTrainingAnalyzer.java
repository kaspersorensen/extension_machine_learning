package org.datacleaner.components.machinelearning;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.datacleaner.api.Analyzer;
import org.datacleaner.api.Configured;
import org.datacleaner.api.Description;
import org.datacleaner.api.Initialize;
import org.datacleaner.api.InputColumn;
import org.datacleaner.api.InputRow;
import org.datacleaner.api.NumberProperty;
import org.datacleaner.components.machinelearning.api.MLClassification;
import org.datacleaner.components.machinelearning.api.MLClassificationMetadata;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainer;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainerRecord;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainingOptions;
import org.datacleaner.components.machinelearning.api.MLClassifier;
import org.datacleaner.components.machinelearning.impl.MLClassificationTrainerRecordImpl;
import org.datacleaner.result.Crosstab;
import org.datacleaner.result.CrosstabDimension;
import org.datacleaner.result.CrosstabNavigator;
import org.datacleaner.util.Percentage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("Classifier training")
public class MLTrainingAnalyzer implements Analyzer<MLTrainingResult> {

    private static final Logger logger = LoggerFactory.getLogger(MLTrainingAnalyzer.class);

    @Configured
    InputColumn<?> classification;

    @Configured
    InputColumn<Number>[] features;

    @Configured
    @Description("Determine how much (if any) of the records should be used for cross-validation.")
    @NumberProperty(negative = false)
    Percentage crossValidationSampleRate = new Percentage(10);

    @Configured
    @NumberProperty(negative = false, zero = false)
    int epochs = 10;

    @Configured
    @NumberProperty(negative = false, zero = false)
    int layerSize = 64;

    @Configured
    MLAlgorithm algorithm = MLAlgorithm.RANDOM_FOREST;

    private AtomicInteger recordCounter;
    private Collection<MLClassificationTrainerRecord> trainingRecords;
    private Collection<MLClassificationTrainerRecord> crossValidationRecords;

    @Initialize
    public void init() {
        recordCounter = new AtomicInteger();
        trainingRecords = new ConcurrentLinkedQueue<>();
        crossValidationRecords = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run(InputRow row, int distinctCount) {
        final Object classificationValue = row.getValue(classification);
        if (classificationValue == null) {
            logger.warn("Encountered null classification value, skipping row: {}", row);
        }
        final double[] featureValues = new double[features.length];
        for (int i = 0; i < featureValues.length; i++) {
            final Number featureValue = row.getValue(features[i]);
            if (featureValue == null) {
                logger.warn("Encountered <null> {} value, defaulting to 0 in row: {}", features[i].getName(), row);
                featureValues[i] = 0;
            } else {
                featureValues[i] = featureValue.doubleValue();
            }
        }
        final MLClassificationTrainerRecord record =
                new MLClassificationTrainerRecordImpl(classificationValue, featureValues);

        final int recordNumber = recordCounter.incrementAndGet();
        if (recordNumber % 100 > crossValidationSampleRate.getNominator()) {
            trainingRecords.add(record);
        } else {
            crossValidationRecords.add(record);
        }
    }

    @Override
    public MLTrainingResult getResult() {
        final MLClassificationTrainingOptions options = new MLClassificationTrainingOptions(epochs, layerSize);
        final MLClassificationTrainer trainer = algorithm.createTrainer(options);
        logger.info("Starting training of {} with options {}", algorithm, options);
        final MLClassifier classifier = trainer.train(trainingRecords);

        final Crosstab<Integer> trainedRecordsConfusionMatrix =
                createConfusionMatrixCrosstab(classifier, trainingRecords);
        final Crosstab<Integer> crossValidationConfusionMatrix =
                createConfusionMatrixCrosstab(classifier, crossValidationRecords);

        return new MLTrainingResult(classifier, trainedRecordsConfusionMatrix, crossValidationConfusionMatrix);
    }

    private static Crosstab<Integer> createConfusionMatrixCrosstab(MLClassifier classifier,
            Collection<MLClassificationTrainerRecord> records) {
        final MLClassificationMetadata metadata = classifier.getMetadata();
        final List<String> classificationLabels = metadata.getClassifications().stream()
                .map(MLTrainingAnalyzer::getClassificationLabel).collect(Collectors.toList());

        final Crosstab<Integer> crosstab = new Crosstab<>(Integer.class, "Expected", "Actual");
        final CrosstabDimension expectedDimension = crosstab.getDimension(0);
        final CrosstabDimension actualDimension = crosstab.getDimension(1);
        expectedDimension.addCategories(classificationLabels);
        actualDimension.addCategories(classificationLabels);

        for (MLClassificationTrainerRecord crossValidationRecord : records) {
            final MLClassification result = classifier.classify(crossValidationRecord.getFeatureValues());
            final String actual = getClassificationLabel(metadata.getClassification(result.getBestClassificationIndex()));
            final String expected = getClassificationLabel(crossValidationRecord.getClassification());

            final CrosstabNavigator<Integer> crosstabPath =
                    crosstab.navigate().where(expectedDimension, expected).where(actualDimension, actual);
            final Integer valueBefore = crosstabPath.get();
            if (valueBefore == null) {
                crosstabPath.put(1);
            } else {
                crosstabPath.put(valueBefore.intValue() + 1);
            }
        }
        return crosstab;
    }

    private static String getClassificationLabel(Object classification) {
        return classification.toString();
    }
}
