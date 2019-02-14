package org.datacleaner.components.machinelearning.impl;

import java.util.Collection;

import org.datacleaner.components.machinelearning.api.MLFeatureModifier;
import org.datacleaner.components.machinelearning.api.MLFeatureModifierType;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

public class VectorNGramFeatureModifier implements MLFeatureModifier {

    private static final long serialVersionUID = 1L;

    public static Iterable<String> split(Object value) {
        final String str;
        if (value == null) {
            str = "";
        } else {
            str = value.toString().toLowerCase().chars().map(c -> {
                if (Character.isLetter(c)) {
                    return c;
                }
                // replace punctuation and such, leaving only letters and whitespace
                return ' ';
            }).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        }
        return Splitter.on(CharMatcher.whitespace()).omitEmptyStrings().split(str);
    }

    private final String[] grams;
    private final int n;

    public VectorNGramFeatureModifier(int n, Collection<String> grams) {
        this.n = n;
        this.grams = grams.toArray(String[]::new);
    }

    @Override
    public double[] generateFeatureValues(Object value) {
        final double[] result = new double[getFeatureCount()];
        final Iterable<String> parts = split(value);
        for (String part : parts) {
            if (part.length() >= n) {
                for (int i = 0; i < grams.length; i++) {
                    final String gram = grams[i];
                    if (part.contains(gram)) {
                        result[i] = 1;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public int getFeatureCount() {
        return grams.length;
    }

    @Override
    public MLFeatureModifierType getType() {
        return MLFeatureModifierType.getNGramType(n);
    }
}
