package org.datacleaner.components.machinelearning.api;

import org.apache.metamodel.util.HasName;

/**
 * Represents the types of functions can be applied to columns for transforming them into features. The functions are
 * applied to the values of the raw data and transforms it to a set of numeric features.
 */
public enum MLFeatureModifierType implements HasName {

    DIRECT_NUMERIC("Direct (0.0 to 1.0)", Number.class),

    DIRECT_BOOL("Direct (1 or 0)", Boolean.class),

    SCALED_MIN_MAX("Scaled (Min-Max)", Number.class),

    VECTOR_ONE_HOT_ENCODING("Vector (One Hot Encoding)", String.class),

    VECTOR_2_GRAM("Vector (2-gram)", String.class),

    VECTOR_3_GRAM("Vector (3-gram)", String.class),

    VECTOR_4_GRAM("Vector (4-gram)", String.class),

    VECTOR_5_GRAM("Vector (5-gram)", String.class),

    ;

    private final Class<?> applicableDataType;
    private final String name;

    private MLFeatureModifierType(final String name, final Class<?> applicableDataType) {
        this.name = name;
        this.applicableDataType = applicableDataType;
    }

    public Class<?> getApplicableDataType() {
        return applicableDataType;
    }

    @Override
    public String getName() {
        return name;
    }
}
