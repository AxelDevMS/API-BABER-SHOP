package ams.dev.api.barber_shop.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.function.Function;

@Getter
@Builder
public class ExcelReportConfig<T> {

    private final String sheetName;
    private final String[] headers;

    @Singular
    private final List<Function<T, Object>> fieldExtractors;

    private final String title;

    @Builder.Default
    private final boolean showFooter = true;

    /**
     * Método para convertir la List de extractores a Array
     */
    @SuppressWarnings("unchecked")
    public Function<T, Object>[] getFieldExtractorsAsArray() {
        if (fieldExtractors == null) {
            return (Function<T, Object>[]) new Function[0];
        }
        return fieldExtractors.toArray(new Function[0]);
    }

}