package com.keuss.poc.apachecamel.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Book implements Serializable {

    private int id;
    private String title;
    private String author;

}
