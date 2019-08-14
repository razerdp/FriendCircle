package com.razerdp.lib.processor.service;

import com.razerdp.lib.annotations.ServiceImpl;
import com.razerdp.lib.processor.BaseProcessor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.razerdp.lib.config.AnnotationConfig.AndroidConfig.PACKAGE_SPARSEARRAY;
import static com.razerdp.lib.config.AnnotationConfig.ServiceConfig.APT_FIELD_NAME;
import static com.razerdp.lib.config.AnnotationConfig.ServiceConfig.APT_FILE_NAME;
import static com.razerdp.lib.config.AnnotationConfig.ServiceConfig.APT_INTERFACE;
import static com.razerdp.lib.config.AnnotationConfig.ServiceConfig.APT_INTERFACE_METHOD;
import static com.razerdp.lib.config.AnnotationConfig.ServiceConfig.APT_PACKAGE_NAME;
import static com.razerdp.lib.config.AnnotationConfig.ServiceConfig.PACKAGE_NAME;

/**
 * Created by razerdp on 2019/8/12.
 */
public class ModuleServiceProcessor extends BaseProcessor {
    private static final String TAG = "ModuleServiceProcessor";
    //get module arguments from gradle
    private static final String KEY_MODULE_NAME = "MODULE_NAME";

    private String moduleName;

    //key = service interface for every module
    //value = list service impl from key for every module
    private static final HashMap<TypeName, List<InnerAptInfo>> mServiceImplMap = new HashMap<>();
    private final ParameterizedTypeName hashMapClass = ParameterizedTypeName.get(
            ClassName.get(HashMap.class),//hashmap.class
            ClassName.get(Class.class),//key class
            ParameterizedTypeName.get(
                    ClassName.get(PACKAGE_SPARSEARRAY, "SparseArray"),//sparsearray.class,
                    ClassName.get(Object.class)));//object.class

    private static class InnerAptInfo {
        final TypeElement mirror;
        final String fullClassName;
        final int tag;

        public InnerAptInfo(TypeElement mirror, String fullClassName, int tag) {
            this.mirror = mirror;
            this.fullClassName = fullClassName;
            this.tag = tag;
        }
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        Map<String, String> options = processingEnvironment.getOptions();
        if (options == null || options.isEmpty()) return;
        moduleName = options.get(KEY_MODULE_NAME);
    }

    @Override
    protected void onSetSupportedAnnotation(Set<Class<? extends Annotation>> result) {
        result.add(ServiceImpl.class);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (roundEnvironment.processingOver()) {
            if (!set.isEmpty()) {
                return false;
            }
        }
        if (set.isEmpty()) {
            logi(TAG + "annotations is empty,return");
            return false;
        }

        logi(TAG + "start processing =========== ");
        mServiceImplMap.clear();

        scanClass(set, roundEnvironment);
        if (mServiceImplMap.isEmpty()) {
            loge(TAG + " find no class impl for @ServiceImpl");
            return false;
        }

        printScanned();
        generateJavaFile();
        return false;
    }

    private void generateJavaFile() {
        try {
            JavaFile javaFile = JavaFile.builder(APT_PACKAGE_NAME, createType())
                    .addFileComment("$S", "Generated code from " + APT_PACKAGE_NAME + "." + APT_FILE_NAME + " Do not modify!")
                    .build();
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            loge(e);
        }
    }

