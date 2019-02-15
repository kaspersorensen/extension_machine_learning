
package org.datacleaner.components.machinelearning.ui;

import javax.inject.Inject;

import org.datacleaner.api.Renderer;
import org.datacleaner.api.RendererBean;
import org.datacleaner.api.RendererPrecedence;
import org.datacleaner.bootstrap.WindowContext;
import org.datacleaner.components.machinelearning.MLTrainingAnalyzer;
import org.datacleaner.configuration.DataCleanerConfiguration;
import org.datacleaner.guice.DCModule;
import org.datacleaner.job.builder.AnalyzerComponentBuilder;
import org.datacleaner.panels.AnalyzerComponentBuilderPresenter;
import org.datacleaner.panels.ComponentBuilderPresenterRenderingFormat;
import org.datacleaner.widgets.properties.PropertyWidgetFactory;

@RendererBean(ComponentBuilderPresenterRenderingFormat.class)
public class MLTrainingAnalyzerPresenterRenderer implements
        Renderer<AnalyzerComponentBuilder<MLTrainingAnalyzer>, AnalyzerComponentBuilderPresenter> {
    @Inject
    WindowContext windowContext;

    @Inject
    DataCleanerConfiguration configuration;

    @Inject
    DCModule dcModule;

    @Override
    public RendererPrecedence getPrecedence(AnalyzerComponentBuilder<MLTrainingAnalyzer> ajb) {
        if (ajb.getDescriptor().getComponentClass() == MLTrainingAnalyzer.class) {
            return RendererPrecedence.HIGH;
        }
        return RendererPrecedence.NOT_CAPABLE;
    }

    @Override
    public AnalyzerComponentBuilderPresenter render(AnalyzerComponentBuilder<MLTrainingAnalyzer> ajb) {
        final PropertyWidgetFactory propertyWidgetFactory =
                dcModule.createChildInjectorForComponent(ajb).getInstance(PropertyWidgetFactory.class);

        return new MLTrainingAnalyzerPresenter(ajb, windowContext, propertyWidgetFactory, configuration, dcModule);
    }
}
