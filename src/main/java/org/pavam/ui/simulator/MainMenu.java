package org.pavam.ui.simulator;

import org.pavam.persistence.entity.BoardColumnEntity;
import org.pavam.persistence.entity.BoardColumnKindEnum;
import org.pavam.persistence.entity.BoardEntity;
import org.pavam.service.BoardQueryService;
import org.pavam.service.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.pavam.persistence.config.ConnectionConfig.getConnection;
import static org.pavam.persistence.entity.BoardColumnKindEnum.INITIAL;

public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);

    public void execute() throws SQLException {
        var option = -1;
        System.out.println("Welcome to the Task Board!");
        while(true) {
            System.out.println("\nSelect an option:");
            System.out.println("\t1 - Create a board");
            System.out.println("\t2 - Select a board");
            System.out.println("\t3 - Delete a board");
            System.out.println("\t4 - Exit program");
            option = scanner.nextInt();
            switch(option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Select a valid option");
            }
        }
    }

    private void createBoard() throws SQLException {
        BoardEntity boardEntity = new BoardEntity();
        System.out.println("Insert board name:");
        var boardName = scanner.next();
        boardEntity.setName(boardName);

        System.out.println("Will the board have additional columns?\nInsert the number of new columns, or 0 to none.");
        var additionalColumns = scanner.nextInt();
        List<BoardColumnEntity> boardColumnEntityList = new ArrayList<>();
        boardEntity.setBoardColumns(boardColumnEntityList);

        System.out.println("Initial column name: ");
        var initialColumnName = scanner.next();
        BoardColumnEntity initialColumnEntity = createBoardColumn(initialColumnName, INITIAL, 0);
        boardColumnEntityList.add(initialColumnEntity);

        for(int i = 0; i < additionalColumns; i++) {
            System.out.println("Pending column name: ");
            var columnName = scanner.next();
            BoardColumnEntity pendingColumnEntity = createBoardColumn(columnName, INITIAL, i+1);
            boardColumnEntityList.add(pendingColumnEntity);
        }

        System.out.println("Final column name: ");
        var finalColumnName = scanner.next();
        BoardColumnEntity finalColumnEntity = createBoardColumn(initialColumnName, INITIAL, additionalColumns + 1);
        boardColumnEntityList.add(finalColumnEntity);

        System.out.println("Canceled column name: ");
        var canceledColumnName = scanner.next();
        BoardColumnEntity canceledColumnEntity = createBoardColumn(initialColumnName, INITIAL, additionalColumns + 2);
        boardColumnEntityList.add(canceledColumnEntity);

        try(var connection = getConnection()) {
            var boardService = new BoardService(connection);
            boardService.insert(boardEntity);
        }

    }

    private void selectBoard() throws SQLException {
        System.out.println("Type board ID you want to select:");
        var boardId = scanner.nextLong();
        try(var connection = getConnection()) {
            BoardQueryService boardQueryService = new BoardQueryService(connection);
            var boardSelected = boardQueryService.findById(boardId);
            boardSelected.ifPresentOrElse(
                    board -> new BoardMenu(board).execute(),
                    () -> System.out.printf("Board not found - ID %d\n", boardId));
        }
    }

    private void deleteBoard() throws SQLException {
        System.out.println("Type boards ID yto be deleted:");
        int boardId = scanner.nextInt();
        try(var connection = getConnection()) {
            var boardService = new BoardService(connection);
            if(boardService.delete(boardId)) {
                System.out.println("Board deleted");
            } else {
                System.out.printf("Board not found - ID %d\n", boardId);
            }
        }
    }

    private BoardColumnEntity createBoardColumn(final String name, final BoardColumnKindEnum kind, final int order) {
        BoardColumnEntity boardColumnEntity = new BoardColumnEntity();
        boardColumnEntity.setDescription(name);
        boardColumnEntity.setKind(kind);
        boardColumnEntity.setOrder(order);
        return boardColumnEntity;
    }



}
