package org.pavam.ui.simulator;

import lombok.AllArgsConstructor;
import org.pavam.dto.BoardColumnInfoDTO;
import org.pavam.persistence.entity.BoardEntity;
import org.pavam.persistence.entity.CardEntity;
import org.pavam.service.BoardColumnQueryService;
import org.pavam.service.BoardQueryService;
import org.pavam.service.CardQueryService;
import org.pavam.service.CardService;

import javax.smartcardio.Card;
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

    private void createCard() throws SQLException {
        var cardEntity = new CardEntity();
        System.out.println("Title of the Card:");
        cardEntity.setTitle(scanner.next());
        System.out.println("Description of the card:");
        cardEntity.setDescription(scanner.next());
        cardEntity.setBoardColumnEntity(boardEntity.getInitialColumn());
        try(var connection = getConnection()) {
            var cardService = new CardService(connection).insert(cardEntity);
        }
    }

    private void moveCard() throws SQLException {
        System.out.println("Insert Card ID to be moved:");
        var cardIdToMove = scanner.nextLong();
        var boardColumnInfoDtoList = boardEntity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind())).toList();
        try(var connection = getConnection()) {
            var cardService = new CardService(connection);
            cardService.moveToNextColumn(cardIdToMove, boardColumnInfoDtoList);
        } catch(RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void blockCard() throws SQLException {
        System.out.println("Card ID to be blocked:");
        var cardIdToBlock = scanner.nextLong();
        System.out.println("Blocking reason:");
        var blockingReason = scanner.next();
        var boardColumnInfoDtoList = boardEntity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind())).toList();
        try(var connection = getConnection()) {
            new CardService(connection).block(cardIdToBlock, blockingReason, boardColumnInfoDtoList);
        }
    }

    private void unblockCard() {
    }

    private void cancelCard() throws SQLException {
        System.out.println("Card ID to Cancel");
        var cardIdToCancel = scanner.nextLong();
        var cancelColumnEntity = boardEntity.getCancelColumn();
        var boardColumnInfoDtoList = boardEntity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind())).toList();
        try(var connection = getConnection()) {
            var cardService = new CardService(connection);
            cardService.cancelCard(cardIdToCancel, cancelColumnEntity.getId(), boardColumnInfoDtoList);
            System.out.println("Card canceled.");
        } catch(RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void showBoard() throws SQLException {
        try(var connection = getConnection()) {
            var optionalBoardDetails = new BoardQueryService(connection).showBoardDetails(boardEntity.getId());
            optionalBoardDetails.ifPresent(bd -> {
                System.out.printf("Board %d - %s\n", bd.id(), bd.name());
                bd.columnsDto().forEach(c -> {
                    System.out.printf("Column %s - Type %s has %d cards\n", c.description(), c.kind(), c.cardsAmount());
                });
                System.out.println();
            });
        }
    }

    private void showColumnWithCards() throws SQLException {
        var columnsId = boardEntity.getBoardColumns().stream().map(column -> column.getId()).toList();
        var selectedColumnId = -1L;
        while (!columnsId.contains(selectedColumnId)) {
            System.out.printf("Select a column ID from board %s\n", boardEntity.getName());
            boardEntity.getBoardColumns().forEach(column -> {
                System.out.printf("Column %d - %s - Kind %s\n", column.getId(), column.getDescription(), column.getKind());
            });
            selectedColumnId = scanner.nextLong();
        }
        try(var connection = getConnection()) {
            var columnOptional = new BoardColumnQueryService(connection).findById(selectedColumnId);
            columnOptional.ifPresent(column -> {
                System.out.printf("Column %s kind %s\n", column.getDescription(), column.getKind());
                column.getCardEntityList().forEach(card -> {
                    System.out.printf("Card %d - %s - Description: %s\n", card.getId(), card.getTitle(), card.getDescription());
                });
                System.out.println();
            });
        }
    }

    private void showCard() throws SQLException {
        System.out.println("Type the Card ID you want to see:");
        var cardID = scanner.nextLong();
        try(var connection = getConnection()) {
            new CardQueryService(connection).findById(cardID).ifPresentOrElse(
                    c -> {
                        System.out.printf("Card %s - %s\n", c.id(), c.title());
                        System.out.printf("Description - %s\n", c.description());
                        System.out.println(c.blocked() ? ("Blocked. Reason: " + c.blockReason()) : "Not blocked");
                        System.out.printf("Blocked %d times\n", c.blocksAmount());
                        System.out.printf("In column: %s\n", c.columnName());
                    },
                    () -> System.out.printf("Card ID not found: %d\n", cardID)
            );
            System.out.println();
        }
    }
}
