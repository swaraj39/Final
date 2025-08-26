package com.pack.demo.ModelDAO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "review")
@Data
public class Reviwer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Lob//for large text
    private String reviews;

    public Reviwer(String name,String text){
        this.name= name;
        this.reviews=text;
    }

    public Reviwer(){

    }
}
