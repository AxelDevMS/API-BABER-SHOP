package ams.dev.api.barber_shop.util;

import lombok.Builder;
import lombok.Getter;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Optional;

/**
 * Representa un estilo aplicable a una fila o celda en Excel.
 * Inmutable y creado mediante Builder.
 */
@Getter
@Builder
public class ExcelRowStyle {

    private final IndexedColors backgroundColor;
    private final IndexedColors textColor;
    private final boolean bold;

    /**
     * Verifica si el estilo tiene color de fondo
     */
    public boolean hasBackgroundColor() {
        return backgroundColor != null;
    }

    /**
     * Verifica si el estilo tiene color de texto
     */
    public boolean hasTextColor() {
        return textColor != null;
    }

    /**
     * Obtiene el color de fondo como Optional
     */
    public Optional<IndexedColors> getBackgroundColorOptional() {
        return Optional.ofNullable(backgroundColor);
    }

    /**
     * Obtiene el color de texto como Optional
     */
    public Optional<IndexedColors> getTextColorOptional() {
        return Optional.ofNullable(textColor);
    }

    /**
     * Builder personalizado con valores por defecto
     */
    public static class ExcelRowStyleBuilder {

        public ExcelRowStyle build() {
            return new ExcelRowStyle(
                    backgroundColor,
                    textColor,
                    bold
            );
        }
    }
}