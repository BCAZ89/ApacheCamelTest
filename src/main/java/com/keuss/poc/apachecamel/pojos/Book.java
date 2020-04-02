package com.keuss.poc.apachecamel.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Book {

    private int id;
    private String title;
    private String author;

}
