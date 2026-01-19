package org.pavam.ui.simulator;

import lombok.AllArgsConstructor;
import org.pavam.persistence.entity.BoardEntity;
import org.pavam.service.BoardQueryService;

import java.sql.SQLException;
import java.util.Scanner;

import static org.pavam.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {

    private final BoardEntity boardEntity;

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() {
        try {
            var option = -1;
            while(option != 9) {
                System.out.printf("Welcome to board %s.\n", boardEntity.getName());
                System.out.println("Select an option:");
                System.out.println("\t1 - Create a card");
                System.out.println("\t2 - Move a card");
                System.out.println("\t3 - Block a card");
                System.out.println("\t4 - Unblock a card");
                System.out.println("\t5 - Cancel a card");
                System.out.println("\t6 - See board");
                System.out.println("\t7 - See column with card");
                System.out.println("\t8 - See card");
                System.out.println("\t9 - Go back to Main Menu");
                System.out.println("\t10 - Exit program");
                option = scanner.nextInt();
                switch(option) {
                    case 1 -> createCard();
                    case 2 -> moveCard();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumnWithCards();
                    case 8 -> showCard();
                    case 9 -> {
                        break;
                    }
                    case 10 -> System.exit(0);
                    default -> System.out.println("Select a valid option");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createCard() {
        
    }

    private void moveCard() {
    }

    private void blockCard() {
    }

    private void unblockCard() {
    }

    private void cancelCard() {
    }

    private void showBoard() throws SQLException {
        try(var connection = getConnection()) {
            var optionalBoardDetails = new BoardQueryService(connection).showBoardDetails(boardEntity.getId());
            optionalBoardDetails.ifPresent(bd -> {
                System.out.printf("Board %d - %s\n", bd.id(), bd.name());
                bd.columnsDto().forEach(c -> {
                    System.out.printf("Column %s - Type %s has %d cards", c.description(), c.kind(), c.cardsAmount());
                });
            });
        }
    }

    private void showColumnWithCards() {
    }

    private void showCard() {
        
    }
}
