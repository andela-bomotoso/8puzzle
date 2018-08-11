import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Board {
    private int[][] blocksCopy;
    private int n;

    public Board(int[][] blocks) {
        if (blocks == null)
            throw new IllegalArgumentException();
        this.n = blocks.length;
        blocksCopy = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocksCopy[i][j] = blocks[i][j];

    }       // construct a board from an n-by-n array of blocksCopy

    // (where blocksCopy[i][j] = block in row i, column j)
    public int dimension() {
        return blocksCopy.length;
    }           // board dimension n

    public int hamming() {
        int outOfPlaceBlocks = 0;
        int counter = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                counter++;
                if (blocksCopy[i][j] != counter)
                    outOfPlaceBlocks++;
            }
        if (outOfPlaceBlocks > 0)
            outOfPlaceBlocks--;
        return outOfPlaceBlocks;
    }               // number of blocksCopy out of place

    public int manhattan() {
        int manhattanSum = 0;
        int currentmanhattan = 0;
        int expectedRow = 0;
        int expectedColumn = 0;
        int num = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                num = blocksCopy[i][j];
                if (num == 0)
                    continue;
                if (num % n != 0)
                    expectedRow = num / n;
                else
                    expectedRow = (num / n) - 1;

                expectedColumn = num - (expectedRow * n + 1);
                currentmanhattan = Math.abs(expectedRow - i) + Math.abs(expectedColumn - j);
                manhattanSum = manhattanSum + currentmanhattan;
            }
        }

        return manhattanSum;
    }          // sum of Manhattan distances between blocksCopy and goal

    public boolean isGoal() {
        int counter = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1)
                    continue;
                if (blocksCopy[i][j] != counter)
                    return false;
                counter++;
            }
        }
        return true;
    }           // is this board the goal board?

    public Board twin() {
        ArrayList<Integer> arrayList = flattenArray(blocksCopy);
        int[][] blocksCopy = this.blocksCopy;
        outerloop:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                int index = i * n + j;
                if ((blocksCopy[i][j] != 0) && (blocksCopy[i][j + 1] != 0)) {
                    Collections.swap(arrayList, index, index + 1);
                    break outerloop;
                }
            }
        }

        return listToBoard(arrayList);
    }          // a board that is obtained by exchanging any pair of blocksCopy

    public boolean equals(Object y) {
        // int[][] com = (new int[n][n])(y);
        if (this == y)
            return true;
        if (y == null) return false;
        if (y instanceof Board) {
            Board anotherBoard = (Board) y;
            if (anotherBoard.dimension() == this.dimension()) {
                int[][] thisBoard = this.blocksCopy;
                int[][] thatBoard = anotherBoard.blocksCopy;
                for (int i = 0; i < dimension(); i++) {
                    for (int j = 0; j < dimension(); j++) {
                        if (thisBoard[i][j] != thatBoard[i][j])
                            return false;
                    }
                }
                return true;
            }
        }
        return false;
    }    // does this board equal y?

    private int getIndexOfBlank(int[][] blocks) {
        ArrayList<Integer> arr = flattenArray(blocks);
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i : arr) {
            arrayList.add(i);
        }
        int zeroIndex = arrayList.indexOf(0);
        return zeroIndex;
    }


    public Iterable<Board> neighbors() {

        Stack<Board> boardNeighbours = new Stack<>();
        int zeroIndex = getIndexOfBlank(blocksCopy);

        //swap left
        //Only swap if the blank block is not at the extreme left
        if ((zeroIndex % n) != 0) {
            ArrayList<Integer> arr1 = flattenArray(blocksCopy);
            Collections.swap(arr1, zeroIndex, zeroIndex - 1);
            boardNeighbours.push(listToBoard(arr1));
        }

        //swap right
        //Only swap if the blank block is not at the extreme right
        if ((zeroIndex + 1) % n != 0) {
            ArrayList<Integer> arr2 = flattenArray(blocksCopy);
            Collections.swap(arr2, zeroIndex, zeroIndex + 1);
            boardNeighbours.push(listToBoard(arr2));
        }

        //swap top
        if (zeroIndex >= n) {
            ArrayList<Integer> arr3 = flattenArray(blocksCopy);
            Collections.swap(arr3, zeroIndex, zeroIndex - n);
            boardNeighbours.push(listToBoard(arr3));
        }

        //swap bottom
        if (zeroIndex < (n * n - n)) {
            ArrayList<Integer> arr4 = flattenArray(blocksCopy);
            Collections.swap(arr4, zeroIndex, zeroIndex + n);
            boardNeighbours.push(listToBoard(arr4));
        }

        return boardNeighbours;
    }  // all neighboring boards

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocksCopy[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }    // string representation of this board (in the output format specified below)

    private static ArrayList flattenArray(int[][] initialArray) {
        ArrayList<Integer> flattenedArray = new ArrayList<>();
        for (int i = 0; i < initialArray.length; i++) {
            for (int j = 0; j < initialArray[i].length; j++) {
                flattenedArray.add(initialArray[i][j]);
            }
        }

        return flattenedArray;
    }

    private Board listToBoard(ArrayList<Integer> arrayList) {

        int[][] returnBlock = new int[dimension()][dimension()];

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                returnBlock[i][j] = arrayList.get(i * n + j);
            }
        }
        return new Board(returnBlock);

    }

    public static void main(String[] args) {
        int[][] blocks = {{10, 11, 2}, {3, 14, 5}, {6, 7, 8}};
        String boardString = 3 + "\n";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardString += blocks[i][j] + " ";
            }
            boardString += "\n";
        }
        System.out.println(boardString);

    }

}
