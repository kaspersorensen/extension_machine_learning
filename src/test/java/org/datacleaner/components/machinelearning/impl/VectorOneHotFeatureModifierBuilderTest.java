package org.datacleaner.components.machinelearning.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.datacleaner.components.machinelearning.api.MLFeatureModifier;
import org.junit.Test;

public class VectorOneHotFeatureModifierBuilderTest {

    @Test
    public void testBuildSimple() {
        final VectorOneHotEncodingFeatureModifierBuilder builder = new VectorOneHotEncodingFeatureModifierBuilder();
        builder.addRecordValue("RED");
        builder.addRecordValue("GREEN");
        builder.addRecordValue("red ");
        builder.addRecordValue("BLUE");

        final MLFeatureModifier modifier = builder.build();
        assertEquals(3, modifier.getFeatureCount());

        final double[] result = modifier.generateFeatureValues(" Red");
        assertEquals("[0.0, 0.0, 1.0]", Arrays.toString(result));
    }

    @Test
    public void testBuildWithLimitOutput() {
        final VectorOneHotEncodingFeatureModifierBuilder builder = new VectorOneHotEncodingFeatureModifierBuilder(3,
                false);
        builder.addRecordValue("RED");
        builder.addRecordValue("RED");
        builder.addRecordValue("GREEN");
        builder.addRecordValue("GREEN");
        builder.addRecordValue("YELLOW");
        builder.addRecordValue("BLUE");

        final MLFeatureModifier modifier = builder.build();
        assertEquals(2, modifier.getFeatureCount());
        
        final double[] result1 = modifier.generateFeatureValues("YELLOW");
        assertEquals("[0.0, 0.0]", Arrays.toString(result1));
        
        final double[] result2 = modifier.generateFeatureValues("RED");
        assertEquals("[0.0, 1.0]", Arrays.toString(result2));
    }
}
