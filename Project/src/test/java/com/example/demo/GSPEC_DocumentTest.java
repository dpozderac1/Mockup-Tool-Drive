package com.example.demo;


import com.example.demo.Controllers.GSPEC_DocumentController;
import com.example.demo.Controllers.ProjectController;
import com.example.demo.Models.*;
import com.example.demo.Repositories.GSPEC_DocumentRepository;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.Services.GSPEC_DocumentService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GSPEC_DocumentController.class)
public class GSPEC_DocumentTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GSPEC_DocumentRepository gspec_documentRepository;

    @MockBean
    private MockupRepository mockupRepository;

    @MockBean
    private GSPEC_DocumentService gspec_documentService;

    @Test
    public void testGetGSPECs() throws Exception{

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        List<GSPEC_Document> gspecs = Arrays.asList(gspec);
        given(gspec_documentService.getAllGSPECs()).willReturn(gspecs);

        mvc.perform(get("/gspec_documents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].name").value(gspec.getName()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", hasSize(1)));

    }

    @Test
    public void testGetGSPECByID() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        given(gspec_documentService.getOneGSPEC(1L)).willReturn(gspec);

        mvc.perform(MockMvcRequestBuilders
                .get("/gspec_document/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(gspec.getName()));
    }

    @Test
    public void testGetGSPECsOfMockup() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Mockup mockup = new Mockup(null, "Mockup", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        mockup.setID(Long.valueOf(1));

        GSPEC_Document gspec_document = new GSPEC_Document(mockup, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        gspec_document.setID(Long.valueOf(2));

        List<GSPEC_Document> gspec_documents = Arrays.asList(gspec_document);
        given(gspec_documentService.allGSPECsOfMockup(1L)).willReturn(gspec_documents);

        mvc.perform(MockMvcRequestBuilders
                .get("/GSPEC_Documents/mockup/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(gspec_document.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].file").value(gspec_document.getFile()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date_created").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date_modified").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accessed_date").isNotEmpty());
    }

    @Test
    public void deleteGSPEC() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/delete/gspec_document/{id}", 1))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testPostMockup() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        given(gspec_documentService.newGSPEC(ArgumentMatchers.any(GSPEC_Document.class))).willReturn(gspec);

        mvc.perform(MockMvcRequestBuilders
                .post("/addGSPEC_Document")
                .content(asJsonString(gspec))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(gspec.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.file").value(gspec.getFile()))
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
    public void updateGSPEC() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec_document = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        given(gspec_documentService.addOrReplaceGSPEC(ArgumentMatchers.any(GSPEC_Document.class), ArgumentMatchers.anyLong())).willReturn(gspec_document);

        mvc.perform( MockMvcRequestBuilders
                .put("/addOrUpdateGSPEC_Document/{id}", 1)
                .content(asJsonString(gspec_document))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(gspec_document.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mockupId").value(gspec_document.getMockupId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.file").value(gspec_document.getFile()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_created").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_modified").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessed_date").isNotEmpty());
    }

}
