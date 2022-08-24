package com.example.johnsondemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {

    private String currencyName;
    private String chineseName;
    private Double exchangeRate;

}
