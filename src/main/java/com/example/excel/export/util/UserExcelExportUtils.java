package com.example.excel.export.util;

import java.util.List;

public class UserExcelExportUtils {
    private UserExcelExportUtils() {}
    public static final List<String> COLUMN_NAMES = List.of(
            "id", "fullName", "username", "email"
    );
    public static final String RESOURCE_NAME = "Users";
}
