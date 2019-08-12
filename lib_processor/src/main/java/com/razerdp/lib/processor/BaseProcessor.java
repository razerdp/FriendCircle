package com.razerdp.lib.processor;


import com.razerdp.lib.processor.util.Logger;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by razerdp on 2019/8/12.
 */
public abstract class BaseProcessor extends AbstractProcessor {
    protected Filer mFiler;
    protected Logger mLogger;
    protected Types mTypesUtil;
    protected Elements mElementUtil;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mFiler = processingEnvironment.getFiler();
        mTypesUtil = processingEnvironment.getTypeUtils();
        mElementUtil = processingEnvironment.getElementUtils();

        mLogger = new Logger(processingEnvironment.getMessager());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotationsSet = new LinkedHashSet<>();
        Set<Class<? extends Annotation>> result = new LinkedHashSet<>();
        onSetSupportedAnnotation(result);
        for (Class<? extends Annotation> aClass : result) {
            supportedAnnotationsSet.add(aClass.getCanonicalName());
        }
        return supportedAnnotationsSet;
    }

    protected abstract void onSetSupportedAnnotation(Set<Class<? extends Annotation>> result);


    public void logi(CharSequence which) {
        mLogger.i(which);
    }

    public void loge(CharSequence which) {
        mLogger.e(which);
    }

    public void logw(CharSequence which) {
        mLogger.w(which);
    }

    public void loge(Throwable which) {
        mLogger.e(which);
    }
}
