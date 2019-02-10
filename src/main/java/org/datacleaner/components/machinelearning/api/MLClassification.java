package org.datacleaner.components.machinelearning.api;

public interface MLClassification {

    /**
     * Gets the index of the classification with the highest confidence/score.
     * 
     * @return
     */
    int getBestClassificationIndex();

    /**
     * Gets the confidence/score of a particular classification.
     * 
     * @param classIndex
     * @return
     * @throws IndexOutOfBoundsException
     */
    double getConfidence(int classIndex) throws IndexOutOfBoundsException;
}
