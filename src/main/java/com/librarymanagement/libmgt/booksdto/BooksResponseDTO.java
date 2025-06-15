package com.librarymanagement.libmgt.booksdto;

import java.time.LocalDate;

import com.librarymanagement.libmgt.books.Status;

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
public class BooksResponseDTO {
    private Long id;
    private String topic;
    private String author;
    private String isbn;
    private LocalDate publishedDate;
    private Status status;
    
    public static BooksResponseDTO fromEntity(BooksDTO booksDTO) {
        return BooksResponseDTO.builder()
                .topic(booksDTO.getTopic())
                .author(booksDTO.getAuthor())
                .isbn(booksDTO.getIsbn())
                .publishedDate(booksDTO.getPublishedDate())
                .status(booksDTO.getStatus())
                .build();
    }

    public BooksDTO toEntity() {
        return BooksDTO.builder()
                .topic(this.topic)
                .author(this.author)
                .isbn(this.isbn)
                .publishedDate(this.publishedDate)
                .status(this.status)
                .build();
    }
}
