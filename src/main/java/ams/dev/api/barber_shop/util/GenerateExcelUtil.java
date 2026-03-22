package ams.dev.api.barber_shop.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateExcelUtil {

    // Constantes - valores limpios y profesionales
    private static final int HEADER_ROW_HEIGHT = 25;
    private static final int DATA_ROW_HEIGHT = 18;
    private static final String DATE_FORMAT = "dd/mm/yyyy";
    private static final String DATETIME_FORMAT = "dd/mm/yyyy hh:mm";
    private static final String CURRENCY_FORMAT = "$ #,##0.00";
    private static final String DEFAULT_NULL_VALUE = "-";

    /**
     * Genera un Excel elegante y profesional
     */
    public <T> byte[] generateExcel(List<T> data, ExcelReportConfig<T> config) throws IOException {
        validateInputs(data, config);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(config.getSheetName());

            // Crear estilos minimalistas
            StyleContainer styles = createElegantStyles(workbook);

            int rowNum = 0;

            // Título (si existe)
            if (config.getTitle() != null) {
                rowNum = createTitleSection(sheet, config.getTitle(), styles.getTitleStyle(),
                        config.getHeaders().length, rowNum);
            }

            // Subtítulo con fecha
            rowNum = createSubtitleSection(sheet, "Generado: " + formatCurrentDate(),
                    styles.getSubtitleStyle(), config.getHeaders().length, rowNum);

            // Espaciado
            rowNum++;

            // Headers elegantes
            rowNum = createHeaders(sheet, config.getHeaders(), styles.getHeaderStyle(), rowNum);

            // Datos
            rowNum = createDataRows(sheet, data, config, styles, rowNum);

            // Línea separadora
            rowNum++;

            // Pie con totales
            createFooter(sheet, data.size(), styles.getFooterStyle(), rowNum);

            // Autoajustar columnas con márgenes elegantes
            autoSizeColumns(sheet, config.getHeaders().length);

            return convertWorkbookToBytes(workbook);
        }
    }

    // =========================================================================
    // ESTILOS ELEGANTES (MINIMALISTAS)
    // =========================================================================

    private StyleContainer createElegantStyles(Workbook workbook) {
        return StyleContainer.builder()
                .workbook(workbook)
                .titleStyle(createTitleStyle(workbook))
                .subtitleStyle(createSubtitleStyle(workbook))
                .headerStyle(createHeaderStyle(workbook))
                .textStyle(createTextStyle(workbook))
                .dateStyle(createDateStyle(workbook))
                .currencyStyle(createCurrencyStyle(workbook))
                .footerStyle(createFooterStyle(workbook))
                .build();
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setFontName("Calibri");
        font.setColor(IndexedColors.GREY_80_PERCENT.getIndex());

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createSubtitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setFontHeightInPoints((short) 10);
        font.setFontName("Calibri");
        font.setColor(IndexedColors.GREY_50_PERCENT.getIndex());

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Calibri");
        font.setColor(IndexedColors.BLACK.getIndex());

        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createTextStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setFontHeightInPoints((short) 10);
        font.setFontName("Calibri");

        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setFontHeightInPoints((short) 10);
        font.setFontName("Calibri");

        style.setFont(font);
        style.setDataFormat(workbook.createDataFormat().getFormat(DATE_FORMAT));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setFontHeightInPoints((short) 10);
        font.setFontName("Calibri");

        style.setFont(font);
        style.setDataFormat(workbook.createDataFormat().getFormat(CURRENCY_FORMAT));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createFooterStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Calibri");

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    // =========================================================================
    // CONSTRUCCIÓN DEL REPORTE
    // =========================================================================

    private int createTitleSection(Sheet sheet, String title, CellStyle style,
                                   int columnCount, int startRow) {
        Row row = sheet.createRow(startRow);
        row.setHeightInPoints(20);

        Cell cell = row.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);

        if (columnCount > 1) {
            sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 0, columnCount - 1));
        }

        return startRow + 1;
    }

    private int createSubtitleSection(Sheet sheet, String subtitle, CellStyle style,
                                      int columnCount, int startRow) {
        Row row = sheet.createRow(startRow);
        row.setHeightInPoints(15);

        Cell cell = row.createCell(0);
        cell.setCellValue(subtitle);
        cell.setCellStyle(style);

        if (columnCount > 1) {
            sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 0, columnCount - 1));
        }

        return startRow + 1;
    }

    private int createHeaders(Sheet sheet, String[] headers, CellStyle style, int startRow) {
        Row row = sheet.createRow(startRow);
        row.setHeightInPoints(HEADER_ROW_HEIGHT);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }

        return startRow + 1;
    }

    private <T> int createDataRows(Sheet sheet, List<T> data, ExcelReportConfig<T> config,
                                   StyleContainer styles, int startRow) {
        int currentRow = startRow;

        for (T item : data) {
            Row row = sheet.createRow(currentRow++);
            row.setHeightInPoints(DATA_ROW_HEIGHT);

            Function<T, Object>[] extractors = config.getFieldExtractorsAsArray();

            for (int i = 0; i < extractors.length; i++) {
                Cell cell = row.createCell(i);
                Object value = extractors[i].apply(item);

                // Determinar estilo según tipo de dato
                CellStyle cellStyle = determineStyle(value, styles, config.getHeaders()[i]);
                writeCellValue(cell, value, cellStyle);
            }
        }

        return currentRow;
    }

    private CellStyle determineStyle(Object value, StyleContainer styles, String header) {
        if (value == null) return styles.getTextStyle();

        return switch (value) {
            case Date date -> styles.getDateStyle();
            case LocalDateTime localDateTime -> styles.getDateStyle();
            case Number number -> {
                String headerLower = header.toLowerCase();
                if (headerLower.contains("precio") || headerLower.contains("monto") ||
                        headerLower.contains("total") || headerLower.contains("costo")) {
                    yield styles.getCurrencyStyle();
                }
                yield styles.getTextStyle();
            }
            default -> styles.getTextStyle();
        };
    }

    private void writeCellValue(Cell cell, Object value, CellStyle style) {
        if (value == null) {
            cell.setCellValue(DEFAULT_NULL_VALUE);
            cell.setCellStyle(style);
            return;
        }

        switch (value) {
            case Number number -> {
                cell.setCellValue(number.doubleValue());
                cell.setCellStyle(style);
            }
            case Date date -> {
                cell.setCellValue(date);
                cell.setCellStyle(style);
            }
            case LocalDateTime localDateTime -> {
                cell.setCellValue(localDateTime);
                cell.setCellStyle(style);
            }
            case Boolean bool -> {
                cell.setCellValue(bool ? "Sí" : "No");
                cell.setCellStyle(style);
            }
            default -> {
                cell.setCellValue(value.toString());
                cell.setCellStyle(style);
            }
        }
    }

    private void createFooter(Sheet sheet, int totalRows, CellStyle style, int startRow) {
        Row row = sheet.createRow(startRow);

        Cell labelCell = row.createCell(0);
        labelCell.setCellValue("Total de registros:");
        labelCell.setCellStyle(style);

        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(totalRows);
        valueCell.setCellStyle(style);
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            // Agregar un pequeño margen para que no quede apretado
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, currentWidth + 500);
        }
    }

    private byte[] convertWorkbookToBytes(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private <T> void validateInputs(List<T> data, ExcelReportConfig<T> config) {
        if (data == null) {
            throw new IllegalArgumentException("La lista de datos no puede ser null");
        }
        if (config == null) {
            throw new IllegalArgumentException("La configuración no puede ser null");
        }
    }

    private String formatCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String generateFileName(String prefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return String.format("%s_%s.xlsx", prefix, timestamp);
    }

    // =========================================================================
    // CLASE INTERNA - CONTENEDOR DE ESTILOS
    // =========================================================================

    @lombok.Builder
    @lombok.Getter
    private static class StyleContainer {
        private final Workbook workbook;
        private final CellStyle titleStyle;
        private final CellStyle subtitleStyle;
        private final CellStyle headerStyle;
        private final CellStyle textStyle;
        private final CellStyle dateStyle;
        private final CellStyle currencyStyle;
        private final CellStyle footerStyle;
    }
}