package com.example.excel.export.exception;

public class UnableToExportExcelException extends RuntimeException {
    public UnableToExportExcelException(String resourceName) {
        super("Unable to export resource with name " + resourceName);
    }
}
