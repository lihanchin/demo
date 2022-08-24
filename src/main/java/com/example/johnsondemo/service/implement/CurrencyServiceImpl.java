package com.example.johnsondemo.service.implement;

import com.example.johnsondemo.Utility;
import com.example.johnsondemo.dto.CurrencyDTO;
import com.example.johnsondemo.dto.ParseJsonDTO;
import com.example.johnsondemo.exceptionHandler.NotFoundException;
import com.example.johnsondemo.po.CurrencyPO;
import com.example.johnsondemo.repository.CurrencyRepo;
import com.example.johnsondemo.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

import org.json.JSONObject;

@Slf4j
@Service
public class CurrencyServiceImpl implements CurrencyService {

    final CurrencyRepo currencyRepo;
    final Utility utility;

    public CurrencyServiceImpl(CurrencyRepo currencyRepo, Utility utility) {
        this.currencyRepo = currencyRepo;
        this.utility = utility;
    }

    @Override
    public void createNewCurrency(CurrencyDTO currencyDTO) {

        String upperCaseName = currencyDTO.getCurrencyName().toUpperCase();
        CurrencyPO currencyPO = new CurrencyPO(upperCaseName,
                currencyDTO.getChineseName(),
                currencyDTO.getExchangeRate(),
                new Timestamp(System.currentTimeMillis()));
        currencyRepo.save(currencyPO);
        log.info("save " + upperCaseName + " successfully");

    }

    @Override
    @Transactional
    public void deleteCurrency(String currencyName) {

        String upperCaseName = currencyName.toUpperCase();
        CurrencyPO currencyPO = currencyRepo.findCurrencyPOByCurrencyName(upperCaseName).orElseThrow(() -> new NotFoundException("尚無 " + upperCaseName + " 相關資料"));
        currencyRepo.delete(currencyPO);
        log.info("delete " + upperCaseName + " successfully");

    }

    @Override
    public List<CurrencyDTO> findAllCurrencies() {

        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        for (CurrencyPO currencyPO : currencyRepo.findAll()) {
            CurrencyDTO currencyDTO = new CurrencyDTO(currencyPO.getCurrencyName(),
                    currencyPO.getCurrencyName(),
                    currencyPO.getExchangeRate());
            currencyDTOList.add(currencyDTO);
        }
        return currencyDTOList;

    }

    @Override
    public CurrencyDTO findCurrencyByName(String currencyName) {

        String upperCaseName = currencyName.toUpperCase();
        CurrencyPO currencyPO = currencyRepo.findCurrencyPOByCurrencyName(upperCaseName).orElseThrow(() -> new NotFoundException("查無 " + upperCaseName + " 相關資料"));
        return new CurrencyDTO(currencyPO.getCurrencyName(), currencyPO.getChineseName(), currencyPO.getExchangeRate());

    }

    @Override
    public CurrencyDTO updateCurrency(CurrencyDTO currencyDTO) {

        String upperCaseName = currencyDTO.getCurrencyName().toUpperCase();
        CurrencyPO currencyPO = currencyRepo.findCurrencyPOByCurrencyName(upperCaseName).orElseThrow(() -> new NotFoundException("尚無 " + upperCaseName + " 相關資料，請先新增"));

        currencyPO.setCurrencyName(upperCaseName);
        currencyPO.setChineseName(currencyDTO.getChineseName());
        currencyPO.setExchangeRate(currencyDTO.getExchangeRate());
        currencyPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        log.info(upperCaseName + " update successfully");
        currencyRepo.save(currencyPO);
        BeanUtils.copyProperties(currencyPO,currencyDTO);
        return currencyDTO;

    }

    @Override
    public String coindeskAPI() {

        RestTemplate restTemplate = new RestTemplate();
        String coindeskUrl = "https://api.coindesk.com/v1/bpi/currentprice.json";
        return restTemplate.getForObject(coindeskUrl, String.class);

    }

    @Override
    public List<ParseJsonDTO> updateTimeAndParserAPI() {

        JSONObject currenciesInfo = new JSONObject(coindeskAPI()).getJSONObject("bpi");

        log.info("轉換資料 - 更新時間");
        Map<String, String> newTime = utility.updateNewTime(new Timestamp(System.currentTimeMillis()));

        log.info("轉換資料 - 美金");
        Map<String, Object> usdInfo = currenciesInfo.getJSONObject("USD").toMap();
        ParseJsonDTO usdResult = new ParseJsonDTO(usdInfo.get("code").toString(), "美金", usdInfo.get("rate").toString(), newTime);

        log.info("轉換資料 - 英鎊");
        Map<String, Object> gbpInfo = currenciesInfo.getJSONObject("GBP").toMap();
        ParseJsonDTO gbpResult = new ParseJsonDTO(gbpInfo.get("code").toString(), "英鎊", gbpInfo.get("rate").toString(), newTime);

        log.info("轉換資料 - 歐元");
        Map<String, Object> eurInfo = currenciesInfo.getJSONObject("EUR").toMap();
        ParseJsonDTO eurResult = new ParseJsonDTO(eurInfo.get("code").toString(), "歐元", eurInfo.get("rate").toString(), newTime);

        log.info("轉換資料完成");
        List<ParseJsonDTO> allResult = new ArrayList<>();
        allResult.add(usdResult);
        allResult.add(gbpResult);
        allResult.add(eurResult);

        return allResult;
    }

}
