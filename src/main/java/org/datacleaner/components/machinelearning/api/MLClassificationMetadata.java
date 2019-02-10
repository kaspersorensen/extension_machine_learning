package org.datacleaner.components.machinelearning.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public final class MLClassificationMetadata implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Object> classifications;

    public MLClassificationMetadata(List<Object> classifications) {
        this.classifications = classifications;
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
}
