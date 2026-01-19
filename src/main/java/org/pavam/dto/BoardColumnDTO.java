package org.pavam.dto;

import org.pavam.persistence.entity.BoardColumnKindEnum;

public record BoardColumnDTO(Long id,
                             String description,
                             BoardColumnKindEnum kind,
                             int cardsAmount) {

}
