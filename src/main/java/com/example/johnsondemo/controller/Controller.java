package com.example.johnsondemo.controller;

import com.example.johnsondemo.dto.CurrencyDTO;
import com.example.johnsondemo.dto.ParseJsonDTO;
import com.example.johnsondemo.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class Controller {

    final CurrencyService currencyService;

    public Controller(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNewCurrency(@RequestBody CurrencyDTO currencyDTO){

        log.info("add new currency - " + currencyDTO.getCurrencyName().toUpperCase());
        currencyService.createNewCurrency(currencyDTO);
        return ResponseEntity.status(201).build();

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCurrency(@RequestParam String currencyName){

        log.info("delete currency - " + currencyName.toUpperCase());
        currencyService.deleteCurrency(currencyName);
        return ResponseEntity.status(204).build();

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCurrency(@RequestBody CurrencyDTO currencyDTO){

        log.info("update currency - " + currencyDTO.getCurrencyName().toUpperCase());
        return ResponseEntity.status(201).body(currencyService.updateCurrency(currencyDTO));

    }

    @GetMapping("/search")
    public ResponseEntity<CurrencyDTO> searchCurrency(@RequestParam(value = "currencyName") String currencyName){
        return ResponseEntity.ok().body(currencyService.findCurrencyByName(currencyName));
    }

    @GetMapping("/searchAll")
    public ResponseEntity<List<CurrencyDTO>> searchCurrency(){
        return ResponseEntity.ok().body(currencyService.findAllCurrencies());
    }

    @GetMapping("/callAPI")
    public String callCoindeskAPI(){
        return currencyService.coindeskAPI();
    }

    @GetMapping("/parseJson")
    public List<ParseJsonDTO> parseCoindeskAPI(){
        return currencyService.updateTimeAndParserAPI();
    }

}
