package org.datacleaner.components.machinelearning;

import java.util.List;
import java.util.stream.Collectors;

import org.datacleaner.components.machinelearning.api.MLClassification;
import org.datacleaner.components.machinelearning.api.MLClassificationMetadata;
import org.datacleaner.components.machinelearning.api.MLClassificationTrainerRecord;
import org.datacleaner.components.machinelearning.api.MLClassifier;
import org.datacleaner.result.Crosstab;
import org.datacleaner.result.CrosstabDimension;
import org.datacleaner.result.CrosstabNavigator;

public class MLConfusionMatrixBuilder {

    private final MLClassifier classifier;
    private final Crosstab<Integer> crosstab;
    private final CrosstabDimension expectedDimension;
    private final CrosstabDimension actualDimension;

    public MLConfusionMatrixBuilder(MLClassifier classifier) {
        this.classifier = classifier;
        this.crosstab = new Crosstab<>(Integer.class, "Expected", "Actual");

        this.expectedDimension = crosstab.getDimension(0);
        this.actualDimension = crosstab.getDimension(1);
        final List<String> classificationLabels = classifier.getMetadata().getClassifications().stream()
                .map(this::getClassificationLabel).collect(Collectors.toList());
        this.expectedDimension.addCategories(classificationLabels);
        this.actualDimension.addCategories(classificationLabels);

        // set all counts to 0
        for (String label1 : classificationLabels) {
            final CrosstabNavigator<Integer> nav = crosstab.where(expectedDimension, label1);
            for (String label2 : classificationLabels) {
                nav.where(actualDimension, label2).put(0);
            }
        }
    }

    public void append(MLClassificationTrainerRecord record) {
        final MLClassificationMetadata metadata = classifier.getMetadata();

        final MLClassification result = classifier.classify(record.getFeatureValues());
        final String actual = getClassificationLabel(metadata.getClassification(result.getBestClassificationIndex()));
        final String expected = getClassificationLabel(record.getClassification());

        final CrosstabNavigator<Integer> crosstabPath =
                crosstab.navigate().where(expectedDimension, expected).where(actualDimension, actual);
        final Integer valueBefore = crosstabPath.get();
        if (valueBefore == null) {
            crosstabPath.put(1);
        } else {
            crosstabPath.put(valueBefore.intValue() + 1);
        }
    }

    private String getClassificationLabel(Object classification) {
        return classification.toString();
    }

    public Crosstab<Integer> build() {
        return crosstab;
    }
}
