package org.pavam.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardColumnEntity {

    private Long id;
    private String description;
    private int order;
    private BoardColumnKindEnum kind;
    private BoardEntity boardEntity = new BoardEntity();
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CardEntity> cardEntityList = new ArrayList<>();

}
