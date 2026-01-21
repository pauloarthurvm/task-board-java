package org.pavam.service;

import lombok.AllArgsConstructor;
import org.pavam.persistence.dao.CardDAO;
import org.pavam.persistence.entity.CardEntity;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class CardService {

    private final Connection connection;

    public final CardEntity insert(CardEntity cardEntity) throws SQLException {
        try {
            var cardDao = new CardDAO(connection);
            cardDao.insert(cardEntity);
            connection.commit();
            return cardEntity;
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }

}
