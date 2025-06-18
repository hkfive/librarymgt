package com.librarymanagement.libmgt.booksdto;

import java.time.LocalDate;

import com.librarymanagement.libmgt.books.Books;
import com.librarymanagement.libmgt.books.Status;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BooksDTO {
	private Long id;
	@NotBlank
    private String topic;
	@NotBlank
    private String author;
    @NotBlank
    private String isbn;
    private LocalDate publishedDate;
    private Status status;
    
    
    public static BooksDTO fromEntity(Books books) {
        return BooksDTO.builder()
        		.id(books.getId())
                .topic(books.getTopic())
                .author(books.getAuthor())
                .isbn(books.getIsbn())
                .publishedDate(books.getPublishedDate())
                .status(books.getStatus())
                .build();
    }

    public Books toEntity() {
        return Books.builder()
        		.id(this.id)
                .topic(this.topic)
                .author(this.author)
                .isbn(this.isbn)
                .publishedDate(this.publishedDate)
                .status(this.status)
                .build();
    }
}
