package org.africa.semicolon.jlims_refactored.services;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.BookRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.InventoryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.dtos.request.AddBookRequest;
import org.africa.semicolon.jlims_refactored.dtos.response.AddBookResponse;
import org.africa.semicolon.jlims_refactored.enums.Genre;
import org.africa.semicolon.jlims_refactored.enums.Role;
import org.africa.semicolon.jlims_refactored.services.ServiceImplementations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
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
    private Book book2;
    private User user;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;
    private AddBookRequest addBookRequest;
    private AddBookResponse addBookResponse;


    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        userRepository.deleteAll();
        inventoryRepository.deleteAll();


        book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setNumOfCopies(6);
        book.setGenre(Genre.DRAMA);

        book2 = new Book();
        book2.setTitle("This is a book");
        book2.setAuthor("John-Daniel");
        book2.setGenre(Genre.ACTION);
        book2.setNumOfCopies(1);

        user = new User();
        user.setUsername("John");
        user.setPassword("password");
        user.setEmail("john@gmail.com");
        user.setRole(Role.LIBRARIAN);
        user.setLoggedIn(true);


        addBookRequest = new AddBookRequest();
        addBookRequest.setTitle("This title");
        addBookRequest.setAuthor("John");
        addBookRequest.setGenre(Genre.ACTION);
        addBookRequest.setNoOfCopies(3);
        addBookRequest.setIsAvailable("Yes");
        addBookRequest.setDateTime(LocalDateTime.now());
        addBookRequest.setUsername("John");

    }

    @Test
    public void viewAllBooks_test() {
        bookRepository.save(book);
        bookRepository.save(book2);
        List<Book> books = libraryService.viewAllBooks();
        assertEquals(bookRepository.count(), books.size());
    }

    @Test
    public void viewAllBooksWhen_ThereAreNoBooks_InTheLibrary_returnsEmptyList_test() {
        bookRepository.save(book);
        List<Book> books = libraryService.viewAllBooks();
        assertEquals(bookRepository.count(), books.size());
    }

    @Test
    public void checkBookCountForTwo_whenNoBookWasSaved_throwsException_test() {
        assertThrows(IllegalArgumentException.class, () -> libraryService.viewAllBooks());
    }

    @Test
    public void findAllBooksByGenre_test() {
        bookRepository.save(book);
        List<Book> booksFound = libraryService.findByGenre(book.getGenre());
        assertEquals("Book Title", booksFound.getFirst().getTitle());
    }

    @Test
    public void findAllBooksByTitle_test() {
        bookRepository.save(book);
        List<Book> foundBooks = libraryService.findBooksByTitle("Book title");
        assertEquals(0, foundBooks.size());
        assertEquals("author", foundBooks.get(0).getAuthor());

        List<Book> foundBooks2 = libraryService.findBooksByTitle("Book Title");
        assertEquals(1, foundBooks2.size());
    }

    @Test
    public void findAllBooksByAuthor_test() {
        bookRepository.save(book);
        bookRepository.save(book2);
        List<Book> foundBooks1 = libraryService.findBooksByAuthor("author");
        assertEquals("Book Title", foundBooks1.getFirst().getTitle());

        List<Book> foundBook2 = libraryService.findBooksByAuthor("John-Daniel");
        assertNotNull(foundBook2);
    }

    @Test
    public void viewAllUsers_test() {
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

    @Test
    public void findUserByUsername_test() {
        userRepository.save(user);
        User foundUser = libraryService.findUserByUsername(user.getUsername());
        assertNotNull(foundUser);
    }

    @Test
    public void onlyLibrarianCanDeleteUser_test() {
        userRepository.save(user);
        User user1 = new User();
        user1.setUsername("JohnDaniel");
        user1.setPassword("password");
        user1.setEmail("john@gmail.com");
        user1.setRole(Role.MEMBER);
        user1.setLoggedIn(true);
        userRepository.save(user1);

        assertEquals(2, userRepository.count());
        List<Inventory> foundUser = libraryService.deleteUser(user.getUsername(), user1);
        assertNotNull(foundUser);
        assertEquals(1, userRepository.count());
        assertEquals(0, foundUser.size());
    }

    @Test
    public void memberCannotDeleteAUser_throwsException_test() {
        userRepository.save(user);
        User user1 = new User();
        user1.setUsername("JohnDaniel");
        user1.setPassword("password");
        user1.setEmail("john@gmail.com");
        user1.setRole(Role.MEMBER);
        user1.setLoggedIn(true);
        userRepository.save(user1);

        assertEquals(2, userRepository.count());
        assertThrows(IllegalArgumentException.class, () -> libraryService.deleteUser(user1.getUsername(), user));
        assertEquals(2, userRepository.count());
    }

    @Test
    public void librarianAddsABook_assertThatBookIsSaved_timeAddedIsReturned_test() {
        userRepository.save(user);

        addBookResponse = userServiceImpl.addBook(addBookRequest);
        var recordOfBookAdded = libraryService.getInventory(user.getRole(), addBookResponse.getUserId());
        assertEquals(1, userRepository.count());
        assertEquals(1, bookRepository.count());

        System.out.println(userRepository.findByUsername(user.getUsername()));
        userServiceImpl = new UserServiceImpl();

        assertEquals(recordOfBookAdded.getFirst().getUserId(), userRepository.findByUsername(addBookResponse.getUserId()));
        assertEquals(recordOfBookAdded.getFirst().getNoOfCopyOfBooks(), addBookResponse.getBookQuantity());
    }

    @Test
    public void librarianSearchForUser_canViewAllBooksBorrowed_test() {
        userRepository.save(user);

        addBookResponse = userServiceImpl.addBook(addBookRequest);
        assertNotNull(addBookResponse);

        List<Inventory> viewAllBooksBorrowedByUser = libraryService.viewAllBooksBorrowedByUser(user.getUsername());
        assertNotNull(viewAllBooksBorrowedByUser);
        System.out.println(viewAllBooksBorrowedByUser);
    }

    @Test
    public void testThatLibrarianCanViewAllBooksReturnedByUser(){
        userRepository.save(user);

        addBookResponse = userServiceImpl.addBook(addBookRequest);
        assertEquals(1, userRepository.count());
        assertEquals(1, bookRepository.count());

        List<Book> viewAllBooksReturnedByUser = libraryService.viewAllBooksReturnedByUser(user.getUsername());
        assertNotNull(viewAllBooksReturnedByUser);
//        assertEquals(viewAllBooksReturnedByUser.get(1), addBookResponse.getBookId());
    }
}
