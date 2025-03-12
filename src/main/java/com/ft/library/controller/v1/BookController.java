package com.ft.library.controller.v1;

import com.ft.library.model.dto.request.CreateBookRequest;
import com.ft.library.model.dto.response.GenericResponse;
import com.ft.library.model.entity.Book;
import com.ft.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<GenericResponse<List<Book>>> getAllBook() {
        List<Book> allBook = bookService.getAllBook();
        return ResponseEntity.ok(GenericResponse.of("Success", "Success", allBook));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<Book>> getBookById(@PathVariable long id) {
        Book bookById = bookService.getBookById(id);
        return ResponseEntity.ok(GenericResponse.of("Success", "Success", bookById));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<GenericResponse<Book>> getBookByIsbn(@PathVariable String isbn) {
        Book bookByIsbn = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(GenericResponse.of("Success", "Success", bookByIsbn));
    }

    @PostMapping
    public ResponseEntity<GenericResponse<?>> createBook(@RequestBody CreateBookRequest body) {
        bookService.createBook(body);
        return ResponseEntity.ok(GenericResponse.of("Success", "Success", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<?>> updateBook(@PathVariable long id, @RequestBody CreateBookRequest body) {
        bookService.updateBook(id, body);
        return ResponseEntity.ok(GenericResponse.of("Success", "Success", null));
    }
}
