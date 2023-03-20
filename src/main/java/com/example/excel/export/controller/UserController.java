package com.example.excel.export.controller;


import com.example.excel.export.dto.DownloadFileDTO;
import com.example.excel.export.exception.UnableToExportExcelException;
import com.example.excel.export.service.UserService;
import com.example.excel.export.util.ExcelExportUtil;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("export")
    public ResponseEntity<Resource> exportToXLS() {
        DownloadFileDTO downloadFileDTO = userService.exportToXLS();

        return new ResponseEntity<>(downloadFileDTO.getResource(),
                ExcelExportUtil.prepareHeaders(downloadFileDTO),
                HttpStatus.OK);
    }

    @ExceptionHandler(UnableToExportExcelException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(UnableToExportExcelException exception) {
        return exception.getMessage();
    }
    
}
