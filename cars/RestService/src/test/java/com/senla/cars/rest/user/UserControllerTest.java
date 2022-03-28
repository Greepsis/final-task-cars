package com.senla.cars.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.cars.serviceImpl.model.User;
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

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.yml")
@Rollback
public class UserControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void createCorrectTestUser(){
        User user = new User(null,"user15@gmail.com","$2a$10$tY3IWPwLiIU.F4czEvnjKejuo9/UIZ384ty8.65HM4cWBkgAfGMRG",
                "USER",null,null);
        userRepository.save(user);
    }

    @Test
    @Transactional
    public void correctDeletionUser_thenStatus_isOk() throws Exception {
        mockMvc.perform(
                delete("/api/user")
                        .header("Authorization","Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").value("User can be deleted"));
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

}
