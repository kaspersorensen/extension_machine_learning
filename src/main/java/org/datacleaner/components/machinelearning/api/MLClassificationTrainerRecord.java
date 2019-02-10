package org.datacleaner.components.machinelearning.api;

public interface MLClassificationTrainerRecord {

    public Object getClassification();
    
    public double[] getFeatureValues();
}
