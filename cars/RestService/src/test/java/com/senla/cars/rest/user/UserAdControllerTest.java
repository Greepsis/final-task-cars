package com.senla.cars.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.cars.serviceImpl.model.Ad;
import com.senla.cars.serviceImpl.model.Brand;
import com.senla.cars.serviceImpl.model.Model;
import com.senla.cars.serviceImpl.model.User;
import com.senla.cars.serviceImpl.repository.AdRepository;
import com.senla.cars.serviceImpl.repository.BrandRepository;
import com.senla.cars.serviceImpl.repository.ModelRepository;
import com.senla.cars.serviceImpl.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.yml")
@Rollback
public class UserAdControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void correctRegistrationAd_thenStatus_isCreated() throws Exception{
        mockMvc.perform(
                post("/api/user/ad")
                        .content(objectMapper.writeValueAsString(createRegistrationAdDto()))
                        .header("Authorization","Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void correctUpdateAd_thenStatus_isOk() throws Exception{
        HashMap<String,Object> registrationDto = createRegistrationAdDto();
        registrationDto.put("sellerName","Arkadi");
        registrationDto.put("mobilePhone", "+375332828902");
        registrationDto.put("engineVolume","3.5");
        Ad ad = createTestAd();
        mockMvc.perform(
                put("/api/user/ad/{id}",ad.getId())
                        .content(objectMapper.writeValueAsString(registrationDto))
                        .header("Authorization","Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ad.getId()))
                .andExpect(jsonPath("$.data.sellerName").value(registrationDto.get("sellerName")))
                .andExpect(jsonPath("$.data.mobilePhone").value(registrationDto.get("mobilePhone")))
                .andExpect(jsonPath("$.data.engineVolume").value(registrationDto.get("engineVolume")));
    }

    @Test
    @Transactional
    public void correctDeactivationAd_thenStatus_isOk() throws Exception{
        Ad ad = createTestAd();
        mockMvc.perform(
                put("/api/user/ad/{id}/deactivation",ad.getId())
                        .header("Authorization","Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.deactivated").value("true"));
    }

    @Test
    @Transactional
    public void deactivationAd_withNonexistentAdId_thenNotFoundException_thenStatus_isNotFound() throws Exception {
        Ad ad = createTestAd();
        Integer id = 6;
        mockMvc.perform(
                put("/api/user/ad/{id}/deactivation",id)
                        .header("Authorization","Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.message").value("Ad not found".toUpperCase(Locale.ROOT)));
    }



    private String getUserToken() throws Exception{
        HashMap<String,String> registrationDto = new HashMap<>();
        registrationDto.put("password","userUser1515");
        registrationDto.put("email", "user15@gmail.com");
        ResultActions resultActions = mockMvc.perform(
                        post("/api/auth")
                                .content(objectMapper.writeValueAsString(registrationDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.data.token").isNotEmpty());
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String dataAuth = jsonParser.parseMap(resultActions.andReturn().getResponse().getContentAsString()).get("data").toString();
        String[] data = dataAuth.split(",");
        String resultToken = data[1];
        return resultToken.substring(7,resultToken.length() - 1);
    }


    @Before
    public void createCorrectTestUser(){
        User user = new User(null,"user15@gmail.com","$2a$10$tY3IWPwLiIU.F4czEvnjKejuo9/UIZ384ty8.65HM4cWBkgAfGMRG",
                "USER",null,null);
        userRepository.save(user);
    }


    @Before
    public void createCorrectTestModelAndBrand(){
        Brand brand = new Brand(null,"Jeep");
        brandRepository.save(brand);
        Model model = new Model(null,"Wrangler",brand);
        modelRepository.save(model);
    }

    private HashMap<String,Object> createRegistrationAdDto(){
        HashMap<String,Object> registrationDto = new HashMap<>();
        registrationDto.put("sellerName","Ivan");
        registrationDto.put("mobilePhone", "+375292668902");
        registrationDto.put("modelName","Wrangler");
        registrationDto.put("yearIssue","2012");
        registrationDto.put("mileage","54000");
        registrationDto.put("engineVolume","2.5");
        registrationDto.put("engineType","Petrol");
        registrationDto.put("transmission","Machine");
        registrationDto.put("region","Gomal");
        registrationDto.put("customsClearance","true");
        registrationDto.put("exchange","true");
        registrationDto.put("price","54000");
        return registrationDto;
    }

    private Ad createTestAd(){
        User user = userRepository.getUserByEmail("user15@gmail.com");
        Model model = modelRepository.findModelByName("Wrangler");
        Ad ad = new Ad(null,user,"Ivan","+375292668902", model,
                        2012,54000,2.5, "Petrol","Machine","Gomal",
                        true,true,54000, LocalDateTime.now(), false);
        return adRepository.save(ad);
    }


}
