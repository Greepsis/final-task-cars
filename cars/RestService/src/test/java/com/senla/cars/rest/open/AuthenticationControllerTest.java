package com.senla.cars.rest.open;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.cars.serviceImpl.model.User;
import com.senla.cars.serviceImpl.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.yml")
public class AuthenticationControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;




    @Test
    @Transactional
    public void correctRegistrationUser_thenStatus_isCreated() throws Exception{
        HashMap<String,String> registrationDto = new HashMap<>();
        registrationDto.put("password","userUser1515");
        registrationDto.put("email","user15@gmail.com");

        mockMvc.perform(
                post("/api/auth/registration")
                        .content(objectMapper.writeValueAsString(registrationDto))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value(registrationDto.get("email")));

    }

    @Test
    @Transactional
    public void givenEmail_whenGetEmailAlreadyExists_thenStatus_isBadRequest() throws Exception{
        HashMap<String,String> registrationDto = new HashMap<>();
        registrationDto.put("password","userUser1515");
        registrationDto.put("email", createCorrectTestUser().getEmail());
        mockMvc.perform(
                        post("/api/auth/registration")
                                .content(objectMapper.writeValueAsString(registrationDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void correctLoginUser_thenStatus_isOk() throws Exception{
        HashMap<String,String> registrationDto = new HashMap<>();
        registrationDto.put("password","userUser1515");
        registrationDto.put("email", createCorrectTestUser().getEmail());
        mockMvc.perform(
                        post("/api/auth")
                                .content(objectMapper.writeValueAsString(registrationDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").isNotEmpty());

    }
    @Test
    @Transactional
    public void givenLogin_whenInvalidEmailOrPassword_thenStatus_isBadRequest() throws Exception{
        HashMap<String,String> registrationDto = new HashMap<>();
        registrationDto.put("password","userUser1515");
        registrationDto.put("email","user15@gmail.com");
        mockMvc.perform(
                        post("/api/auth")
                                .content(objectMapper.writeValueAsString(registrationDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    @Transactional
    public void givenLogin_whenBlockedEmailException_thenMessage_thenStatus_isLocked() throws Exception{
        HashMap<String,String> registrationDto = new HashMap<>();
        registrationDto.put("password","userUser1515");
        registrationDto.put("email", createBlockingUser().getEmail());
        mockMvc.perform(
                        post("/api/auth")
                                .content(objectMapper.writeValueAsString(registrationDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.message").value("User has been blocked".toUpperCase(Locale.ROOT)));
    }

    @Test
    @Transactional
    public void givenLogin_whenDeletedUserException_thenMessage_thenStatus_isBadRequest() throws Exception{
        HashMap<String,String> registrationDto = new HashMap<>();
        registrationDto.put("password","userUser1515");
        registrationDto.put("email", createDeletionUser().getEmail());
        mockMvc.perform(
                        post("/api/auth")
                                .content(objectMapper.writeValueAsString(registrationDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.message").value("User has been deleted".toUpperCase(Locale.ROOT)));
    }


    private User createCorrectTestUser(){
        User user = new User(null,"user15@gmail.com","$2a$10$tY3IWPwLiIU.F4czEvnjKejuo9/UIZ384ty8.65HM4cWBkgAfGMRG",
                "USER",null,null);
        return userRepository.save(user);
    }

    private User createBlockingUser(){
        User user = new User(null,"user15@gmail.com","$2a$10$tY3IWPwLiIU.F4czEvnjKejuo9/UIZ384ty8.65HM4cWBkgAfGMRG",
                "USER", LocalDate.now(),null);
        return userRepository.save(user);
    }

    private User createDeletionUser(){
        User user = new User(null,"user15@gmail.com","$2a$10$tY3IWPwLiIU.F4czEvnjKejuo9/UIZ384ty8.65HM4cWBkgAfGMRG",
                "USER",null,LocalDate.now());
        return userRepository.save(user);
    }

}
