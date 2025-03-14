package com.example.hw3q5;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppInterface extends RelativeLayout
{
    private final int BORDSIZE = 9;
    private EditText[][] board;
    private GridLayout boardGrid;
    private Button newPuzzleButton;
    private Button savePuzzleButton;
    private Button retrievePuzzleButton;

    public AppInterface(Context context, int size, int width , OnClickListener buttonHandler)
    {
        super(context);


        // Create a GridLayout for the Sudoku board
        boardGrid = new GridLayout(context);
        boardGrid.setRowCount(BORDSIZE);
        boardGrid.setColumnCount(BORDSIZE);
        boardGrid.setId(View.generateViewId()); // Unique ID

        // Sudoku board setup
        board = new EditText[BORDSIZE][BORDSIZE];

        //creating Sudoku
        for (int x = 0; x < BORDSIZE; x++)
        {
            for (int y = 0; y < BORDSIZE; y++)
            {
                board[x][y] = new EditText(context);
                board[x][y].setText("");
                board[x][y].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                board[x][y].setGravity(Gravity.CENTER);
                board[x][y].setTextSize(Math.min((float) (width * 0.2), 24)); // Limit size
                board[x][y].setTextColor(Color.BLACK);
                board[x][y].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = width;
                params.height = width;
                params.rowSpec = GridLayout.spec(x, 1);
                params.columnSpec = GridLayout.spec(y, 1);
                params.topMargin = params.bottomMargin = 1;
                params.leftMargin = params.rightMargin = 1;
                board[x][y].setLayoutParams(params);

                boardGrid.addView(board[x][y]);
            }
        }

        // Sudoku Grid Layout Parameters
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        gridParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        gridParams.topMargin = 50;
        boardGrid.setLayoutParams(gridParams);

        //add the Sudoku to the screen
        addView(boardGrid);

        //draw buttons and display them to the screen
        drawButtons(context,buttonHandler);

    }

    //method to display the buttons on the screen
    private void drawButtons(Context context,OnClickListener buttonHandler)
    {
        final int DP = (int)(getResources().getDisplayMetrics().density);

        //New Puzzle Button
        newPuzzleButton = new Button(context);
        newPuzzleButton.setId(Button.generateViewId());
        newPuzzleButton.setText("New");
        newPuzzleButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        newPuzzleButton.setBackgroundColor(Color.parseColor("#6495ED"));
        newPuzzleButton.setOnClickListener(buttonHandler);
        //layouts for the new puzzle button
        RelativeLayout.LayoutParams newPuzzleBtnLayout = new RelativeLayout.LayoutParams(0,0);
        newPuzzleBtnLayout.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        newPuzzleBtnLayout.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        newPuzzleBtnLayout.leftMargin = 50 * DP;
        newPuzzleBtnLayout.topMargin = 150 * DP;
        newPuzzleBtnLayout.addRule(RelativeLayout.BELOW,boardGrid.getId());
        newPuzzleButton.setLayoutParams(newPuzzleBtnLayout);
        addView(newPuzzleButton);

        //save puzzle button
        savePuzzleButton = new Button(context);
        savePuzzleButton.setId(Button.generateViewId());
        savePuzzleButton.setText("Save");
        savePuzzleButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        savePuzzleButton.setBackgroundColor(Color.parseColor("#6495ED"));
        savePuzzleButton.setOnClickListener(buttonHandler);
        //layouts for the save puzzle button
        RelativeLayout.LayoutParams savePuzzleBtnLayout = new RelativeLayout.LayoutParams(0,0);
        savePuzzleBtnLayout.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        savePuzzleBtnLayout.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        savePuzzleBtnLayout.leftMargin = 50 * DP;
        savePuzzleBtnLayout.topMargin = 150 * DP;
        savePuzzleBtnLayout.addRule(RelativeLayout.BELOW,boardGrid.getId());
        savePuzzleBtnLayout.addRule(RelativeLayout.RIGHT_OF,newPuzzleButton.getId());
        savePuzzleButton.setLayoutParams(savePuzzleBtnLayout);
        addView(savePuzzleButton);

        //retrieve puzzle button
        retrievePuzzleButton = new Button(context);
        retrievePuzzleButton.setId(Button.generateViewId());
        retrievePuzzleButton.setText("Retrieve");
        retrievePuzzleButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        retrievePuzzleButton.setBackgroundColor(Color.parseColor("#6495ED"));
        retrievePuzzleButton.setOnClickListener(buttonHandler);
        //layouts for the retrieve puzzle button
        RelativeLayout.LayoutParams retrievePuzzleBtnLayout = new RelativeLayout.LayoutParams(0,0);
        retrievePuzzleBtnLayout.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        retrievePuzzleBtnLayout.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        retrievePuzzleBtnLayout.leftMargin = 50 * DP;
        retrievePuzzleBtnLayout.topMargin = 150 * DP;
        retrievePuzzleBtnLayout.addRule(RelativeLayout.BELOW,boardGrid.getId());
        retrievePuzzleBtnLayout.addRule(RelativeLayout.RIGHT_OF,savePuzzleButton.getId());
        retrievePuzzleButton.setLayoutParams(retrievePuzzleBtnLayout);
        addView(retrievePuzzleButton);


    }


    // Method to display the board
    public void drawInitialBoard(int[][] brd)
    {
        for (int x = 0; x < BORDSIZE; x++)
        {
            for (int y = 0; y < BORDSIZE; y++)
            {
                board[x][y].setEnabled(true);
                if (brd[x][y] == 0)
                {
                    board[x][y].setText("");
                    board[x][y].setBackgroundColor(Color.parseColor("#A8A8A8"));
                }
                else
                {
                    board[x][y].setText(String.valueOf(brd[x][y]));
                    board[x][y].setBackgroundColor(Color.parseColor("#606060"));
                    board[x][y].setEnabled(false);
                }
            }
        }
    }

    public String getInput(int x, int y)
    {
        return board[x][y].getText().toString();
    }

    public void clear(int x, int y)
    {
        board[x][y].setText("");
    }

    public void setTextChangeHandler(TextWatcher temp, int x, int y)
    {
        board[x][y].addTextChangedListener(temp);
    }

    //method to return the button id
    public int findButton(Button button)
    {
        int id= 0 ;
        //if new puzzle button is clicked, return 1
        if(button.getId() == newPuzzleButton.getId())
        {
            id = 1;
        }
        //if save puzzle button is clicked, return 2
        if(button.getId() == savePuzzleButton.getId())
        {
            id = 2;
        }
        //if retrieve puzzle button is clicked, return 3
        if(button.getId() == retrievePuzzleButton.getId())
        {
            id = 3;
        }

        return id;
    }
}
