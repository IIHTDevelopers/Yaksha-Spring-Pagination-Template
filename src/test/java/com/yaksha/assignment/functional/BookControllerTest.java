package com.yaksha.assignment.functional;

import static com.yaksha.assignment.utils.TestUtils.businessTestFile;
import static com.yaksha.assignment.utils.TestUtils.currentTest;
import static com.yaksha.assignment.utils.TestUtils.testReport;
import static com.yaksha.assignment.utils.TestUtils.yakshaAssert;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.yaksha.assignment.controller.BookController;
import com.yaksha.assignment.entity.Book;
import com.yaksha.assignment.service.BookService;
import com.yaksha.assignment.utils.JavaParserUtils;
import com.yaksha.assignment.utils.MasterData;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService; // Mock the BookService

	private Book book1;
	private Book book2;

	@BeforeEach
	public void setup() {
		// Setup test data
		book1 = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 4.5);
		book2 = new Book(2L, "1984", "George Orwell", 4.0);

		// Mock the service layer calls
		Mockito.when(bookService.getBookById(Mockito.anyLong())).thenReturn(book1);
		Mockito.when(bookService.getBooksByTitle(Mockito.anyString(), Mockito.any()))
				.thenReturn(new PageImpl<>(Arrays.asList(book1)));
		Mockito.when(bookService.getBooksByRatingAbove(Mockito.anyDouble(), Mockito.any()))
				.thenReturn(new PageImpl<>(Arrays.asList(book1)));
		Mockito.when(bookService.getBooksByAuthor(Mockito.anyString(), Mockito.any()))
				.thenReturn(new PageImpl<>(Arrays.asList(book2)));
		Mockito.when(bookService.getAllBooks(Mockito.any())).thenReturn(new PageImpl<>(Arrays.asList(book1, book2)));
	}

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	// Test to check GET request for all books with pagination
	@Test
	public void testGetBooks() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/books?page=0&size=2")
				.contentType("application/json").accept("application/json");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(), result.getResponse().getContentAsString() != null ? "true" : "false",
				businessTestFile);
	}

	// Test to check GET request for a book by ID
	@Test
	public void testGetBookById() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/books/1").contentType("application/json")
				.accept("application/json");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contentEquals(MasterData.asJsonString(book1)) ? "true"
						: "false"),
				businessTestFile);
	}

	// Test to check GET request for books by title with pagination
	@Test
	public void testGetBooksByTitle() throws Exception {
		String title = "The Great Gatsby";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/books/search/title/" + title + "?page=0&size=2").contentType("application/json")
				.accept("application/json");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseContent = result.getResponse().getContentAsString();
		yakshaAssert(currentTest(), (responseContent != null && !responseContent.isEmpty() ? "true" : "false"),
				businessTestFile);
	}

	// Test to check GET request for books by rating above a certain threshold with
	// pagination
	@Test
	public void testGetBooksByRatingAbove() throws Exception {
		double rating = 4.0;
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/books/search/rating/" + rating + "?page=0&size=2").contentType("application/json")
				.accept("application/json");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseContent = result.getResponse().getContentAsString();
		yakshaAssert(currentTest(), (responseContent != null && !responseContent.isEmpty() ? "true" : "false"),
				businessTestFile);
	}

	// Test to check GET request for books by author with pagination
	@Test
	public void testGetBooksByAuthor() throws Exception {
		String author = "George Orwell";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/books/search/author/" + author + "?page=0&size=2").contentType("application/json")
				.accept("application/json");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseContent = result.getResponse().getContentAsString();
		yakshaAssert(currentTest(), (responseContent != null && !responseContent.isEmpty() ? "true" : "false"),
				businessTestFile);
	}

	// Test to check if the 'BookController' class has @RestController annotation
	@Test
	public void testControllerAnnotations() throws IOException {
		boolean hasRestControllerAnnotation = JavaParserUtils.checkClassAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "RestController");
		System.out.println("Controller has @RestController annotation: " + hasRestControllerAnnotation);

		boolean hasRequestMappingAnnotation = JavaParserUtils.checkClassAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "RequestMapping");
		System.out.println("Controller has @RequestMapping annotation: " + hasRequestMappingAnnotation);

		// Using yakshaAssert at the end to verify all conditions
		yakshaAssert(currentTest(), (hasRestControllerAnnotation && hasRequestMappingAnnotation ? "true" : "false"),
				businessTestFile);
	}

	// Test to check method annotations in BookController class
	@Test
	public void testMethodAnnotations() throws IOException {
		boolean hasGetMappingAnnotationForGetBooks = JavaParserUtils.checkMethodAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooks", "GetMapping");
		System.out.println("getBooks method has @GetMapping annotation: " + hasGetMappingAnnotationForGetBooks);

		boolean hasGetMappingAnnotationForGetBookById = JavaParserUtils.checkMethodAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBookById", "GetMapping");
		System.out.println("getBookById method has @GetMapping annotation: " + hasGetMappingAnnotationForGetBookById);

		boolean hasGetMappingAnnotationForGetBooksByTitle = JavaParserUtils.checkMethodAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByTitle", "GetMapping");
		System.out.println(
				"getBooksByTitle method has @GetMapping annotation: " + hasGetMappingAnnotationForGetBooksByTitle);

		boolean hasGetMappingAnnotationForGetBooksByRating = JavaParserUtils.checkMethodAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByRatingAbove",
				"GetMapping");
		System.out.println("getBooksByRatingAbove method has @GetMapping annotation: "
				+ hasGetMappingAnnotationForGetBooksByRating);

		boolean hasGetMappingAnnotationForGetBooksByAuthor = JavaParserUtils.checkMethodAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByAuthor", "GetMapping");
		System.out.println(
				"getBooksByAuthor method has @GetMapping annotation: " + hasGetMappingAnnotationForGetBooksByAuthor);

		// Using yakshaAssert at the end to verify all conditions
		yakshaAssert(currentTest(),
				(hasGetMappingAnnotationForGetBooks && hasGetMappingAnnotationForGetBookById
						&& hasGetMappingAnnotationForGetBooksByTitle && hasGetMappingAnnotationForGetBooksByRating
						&& hasGetMappingAnnotationForGetBooksByAuthor ? "true" : "false"),
				businessTestFile);
	}

	// Test to check method parameters annotations in BookController class
	@Test
	public void testMethodParameterAnnotations() throws IOException {
		boolean hasRequestParamAnnotationForGetBooksPage = JavaParserUtils.checkMethodParameterAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooks", "page",
				"RequestParam");
		System.out.println(
				"getBooks method has @RequestParam on 'page' parameter: " + hasRequestParamAnnotationForGetBooksPage);

		boolean hasRequestParamAnnotationForGetBooksSize = JavaParserUtils.checkMethodParameterAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooks", "size",
				"RequestParam");
		System.out.println(
				"getBooks method has @RequestParam on 'size' parameter: " + hasRequestParamAnnotationForGetBooksSize);

		boolean hasPathVariableAnnotationForGetBookById = JavaParserUtils.checkMethodParameterAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBookById", "id",
				"PathVariable");
		System.out.println(
				"getBookById method has @PathVariable on 'id' parameter: " + hasPathVariableAnnotationForGetBookById);

		boolean hasRequestParamAnnotationForGetBooksByTitle = JavaParserUtils.checkMethodParameterAnnotation(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByTitle", "title",
				"PathVariable");
		System.out.println("getBooksByTitle method has @PathVariable on 'title' parameter: "
				+ hasRequestParamAnnotationForGetBooksByTitle);

		// Using yakshaAssert at the end to verify all conditions
		yakshaAssert(currentTest(),
				(hasRequestParamAnnotationForGetBooksPage && hasRequestParamAnnotationForGetBooksSize
						&& hasPathVariableAnnotationForGetBookById && hasRequestParamAnnotationForGetBooksByTitle
								? "true"
								: "false"),
				businessTestFile);
	}

	// Test to check the method return types for BookController class
	@Test
	public void testMethodReturnTypes() throws IOException {
		boolean isGetBooksReturnTypeCorrect = JavaParserUtils.checkMethodReturnType(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooks",
				"ResponseEntity<Page<Book>>");
		System.out.println("getBooks method return type is ResponseEntity<Page<Book>>: " + isGetBooksReturnTypeCorrect);

		boolean isGetBookByIdReturnTypeCorrect = JavaParserUtils.checkMethodReturnType(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBookById",
				"ResponseEntity<Book>");
		System.out.println("getBookById method return type is ResponseEntity<Book>: " + isGetBookByIdReturnTypeCorrect);

		boolean isGetBooksByTitleReturnTypeCorrect = JavaParserUtils.checkMethodReturnType(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByTitle",
				"ResponseEntity<Page<Book>>");
		System.out.println("getBooksByTitle method return type is ResponseEntity<Page<Book>>: "
				+ isGetBooksByTitleReturnTypeCorrect);

		boolean isGetBooksByRatingAboveReturnTypeCorrect = JavaParserUtils.checkMethodReturnType(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByRatingAbove",
				"ResponseEntity<Page<Book>>");
		System.out.println("getBooksByRatingAbove method return type is ResponseEntity<Page<Book>>: "
				+ isGetBooksByRatingAboveReturnTypeCorrect);

		boolean isGetBooksByAuthorReturnTypeCorrect = JavaParserUtils.checkMethodReturnType(
				"src/main/java/com/yaksha/assignment/controller/BookController.java", "getBooksByAuthor",
				"ResponseEntity<Page<Book>>");
		System.out.println("getBooksByAuthor method return type is ResponseEntity<Page<Book>>: "
				+ isGetBooksByAuthorReturnTypeCorrect);

		// Using yakshaAssert at the end to verify all conditions
		yakshaAssert(currentTest(),
				(isGetBooksReturnTypeCorrect && isGetBookByIdReturnTypeCorrect && isGetBooksByTitleReturnTypeCorrect
						&& isGetBooksByRatingAboveReturnTypeCorrect && isGetBooksByAuthorReturnTypeCorrect ? "true"
								: "false"),
				businessTestFile);
	}
}
