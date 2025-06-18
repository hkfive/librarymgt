package com.librarymanagement.libmgt.booksservice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.librarymanagement.libmgt.books.Status;
import com.librarymanagement.libmgt.booksdto.BooksDTO;
import com.librarymanagement.libmgt.booksdto.BooksResponseDTO;

@Service
public interface BooksService {
	 BooksResponseDTO createBook(BooksDTO dto);
	 List<BooksResponseDTO> getAllBooks(Optional<String> author, Optional<Status> status);
	 BooksResponseDTO getBookById(Long id);
	 BooksResponseDTO updateBook(Long id, BooksDTO booksDTO);
	 void deleteBook(Long id);
	 public List<BooksResponseDTO> getBooksAfterDate(LocalDate date);
}
