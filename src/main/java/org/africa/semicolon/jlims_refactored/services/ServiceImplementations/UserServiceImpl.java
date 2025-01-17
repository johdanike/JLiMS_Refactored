package org.africa.semicolon.jlims_refactored.services.ServiceImplementations;

import org.africa.semicolon.jlims_refactored.data.models.Book;
import org.africa.semicolon.jlims_refactored.data.models.Inventory;
import org.africa.semicolon.jlims_refactored.data.models.Library;
import org.africa.semicolon.jlims_refactored.data.models.User;
import org.africa.semicolon.jlims_refactored.data.repositories.BookRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.InventoryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.LibraryRepository;
import org.africa.semicolon.jlims_refactored.data.repositories.UserRepository;
import org.africa.semicolon.jlims_refactored.dtos.request.*;
import org.africa.semicolon.jlims_refactored.dtos.response.*;
import org.africa.semicolon.jlims_refactored.enums.Role;
import org.africa.semicolon.jlims_refactored.exceptions.BookAlreadyExistsException;
import org.africa.semicolon.jlims_refactored.exceptions.BookDetailsCannotBeEmptyException;
import org.africa.semicolon.jlims_refactored.exceptions.UserNameFieldCannotBeEmptyException;
import org.africa.semicolon.jlims_refactored.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements org.africa.semicolon.jlims_refactored.services.UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private AuthenticationServiceImpl authenticationServiceImpl;


    @Override
    public AccountRegisterResponse register(AccountRegisterRequest registerRequest) {
        return authenticationServiceImpl.registerAccount(registerRequest);
    }

    @Override
    public LogInResponse logIn(LoginRequest loginRequest) {
        return authenticationServiceImpl.login(loginRequest);
    }

    @Override
    public LogOutResponse logOut(LogoutRequest logoutRequest) {
        return authenticationServiceImpl.logout(logoutRequest);
    }

    @Override
    public AddBookResponse addBook(AddBookRequest addBookRequest) {
        bookExistsValidator(addBookRequest.getTitle(), addBookRequest.getAuthor());
        Book book = getBookDetailsForBookStocking(addBookRequest);

        AddBookResponse response = new AddBookResponse();
        response.setBookName(book.getTitle());
        response.setBookId(book.getId());
        response.setBookQuantity(book.getNumOfCopies());
        response.setMessage("Book added successfully");
        return response;
    }

    private Book getBookDetailsForBookStocking(AddBookRequest addBookRequest) {
        if (isBookDetailsEmptyOrNull(addBookRequest)) throw new IllegalArgumentException("Title or author cannot be empty");
        Book savedBook = getBookRequest(addBookRequest);
        setNewInventory(savedBook);

        Library library = new Library();
        library.setInventories(inventoryRepository.findAll());
        library.setUsers(userRepository.findAll());
        library.setBooks(bookRepository.findAll());
        libraryRepository.save(library);
        return savedBook;
    }

    private void setNewInventory(Book savedBook) {
        Inventory inventory = new Inventory();
        inventory.setBookId(savedBook.getId());
        inventory.setNoOfCopyOfBooks(savedBook.getNumOfCopies());
        inventory.setReturned(false);
        inventory.setBorrowed(false);
        System.out.println("bookId : "+ savedBook.getId());
        inventoryRepository.save(inventory);
    }

    private Book getBookRequest(AddBookRequest addBookRequest) {
        Book book = new Book();
        book.setTitle(addBookRequest.getTitle().trim().strip());
        book.setAuthor(addBookRequest.getAuthor().trim().strip());
        book.setGenre(addBookRequest.getGenre());
        book.setNumOfCopies(addBookRequest.getNoOfCopies() != null ? addBookRequest.getNoOfCopies() : 0);
        Book savedBook = bookRepository.save(book);
        return savedBook;
    }

    private static boolean isBookDetailsEmptyOrNull(AddBookRequest addBookRequest){
        if (addBookRequest.getTitle() == null ||
                addBookRequest.getAuthor() == null )
            throw new IllegalArgumentException("Book fields cannot be empty");
        return false;
    }

    @Override
    public BorrowBookResponse borrowBook(BorrowBookRequest borrowBookRequest) {
        User registeredMember = findUserByUsername(borrowBookRequest.getUsername());
        usernameValidator(registeredMember);

        int numOfCopiesAvailable = newBookQuantityAfterBorrowing(borrowBookRequest);
        boolean bookId = findBookById(borrowBookRequest.getBookId());

        BorrowBookResponse response = getBorrowBookResponse(borrowBookRequest, numOfCopiesAvailable);
        updateInventoryForBorrowedBook(borrowBookRequest, registeredMember, numOfCopiesAvailable);
        return response;
    }

    private void updateInventoryForBorrowedBook(BorrowBookRequest borrowBookRequest, User registeredMember, int numOfCopiesAvailable) {
        Inventory inventory = new Inventory();
        inventory.setBorrowed(true);
        inventory.setBookId(borrowBookRequest.getBookId());
        inventory.setUserId(registeredMember.getId());
        inventory.setDateBorrowed(borrowBookRequest.getBorrowDate());
        inventory.setNoOfCopyOfBooks(numOfCopiesAvailable);
        inventoryRepository.save(inventory);
    }


    private static BorrowBookResponse getBorrowBookResponse(BorrowBookRequest borrowBookRequest, int numOfCopiesAvailable) {
        BorrowBookResponse response = new BorrowBookResponse();
        response.setBookId(borrowBookRequest.getBookId());
        response.setName(borrowBookRequest.getUsername());
        response.setRole(borrowBookRequest.getRole());
        response.setAuthor(borrowBookRequest.getAuthor());
        response.setBookName(borrowBookRequest.getBookName());
        response.setMessage("Book borrowed successfully");
        response.setQuantity(numOfCopiesAvailable);
        response.setBorrowDate(borrowBookRequest.getBorrowDate());
        return response;
    }

    private boolean findBookById(String bookId) {
        return bookRepository.existsById(bookId);
    }

    private int newBookQuantityAfterBorrowing(BorrowBookRequest borrowBookRequest){
        bookExistsValidator(borrowBookRequest.getBookName(), borrowBookRequest.getAuthor());
        int presentNumOfCopies = 0;
        Optional<Book> book = bookRepository.findById(borrowBookRequest.getBookId());
        if (book.isPresent()){
            presentNumOfCopies = book.get().getNumOfCopies();
        }
        if (presentNumOfCopies <= 0)
            throw new IllegalArgumentException("Book is currently out of stock, " +
                    "mind checking another book?");
        book.get().setNumOfCopies(presentNumOfCopies - 1);
        bookRepository.save(book.get());
        return book.get().getNumOfCopies();
    }

    private User findUserByUsername(String username) {
        if(username != null) return userRepository.findByUsername(username);
        throw new UserNotFoundException("User not found!");
    }

    @Override
    public ReturnBookResponse returnBook(ReturnBookRequest returnBookRequest) {
        User registeredMember = findUserByUsername(returnBookRequest.getUsername());
        usernameValidator(registeredMember);

        int numOfCopiesAvailable = newBookQuantityAfterReturning(returnBookRequest);
        System.out.println("no of copies" + numOfCopiesAvailable);

        ReturnBookResponse response = getReturnBookResponse(returnBookRequest, numOfCopiesAvailable);
        updateInventoryForBorrowedBookReturned(returnBookRequest, registeredMember, numOfCopiesAvailable);
        return response;
    }

    private void updateInventoryForBorrowedBookReturned(ReturnBookRequest returnBookRequest, User registeredMember, int numOfCopiesAvailable) {
        BorrowBookResponse borrowBookResponse = new BorrowBookResponse();
        Inventory inventory = new Inventory();
        inventory.setBorrowed(false);
        inventory.setUserId(registeredMember.getId());
        inventory.setNoOfCopyOfBooks(numOfCopiesAvailable);
        inventory.setDateReturned(LocalDate.now());
        inventory.setReturned(true);
        inventory.setDateBorrowed(borrowBookResponse.getBorrowDate());
        inventory.setBookId(returnBookRequest.getBookId());
        inventoryRepository.save(inventory);
    }

    private static ReturnBookResponse getReturnBookResponse(ReturnBookRequest returnBookRequest, int numOfCopiesAvailable) {
        ReturnBookResponse response = new ReturnBookResponse();
        response.setBookId(returnBookRequest.getBookId());
        response.setQuantity(numOfCopiesAvailable);
        response.setUsername(returnBookRequest.getUsername());
        response.setMessage("Book returned successfully");
        return response;
    }

    private int newBookQuantityAfterReturning(ReturnBookRequest returnBookRequest) {
        bookExistsValidator(returnBookRequest.getBookName(), returnBookRequest.getAuthor());
        int presentNumOfCopies = 0;
                                                                                                                                                     Optional<Book> book = bookRepository.findById(returnBookRequest.getBookId());
        Book foundBook = book.orElseThrow(() -> new IllegalArgumentException("Book " + returnBookRequest.getBookId()+" not found!"));
        presentNumOfCopies = foundBook.getNumOfCopies();
        foundBook.setNumOfCopies(presentNumOfCopies + 1);
        System.out.println("new number of copies is " + book.get().getNumOfCopies());

        bookRepository.save(foundBook);
        return foundBook.getNumOfCopies();
    }

    @Override
    public DeleteBookResponse deleteBook(DeleteBookRequest deleteBookRequest) {
        checkIfUserIsALibrarian(findUserRole(deleteBookRequest.getUsername()));
        User registeredMember = findUserByUsername(deleteBookRequest.getUsername());
        usernameValidator(registeredMember);

        bookExistsValidator(deleteBookRequest.getBookId());
        DeleteBookResponse deleteBookResponse = new DeleteBookResponse();
        deleteBookResponse.setBookId(deleteBookRequest.getBookId());
        deleteBookResponse.setMessage("Book deleted successfully");
        bookRepository.deleteById(deleteBookRequest.getBookId());

        updateInventoryForDeleteBookRequest(deleteBookRequest, registeredMember);
        return deleteBookResponse;
    }

    @Override
    public List<Book> findBooksBorrowedByMember(String username) {
        checkIfUserIsALibrarian(findUserRole(username));
        User user = findUserByUsername(username);
        List<Book> books =  bookRepository.getBooksBorrowedBy(user.getId());
        return books;
    }

    private Role findUserRole(String username) {
        if(username != null) {
            User user = findUserByUsername(username);
            return user.getRole();
        }
        return null;
    }

    private void updateInventoryForDeleteBookRequest(DeleteBookRequest deleteBookRequest, User registeredMember) {
        Inventory inventory = new Inventory();
        inventory.setBookId(deleteBookRequest.getBookId());
        inventory.setBorrowed(false);
        inventory.setNoOfCopyOfBooks(0);
        inventory.setReturned(true);
        inventory.setId(registeredMember.getId());
        inventory.setNoOfCopyOfBooks(null);
        inventory.setDateReturned(null);
        inventoryRepository.save(inventory);
    }

    private void bookExistsValidator(String bookId) {
        if (bookId == null) throw new IllegalArgumentException("Book id cannot be null");
        bookRepository.findById(bookId);
    }

    private void bookExistsValidator(String bookName, String author) {
        if(bookName == null || author == null || author.trim().isEmpty()) {
            throw new BookDetailsCannotBeEmptyException("Title cannot be null");
        } else if (bookName.equalsIgnoreCase(bookRepository.findByTitle(bookName)) && author.equalsIgnoreCase(bookRepository.findByAuthor(author))) {
            throw new BookAlreadyExistsException("Book already exists");
        }
    }

    private void usernameValidator(User user){
        if(user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new UserNameFieldCannotBeEmptyException("Username cannot be null");
        }
    }

    private void checkIfUserIsALibrarian(Role role){
        if (!role.equals(Role.LIBRARIAN)) throw new IllegalArgumentException("Access denied");
    }
}
