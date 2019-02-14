package org.datacleaner.components.machinelearning.impl;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.stream.Collectors;

import org.datacleaner.components.machinelearning.api.MLFeatureModifier;
import org.junit.Test;

public class VectorNGramFeatureModifierBuilderTest {

    @Test
    public void testBuild() {
        final VectorNGramFeatureModifierBuilder builder = new VectorNGramFeatureModifierBuilder(3);
        builder.addRecordValue("s-o s-h-o-r-t i-s i-g-n-o-r-e-d");
        assertEquals("", toString(builder));
        builder.addRecordValue("Hello world");
        assertEquals("ell,hel,llo,orl,rld,wor", toString(builder));
        builder.addRecordValue("hello");
        builder.addRecordValue("WORLD");
        assertEquals("ell,hel,llo,orl,rld,wor", toString(builder));
        builder.addRecordValue("Hello-world");
        assertEquals("ell,hel,llo,orl,rld,wor", toString(builder));

        final MLFeatureModifier modifier = builder.build();
        final double[] result = modifier.generateFeatureValues("world");
        assertZeroOneFeatureCounts(3, 3, result);
    }

    private void assertZeroOneFeatureCounts(int expected0, int expected1, double[] features) {
        int count0 = 0;
        int count1 = 0;
        for (double f : features) {
            if (f == 1d) {
                count1++;
            } else if (f == 0d) {
                count0++;
            }
        }
        assertEquals(expected0, count0);
        assertEquals(expected1, count1);
    }

    private String toString(VectorNGramFeatureModifierBuilder builder) {
        final Set<String> grams = builder.getGrams();
        return grams.stream().sorted().collect(Collectors.joining(","));
    }
}
