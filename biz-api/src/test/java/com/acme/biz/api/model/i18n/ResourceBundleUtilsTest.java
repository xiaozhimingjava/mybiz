package com.acme.biz.api.model.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleUtilsTest {


    @Test
    public void testPropertiesResourceBundle() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("META-INF.Messages");

        System.out.println(resourceBundle.getString("my.name"));
    }

    @Test
    public void testJavaClassesResourceBundle() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("com.acme.biz.api.model.i18n.HardCodeResourceBundle");

        System.out.println(resourceBundle.getString("my.name"));
    }



}
