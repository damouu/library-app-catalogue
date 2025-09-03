package com.example.demo.book;

import com.example.demo.student_id_card.StudentIdCard;
import com.example.demo.student_id_card.StudentIdCardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;

@Service
@Data
public class BookService {

    private final BookRepository bookRepository;

    private final StudentIdCardRepository studentIdCardRepository;

    public ResponseEntity<?> getBooks(Map allParams) {
        PageRequest pageable;
        if (allParams.size() == 2 && allParams.containsKey("page") && allParams.containsKey("size")) {
            pageable = PageRequest.of(Integer.parseInt((String) allParams.get("page")), Integer.parseInt((String) allParams.get("size")));
            Page<Book> pages = bookRepository.findAll(pageable);
            return ResponseEntity.ok(pages.toList());
        } else {
            final Specification<Book> specification = BookSpecification.filterBook(allParams);
            if (!allParams.containsKey("page") && !allParams.containsKey("size")) {
                pageable = PageRequest.of(1, 20, Sort.unsorted());
            } else if (allParams.size() > 2 && allParams.containsKey("page") && allParams.containsKey("size")) {
                pageable = PageRequest.of(Integer.parseInt((String) allParams.get("page")), Integer.parseInt((String) allParams.get("size")), Sort.unsorted());
            } else {
                if (allParams.containsKey("page")) {
                    pageable = PageRequest.of(Integer.parseInt((String) allParams.get("page")), 20);
                } else {
                    pageable = PageRequest.of(1, Integer.parseInt((String) allParams.get("size")));
                }
            }
            final Page<Book> books = (bookRepository.findAll(specification, pageable));
            return ResponseEntity.ok(books);
        }
    }

    public List<Book> searchBooks(String search) throws ResponseStatusException {
        final Specification<Book> specification = BookSpecification.searchBook(search);
        final List<Book> books = bookRepository.findAll(specification);
        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return books;
    }

    /**
     * find a book by the passed UUID.
     *
     * @param bookUuid a book's UUID
     * @return Array returns if the given UUID exists, a book resource and also attach to it, the studentCard associated to it.
     * @throws ResponseStatusException throws an exception if the given UUID does not correspond to a book in the database.
     */
    public ResponseEntity<Map<String, Map<String, String>>> getBookUuid(UUID bookUuid) throws ResponseStatusException {
        Book book = bookRepository.findByUuid(bookUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));
        Map<String, String> bookData = new HashMap<>();
        Map<String, Map<String, String>> response = new HashMap<>();
//        if (book.getStudentIdCard() != null) {
//            Map<String, String> studentCard = new HashMap<>();
//            studentCard.put("studentCardUUID", String.valueOf(book.getStudentIdCard().getUuid()));
//            response.put("studentCard", studentCard);
//        }
        bookData.put("UUID", String.valueOf(book.getUuid()));
        bookData.put("author", book.getAuthor());
        bookData.put("genre", book.getGenre());
        bookData.put("publisher", book.getPublisher());
        bookData.put("title", book.getTitle());
        bookData.put("total_pages", String.valueOf(book.getTotalPages()));
        bookData.put("created_at", String.valueOf(book.getCreated_at()));
        response.put("book", bookData);
        return ResponseEntity.ok(response);
    }

    /**
     * deletes a book by providing a book's UUID, if the given UUID does not correspond to an existing bok in the db, throws an error
     *
     * @param bookUuid a book's UUID
     * @return returns an ResponseEntity object with a confirmation message in it, confirming the book is deleted.
     * @throws ResponseStatusException throws if the given UUID does not correspond to an existing book in the db.
     */
    public ResponseEntity<?> deleteBook(UUID bookUuid) throws ResponseStatusException {
        Book book = bookRepository.findByUuid(bookUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));
        book.setDeleted_at(LocalDate.now());
        bookRepository.save(book);
        return ResponseEntity.accepted().build();
    }

    /**
     * inserts the passed book object into the database.
     *
     * @param book a book's UUID
     * @return ResponseEntity return with a location URI in the header of the response with the created book in it.
     * @throws ResponseStatusException throws if the given UUID does not correspond to an existing book in the db.
     */
    public ResponseEntity<Book> postBook(Book book) {
        book.setUuid(UUID.randomUUID());
        bookRepository.save(book);
        return ResponseEntity.status(201).location(URI.create("http://localhost:8083/api/book/" + book.getUuid())).body(book);
    }

    /**
     * @param bookUuid    the wanted book to be updated.
     * @param bookUpdates a key value array containing the updated data to be replaced with the current book.
     * @return ResponseEntity returns a ResponseEntity object with a URI location of the updated book.
     * @throws ResponseStatusException throws an exception if the given UUID does not exist in the database.
     */
    public ResponseEntity<Book> updateBook(UUID bookUuid, HashMap<String, String> bookUpdates) {
        Book book = bookRepository.findByUuid(bookUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));
        ObjectMapper oMapper = new ObjectMapper();
        Map map = oMapper.convertValue(book, Map.class);
        for (Object s : map.keySet()) {
            for (String s1 : bookUpdates.keySet()) {
                if (s.equals(s1)) {
                    map.put(s, bookUpdates.get(s1));
                }
            }
        }
        Book book1 = oMapper.convertValue(map, Book.class);
        book1.setId(book.getId());
        bookRepository.save(book1);
        Book book11 = bookRepository.findByUuid(book1.getUuid()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));
        return ResponseEntity.status(200).location(URI.create("http://localhost:8083/api/book/" + book11.getUuid())).body(book11);
    }

    public ResponseEntity<?> insertStudentBorrowBooks(UUID bookUuid, UUID studentUuidCard) {
        Book book = bookRepository.findByUuid(bookUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book does not exist"));
        StudentIdCard studentIdCard = studentIdCardRepository.findStudentIdCardByUuid(studentUuidCard).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "studentIdCard does not exist" + studentUuidCard));
        if (book.getStudentIdCard() == null) {
            book.setStudentIdCard((Set<BookStudent>) studentIdCard);
            bookRepository.save(book);
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> returnStudentBorrowBooks(UUID bookUuid, UUID studentUuidCard) {
        Book book = bookRepository.findByUuid(bookUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book does not exist"));
        StudentIdCard studentIdCard = studentIdCardRepository.findStudentIdCardByUuid(studentUuidCard).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "studentIdCard does not exist" + studentUuidCard));
//        if (book.getStudentIdCard() != null && book.getStudentIdCard().getUuid().equals(studentIdCard.getUuid())) {
//            book.setStudentIdCard(null);
//            bookRepository.save(book);
//            return ResponseEntity.status(200).build();
//        }
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> getBooksStudentCard(UUID studentUuidCard) {
        StudentIdCard studentIdCard = studentIdCardRepository.findStudentIdCardByUuid(studentUuidCard).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "studentIdCard does not exist" + studentUuidCard));
        if (!studentIdCard.getBooks().isEmpty()) {
            return ResponseEntity.status(200).body(studentIdCard.getBooks());
        }
        return ResponseEntity.noContent().build();
    }
}
