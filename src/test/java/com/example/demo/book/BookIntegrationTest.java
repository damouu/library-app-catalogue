//package com.example.demo.book;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//
//import java.net.URI;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.UUID;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class BookIntegrationTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Test
//    void getBookUuid() {
//        Book book = new Book(UUID.randomUUID(), "tittle", "genre", 200, "publisher", "author", LocalDate.now());
//        bookRepository.save(book);
//        ResponseEntity<Book> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/book/" + book.getUuid(), Book.class);
//        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
//        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getHeaders().getContentType()).isCompatibleWith(MediaType.APPLICATION_JSON));
//        Assertions.assertEquals(Objects.requireNonNull(responseEntity.getBody()).getUuid(), book.getUuid());
//    }
//
//    @Test
//    void postBook() {
//        Book book = new Book(null, "tittle", "genre", 200, "publisher", "author", LocalDate.now());
//        ResponseEntity<Book> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/book", book, Book.class);
//        Optional<Book> book1 = bookRepository.findByUuid(Objects.requireNonNull(responseEntity.getBody()).getUuid());
//        Assertions.assertEquals(201, responseEntity.getStatusCodeValue());
//        Assertions.assertEquals(URI.create("http://localhost:8083/api/book/" + book1.get().getUuid()), responseEntity.getHeaders().getLocation());
//        Assertions.assertEquals(responseEntity.getBody().getUuid(), book1.get().getUuid());
//    }
//
//    @Test
//    void deleteBook() {
//        Book book = new Book(UUID.randomUUID(), "DELETE", "DELETE", 200, "publisher", "author", LocalDate.now());
//        bookRepository.save(book);
//        restTemplate.delete("http://localhost:" + port + "/api/book/" + book.getUuid());
//        ResponseEntity<Book> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/book/" + book.getUuid(), Book.class);
//        Assertions.assertEquals(404, responseEntity.getStatusCodeValue());
//        Assertions.assertNotEquals(book.getUuid(), responseEntity.getBody().getUuid());
//    }
//
//    @Test
//    void updateBook() {
//        Book book = new Book(UUID.randomUUID(), "title", "genre", 200, "publisher", "author", LocalDate.now());
//        bookRepository.save(book);
//        HashMap<String, String> bookUpdates = new HashMap<>();
//        bookUpdates.put("title", "UPDATED");
//        bookUpdates.put("genre", "UPDATED");
//        restTemplate.put("http://localhost:" + port + "/api/book/" + book.getUuid(), bookUpdates);
//        ResponseEntity<Book> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/book/" + book.getUuid(), Book.class);
//        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
//        Assertions.assertEquals(book.getUuid(), responseEntity.getBody().getUuid());
//        Assertions.assertEquals(bookUpdates.get("title"), Objects.requireNonNull(responseEntity.getBody()).getTitle());
//        Assertions.assertEquals(bookUpdates.get("genre"), Objects.requireNonNull(responseEntity.getBody()).getGenre());
//        Assertions.assertNotEquals(book.getTitle(), Objects.requireNonNull(responseEntity.getBody()).getTitle());
//        Assertions.assertNotEquals(book.getGenre(), Objects.requireNonNull(responseEntity.getBody()).getGenre());
//    }
//
//}
