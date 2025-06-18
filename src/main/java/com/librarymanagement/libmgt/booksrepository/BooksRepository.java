package com.librarymanagement.libmgt.booksrepository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.librarymanagement.libmgt.books.Books;

public interface BooksRepository extends JpaRepository<Books, Long>{

	boolean findByIsbn(String isbn);

}
