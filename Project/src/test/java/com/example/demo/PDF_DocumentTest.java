package com.example.demo;

import com.example.demo.Controllers.PDF_DocumentController;
import com.example.demo.Controllers.ProjectController;
import com.example.demo.ErrorMessageHandling.ApiError;
import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import com.example.demo.Models.Project;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.PDF_DocumentRepository;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.Services.MockupService;
import com.example.demo.Services.PDF_DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
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
@WebMvcTest(PDF_DocumentController.class)
public class PDF_DocumentTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PDF_DocumentRepository pdf_documentRepository;

    @MockBean
    private MockupRepository mockupRepository;

    @MockBean
    private PDF_DocumentService pdf_documentService;

    @Test
    public void testGetPDFs() throws Exception{

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        PDF_Document pdf = new PDF_Document(null, "PDF", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        List<PDF_Document> pdfs = Arrays.asList(pdf);
        given(pdf_documentService.getAllPDFs()).willReturn(new ResponseEntity<>(pdfs, HttpStatus.OK));

        mvc.perform(get("/pdf_documents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].name").value(pdf.getName()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", hasSize(1)));

    }

    @Test
    public void testGetPDFByID() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        PDF_Document pdf = new PDF_Document(null, "PDF", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        given(pdf_documentService.getOnePDF(1L)).willReturn(new ResponseEntity<>(pdf, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .get("/pdf_document/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(pdf.getName()));
    }

    @Test
    public void testGetPDFsOfMockup() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Mockup mockup = new Mockup(null, "Mockup", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        mockup.setID(Long.valueOf(1));

        PDF_Document pdf_document = new PDF_Document(mockup, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        pdf_document.setID(Long.valueOf(2));

        List<PDF_Document> pdf_documents = Arrays.asList(pdf_document);
        given(pdf_documentService.allPDFsOfMockup(1L)).willReturn(new ResponseEntity<>(pdf_documents, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .get("/PDF_Documents/mockup/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(pdf_document.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].file").value(pdf_document.getFile()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date_created").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date_modified").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accessed_date").isNotEmpty());
    }

    @Test
    public void deletePDF() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/delete/pdf_document/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostPDF() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        PDF_Document pdf = new PDF_Document(null, "PDF", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        given(pdf_documentService.newPDF(ArgumentMatchers.any(PDF_Document.class))).willReturn(new ResponseEntity<>(pdf, HttpStatus.CREATED));
        mvc.perform(MockMvcRequestBuilders
                .post("/addPDF_Document")
                .content(asJsonString(pdf))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(pdf.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.file").value(pdf.getFile()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_created").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_modified").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessed_date").isNotEmpty());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void updatePDF() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        PDF_Document pdf_document = new PDF_Document(null, "GSPEC", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        given(pdf_documentService.addOrReplacePDF(ArgumentMatchers.any(PDF_Document.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity<>(pdf_document, HttpStatus.OK));
        pdf_document.setID(Long.valueOf(1));

        mvc.perform( MockMvcRequestBuilders
                .put("/addOrUpdatePDF_Document/{id}", 1)
                .content(asJsonString(pdf_document))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(pdf_document.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mockupId").value(pdf_document.getMockupId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.file").value(pdf_document.getFile()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_created").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_modified").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessed_date").isNotEmpty());
    }

    //Error handling

    @Test
    public void testGetPDFByIDDoNotExist() throws Exception{
        List<String> errors = new ArrayList<>();
        errors.add("PDF document with id " + '1' + " does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Object Not Found", errors);
        given(pdf_documentService.getOnePDF(1L)).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .get("/pdf_document/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("PDF document with id " + '1' + " does not exist!"));
    }

    @Test
    public void testGetPDFsOfMockupDoNotExist() throws Exception{
        List<String> errors = new ArrayList<>();
        errors.add("Mockup with id " + '1' + " does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Object Not Found", errors);
        given(pdf_documentService.allPDFsOfMockup(1L)).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .get("/PDF_Documents/mockup/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Mockup with id " + '1' + " does not exist!"));

    }

    @Test
    public void handleMethodArgumentNotValid() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        PDF_Document pdf_document = new PDF_Document(null, "PDF", null, format.parse( "2020-6-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));

        mvc.perform(MockMvcRequestBuilders
                .put("/addOrUpdatePDF_Document/{id}", 1)
                .content(asJsonString(pdf_document))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("The date should be in the past or present date!"));

    }

    @Test
    public void handleHttpRequestMethodNotSupported() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .put("/delete/pdf_document/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("PUT method is not supported for this request. Supported methods are DELETE "));

    }

    @Test
    public void handleObjectAlreadyExistsException() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        PDF_Document pdf = new PDF_Document(null, "PDF", null, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-17" ));
        pdf.setID(1L);

        List<String> errors = new ArrayList<>();
        errors.add("PDF document with id 1 already exists!");
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Object Already Exists", errors);

        given(pdf_documentService.newPDF(ArgumentMatchers.any(PDF_Document.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .post("/addPDF_Document")
                .content(asJsonString(pdf))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("PDF document with id 1 already exists!"));
    }

}
