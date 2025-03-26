package com.example.clickandrace.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data


public class Car extends Product {
    private String brand;
    private String model;
    private int km;
    private int numOwners;


}
