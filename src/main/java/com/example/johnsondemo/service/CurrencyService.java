package com.example.johnsondemo.service;

import com.example.johnsondemo.dto.CurrencyDTO;
import com.example.johnsondemo.dto.ParseJsonDTO;

import java.util.List;

public interface CurrencyService {

    void createNewCurrency(CurrencyDTO currencyDTO);

    void deleteCurrency(String currencyName);

    List<CurrencyDTO> findAllCurrencies();

    CurrencyDTO findCurrencyByName(String currencyName);

    CurrencyDTO updateCurrency(CurrencyDTO currencyDTO);

    String coindeskAPI();

    List<ParseJsonDTO> updateTimeAndParserAPI();

}
