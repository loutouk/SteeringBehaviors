import java.util.ArrayList;

public class SpaceDivider {

    private Cell[][] cells;
    private int cellLength;

    // cellsLength should be approximately the size of the bird/object hitbox or visibility radius
    public SpaceDivider(int width, int height, int cellsLength, ArrayList<Bird> birds) {
        this.cellLength = cellsLength;

        this.cells = new Cell[width / cellsLength][];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Cell[height / cellsLength];
        }
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell getCorrespondingBirdsCell(int x, int y) {
        int xIndex = x / cellLength - 1;
        int yIndex = y / cellLength - 1;
        if (x / cellLength - 1 < 0) xIndex = 0;
        if (y / cellLength - 1 < 0) yIndex = 0;
        return cells[xIndex][yIndex];
    }

}
