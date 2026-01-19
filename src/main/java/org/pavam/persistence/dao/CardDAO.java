package org.pavam.persistence.dao;

import lombok.AllArgsConstructor;
import org.pavam.dto.CardDetails;

import java.sql.Connection;

@AllArgsConstructor
public class CardDAO {

    private final Connection connection;

    public CardDetails findCardById(final Long id) {
        // TODO
        return null;
    }
}
