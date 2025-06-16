package com.librarymanagement.libmgt.booksservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.librarymanagement.libmgt.books.Books;
import com.librarymanagement.libmgt.books.Status;
import com.librarymanagement.libmgt.booksdto.BooksDTO;
import com.librarymanagement.libmgt.booksdto.BooksResponseDTO;
import com.librarymanagement.libmgt.booksrepository.BooksRepository;
import com.librarymanagement.libmgt.booksserviceImpl.BooksServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BooksServiceImplTest {
	@Mock
    private BooksRepository repository;
	
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BooksServiceImpl service;
    
    @Test
    void testCreateBook_Success() {
        BooksDTO booksDTO = new BooksDTO();
        booksDTO.setAuthor("JK Rowling");
        booksDTO.setIsbn("1234567890");
        booksDTO.setPublishedDate(LocalDate.now());
        booksDTO.setTopic("Mystery");

        Books bookEntity = new Books();
        bookEntity.setAuthor("JK Rowling");
        bookEntity.setIsbn("1234567890");
        bookEntity.setPublishedDate(LocalDate.now());
        bookEntity.setTopic("Mystery");
        bookEntity.setStatus(Status.AVAILABLE);

        BooksResponseDTO expectedResponse = new BooksResponseDTO();
        expectedResponse.setAuthor("JK Rowling");
        expectedResponse.setIsbn("1234567890");
        expectedResponse.setTopic("Mystery");

        when(repository.existsByIsbn("1234567890")).thenReturn(false);
        when(modelMapper.map(booksDTO, Books.class)).thenReturn(bookEntity);
        when(repository.save(bookEntity)).thenReturn(bookEntity);
        when(modelMapper.map(bookEntity, BooksResponseDTO.class)).thenReturn(expectedResponse);

        BooksResponseDTO response = service.createBook(booksDTO);

        assertEquals("1234567890", response.getIsbn());
        verify(repository).save(bookEntity);
    }
    
    @Test
    void testCreateBook_Fails_WhenIsbnAlreadyExists() {

        BooksDTO booksDTO = BooksDTO.builder()
            .id(1L)
            .topic("Science")
            .author("Charles Dickens")
            .isbn("Isbn_Duplicate")
            .publishedDate(LocalDate.of(2020, 1, 1))
            .status(Status.AVAILABLE)
            .build();

        when(repository.existsByIsbn("Isbn_Duplicate")).thenReturn(true);

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> service.createBook(booksDTO)
        );

        assertEquals("ISBN already exists", exception.getMessage());
        verify(repository).existsByIsbn("Isbn_Duplicate");
        verify(repository, org.mockito.Mockito.never()).save(org.mockito.Mockito.any());
    }
    
    
    @Test
    void testGetAllBooks_FilteredByAuthorAndStatus_Success() {

        Books book1 = Books.builder()
            .id(1L)
            .topic("Java")
            .author("JK Rowling")
            .isbn("1111")
            .publishedDate(LocalDate.of(2020, 1, 1))
            .status(Status.AVAILABLE)
            .build();

        Books book2 = Books.builder()
            .id(2L)
            .topic("Spring")
            .author("JK Rowling")
            .isbn("2222")
            .publishedDate(LocalDate.of(2021, 1, 1))
            .status(Status.AVAILABLE)
            .build();

        List<Books> booksList = List.of(book1, book2);
        when(repository.findAll()).thenReturn(booksList);

        BooksResponseDTO dto1 = BooksResponseDTO.builder()
            .id(1L)
            .topic("Java")
            .author("JK Rowling")
            .isbn("1111")
            .publishedDate(LocalDate.of(2020, 1, 1))
            .status(Status.AVAILABLE)
            .build();

        BooksResponseDTO dto2 = BooksResponseDTO.builder()
            .id(2L)
            .topic("Spring")
            .author("JK Rowling")
            .isbn("2222")
            .publishedDate(LocalDate.of(2021, 1, 1))
            .status(Status.AVAILABLE)
            .build();

        when(modelMapper.map(book1, BooksResponseDTO.class)).thenReturn(dto1);
        when(modelMapper.map(book2, BooksResponseDTO.class)).thenReturn(dto2);

        List<BooksResponseDTO> result = service.getAllBooks(Optional.of("JK Rowling"), Optional.of(Status.AVAILABLE));

        assertEquals(2, result.size());
        assertEquals("JK Rowling", result.get(0).getAuthor());
        assertEquals(Status.AVAILABLE, result.get(1).getStatus());
        verify(repository).findAll();
    }
    @Test
    void testGetAllBooks_FilteredByAuthorOnly_Success() {
        Books book1 = Books.builder()
            .id(1L)
            .topic("Java")
            .author("JK Rowling")
            .isbn("1111")
            .publishedDate(LocalDate.of(2020, 5, 10))
            .status(Status.AVAILABLE)
            .build();

        Books book2 = Books.builder()
            .id(2L)
            .topic("Python")
            .author("Shakesphere")
            .isbn("2222")
            .publishedDate(LocalDate.of(2021, 6, 20))
            .status(Status.AVAILABLE)
            .build();

        when(repository.findAll()).thenReturn(List.of(book1, book2));

        BooksResponseDTO dto1 = BooksResponseDTO.builder()
            .id(1L)
            .topic("Java")
            .author("JK Rowling")
            .isbn("1111")
            .publishedDate(LocalDate.of(2020, 5, 10))
            .status(Status.AVAILABLE)
            .build();

        when(modelMapper.map(book1, BooksResponseDTO.class)).thenReturn(dto1);

        List<BooksResponseDTO> result = service.getAllBooks(Optional.of("JK Rowling"), Optional.empty());

        assertEquals(1, result.size());
        assertEquals("JK Rowling", result.get(0).getAuthor());
        verify(repository).findAll();
    }
    
    @Test
    void testGetAllBooks_FilteredByStatusOnly_Success() {

        Books book1 = Books.builder()
            .id(1L)
            .topic("Java")
            .author("JK Rowling")
            .isbn("1111")
            .publishedDate(LocalDate.of(2020, 5, 10))
            .status(Status.AVAILABLE)
            .build();

        Books book2 = Books.builder()
            .id(2L)
            .topic("Python")
            .author("Shakesphere")
            .isbn("2222")
            .publishedDate(LocalDate.of(2021, 6, 20))
            .status(Status.BORROWED)
            .build();

        when(repository.findAll()).thenReturn(List.of(book1, book2));

        BooksResponseDTO dto1 = BooksResponseDTO.builder()
            .id(1L)
            .topic("Java")
            .author("JK Rowling")
            .isbn("1111")
            .publishedDate(LocalDate.of(2020, 5, 10))
            .status(Status.AVAILABLE)
            .build();

        when(modelMapper.map(book1, BooksResponseDTO.class)).thenReturn(dto1);

        List<BooksResponseDTO> result = service.getAllBooks(Optional.empty(), Optional.of(Status.AVAILABLE));

        assertEquals(1, result.size());
        assertEquals(Status.AVAILABLE, result.get(0).getStatus());
        verify(repository).findAll();
    }
}

