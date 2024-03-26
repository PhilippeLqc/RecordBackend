package com.recordbackend.Dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class EmailDetailsDto {

    private String template;

    private String to;

    private String subject;

    private Map<String, Object> variables;
}
