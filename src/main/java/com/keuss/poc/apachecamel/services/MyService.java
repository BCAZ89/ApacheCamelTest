package com.keuss.poc.apachecamel.services;

import com.keuss.poc.apachecamel.exceptions.CustomException;
import com.keuss.poc.apachecamel.pojos.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MyService {
    public String doSomething(String body, Map headers, String jmsCorrelationIDparam) {
        // process the in body and return whatever you want
        System.out.println("MyService doSomething body " + body);
        System.out.println("MyService doSomething jmsCorrelationIDparam " + jmsCorrelationIDparam);
        headers.forEach((k, v) -> System.out.println(k + ":" + v));
        return "Bye World";
    }

    public String doSomethingBis(String body) {
        System.out.println("MyService doSomethingBis body " + body);
        return body;
    }

    public Book doSomethingJson(Book book) throws CustomException {
        //test
        if (book.getId() == 1) {
            throw new CustomException("MY ERROR !", 9999);
        }
        //----
        System.out.println("MyService doSomethingJson body " + book);
        return book;
    }


    public List<Book> doSomethingJsonList(List<Book> books) {
        System.out.println("MyService doSomethingJsonList body " + books);
        return books;
    }

}
