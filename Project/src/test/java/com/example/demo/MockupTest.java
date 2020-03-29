package com.example.demo;


import com.example.demo.Controllers.MockupController;
import com.example.demo.Controllers.ProjectController;
import com.example.demo.Models.Mockup;
import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import com.example.demo.Models.VersionNames;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.Services.MockupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MockupController.class)
public class MockupTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MockupRepository mockupRepository;

    @MockBean
    private VersionRepository versionRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private MockupService mockupService;

    @Test
    public void testGetMockups() throws Exception{

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Mockup mockup = new Mockup(null, "Mockup", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        List<Mockup> mockupi = Arrays.asList(mockup);
        given(mockupService.getAllMockups()).willReturn(mockupi);

        mvc.perform(get("/mockups")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].name").value(mockup.getName()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", hasSize(1)));

    }

    @Test
    public void testGetMockupByID() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Mockup mockup = new Mockup(null, "Mockup", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        given(mockupService.getOneMockup(1L)).willReturn(mockup);

        mvc.perform(MockMvcRequestBuilders
                .get("/mockup/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockup.getName()));
    }

    @Test
    public void testGetMockupsOfVersion() throws Exception{
        Version version = new Version(null, VersionNames.DESKTOP);
        version.setID(Long.valueOf(1));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Mockup mockup = new Mockup(version, "Mockup", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        mockup.setID(Long.valueOf(2));

        List<Mockup> mockups = Arrays.asList(mockup);
        given(mockupService.allMockupsOfVersion(1L)).willReturn(mockups);

        mvc.perform(MockMvcRequestBuilders
                .get("/mockups/version/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(mockup.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].file").value(mockup.getFile()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date_created").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date_modified").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accessed_date").isNotEmpty());
    }

    @Test
    public void deleteMockup() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/delete/mockup/{id}", 1))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testPostMockup() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Mockup mockup = new Mockup(null, "Mockup", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        given(mockupService.newMockup(ArgumentMatchers.any(Mockup.class))).willReturn(mockup);

        mvc.perform(MockMvcRequestBuilders
                .post("/addMockup")
                .content(asJsonString(mockup))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockup.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.file").value(mockup.getFile()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_created").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_modified").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessed_date").isNotEmpty());;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void updateMockup() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Mockup mockup = new Mockup(null, "Mockup", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        given(mockupService.addOrReplace(ArgumentMatchers.any(Mockup.class), ArgumentMatchers.anyLong())).willReturn(mockup);
        mockup.setID(Long.valueOf(1));

        mvc.perform( MockMvcRequestBuilders
                .put("/addOrUpdateMockup/{id}", 1)
                .content(asJsonString(mockup))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockup.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.versionId").value(mockup.getVersionId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.file").value(mockup.getFile()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_created").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_modified").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessed_date").isNotEmpty());
    }

}
