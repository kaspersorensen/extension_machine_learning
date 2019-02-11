package org.datacleaner.components.machinelearning;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.datacleaner.api.InputColumn;
import org.datacleaner.components.machinelearning.api.MLClassificationMetadata;
import org.datacleaner.data.MockInputColumn;
import org.datacleaner.data.MockInputRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MLTrainingAnalyzerTest {

    private static final String RED = "red";
    private static final String GREEN = "green";
    private static final String BLUE = "blue";

    @Parameters
    public static final List<Object[]> data() {
        return Arrays.asList(
                new Object[] {MLAlgorithm.RANDOM_FOREST},
                new Object[] {MLAlgorithm.SVM}
        );
    }

    private final MLAlgorithm algorithm;
    
    public MLTrainingAnalyzerTest(MLAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testScenario() {
        final MockInputColumn<String> color = new MockInputColumn<>("color");
        final MockInputColumn<Double> red = new MockInputColumn<>(RED);
        final MockInputColumn<Double> green = new MockInputColumn<>(GREEN);
        final MockInputColumn<Double> blue = new MockInputColumn<>(BLUE);

        final MLTrainingAnalyzer analyzer = new MLTrainingAnalyzer();
        analyzer.algorithm = algorithm;
        analyzer.classification = color;
        analyzer.features = new InputColumn[] { red, green, blue };
        analyzer.epochs = 10;
        analyzer.layerSize = 10;
        analyzer.init();

        for (int i = 0; i < 1000; i++) {
            analyzer.run(new MockInputRow().put(color, RED).put(red, randomHigh()).put(green, randomLow()).put(blue,
                    randomLow()), 1);
            analyzer.run(new MockInputRow().put(color, BLUE).put(red, randomLow()).put(green, randomLow()).put(blue,
                    randomHigh()), 1);
            analyzer.run(new MockInputRow().put(color, GREEN).put(red, randomLow()).put(green, randomHigh()).put(blue,
                    randomLow()), 1);
        }

        final MLAnalyzerResult result = analyzer.getResult();
        final MLClassificationMetadata metadata = result.getTrainedClassifier().getMetadata();

        assertEquals(RED, metadata.getClassification(
                result.getTrainedClassifier().classify(new double[] { 1, 0, 0 }).getBestClassificationIndex()));
        assertEquals(GREEN, metadata.getClassification(
                result.getTrainedClassifier().classify(new double[] { 0, 1, 0 }).getBestClassificationIndex()));
        assertEquals(BLUE, metadata.getClassification(
                result.getTrainedClassifier().classify(new double[] { 0, 0, 1 }).getBestClassificationIndex()));

    }

    private double randomLow() {
        return Math.random() / 3;
    }

    private double randomHigh() {
        return Math.max(0.6, Math.random());
    }
}
