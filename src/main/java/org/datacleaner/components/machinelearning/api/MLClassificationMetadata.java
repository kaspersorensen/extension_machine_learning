package org.datacleaner.components.machinelearning.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public final class MLClassificationMetadata implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final Class<?> classificationType;
    private final List<Object> classifications;
    private final List<String> featureNames;

    public MLClassificationMetadata(Class<?> classificationType, List<Object> classifications, List<String> featureNames) {
        this.classificationType = classificationType;
        this.classifications = classifications;
        this.featureNames = featureNames;
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

    public int getFeatureCount() {
        return featureNames.size();
    }
    
    public List<String> getFeatureNames() {
        return Collections.unmodifiableList(featureNames);
    }

    public Class<?> getClassificationType() {
        return classificationType;
    }
}
