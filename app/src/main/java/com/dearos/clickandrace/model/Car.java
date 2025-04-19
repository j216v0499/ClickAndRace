package com.dearos.clickandrace.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data

public class Car extends Product {
    private String brand;
    private String model;
    private int km;
    private int numOwners;


}
