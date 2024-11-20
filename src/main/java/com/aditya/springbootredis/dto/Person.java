package com.aditya.springbootredis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Person implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private Integer age;
}
