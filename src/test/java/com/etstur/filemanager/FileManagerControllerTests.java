package com.etstur.filemanager;

import com.etstur.filemanager.dto.request.LoginRequestDTO;
import com.etstur.filemanager.dto.response.FileInformationResponseDTO;
import com.etstur.filemanager.dto.response.LoginResponseDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileManagerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static String jwt;
    private static long fileInformationId = 0;
    @Autowired
    private Gson gson;

    @BeforeEach
    public void setup() throws Exception {
        if(jwt == null){
            LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder().email("test@test.test").password("1").build();
            String firstName = "Test";
            String lastName = "User";

            MvcResult mvcResult = mockMvc.perform(post("/api/v1/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(loginRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName", is(firstName)))
                    .andExpect(jsonPath("$.lastName", is(lastName)))
                    .andDo(print())
                    .andReturn();

            LoginResponseDTO loginResponseDTO = gson.fromJson(mvcResult.getResponse().getContentAsString(), LoginResponseDTO.class);
            this.jwt = loginResponseDTO.getJwt();
        }
    }

    @Test
    @Order(1)
    public void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file","TEST.pdf",MediaType.MULTIPART_FORM_DATA.getType(),Files.readAllBytes(Path.of("test_file/TEST.pdf")));
        mockMvc.perform(multipart("/api/v1/upload-file")
                        .file(file)
                        .header("Authorization","Bearer "+this.jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(2)
    public void testGetFileInformation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/get-file-information")
                        .header("Authorization","Bearer "+this.jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<FileInformationResponseDTO> fileInformationResponseDTO = gson.fromJson(mvcResult.getResponse().getContentAsString(), new TypeToken<List<FileInformationResponseDTO>>(){}.getType());
        this.fileInformationId = fileInformationResponseDTO.get(0).getFileInformationId();
    }

    @Test
    @Order(3)
    public void testGetFileById() throws Exception {
        byte[] fileArr = Files.readAllBytes(Path.of("test_file/TEST.pdf"));
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/get-file-by-id")
                        .header("Authorization","Bearer "+this.jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fileInformationId",String.valueOf(this.fileInformationId)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        byte[] responseArr = mvcResult.getResponse().getContentAsByteArray();
        assertEquals(fileArr.length, responseArr.length);
    }

    @Test
    @Order(4)
    public void testDeleteFileById() throws Exception {
        mockMvc.perform(delete("/api/v1/delete-file-by-id")
                        .header("Authorization","Bearer "+this.jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fileInformationId",String.valueOf(this.fileInformationId)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
