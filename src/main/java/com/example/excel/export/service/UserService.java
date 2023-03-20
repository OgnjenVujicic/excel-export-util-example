package com.example.excel.export.service;

import com.example.excel.export.dto.DownloadFileDTO;
import com.example.excel.export.dto.UserExportDTO;
import com.example.excel.export.util.ExcelExportUtil;
import com.example.excel.export.util.UserExcelExportUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class UserService {
 

    public DownloadFileDTO exportToXLS() {

        List<UserExportDTO> exportUsers = mapUsersToExportDTO();

        return ExcelExportUtil.export(exportUsers, UserExcelExportUtils.COLUMN_NAMES, UserExcelExportUtils.RESOURCE_NAME);
    }


    /**
     * This method mocks call to DB and returns list of users.
     */
    private List<UserExportDTO> mapUsersToExportDTO() {
        return List.of(
                new UserExportDTO(1, "Jhon Legend", "jhonlegend","jhonlegend@gmail.com"),
                new UserExportDTO(2, "Aron Smith", "aronsmith","aronsmith@gmail.com"),
                new UserExportDTO(3, "Arnold Wild", "arnoldwild","arnoldwild@gmail.com")
        );
    }


}