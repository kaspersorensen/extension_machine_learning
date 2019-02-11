package org.datacleaner.components.machinelearning.api;

import java.io.Serializable;
import java.util.List;

public class MLClassificationTrainingOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<String> featureNames;
    private final int layerSize;
    private final int epochs;
    private final Class<?> classificationType;

    public MLClassificationTrainingOptions(Class<?> classificationType, List<String> featureNames, int epochs,
            int layerSize) {
        this.classificationType = classificationType;
        this.featureNames = featureNames;
        this.epochs = epochs;
        this.layerSize = layerSize;
    }

    public int getEpochs() {
        return epochs;
    }

    public int getLayerSize() {
        return layerSize;
    }

    public List<String> getFeatureNames() {
        return featureNames;
    }

    public Class<?> getClassificationType() {
        return classificationType;
    }

    @Override
    public String toString() {
        return "MLClassificationTrainingOptions[layerSize=" + layerSize + ",epochs=" + epochs + "]";
    }
}
