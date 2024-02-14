package com.acme.biz.api.model.i18n;

import java.util.ListResourceBundle;

public class HardCodeResourceBundle extends ListResourceBundle {
    private static final Object[][] contents = {
            {"my.name", "Mercy Ma"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
