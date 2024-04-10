package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.persistence.Column;

@Data
public class Genre {
    private Integer id;

    @Column(nullable = true)
    private String name;
}
