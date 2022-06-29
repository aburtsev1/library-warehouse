package com.xpinjection.librarywarehouse.adaptors.httpclient;

import com.xpinjection.librarywarehouse.adaptors.httpclient.dto.BookDto;
import com.xpinjection.test.FeignClientTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@FeignClientTest(port = 8080, stubs = "classpath*:wiremock/**/*.json")
public class LibraryClientIntegrationTest {
    @Autowired
    private LibraryClient libraryClient;

    @Test
    void ifBooksFoundByAuthorThenTheyAreReturned() {
        var books = libraryClient.findByAuthor("Josh Bloch");

        assertThat(books).contains(new BookDto(9L, "Effective Java", "Josh Bloch"));
    }

    @Test
    void ifNoBooksFoundByAuthorThenReturnEmptyList() {
        var books = libraryClient.findByAuthor("Unknown");

        assertThat(books).isEmpty();
    }
    @Test
    void ifBooksAddedThenTheyAreReturned() {
        Map<String, String> books = new HashMap<>();
        books.put("test book", "test Author");
        books.put("Hibernate in Action", "Who cares?");
        List<BookDto> returnedBooksList = libraryClient.addBooks(books);
        assertThat(returnedBooksList).contains(new BookDto(17L, "testBook1", "testAuthor1"));
        assertThat(returnedBooksList).contains(new BookDto(18L, "testBook2", "testAuthor2"));
        assertThat(returnedBooksList.size()==2);
    }

}