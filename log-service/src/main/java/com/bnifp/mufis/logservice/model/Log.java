package com.bnifp.mufis.logservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Document("logs")
public class Log {
    @Id
    private Integer id;

    private String name;

    private String data;

    @CreationTimestamp
    protected Date createdAt;
}
