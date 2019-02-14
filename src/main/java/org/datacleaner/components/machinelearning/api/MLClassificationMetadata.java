package org.datacleaner.components.machinelearning.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public final class MLClassificationMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Class<?> classificationType;
    private final List<Object> classifications;
    private final List<String> columnNames;
    private final List<MLFeatureModifier> featureModifiers;

    public MLClassificationMetadata(Class<?> classificationType, List<Object> classifications, List<String> columnNames,
            List<MLFeatureModifier> featureModifiers) {
        this.classificationType = classificationType;
        this.classifications = classifications;
        this.columnNames = columnNames;
        this.featureModifiers = featureModifiers;
    }

    public int getClassCount() {
        return classifications.size();
    }

    public Object getClassification(int index) {
        return classifications.get(index);
    }

    public List<Object> getClassifications() {
        return Collections.unmodifiableList(classifications);
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public List<String> getColumnNames() {
        return Collections.unmodifiableList(columnNames);
    }

    public List<MLFeatureModifier> getFeatureModifiers() {
        return Collections.unmodifiableList(featureModifiers);
    }

    public Class<?> getClassificationType() {
        return classificationType;
    }
}
