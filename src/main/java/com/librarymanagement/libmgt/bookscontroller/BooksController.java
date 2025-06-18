package com.librarymanagement.libmgt.bookscontroller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagement.libmgt.books.Status;
import com.librarymanagement.libmgt.booksdto.BooksDTO;
import com.librarymanagement.libmgt.booksdto.BooksResponseDTO;
import com.librarymanagement.libmgt.booksservice.BooksService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class BooksController {
	  
	@Autowired
	private BooksService booksService;
	
	@PostMapping("/create")	
    public ResponseEntity<BooksResponseDTO> create(@RequestBody @Valid BooksDTO booksdto) {
        return new ResponseEntity<>(booksService.createBook(booksdto), HttpStatus.CREATED);
    }

    @GetMapping("/getallBooks")
    public List<BooksResponseDTO> getAll(
            @RequestParam Optional<String> author,
            @RequestParam Optional<Status> status) {
        return booksService.getAllBooks(author, status);
    }
    
    @GetMapping("/{id}")
    public BooksResponseDTO getById(@PathVariable Long id) {
        return booksService.getBookById(id);
    }

    @PutMapping("/{id}")
    public BooksResponseDTO update(@PathVariable Long id, @RequestBody @Valid BooksDTO booksDTO) {
        return booksService.updateBook(id, booksDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        booksService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/getBooksAfterDate")
    public List<BooksResponseDTO> getBooksAfterDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return booksService.getBooksAfterDate(date);
    }
    
}
