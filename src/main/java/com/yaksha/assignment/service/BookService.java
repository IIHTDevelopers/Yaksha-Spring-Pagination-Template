package com.yaksha.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yaksha.assignment.entity.Book;
import com.yaksha.assignment.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	// Method to fetch all books with pagination
	public Page<Book> getAllBooks(Pageable pageable) {
		return bookRepository.findAll(pageable);
	}

	// Method to fetch a book by its ID
	public Book getBookById(Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	// Method to fetch books by title with pagination
	public Page<Book> getBooksByTitle(String title, Pageable pageable) {
		return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
	}

	// Method to fetch books with a rating above a certain threshold with pagination
	public Page<Book> getBooksByRatingAbove(double rating, Pageable pageable) {
		return bookRepository.findBooksByRatingAbove(rating, pageable);
	}

	// Method to fetch books by author with pagination
	public Page<Book> getBooksByAuthor(String author, Pageable pageable) {
		return bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
	}

	// Method to create a new book
	public Book createBook(Book book) {
		return bookRepository.save(book);
	}
}
