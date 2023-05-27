package com.entity;

import javax.persistence.*;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;


@Data
@Entity
@Table(name = "delivery")
public class GoodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String good_name;

    @Column(nullable = false)
    private Date delivery_date;
}
