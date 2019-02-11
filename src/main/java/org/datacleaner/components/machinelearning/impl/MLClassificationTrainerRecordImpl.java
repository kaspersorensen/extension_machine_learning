package org.datacleaner.components.machinelearning.impl;

import org.datacleaner.api.InputColumn;
import org.datacleaner.api.InputRow;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MLClassificationTrainerRecordImpl implements MLClassificationTrainerRecord {

    private static final Logger logger = LoggerFactory.getLogger(MLClassificationTrainerRecordImpl.class);

    public static MLClassificationTrainerRecord of(InputRow row, InputColumn<?> classification,
            InputColumn<Number>[] features) {
        final Object classificationValue = row.getValue(classification);
        if (classificationValue == null) {
            logger.warn("Encountered null classification value, skipping row: {}", row);
            return null;
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
        return record;
    }

    private final Object classification;
    private final double[] featureValues;

    public MLClassificationTrainerRecordImpl(Object classification, double[] featureValues) {
        this.classification = classification;
        this.featureValues = featureValues;
    }

    @Override
    public Object getClassification() {
        return classification;
    }

    @Override
    public double[] getFeatureValues() {
        return featureValues;
    }
}
