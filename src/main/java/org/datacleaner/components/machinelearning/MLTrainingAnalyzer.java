package org.datacleaner.components.machinelearning;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.SerializationUtils;
import org.apache.metamodel.util.CollectionUtils;
import org.apache.metamodel.util.HasNameMapper;
import org.datacleaner.api.Analyzer;
import org.datacleaner.api.Categorized;
import org.datacleaner.api.ComponentContext;
import org.datacleaner.api.Configured;
import org.datacleaner.api.Description;
import org.datacleaner.api.ExecutionLogMessage;
import org.datacleaner.api.FileProperty;
import org.datacleaner.api.FileProperty.FileAccessMode;
import org.datacleaner.api.Initialize;
import org.datacleaner.api.InputColumn;
import org.datacleaner.api.InputRow;
import org.datacleaner.api.NumberProperty;
import org.datacleaner.api.Provided;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainer;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainerCallback;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainerRecord;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainingOptions;
import org.datacleaner.components.machinelearning.api.MLClassifier;
import org.datacleaner.components.machinelearning.impl.MLClassificationTrainerRecordImpl;
import org.datacleaner.result.Crosstab;
import org.datacleaner.util.Percentage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

@Named("Classifier training")
@Categorized(MachineLearningCategory.class)
public class MLTrainingAnalyzer implements Analyzer<MLAnalyzerResult> {

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

    @Configured(required = false)
    @FileProperty(accessMode = FileAccessMode.SAVE, extension = ".model.ser")
    File saveModelToFile = new File("classifier.model.ser");

    @Inject
    @Provided
    ComponentContext componentContext;

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
        final MLClassificationTrainerRecord record =
                MLClassificationTrainerRecordImpl.of(row, classification, features);
        if (record == null) {
            return;
        }

        final int recordNumber = recordCounter.incrementAndGet();
        if (recordNumber % 100 > crossValidationSampleRate.getNominator()) {
            trainingRecords.add(record);
        } else {
            crossValidationRecords.add(record);
        }
    }

    @Override
    public MLAnalyzerResult getResult() {
        final List<String> featureNames = CollectionUtils.map(features, new HasNameMapper());
        final MLClassificationTrainingOptions options =
                new MLClassificationTrainingOptions(classification.getDataType(), featureNames, epochs, layerSize);
        final MLClassificationTrainer trainer = algorithm.createTrainer(options);
        final int epochs = options.getEpochs();
        log("Training " + algorithm.getName() + " model starting. Records=" + trainingRecords.size() + ", Features="
                + featureNames.size() + ", Epochs=" + epochs + ".");
        final MLClassifier classifier = trainer.train(trainingRecords, new MLClassificationTrainerCallback() {
            @Override
            public void epochDone(int epoch) {
                log("Training " + algorithm.getName() + " progress: Epoch " + epoch + " of " + epochs + " done.");
            }
        });

        if (saveModelToFile != null) {
            logger.info("Saving model to file: {}", saveModelToFile);
            try {
                final byte[] bytes = SerializationUtils.serialize(classifier);
                Files.write(bytes, saveModelToFile);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to save model to file: " + saveModelToFile, e);
            }
        }

        log("Trained " + algorithm.getName() + " model. Creating evaluation matrices.");

        final Crosstab<Integer> trainedRecordsConfusionMatrix =
                createConfusionMatrixCrosstab(classifier, trainingRecords);
        final Crosstab<Integer> crossValidationConfusionMatrix =
                createConfusionMatrixCrosstab(classifier, crossValidationRecords);

        return new MLAnalyzerResult(classifier, trainedRecordsConfusionMatrix, crossValidationConfusionMatrix);
    }

    private void log(String string) {
        componentContext.publishMessage(new ExecutionLogMessage(string));
    }

    private static Crosstab<Integer> createConfusionMatrixCrosstab(MLClassifier classifier,
            Collection<MLClassificationTrainerRecord> records) {
        final MLConfusionMatrixBuilder builder = new MLConfusionMatrixBuilder(classifier);
        for (MLClassificationTrainerRecord record : records) {
            builder.append(record);
        }
        return builder.build();
    }
}
