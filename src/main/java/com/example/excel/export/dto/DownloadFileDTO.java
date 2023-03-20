package com.example.excel.export.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import javax.validation.constraints.NotNull;

/**
 * A DownloadFileDTO dto for data download.
 */
@Value
@Builder
public class DownloadFileDTO
{
    @NotNull
    Resource resource;
    @NotNull
    String fileName;
    @NotNull
    MediaType mediaType;
}
