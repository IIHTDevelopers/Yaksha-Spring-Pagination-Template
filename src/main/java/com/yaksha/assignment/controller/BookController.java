package com.yaksha.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yaksha.assignment.entity.Book;
import com.yaksha.assignment.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

	@Autowired
	private BookService bookService;

	// Endpoint to fetch all books with pagination
	@GetMapping
	public ResponseEntity<Page<Book>> getBooks(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Book> books = bookService.getAllBooks(pageable);
		return ResponseEntity.ok(books);
	}

	// Endpoint to fetch a book by its ID
	@GetMapping("/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable Long id) {
		Book book = bookService.getBookById(id);
		return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build(); // 404 Not Found
	}

	// Endpoint to fetch books by title with pagination
	@GetMapping("/search/title/{title}")
	public ResponseEntity<Page<Book>> getBooksByTitle(@PathVariable String title,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Book> books = bookService.getBooksByTitle(title, pageable);
		return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(books);
	}

	// Endpoint to fetch books with a rating above a certain threshold with
	// pagination
	@GetMapping("/search/rating/{rating}")
	public ResponseEntity<Page<Book>> getBooksByRatingAbove(@PathVariable double rating,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Book> books = bookService.getBooksByRatingAbove(rating, pageable);
		return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(books);
	}

	// Endpoint to fetch books by author with pagination
	@GetMapping("/search/author/{author}")
	public ResponseEntity<Page<Book>> getBooksByAuthor(@PathVariable String author,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Book> books = bookService.getBooksByAuthor(author, pageable);
		return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(books);
	}
}
