package com.librarymanagement.libmgt.books;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Books {
	
	@Id
	@GeneratedValue	(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;
	
	@Column(name = "Topic", nullable = false)
	private String topic;
	
	@Column(name = "Author", nullable = false)
	private String author;
	

    @Column(name = "ISBN",unique = true,nullable = false)
    private String isbn;
    
	@Column(name = "PublishedDate", nullable = false)
	private LocalDate publishedDate;
	
	@Column(name = "Status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	
}