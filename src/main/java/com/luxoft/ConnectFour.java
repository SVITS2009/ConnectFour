package com.luxoft;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * We are implementing connect four game for 2 players
 */
public class ConnectFour {

    //Define the chars for each player like R for Red and G for green
    private static final char[] PLAYERS = {'R', 'G'};

    private final int width, height;

    private final char[][] grid;

    private int lastCol = -1, lastTop = -1;

    public ConnectFour(int w, int h) {
        width = w;
        height = h;
        grid = new char[h][];

        //fill the grid with blank cell
        for (int i = 0; i < h; i++) {
            Arrays.fill(grid[i] = new char[w], '|');
        }
    }

    //We use stream to make a more concise method for representing the board
    public String toString() {
        return IntStream.range(0, width)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining()) + "\n" +
                Arrays.stream(grid).map(String::new)
                        .collect(Collectors.joining("\n"));
    }

    //Get string representation of the row containing the last play of the user
    public String horizontal() {
        return new String(grid[lastTop]);
    }

    //Get string representation of the column containing the last play of the user
    public String vertical() {
        StringBuilder sb = new StringBuilder(height);
        for (int h = 0; h < height; h++) {
            sb.append(grid[h][lastCol]);
        }
        return sb.toString();
    }

    // Get string representation of the / diagonal containing the last play of the user
    public String slashDiagonal() {
        StringBuilder sb = new StringBuilder(height);
        for (int h = 0; h < height; h++) {
            int w = lastCol + lastTop - h;

            if (0 <= w && w < width) {
                sb.append(grid[h][w]);
            }
        }
        return sb.toString();
    }

    // Get string representation of the \ diagonal containing the last play of the user
    public String backSlashDiagonal() {
        StringBuilder sb = new StringBuilder(height);
        for (int h = 0; h < height; h++) {
            int w = lastCol - lastTop + h;

            if (0 <= w && w < width) {
                sb.append(grid[h][w]);
            }
        }
        return sb.toString();
    }

    public static boolean contains(String str, String substring) {
        return str.indexOf(substring) >= 0;
    }

    //create a method to check if last play is winning play
    public boolean isWiningPlay() {
        if (lastCol == -1) {
            System.err.println("No move has been made yet");
            return false;
        }

        char sym = grid[lastTop][lastCol];
        //Wining streak with the last play symbol
        String streak = String.format("%c%c%c%c", sym, sym, sym, sym);

        // check if streak in the row, column, diagonal or backSlash Diagonal
        return contains(horizontal(), streak) || contains(vertical(), streak) ||
                contains(slashDiagonal(), streak) || contains(backSlashDiagonal(), streak);
    }

    //Prompts the user for a column, repeating until a valid choice made
    public void chooseAndDrop(char symbol, Scanner input) {
        do {
            System.out.println("\nPlayer " + symbol + " choose column (1-" + width +") : ");
            int col = input.nextInt();
            //check if col is ok
            if (!(0 <= col && col < width)) {
                System.out.println("Column must be between 0 and " + (width - 1));
                continue;
            }

            //Now we can place the symbol to the first available row in the asked column.
            for (int h = height - 1; h >= 0; h--) {
                if (grid[h][col] == '|') {
                    grid[lastTop = h][lastCol = col] = symbol;
                    return;
                }
            }

            //If column is full ==> we need to ask for new input
            System.out.println("Column " + col + " is full");
        } while (true);
    }

    public static void main(String[] args) {
        //We assemble all the pieces of the puzzle for building the ConnectFour game
        try(Scanner input = new Scanner(System.in)) {
            int height =6;
            int width =7;
            int moves = height*width;

            //Creating ConnectFour Instance
            ConnectFour board = new ConnectFour(width, height);

            //We explain user how to enter their choices
            //System.out.println("Use 0-"+ (width-1) + " to choose a column");
            //We display initial board
            System.out.println(board);

            //We iterate until max moves be reached
            for(int player = 0; moves-- >0; player = 1-player) { // Simple trick to change player
               char symbol = PLAYERS[player];

               // Ask user to choose the column
                board.chooseAndDrop(symbol, input);

                // display the board
                System.out.println(board);

                // We need to check if player is won. If not, we continue otherwise we display the message.
                if(board.isWiningPlay()) {
                    System.out.println("\nPlayer " + symbol + " wins!");
                    return;
                }
            }
            System.out.println("Game over, No winner. Try Again !");
        }
    }
}
