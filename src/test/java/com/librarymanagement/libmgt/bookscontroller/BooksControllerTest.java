package com.librarymanagement.libmgt.bookscontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.librarymanagement.libmgt.books.Status;
import com.librarymanagement.libmgt.bookscontroller.BooksController;
import com.librarymanagement.libmgt.booksdto.BooksDTO;
import com.librarymanagement.libmgt.booksdto.BooksResponseDTO;
import com.librarymanagement.libmgt.booksserviceImpl.BooksServiceImpl;

@WebMvcTest(BooksController.class)
public class BooksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BooksServiceImpl booksService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateBook_ReturnsCreated() throws Exception {
        BooksDTO booksDTO = BooksDTO.builder()
            .author("Shakesphere")
            .isbn("1234567890")
            .topic("Topic A")
            .publishedDate(LocalDate.of(2022, 1, 1))
            .build();

        BooksResponseDTO responseDTO = BooksResponseDTO.builder()
            .author("Shakesphere")
            .isbn("1234567890")
            .topic("Topic A")
            .publishedDate(LocalDate.of(2022, 1, 1))
            .status(Status.AVAILABLE)
            .build();

        when(booksService.createBook(Mockito.any(BooksDTO.class))).thenReturn(responseDTO);

        MvcResult result = mockMvc.perform(post("/api/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booksDTO)))
            .andExpect(status().isCreated())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        BooksResponseDTO actualResponse = objectMapper.readValue(responseBody, BooksResponseDTO.class);

        assertEquals("Shakesphere", actualResponse.getAuthor());
        assertEquals("1234567890", actualResponse.getIsbn());
        assertEquals(Status.AVAILABLE, actualResponse.getStatus());
    }

    @Test
    void testGetAllBooks_WithFilters_ReturnsList() throws Exception {
        BooksResponseDTO book1 = BooksResponseDTO.builder()
            .id(1L)
            .author("Shakesphere")
            .isbn("1234")
            .topic("Tech")
            .publishedDate(LocalDate.of(2020, 5, 10))
            .status(Status.AVAILABLE)
            .build();

        when(booksService.getAllBooks(Optional.of("Shakesphere"), Optional.of(Status.AVAILABLE)))
            .thenReturn(List.of(book1));

        MvcResult result = mockMvc.perform(get("/api/getallBooks")
                .param("author", "Shakesphere")
                .param("status", "AVAILABLE"))
            .andExpect(status().isOk())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<BooksResponseDTO> responseList = objectMapper.readValue(responseBody, new TypeReference<List<BooksResponseDTO>>() {});

        assertEquals(1, responseList.size());
        assertEquals("Shakesphere", responseList.get(0).getAuthor());
        assertEquals(Status.AVAILABLE, responseList.get(0).getStatus());
    }
}

