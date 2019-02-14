package org.datacleaner.components.machinelearning.impl;

import java.util.Objects;

import org.datacleaner.components.machinelearning.api.MLFeatureModifierBuilder;
import org.datacleaner.components.machinelearning.api.MLFeatureModifierBuilderFactory;
import org.datacleaner.components.machinelearning.api.MLFeatureModifierType;

public class MLFeatureModifierBuilderFactoryImpl implements MLFeatureModifierBuilderFactory {

    @Override
    public MLFeatureModifierBuilder create(MLFeatureModifierType type) {
        Objects.requireNonNull(type);
        switch (type) {
        case DIRECT_NUMERIC:
            return new DirectNumericFeatureModifierBuilder();
        case DIRECT_BOOL:
            return new DirectBooleanFeatureModifierBuilder();
        case SCALED_MIN_MAX:
            return new ScaledMinMaxFeatureModifierBuilder();
        case VECTOR_ONE_HOT_ENCODING:
            return new VectorOneHotEncodingFeatureModifierBuilder();
        case VECTOR_2_GRAM:
            return new VectorNGramFeatureModifierBuilder(2);
        case VECTOR_3_GRAM:
            return new VectorNGramFeatureModifierBuilder(3);
        case VECTOR_4_GRAM:
            return new VectorNGramFeatureModifierBuilder(4);
        case VECTOR_5_GRAM:
            return new VectorNGramFeatureModifierBuilder(5);
        }
        throw new UnsupportedOperationException("Unsupported feature modifier type: " + type);
    }

}
