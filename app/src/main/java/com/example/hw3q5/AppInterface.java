package com.example.hw3q5;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;

public class AppInterface extends GridLayout
{
    //the size of the sudoku board
    private final int BORDSIZE = 9;

    private EditText[][] board;
    private GridLayout myGrid;

    public AppInterface(Context context,int size,int width)
    {
        super(context);

        //setting the number of rows and columns
        setRowCount(BORDSIZE);
        setColumnCount(BORDSIZE);

        //create a board in grid layout
        board = new EditText[BORDSIZE][BORDSIZE];

        for(int x  = 0 ; x < BORDSIZE ; x++)
        {
            for(int y = 0 ; y < BORDSIZE ; y++ )
            {
                board[x][y] = new EditText(context);
                board[x][y].setText("");
                board[x][y].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                board[x][y].setGravity(Gravity.CENTER);
                board[x][y].setTextSize((int)(width*0.2));
                board[x][y].setTextColor(Color.parseColor("#FF000000"));
                board[x][y].setInputType(InputType.TYPE_CLASS_NUMBER);
                LayoutParams params = new LayoutParams( );
                params.width = width;
                params.height = width;
                params.rowSpec = GridLayout.spec(x, 1);
                params.columnSpec = GridLayout.spec(y, 1);
                params.topMargin = params.bottomMargin = 1;
                params.leftMargin = params.rightMargin = 1;
                if (x == 0) params.topMargin = 80;
                board[x][y].setLayoutParams(params);
                addView(board[x][y]);
            }
        }

    } //end of constructor

    //method to display the board
    public void drawInitialBoard(int[][] brd)
    {
        //display contents of board
        for(int x  = 0 ; x < BORDSIZE ; x++)
        {
            for (int y = 0; y < BORDSIZE; y++)
            {

                board[x][y].setEnabled(true);

                //if value = o
                if(brd[x][y]==0)
                {
                    //display empty string
                    board[x][y].setText("");
                    //set background color
                    board[x][y].setBackgroundColor(Color.parseColor("#A8A8A8"));
                }
                else
                {
                    //display value
                    board[x][y].setText(String.valueOf(brd[x][y]));
                    //set different background color
                    board[x][y].setBackgroundColor(Color.parseColor("#606060"));
                    //disable edit text
                    board[x][y].setEnabled(false);
                }
            }
        }
    }

    //method to get the input from the edit text
    public String getInput(int x ,int y)
    {
        String strInput = board[x][y].getText().toString();

        return strInput;
    }

    //method displays an empty text at  given location
    public void clear(int x,int y)
    {
        board[x][y].setText("");
    }

    //method adds a textChange handler to edit text
    public void setTextChangeHandler(TextWatcher temp, int x, int y)
    {
        board[x][y].addTextChangedListener(temp);
    }

}
