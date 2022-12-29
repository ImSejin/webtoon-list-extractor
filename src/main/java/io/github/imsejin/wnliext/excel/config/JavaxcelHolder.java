package io.github.imsejin.wnliext.excel.config;

import com.github.javaxcel.core.Javaxcel;
import com.github.javaxcel.core.converter.handler.registry.ExcelTypeHandlerRegistry;
import com.github.javaxcel.core.converter.handler.registry.impl.DefaultExcelTypeHandlerRegistry;
import io.github.imsejin.wnliext.excel.config.handler.BooleanTypeHandler;
import io.github.imsejin.wnliext.excel.config.handler.PlatformTypeHandler;
import lombok.Getter;

/**
 * @since 2.2.0
 */
public final class JavaxcelHolder {

    @Getter
    private static final Javaxcel instance;

    static {
        ExcelTypeHandlerRegistry registry = new DefaultExcelTypeHandlerRegistry();
        registry.add(new PlatformTypeHandler());
        registry.add(new BooleanTypeHandler(true));
        registry.add(new BooleanTypeHandler(false));

        instance = Javaxcel.newInstance(registry);
    }

}
