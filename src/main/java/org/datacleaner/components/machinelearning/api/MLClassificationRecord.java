package org.datacleaner.components.machinelearning.api;

public interface MLClassificationRecord {

    public Object getClassification();
    
    public Object[] getRecordValues();
}
