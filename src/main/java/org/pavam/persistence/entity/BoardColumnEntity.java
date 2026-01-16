package org.pavam.persistence.entity;

import lombok.Data;

@Data
public class BoardColumnEntity {

    private Long id;
    private String description;
    private int order;
    private BoardColumnKindEnum kind;
    private BoardEntity boardEntity = new BoardEntity();

}
