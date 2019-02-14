package org.datacleaner.components.machinelearning.impl;

import java.util.HashSet;
import java.util.Set;

import org.datacleaner.components.machinelearning.api.MLFeatureModifier;
import org.datacleaner.components.machinelearning.api.MLFeatureModifierBuilder;

public class VectorNGramFeatureModifierBuilder implements MLFeatureModifierBuilder {

    private final int n;
    private final Set<String> grams;

    public VectorNGramFeatureModifierBuilder(int n) {
        this.n = n;
        this.grams = new HashSet<>();
    }

    @Override
    public void addRecordValue(Object value) {
        final Iterable<String> parts = VectorNGramFeatureModifier.split(value);
        for (String part : parts) {
            for (int index = 0; index + n <= part.length(); index++) {
                final String gram = part.substring(index, index + n);
                synchronized (this) {
                    grams.add(gram);
                }
            }
        }
    }

    @Override
    public MLFeatureModifier build() {
        return new VectorNGramFeatureModifier(n, grams);
    }

    protected Set<String> getGrams() {
        return grams;
    }
}
