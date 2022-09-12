package io.github.imsejin.wnliext.excel.config;

import com.github.javaxcel.styler.ExcelStyleConfig;
import com.github.javaxcel.styler.config.Configurer;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class HeaderStyleConfig implements ExcelStyleConfig {

    @Override
    public void configure(Configurer configurer) {
        configurer.alignment()
                .horizontal(HorizontalAlignment.CENTER)
                .vertical(VerticalAlignment.CENTER)
                .and()
                .background(FillPatternType.SOLID_FOREGROUND, IndexedColors.GREY_25_PERCENT)
                .border()
                .leftAndRight(BorderStyle.THIN, IndexedColors.BLACK)
                .bottom(BorderStyle.MEDIUM, IndexedColors.BLACK)
                .and()
                .font()
                .name("NanumGothicExtraBold")
                .size(12)
                .color(IndexedColors.BLACK)
                .bold();
    }

}
