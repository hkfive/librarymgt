package com.librarymanagement.libmgt.booksserviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.libmgt.books.Books;
import com.librarymanagement.libmgt.books.Status;
import com.librarymanagement.libmgt.booksdto.BooksDTO;
import com.librarymanagement.libmgt.booksdto.BooksResponseDTO;
import com.librarymanagement.libmgt.booksrepository.BooksRepository;
import com.librarymanagement.libmgt.booksservice.BooksService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BooksServiceImpl implements BooksService {
	
	@Autowired
	private BooksRepository booksRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public BooksResponseDTO createBook(BooksDTO booksDTO) {
		if (booksRepository.existsByIsbn(booksDTO.getIsbn())) {
            throw new IllegalArgumentException("ISBN already exists");
        }

		Books books = modelMapper.map(booksDTO, Books.class);
        books.setStatus(Status.AVAILABLE);
        return modelMapper.map(booksRepository.save(books), BooksResponseDTO.class);
    }
	
	@Override
	public List<BooksResponseDTO> getAllBooks(Optional<String> author, Optional<Status> status) {
	    Stream<Books> stream = booksRepository.findAll().stream();

	    if (author.isPresent()) {
	        stream = stream.filter(b -> b.getAuthor().equalsIgnoreCase(author.get()));
	    }
	    if (status.isPresent()) {
	        stream = stream.filter(b -> b.getStatus() == status.get());
	    }

	    return stream
	            .map(books -> modelMapper.map(books, BooksResponseDTO.class))
	            .collect(Collectors.toList());

	     
	}

	 @Override
	    public BooksResponseDTO getBookById(Long id) {
	        Books book = booksRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
	        return modelMapper.map(book, BooksResponseDTO.class);
	    }

	    @Override
	    public BooksResponseDTO updateBook(Long id, BooksDTO booksDTO) {
	        Books existing = booksRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
	        existing.setTopic(booksDTO.getTopic());
	        existing.setAuthor(booksDTO.getAuthor());
	        existing.setIsbn(booksDTO.getIsbn());
	        existing.setPublishedDate(booksDTO.getPublishedDate());
	        existing.setStatus(booksDTO.getStatus());
	        return modelMapper.map(booksRepository.save(existing), BooksResponseDTO.class);
	    }

	    @Override
	    public void deleteBook(Long id) {
	        Books book = booksRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
	        booksRepository.delete(book);
	    }

}
