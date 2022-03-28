package com.senla.cars.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.cars.serviceImpl.model.*;
import com.senla.cars.serviceImpl.repository.*;
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
public class UserBookmarkControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void createCorrectBookmark_thenStatus_isCreated() throws Exception {
        Ad ad = createTestAd();
        mockMvc.perform(
                post("/api/user/bookmark/{id}",ad.getId())
                        .header("Authorization","Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.ad.id").value(ad.getId()));
    }

    @Test
    @Transactional
    public void correctDeleteBookmark_thenStatus_isOk() throws Exception {
        Ad ad = createTestAd();
        Bookmark bookmark = createTestBookmark(ad.getId());
        mockMvc.perform(
                delete("/api/user/bookmark/{id}",ad.getId())
                        .header("Authorization","Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Bookmark with adId:" + ad.getId() + " can be deleted"));
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

    private Bookmark createTestBookmark(Integer adId){
        Ad ad = adRepository.getById(adId);
        User user = userRepository.getUserByEmail("user15@gmail.com");
        Bookmark bookmark = new Bookmark(ad.getId(),user.getId(),ad,user);
        bookmarkRepository.save(bookmark);
        return bookmark;
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
