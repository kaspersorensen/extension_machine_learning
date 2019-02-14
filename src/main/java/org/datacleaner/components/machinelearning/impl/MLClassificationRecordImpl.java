package org.datacleaner.components.machinelearning.impl;

import java.util.List;

import org.datacleaner.api.InputColumn;
import org.datacleaner.api.InputRow;
import org.datacleaner.components.machinelearning.api.MLClassificationRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MLClassificationRecordImpl implements MLClassificationRecord {

    private static final Logger logger = LoggerFactory.getLogger(MLClassificationRecordImpl.class);

    public static MLClassificationRecord forEvaluation(InputRow row, InputColumn<?>[] featureColumns) {
        final List<Object> values = row.getValues(featureColumns);
        final MLClassificationRecord record = new MLClassificationRecordImpl(null, values.toArray(Object[]::new));
        return record;
    }

    public static MLClassificationRecord forEvaluation(Object[] values) {
        final MLClassificationRecord record = new MLClassificationRecordImpl(null, values);
        return record;
    }

    public static MLClassificationRecord forTraining(InputRow row, InputColumn<?> classification,
            InputColumn<?>[] featureColumns) {
        final Object classificationValue = row.getValue(classification);
        if (classificationValue == null) {
            logger.warn("Encountered null classification value, skipping row: {}", row);
            return null;
        }
        final List<Object> values = row.getValues(featureColumns);
        final MLClassificationRecord record =
                new MLClassificationRecordImpl(classificationValue, values.toArray(Object[]::new));
        return record;
    }

    private final Object classification;
    private final Object[] featureValues;

    private MLClassificationRecordImpl(Object classification, Object[] recordValues) {
        this.classification = classification;
        this.featureValues = recordValues;
    }

    @Override
    public Object getClassification() {
        return classification;
    }

    @Override
    public Object[] getRecordValues() {
        return featureValues;
    }
}
