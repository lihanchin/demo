package com.example.johnsondemo.controller;

import com.example.johnsondemo.dto.CurrencyDTO;
import com.example.johnsondemo.exceptionHandler.NotFoundException;
import com.example.johnsondemo.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
class ControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
//    private Connection testConnection;
//    private String url;
    private String getCurrencyName;

    @MockBean
    private CurrencyService currencyService;

    private final CurrencyDTO testCurrencyDTO = new CurrencyDTO("KRW", "韓元", 0.0247);
    private final CurrencyDTO updateCurrencyDTO = new CurrencyDTO("KRW", "韓元", 0.0333);
    private final CurrencyDTO deleteCurrencyDTO = new CurrencyDTO("USD", "美元", 30.2500);
//    private final String sql = "CREATE TABLE CURRENCY (\n" +
//                                                       "ID  int NOT NULL AUTO_INCREMENT,\n" +
//                                                       "CHINESE_NAME  varchar(10),\n" +
//                                                       "CURRENCY_NAME  varchar(10),\n" +
//                                                       "EXCHANGE_RATE  DOUBLE PRECISION,\n" +
//                                                       "UPDATE_TIME  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"+
//                                                       "PRIMARY KEY (ID) );\n"+
//                               "INSERT INTO CURRENCY (CURRENCY_NAME , CHINESE_NAME , EXCHANGE_RATE)\n" +
//                               "VALUES ('USD', '美元', 30.2500);\n" +
//                               "INSERT INTO CURRENCY (CURRENCY_NAME , CHINESE_NAME , EXCHANGE_RATE )\n" +
//                               "VALUES ('HKD', '港幣', 3.8800);\n" +
//                               "INSERT INTO CURRENCY (CURRENCY_NAME , CHINESE_NAME , EXCHANGE_RATE )\n" +
//                               "VALUES ('JPY', '日圓', 0.2236);";

    @BeforeEach
    private void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        try {
//            Class.forName("org.h2.Driver");
//            url="jdbc:h2:mem:mockdb";
//            log.info("connect mockdb");
//            testConnection = DriverManager.getConnection(url,"sa","password");
//            Statement st = testConnection.createStatement();
//            log.info("add data");
//            st.executeUpdate(sql);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
    }

//    @AfterEach
//    public void cleanUpEach(){
//        try {
//            log.info("connection close");
//            testConnection.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }

   @Test
    void addNewCurrencySuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Mockito.doNothing().when(currencyService).createNewCurrency(testCurrencyDTO);
        RequestBuilder request = MockMvcRequestBuilders.post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testCurrencyDTO));

        mockMvc.perform(request).andExpect(status().isCreated());
    }

    @Test
    void failedToAddNewCurrency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Mockito.doThrow(new NotFoundException()).when(currencyService).createNewCurrency(testCurrencyDTO);
        RequestBuilder request = MockMvcRequestBuilders.post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testCurrencyDTO));

        mockMvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    void deleteCurrencySuccessfully() throws Exception {
        getCurrencyName = deleteCurrencyDTO.getCurrencyName();

        Mockito.doNothing().when(currencyService).deleteCurrency(getCurrencyName);
        RequestBuilder request = MockMvcRequestBuilders.delete("/delete")
                .param("currencyName", getCurrencyName);

        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    void failedToDeleteCurrency() throws Exception {
        getCurrencyName = deleteCurrencyDTO.getCurrencyName();

        Mockito.doThrow(new NotFoundException("尚無 " + getCurrencyName + " 相關資料")).when(currencyService).deleteCurrency(getCurrencyName);
        RequestBuilder request = MockMvcRequestBuilders.delete("/delete")
                .param("currencyName", getCurrencyName);

        mockMvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    void updateCurrencySuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Mockito.when(currencyService.updateCurrency(updateCurrencyDTO)).thenReturn(updateCurrencyDTO);
        RequestBuilder request = MockMvcRequestBuilders.put("/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateCurrencyDTO));

        mockMvc.perform(request).andExpect(status().isCreated());
        System.out.println(mockMvc.perform(request).andReturn().getResponse().getContentAsString());
    }

    @Test
    void failedToUpdateCurrency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CurrencyDTO failUpdate = new CurrencyDTO();
        failUpdate.setCurrencyName("KRW");

        Mockito.when(currencyService.updateCurrency(failUpdate)).thenThrow(new NotFoundException());
        RequestBuilder request = MockMvcRequestBuilders.put("/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(failUpdate));

        log.info("fail update");
        mockMvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    void searchCurrencySuccessfully() throws Exception {

        getCurrencyName = testCurrencyDTO.getCurrencyName();

        Mockito.when(currencyService.findCurrencyByName(getCurrencyName)).thenReturn(testCurrencyDTO);
        RequestBuilder request = MockMvcRequestBuilders.get("/search")
                .param("currencyName", getCurrencyName);

        mockMvc.perform(request).andExpect(status().isOk());
        System.out.println(mockMvc.perform(request).andReturn().getResponse().getContentAsString());

    }

    @Test
    void failedToSearchCurrency() throws Exception {

        getCurrencyName = testCurrencyDTO.getCurrencyName();

        Mockito.when(currencyService.findCurrencyByName(getCurrencyName)).thenThrow(new NotFoundException());
        RequestBuilder request = MockMvcRequestBuilders.get("/search")
                .param("currencyName", getCurrencyName);

        mockMvc.perform(request).andExpect(status().isOk());
        System.out.println(mockMvc.perform(request).andReturn().getResponse().getContentAsString());

    }

    @Test
    void searchAllCurrencies() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.get("/searchAll");

        mockMvc.perform(request).andExpect(status().isOk());
        System.out.println(mockMvc.perform(request).andReturn().getResponse().getContentAsString());

    }

    @Test
    void callCoindeskAPI() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.get("/callAPI");
        mockMvc.perform(request).andExpect(status().isOk());

        log.info("print response content");
        System.out.println(mockMvc.perform(request).andReturn().getResponse().getContentAsString());
    }

    @Test
    void parseCoindeskAPI() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.get("/parseJson");
        mockMvc.perform(request).andExpect(status().isOk());

        log.info("print response content");
        System.out.println(mockMvc.perform(request).andReturn().getResponse().getContentAsString());
    }
}