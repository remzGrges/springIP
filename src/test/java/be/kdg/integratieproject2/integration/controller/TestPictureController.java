package be.kdg.integratieproject2.integration.controller;

import be.kdg.integratieproject2.api.dto.PictureDto;
import be.kdg.integratieproject2.data.implementations.PictureRepository;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class TestPictureController {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PictureRepository pictureRepository;

    private MockMvc mvc;
    private PictureDto pictureDto;
    private HttpHeaders httpHeaders = new HttpHeaders();

    @Before
    public void setup() {
        httpHeaders.add("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsZWFuZGVyLmNvZXZvZXRAc3R1ZGVudC5rZGcuYmUiLCJleHAiOjE1MjA5MzIxNzN9.L8CUUmPR7Ceok1W8UMBc9X97n9dKunypzMRpoG_AxuM6ghcKbh5pZ9-juJ7Iy1SM_qi-GHdvuwmW-_Uu5PzSEA");
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();

        pictureDto = new PictureDto();
        pictureDto.setFilename("test");
        pictureDto.setFiletype("png");
        pictureDto.setValue("sdqfdsfs");
        pictureDto.setPictureId("1");
    }

    @After
    public void clean() {
        pictureRepository.delete(pictureDto.getPictureId());
    }

    @Test
    public void createPicture() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(pictureDto, PictureDto.class);

        mvc.perform(post("/pictures/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(httpHeaders)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void createPictureWrongObject() throws Exception {
        Gson gson = new Gson();
        pictureDto.setValue("");
        String json = gson.toJson(pictureDto, PictureDto.class);

        mvc.perform(post("/pictures/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(httpHeaders)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deletePicture() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(pictureDto, PictureDto.class);

        mvc.perform(post("/pictures/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(httpHeaders)
                .content(json));

        mvc.perform(get("/pictures/delete/" + pictureDto.getPictureId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(httpHeaders)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePictureWrongId() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(pictureDto, PictureDto.class);

        mvc.perform(post("/pictures/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(httpHeaders)
                .content(json));

        mvc.perform(get("/pictures/delete/" + 500)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(httpHeaders)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void getPicture() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(pictureDto, PictureDto.class);

        mvc.perform(post("/pictures/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(httpHeaders)
                .content(json));

        mvc.perform(get("/pictures/get/" + pictureDto.getPictureId()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"pictureId\":\"1\",\"filename\":\"test\",\"filetype\":\"png\",\"value\":\"sdqfdsfs\"}"));
    }

    @Test
    public void getPictureWithNull() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(pictureDto, PictureDto.class);

        mvc.perform(post("/pictures/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .headers(httpHeaders)
                .content(json));

        mvc.perform(get("/pictures/get/" + null))
                .andExpect(status().isNotFound());
    }
}
