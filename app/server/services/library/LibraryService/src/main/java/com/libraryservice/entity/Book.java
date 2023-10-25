package com.libraryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class Book {
    int id;
    UUID book_uid;
    String name;
    String author;
    String genre;
    Condition condition;
    int available_cnt;
}
