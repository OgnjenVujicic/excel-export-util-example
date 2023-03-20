package com.example.excel.export.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserExportDTO {
    private Integer id;
    private String fullName;
    private String username;
    private String email;
}
