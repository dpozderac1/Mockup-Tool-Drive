package com.example.online_testing;

import com.example.online_testing.Controllers.GSPECDocumentController;
import com.example.online_testing.ErrorHandling.ApiError;
import com.example.online_testing.Models.GSPECDocument;
import com.example.online_testing.Services.GSPECDocumentService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static com.example.online_testing.BrowserControllerTests.asJsonString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GSPECDocumentController.class)
public class GSPECDocumentControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GSPECDocumentService gspecDocumentService;

    @Test
    public void deleteGSPECDocumentExists() throws Exception
    {
        JSONObject jo = new JSONObject();
        jo.put("message", "GSPEC Document is successfully deleted!");
        given(gspecDocumentService.deleteGSPECDocumentByID(Long.valueOf(1))).willReturn(new ResponseEntity(jo.toString(), HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders.delete("/GSPECDocument/{id}", 1) )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("GSPEC Document is successfully deleted!"));
    }

    @Test
    public void deleteGSPECDocumentDoesNotExist() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("GSPEC Document does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(gspecDocumentService.deleteGSPECDocumentByID(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders.delete("/GSPECDocument/{id}", 1) )
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("GSPEC Document does not exist!"));
    }

    @Test
    public void createGSPECDocumentDoesExist() throws Exception
    {

        GSPECDocument gspecDocument = new GSPECDocument("Document11", null);

        given(this.gspecDocumentService.saveGSPECDocument(ArgumentMatchers.any(GSPECDocument.class))).willReturn(new ResponseEntity(gspecDocument, HttpStatus.CREATED));

        mvc.perform(MockMvcRequestBuilders
                .post("/addGSPECDocument")
                .content(asJsonString(gspecDocument))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(gspecDocument.getName()));
    }

    @Test
    public void createGSPECDocumentValidationFailed() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("GSPEC document name must be between 5 and 30 characters");
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation Failed", errors);
        GSPECDocument gspecDocument = new GSPECDocument("D", null);

        given(this.gspecDocumentService.saveGSPECDocument(ArgumentMatchers.any(GSPECDocument.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .post("/addGSPECDocument")
                .content(asJsonString(gspecDocument))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("GSPEC document name must be between 5 and 30 characters"));
    }

    @Test
    public void updateGSPECDocumentDoesExist() throws Exception
    {
        GSPECDocument gspecDocument = new GSPECDocument("Document 11", null);
        gspecDocument.setID(Long.valueOf(1));

        GSPECDocument gspecDocument1 = new GSPECDocument("Document 12", null);

        given(this.gspecDocumentService.updateGSPECDocument(ArgumentMatchers.any(GSPECDocument.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(gspecDocument1, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateGSPECDocument/{id}", 1)
                .content(asJsonString(gspecDocument1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(gspecDocument1.getName()));
    }

    @Test
    public void updateGSPECDocumentDoesNotExist() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("The GSPEC Document you want to update does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        GSPECDocument gspecDocument = new GSPECDocument("Document 11", null);
        given(this.gspecDocumentService.updateGSPECDocument(ArgumentMatchers.any(GSPECDocument.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateGSPECDocument/{id}", 1)
                .content(asJsonString(gspecDocument))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("The GSPEC Document you want to update does not exist!"));
    }
}


