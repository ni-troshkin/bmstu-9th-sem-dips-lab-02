package com.libraryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class Library {
    int id;
    UUID library_uid;
    String name;
    String city;
    String address;
}
