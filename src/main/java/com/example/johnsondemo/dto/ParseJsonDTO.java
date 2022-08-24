package com.example.johnsondemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseJsonDTO {
    private String code;
    private String chineseName;
    private String rate;
    private Map<String,String> time;
}
