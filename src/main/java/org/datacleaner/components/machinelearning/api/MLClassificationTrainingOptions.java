package org.datacleaner.components.machinelearning.api;

import java.io.Serializable;

public class MLClassificationTrainingOptions implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final int layerSize;
    private final int epochs;

    public MLClassificationTrainingOptions(int epochs, int layerSize) {
        this.epochs = epochs;
        this.layerSize = layerSize;
    }

    public int getEpochs() {
        return epochs;
    }

    public int getLayerSize() {
        return layerSize;
    }

    @Override
    public String toString() {
        return "MLClassificationTrainingOptions[layerSize=" + layerSize + ",epochs=" + epochs + "]";
    }
}
