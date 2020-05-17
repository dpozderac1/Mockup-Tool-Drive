package com.example.demo;


import com.example.demo.Controllers.GSPEC_DocumentController;
import com.example.demo.Controllers.ProjectController;
import com.example.demo.ErrorMessageHandling.ApiError;
import com.example.demo.Models.*;
import com.example.demo.Repositories.GSPEC_DocumentRepository;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.Services.GSPEC_DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testGetGSPECs() throws Exception{

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        List<GSPEC_Document> gspecs = Arrays.asList(gspec);
        given(gspec_documentService.getAllGSPECs()).willReturn(new ResponseEntity<>(gspecs, HttpStatus.OK));

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

        given(gspec_documentService.getOneGSPEC(1L)).willReturn(new ResponseEntity<>(gspec, HttpStatus.OK));

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
        given(gspec_documentService.allGSPECsOfMockup(1L)).willReturn(new ResponseEntity<>(gspec_documents, HttpStatus.OK));

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
                .andExpect(status().isOk());
    }

    @Test
    public void testPostMockup() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        given(gspec_documentService.newGSPEC(ArgumentMatchers.any(GSPEC_Document.class))).willReturn(new ResponseEntity<>(gspec, HttpStatus.CREATED));

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

        given(gspec_documentService.addOrReplaceGSPEC(ArgumentMatchers.any(GSPEC_Document.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity<>(gspec_document, HttpStatus.OK));

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

    //Error handling

    @Test
    public void testGetGSPECByIDDoNotExist() throws Exception{
        List<String> errors = new ArrayList<>();
        errors.add("GSPEC document with id " + '1' + " does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Object Not Found", errors);
        given(gspec_documentService.getOneGSPEC(1L)).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .get("/gspec_document/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("GSPEC document with id " + '1' + " does not exist!"));
    }

    @Test
    public void testGetGSPECsOfMockupDoNotExist() throws Exception{
        List<String> errors = new ArrayList<>();
        errors.add("Mockup with id " + '1' + " does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Object Not Found", errors);
        given(gspec_documentService.allGSPECsOfMockup(1L)).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .get("/GSPEC_Documents/mockup/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Mockup with id " + '1' + " does not exist!"));

    }

    @Test
    public void handleMethodArgumentNotValid() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec_document = new GSPEC_Document(null, "PDF", null, format.parse( "2020-6-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        mvc.perform(MockMvcRequestBuilders
                .put("/addOrUpdateGSPEC_Document/{id}", 1)
                .content(asJsonString(gspec_document))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("The date should be in the past or present date!"));

    }

    @Test
    public void handleHttpRequestMethodNotSupported() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .put("/delete/gspec_document/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("PUT method is not supported for this request. Supported methods are DELETE "));

    }

    @Test
    public void handleObjectAlreadyExistsException() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec_document = new GSPEC_Document(null, "PDF", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        gspec_document.setID(1L);

        List<String> errors = new ArrayList<>();
        errors.add("GSPEC document with id 1 already exists!");
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Object Already Exists", errors);

        given(gspec_documentService.newGSPEC(ArgumentMatchers.any(GSPEC_Document.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .post("/addGSPEC_Document")
                .content(asJsonString(gspec_document))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("GSPEC document with id 1 already exists!"));
    }

    @Test
    public void addGSPECDocumentRestTemplate() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        HttpEntity<GSPEC_Document> request = new HttpEntity<>(gspec);
        given(restTemplate.postForObject("http://online-testing/addGSPECDocument", request, GSPEC_Document.class)).willReturn(gspec);
        given(gspec_documentService.newGSPEC(ArgumentMatchers.any(GSPEC_Document.class))).willReturn(new ResponseEntity(gspec, HttpStatus.CREATED));

        mvc.perform(MockMvcRequestBuilders
                .post("/addGSPEC_Document")
                .content(asJsonString(gspec))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("GSPEC"));
    }

    @Test
    public void deleteGSPECDocumentRestTemplate() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        gspec.setID(1L);

        restTemplate.delete("http://online-testing/GSPECDocument/{id}", gspec.getID());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","GSPEC document successfully deleted!");
        //given(gspec_documentService.deleteOneGSPEC(ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(jsonObject.toString(), HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .delete("/delete/gspec_document/{id}", 1)
                .content(asJsonString(gspec))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("GSPEC document successfully deleted!"));


    }

    @Test
    public void updateGSPECDocumentRestTemplate() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        gspec.setID(1L);

        GSPEC_Document gspec_new = new GSPEC_Document(null, "GSPEC_new", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        HttpEntity<GSPEC_Document> request = new HttpEntity<>(gspec_new);
        restTemplate.put("http://online-testing/updateGSPECDocument/{id}", request, gspec.getID());

        given(gspec_documentService.addOrReplaceGSPEC(ArgumentMatchers.any(GSPEC_Document.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(gspec_new, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .put("/addOrUpdateGSPEC_Document/{id}", 1)
                .content(asJsonString(gspec))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("GSPEC_new"));


    }

    @Test
    public void addGSPECDocumentRestTemplateAlreadyExists() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        List<String> errors = new ArrayList<>();
        errors.add("GSPEC Document already exists!");
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Object Already Exists", errors);

        HttpEntity<GSPEC_Document> request = new HttpEntity<>(gspec);
        given(restTemplate.postForObject("http://online-testing/addGSPECDocument", request, GSPEC_Document.class)).willReturn(gspec);
        given(gspec_documentService.newGSPEC(ArgumentMatchers.any(GSPEC_Document.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .post("/addGSPEC_Document")
                .content(asJsonString(gspec))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("GSPEC Document already exists!"));


    }

    @Test
    public void deleteGSPECDocumentRestTemplateDoesNotExist() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        gspec.setID(1L);

        restTemplate.delete("http://online-testing/GSPECDocument/{id}", gspec.getID());
        List<String> errors = new ArrayList<>();
        errors.add("GSPEC Document does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Object Not Found", errors);
        //given(gspec_documentService.deleteOneGSPEC(ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .delete("/delete/gspec_document/{id}", 1)
                .content(asJsonString(gspec))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("GSPEC Document does not exist!"));


    }

    @Test
    public void updateGSPECDocumentRestTemplateDoesNotExist() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GSPEC_Document gspec = new GSPEC_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        gspec.setID(1L);

        GSPEC_Document gspec_new = new GSPEC_Document(null, "GSPEC_new", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        HttpEntity<GSPEC_Document> request = new HttpEntity<>(gspec_new);
        restTemplate.put("http://online-testing/updateGSPECDocument/{id}", request, gspec.getID());
        List<String> errors = new ArrayList<>();
        errors.add("The GSPEC Document you want to update does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Object Not Found", errors);
        given(gspec_documentService.addOrReplaceGSPEC(ArgumentMatchers.any(GSPEC_Document.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .put("/addOrUpdateGSPEC_Document/{id}", 1)
                .content(asJsonString(gspec))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("The GSPEC Document you want to update does not exist!"));


    }

}
