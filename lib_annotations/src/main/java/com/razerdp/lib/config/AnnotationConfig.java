package com.razerdp.lib.config;

/**
 * Created by razerdp on 2019/8/12.
 * <p>
 * path config
 */
public final class AnnotationConfig {

    public static class AndroidConfig {
        public static final String PACKAGE_SPARSEARRAY = "android.util";
    }

    public static class ServiceConfig {

        public static final String PACKAGE_NAME = "com.razerdp.github";

        public static final String APT_PACKAGE_NAME = PACKAGE_NAME + ".apt.gen";
        public static final String APT_FILE_NAME = "ServiceImplGen";
        public static final String APT_FIELD_NAME="SERVICE_IMPL_MAP";

        public static final String APT_INTERFACE = "com.razerdp.github.common.modules.base.IModuleServiceProvider";
        public static final String APT_INTERFACE_METHOD="getServiceImplMap";


    }

}
