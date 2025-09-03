package com.example.demo.student_id_card;

import com.example.demo.book.Book;
import com.example.demo.book.BookStudent;
import com.example.demo.course.Course;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.*;

@Service
@Data
public class StudentIdCardService {

    private final StudentIdCardRepository studentIdCardRepository;

    private final StudentRepository studentRepository;

    public ResponseEntity<List<StudentIdCard>> getStudentIdCards(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StudentIdCard> pages = studentIdCardRepository.findAll(pageable);
        return ResponseEntity.ok(pages.toList());
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getStudentIdCard(UUID studentCardUuid) throws ResponseStatusException {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        Optional<StudentIdCard> studentIdCard = Optional.ofNullable(studentIdCardRepository.findStudentIdCardByUuid(studentCardUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student card does not exist")));
        Set<BookStudent> book = studentIdCard.get().getBooks();
        Set<Course> courses = studentIdCard.get().getCourses();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> list = new LinkedHashMap<>();
        list.put("book", book.size());
        list.put("courses", courses.size());
        response.put("status", "success");
        response.put("type", "Collection");
        response.put("size", list);
        data.put("student", studentIdCard.get().getStudent());
        data.put("book", book);
        data.put("courses", courses);
        response.put("data", data);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity deleteStudentIdCard(UUID studentCardUuid) throws ResponseStatusException {
        StudentIdCard studentIdCard = this.studentIdCardRepository.findStudentIdCardByUuid(studentCardUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student card does not exist"));
        studentIdCardRepository.delete(studentIdCard);
        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<StudentIdCard> postStudentIdCard(UUID studentUuid) throws ResponseStatusException {
        Student student = this.studentRepository.findStudentByUuid(studentUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student Uuid does not exist"));
        StudentIdCard studentIdCard = new StudentIdCard(UUID.randomUUID());
        studentIdCard.setStudent(student);
        studentIdCardRepository.save(studentIdCard);
        return ResponseEntity.status(HttpStatus.CREATED).location(URI.create("http://localhost:8083/api/studentCard/" + studentIdCard.getUuid())).body(studentIdCard);
    }


}
