package org.datacleaner.components.machinelearning.api;

import java.io.Serializable;
import java.util.List;

public class MLClassificationTrainingOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<String> columnNames;
    private final int layerSize;
    private final int epochs;
    private final Class<?> classificationType;
    private final List<MLFeatureModifier> featureModifiers;

    public MLClassificationTrainingOptions(Class<?> classificationType, List<String> columnNames,
            List<MLFeatureModifier> featureModifiers, int epochs, int layerSize) {
        this.classificationType = classificationType;
        this.columnNames = columnNames;
        this.featureModifiers = featureModifiers;
        this.epochs = epochs;
        this.layerSize = layerSize;
    }

    public int getEpochs() {
        return epochs;
    }

    public int getLayerSize() {
        return layerSize;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<MLFeatureModifier> getFeatureModifiers() {
        return featureModifiers;
    }

    public Class<?> getClassificationType() {
        return classificationType;
    }

    @Override
    public String toString() {
        return "MLClassificationTrainingOptions[layerSize=" + layerSize + ",epochs=" + epochs + "]";
    }
}
