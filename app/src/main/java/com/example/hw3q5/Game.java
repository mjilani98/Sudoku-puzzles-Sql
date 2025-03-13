package com.example.hw2q8;

public class Game
{
    private int[][] board;

    public Game()
    {
        //create Sudoku
        Sudoku sudoku = new Sudoku();

        //get a board
        board = sudoku.generate();
    }

    //method to return the  board
    public int[][] getBoard()
    {
        return board;
    }

    //method that sets a value on the board
    public void setValue(int value,int x,int y)
    {
        board[x][y] = value;
    }

    public boolean check(int value , int x, int y )
    {
        int startX =0;
        int startY=0;

        //based on the value of x ,y. will determine which box to search in

        //check where to start on x
        if(x<=2)
        {
            startX = 0;
        }
        else if (x<=5)
        {
            startX = 3;
        }
        else if (x<=8)
        {
            startX = 6;
        }

        //check where to start on y
        if(y<=2)
        {
            startY = 0;
        }
        else if (y<=5)
        {
            startY = 3;
        }
        else if (y<=8)
        {
            startY = 6;
        }

        //search in the box if it has the value that entered by the user
        //if exist in the box , return false
        //else return true

        //the loop will check in a box of 3x3
        for( int i = startX ; i < (startX+3) ; i++)
        {
            for( int j = startY ; j < (startY+3) ; j++)
            {
                //if the value exists in the board
                if(board[i][j] == value)
                    return true ;
            }
        }

        return false;
    }

    //method that checks if the board is completed
    //if there is a 0  in the board , it is not completed yet -> returns false
    public boolean checkCompleted()
    {
        for(int x = 0 ; x <board.length ; x++)
        {
            for(int y =0 ; y < board.length;y++)
            {
                if(board[x][y] == 0)
                    return false;
            }
        }

        return true;
    }


}
