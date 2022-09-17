package model;
public class Location {
    private int row;
    private int col;

    public Location(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[" + row + ", " + col + "]";
    }

}
