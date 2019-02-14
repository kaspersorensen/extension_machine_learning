package org.datacleaner.components.machinelearning.api;

public interface MLFeatureModifierBuilderFactory {

    MLFeatureModifierBuilder create(MLFeatureModifierType type);
}