    private TypeSpec createType() {
        //public final ServiceImplGen
        TypeSpec.Builder types = TypeSpec.classBuilder(APT_FILE_NAME + "$$" + moduleName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        //add interface
        types.addSuperinterface(ClassName.get(mElementUtil.getTypeElement(APT_INTERFACE)));
        //add hashmap field
        types.addField(createField());
        //add static code for hashmap
        types.addStaticBlock(createStaticBlock());
        //add interface method
        types.addMethod(createInterfaceMethod());
        return types.build();
    }


    private FieldSpec createField() {
        //private static final SERVICE_IMPL_MAP = new HashMap();
        FieldSpec.Builder builder = FieldSpec.builder(hashMapClass, APT_FIELD_NAME)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T()", hashMapClass);
        return builder.build();
    }

    private CodeBlock createStaticBlock() {
        CodeBlock.Builder builder = CodeBlock.builder();

        final String listName = "mServiceImplList";
        final TypeName sparseArrayType = ClassName.get(PACKAGE_SPARSEARRAY, "SparseArray");

        //SparseArray mServiceImplList
        builder.addStatement("$T " + listName, sparseArrayType);

        for (Map.Entry<TypeName, List<InnerAptInfo>> entry : mServiceImplMap.entrySet()) {
            //mServiceImplList = new SparseArray();
            builder.addStatement(listName + " = new $T()", sparseArrayType);

            for (InnerAptInfo aptInfo : entry.getValue()) {
                //mServiceImplList.put(tag,new impl());
                builder.addStatement(listName + ".put($L , new $T())",
                        aptInfo.tag,
                        ClassName.get(aptInfo.mirror));

            }
            //SERVICE_IMPL_MAP.put(impl.calss,mServiceImplList)
            builder.addStatement(APT_FIELD_NAME + ".put($T.class," + listName + ")", entry.getKey());

        }
        return builder.build();
    }

    private MethodSpec createInterfaceMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(APT_INTERFACE_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return " + APT_FIELD_NAME)
                .returns(hashMapClass);
        return builder.build();
    }

    private void scanClass(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //scan for service impl
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ServiceImpl.class);

        for (Element element : elements) {
            //only get annotation for class type
            if (!(element instanceof TypeElement)) continue;

            if (element.getKind() != ElementKind.CLASS) {
                loge(TAG + " @ServiceImpl is only for class");
                continue;
            }

            if (!element.getModifiers().contains(Modifier.PUBLIC)) {
                loge(TAG + " @ServiceImpl is only for public class");
                continue;
            }

            TypeElement typeElement = (TypeElement) element;
            TypeMirror mirror = typeElement.asType();
            if (!(mirror.getKind() == TypeKind.DECLARED)) {
                continue;
            }

            // check interface
            List<? extends TypeMirror> superClassElement = mTypesUtil.directSupertypes(mirror);
            if (superClassElement == null || superClassElement.size() <= 0) continue;

            TypeMirror serviceInterfaceElement = null;
            for (TypeMirror typeMirror : superClassElement) {
                if (typeMirror.toString().startsWith(PACKAGE_NAME)) {
                    serviceInterfaceElement = typeMirror;
                    break;
                }
            }

            if (serviceInterfaceElement == null) {
                continue;
            }

            //get impl class full name
            TypeName classType = ClassName.get(serviceInterfaceElement);
            List<InnerAptInfo> aptInfos = null;
            if (mServiceImplMap.containsKey(classType)) {
                aptInfos = mServiceImplMap.get(classType);
            }
            if (aptInfos == null) {
                aptInfos = new ArrayList<>();
                mServiceImplMap.put(classType, aptInfos);
            }

            int tag = element.getAnnotation(ServiceImpl.class).value();

            InnerAptInfo innerAptInfo = new InnerAptInfo(typeElement, typeElement.getQualifiedName().toString(), tag);
            aptInfos.add(innerAptInfo);

        }

    }


    private void printScanned() {
        for (Map.Entry<TypeName, List<InnerAptInfo>> entry : mServiceImplMap.entrySet()) {
            String key = entry.getKey().toString();
            StringBuilder builder = new StringBuilder();
            builder.append(" find service impl for : ")
                    .append(key)
                    .append("  [\n");
            List<InnerAptInfo> aptInfos = entry.getValue();
            if (aptInfos != null && aptInfos.size() > 0) {
                for (InnerAptInfo info : aptInfos) {
                    builder.append("class name = ")
                            .append(info.fullClassName)
                            .append('\n')
                            .append(" tag = ")
                            .append(info.tag)
                            .append('\n');
                }
                builder.append("]");
            }
            logi(TAG + builder);
        }
    }
}
