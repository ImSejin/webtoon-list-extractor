package io.github.imsejin.wnliext.excel.config.handler;

import com.github.javaxcel.core.converter.handler.AbstractExcelTypeHandler;
import io.github.imsejin.wnliext.file.model.Platform;

public class PlatformTypeHandler extends AbstractExcelTypeHandler<Platform> {

    public PlatformTypeHandler() {
        super(Platform.class);
    }

    @Override
    protected String writeInternal(Platform value, Object... arguments) throws Exception {
        return value.getCodeName();
    }

    @Override
    public Platform read(String value, Object... arguments) throws Exception {
        return Platform.fromCodeName(value);
    }

}
