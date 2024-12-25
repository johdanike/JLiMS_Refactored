package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.BookRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.enums.Genre;
import org.africa.semicolon.jlims_refactored.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LibraryServiceTest {
    @Autowired
    LibraryService libraryService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    private Book book;
    private User user;


    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        userRepository.deleteAll();

        book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setNumOfCopies(6);
        book.setGenre(Genre.DRAMA);

        book.setTitle("This is a book");
        book.setAuthor("John-Daniel");
        book.setGenre(Genre.ACTION);
        book.setNumOfCopies(1);

        user = new User();
        user.setUsername("John");
        user.setPassword("password");
        user.setEmail("john@gmail.com");
        user.setRole(Role.LIBRARIAN);
        user.setLoggedIn(true);

    }

    @Test
    public void viewAllBooks_test() {
        bookRepository.save(book);
        List<Book> books = libraryService.viewAllBooks();
        assertNotNull(books);
        assertEquals(bookRepository.count(), books.size());

    }

    @Test
    public void viewAllBooksWhen_ThereAreNoBooks_InTheLibrary_returnsEmptyList_test() {
        bookRepository.save(book);
        List<Book> books = libraryService.viewAllBooks();
        assertNotNull(books);
        assertEquals(bookRepository.count(), books.size());
    }

    @Test
    public void checkBookCountForTwo_whenNoBookWasSaved_throwsException_test() {
        assertThrows(IllegalArgumentException.class, () -> libraryService.viewAllBooks());
    }

    @Test
    public void findAllBooksByGenre_test(){
        bookRepository.save(book);
        List<Book> books = libraryService.findByGenre(book.getGenre());
        assertNotNull(books);
    }

    @Test
    public void findAllBooksByTitle_test(){
        bookRepository.save(book);
        List<Book> foundBooks = libraryService.findBooksByTitle("Book title");
        assertNotNull(foundBooks);
    }

    @Test
    public void findAllBooksByAuthor_test(){
        bookRepository.save(book);
        List<Book> foundBooks1 = libraryService.findBooksByAuthor("author");
        assertNotNull(foundBooks1);

        List<Book> foundBook2 = libraryService.findBooksByAuthor("John-Daniel");
        assertNotNull(foundBook2);
    }

    @Test
    public void viewAllUsers_test(){
        userRepository.save(user);
        List<User> foundUsers = libraryService.viewAllUsers();
        assertNotNull(foundUsers);
        assertEquals(userRepository.count(), foundUsers.size());
    }

    @Test
    public void viewAllUsers_whenThereAreNoUsers_returnsEmptyList_test() {
        User user = new User();
        userRepository.save(user);
        List<User> foundUsers = libraryService.viewAllUsers();
        assertNotNull(foundUsers);
    }
}