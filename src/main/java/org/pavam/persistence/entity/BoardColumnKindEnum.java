package org.pavam.persistence.entity;

import java.util.Arrays;

public enum BoardColumnKindEnum {

    INITIAL, FINAL, CANCEL, PENDING;

    public static BoardColumnKindEnum getKindEnumByName(final String name) {
        return Arrays.stream(BoardColumnKindEnum.values())
                .filter(kind -> kind.name().equals(name))
                .findFirst()
                .orElseThrow();
    }

}
