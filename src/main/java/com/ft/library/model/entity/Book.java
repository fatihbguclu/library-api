package com.ft.library.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "isbn")
    private String isbn;

    @Column(nullable = false)
    private String author; // TODO: Change to list of Author object

    @Column(name = "publish_year")
    private Integer publishYear;

    @Column(name = "quantity_available")
    private Integer quantityAvailable;

    @Column(name = "category")
    private String category; // TODO : maybe enum or list of category
}
