package io.github.imsejin.wnliext.excel.config.handler;

import java.util.Locale;

public class BooleanTypeHandler extends com.github.javaxcel.core.converter.handler.impl.lang.BooleanTypeHandler {

    public BooleanTypeHandler(boolean primitive) {
        super(primitive);
    }

    @Override
    protected String writeInternal(Boolean value, Object... arguments) {
        return String.valueOf(value).toUpperCase(Locale.US);
    }

    @Override
    public Boolean read(String value, Object... arguments) {
        return "true".equalsIgnoreCase(value);
    }

}
