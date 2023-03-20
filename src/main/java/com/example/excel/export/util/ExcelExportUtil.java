package com.example.excel.export.util;

import com.example.excel.export.dto.DownloadFileDTO;
import com.example.excel.export.exception.UnableToExportExcelException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ExcelExportUtil {

    private ExcelExportUtil(){}

    public static <R> DownloadFileDTO export(List<R> exportResource, List<String> columnNames, String name) {

        checkParams(exportResource, columnNames, name);

        try (Workbook workbook = new XSSFWorkbook())
        {
            createReportSheet(workbook, exportResource, columnNames, name);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);

            Resource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            String fileName =  name + "_" + dateTimeFormatter.format(LocalDateTime.now()) + ".xlsx";

            return DownloadFileDTO.builder()
                    .resource(resource)
                    .fileName(fileName)
                    .mediaType(MediaTypeFactory
                            .getMediaType(resource)
                            .orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .build();
        } catch (Exception ex) {
            log.error("Error during creation of Excel file.", ex);
            throw new UnableToExportExcelException(name);
        }
    }

    private static <R> void checkParams(List<R> exportResource, List<String> columnNames, String name) {
        if(exportResource.isEmpty() || columnNames.isEmpty() || name.isEmpty()) {
            log.error("Error during creation of Excel file. Some of params are empty.");
            throw new UnableToExportExcelException(name);
        }
    }

    public static <T> void createReportSheet(Workbook workbook, List<T> reportElements, List<String> columnNames, String name) {
        Sheet sheet = workbook.createSheet(name);

        prepareHeaderRow(sheet, workbook, columnNames);
        prepareContentRows(reportElements, sheet, workbook);

        for (int i = 0; i < columnNames.size(); i++)
            sheet.autoSizeColumn(i);
    }

    private static void prepareHeaderRow(Sheet sheet, Workbook workbook, List<String> columnNames) {
        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setVerticalAlignment(VerticalAlignment.DISTRIBUTED);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < columnNames.size(); i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(columnNames.get(i));
            headerCell.setCellStyle(headerStyle);
        }
    }

    private static <T> void prepareContentRows(List<T> reportElements, Sheet sheet, Workbook workbook) {
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBold(false);

        List<Field> fields = Arrays.asList(reportElements.get(0).getClass().getDeclaredFields());

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < reportElements.size(); i++) {
            Row row = sheet.createRow(i + 1);

            T entry = reportElements.get(i);

            for (int j = 0; j < fields.size(); j++) {
                BeanWrapper entryBean = new BeanWrapperImpl(entry);
                Cell cell = row.createCell(j);
                Object obj = entryBean.getPropertyValue(fields.get(j).getName());
                cell.setCellValue(obj == null ? "" : obj.toString());
                cell.setCellStyle(style);
            }
        }
    }

    public static HttpHeaders prepareHeaders(DownloadFileDTO downloadFileDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(downloadFileDTO.getMediaType());

        ContentDisposition disposition = ContentDisposition
                .builder("inline")
                .filename(downloadFileDTO.getFileName())
                .build();
        headers.setContentDisposition(disposition);

        return headers;
    }
}

