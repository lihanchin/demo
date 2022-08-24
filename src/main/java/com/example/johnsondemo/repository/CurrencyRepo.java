package com.example.johnsondemo.repository;

import com.example.johnsondemo.po.CurrencyPO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepo extends JpaRepository<CurrencyPO, Integer> {

    Optional<CurrencyPO> findCurrencyPOByCurrencyName(String currencyName);

    void deleteByCurrencyName(String currencyName);


}
