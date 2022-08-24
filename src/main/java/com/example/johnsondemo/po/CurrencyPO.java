package com.example.johnsondemo.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table(name = "currency")
public class CurrencyPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String currencyName;
    private String chineseName;
    private Double exchangeRate;
    private Timestamp updateTime;

    public CurrencyPO(String currencyName, String chineseName, Double exchangeRate, Timestamp updateTime) {
        this.currencyName = currencyName;
        this.chineseName = chineseName;
        this.exchangeRate = exchangeRate;
        this.updateTime = updateTime;
    }
}
