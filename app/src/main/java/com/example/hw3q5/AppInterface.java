package com.example.hw3q5;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppInterface extends RelativeLayout
{
    private final int BORDSIZE = 9;
    private EditText[][] board;
    private GridLayout boardGrid;
    private Button newPuzzleButton;
    private Button savePuzzleButton;
    private Button retrievePuzzleButton;

    //file name that will save the current puzzle
    private final String CURRENT_PUZZLE = "gameInputs";

    private final String PUZZLE_FLAGS = "gameFlags";

    private Game game;

    public AppInterface(Context context, int size, int width )
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
        drawButtons(context);

    }

    //method to display the buttons on the screen
    private void drawButtons(Context context)
    {
        ButtonListener listener = new ButtonListener();

        final int DP = (int)(getResources().getDisplayMetrics().density);

        //New Puzzle Button
        newPuzzleButton = new Button(context);
        newPuzzleButton.setId(Button.generateViewId());
        newPuzzleButton.setText("New");
        newPuzzleButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        newPuzzleButton.setBackgroundColor(Color.parseColor("#6495ED"));
        newPuzzleButton.setOnClickListener(listener);
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
        savePuzzleButton.setOnClickListener(listener);
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
        retrievePuzzleButton.setOnClickListener(listener);
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

    public void clearBoard()
    {
        for(int x = 0 ; x < BORDSIZE ; x++)
        {
            for (int y = 0; y < BORDSIZE; y++)
            {
                board[x][y].setText("");
                board[x][y].setEnabled(true);
            }
        }
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

    //event handler that handles the button
    public class ButtonListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            int id = findButton((Button) v);

            if(id == 1) // new game
            {
                game = new Game();
                clearBoard();
                drawInitialBoard(game.getBoard());

            }
            else if (id == 2) // save current game
            {
                //save the current puzzle in a game
                try
                {
                    //open file for writing edit text status
                    FileOutputStream fout =getContext().openFileOutput(CURRENT_PUZZLE, Context.MODE_PRIVATE | Context.MODE_APPEND);

                    //String builder to hold the current Puzzle
                    StringBuilder stringBuilder = new StringBuilder();


                    //get the current board
                    int[][] currentBoard = game.getBoard();

                    for (int x = 0; x < BORDSIZE; x++)
                    {
                        for (int y = 0; y < BORDSIZE; y++)
                        {
                            stringBuilder.append(currentBoard[x][y]);
                            if (y < BORDSIZE - 1)
                                stringBuilder.append(","); // Separate numbers by commas
                        }
                    }

                    //write string builder to the file
                    fout.write(stringBuilder.toString().getBytes());
                    fout.close();
                }
                catch (IOException e)
                {

                }

                //save the flags of the current game
                try
                {
                    //open file for writing edit text status
                    FileOutputStream fout =getContext().openFileOutput(PUZZLE_FLAGS, Context.MODE_PRIVATE | Context.MODE_APPEND);

                    //String builder to hold the current Puzzle
                    StringBuilder stringBuilder = new StringBuilder();


                    //get the current board
                    int[][] flags = game.getFlags();

                    for (int x = 0; x < flags.length; x++)
                    {
                        for (int y = 0; y < flags.length; y++)
                        {
                            stringBuilder.append(flags[x][y]);
                            if (y < BORDSIZE - 1)
                                stringBuilder.append(","); // Separate numbers by commas
                        }
                    }

                    //write string builder to the file
                    fout.write(stringBuilder.toString().getBytes());
                    fout.close();
                }
                catch (IOException e)
                {

                }



                //prints
                System.out.println("The current game");
                for (int x = 0; x < game.getBoard().length; x++)
                {
                    for (int y = 0; y < game.getBoard().length; y++)
                    {
                        System.out.print(game.getBoard()[x][y]+" ");
                    }
                    System.out.println();
                }

                System.out.println("The flags");
                int[][] flags = game.getFlags();
                for (int x = 0; x < flags.length; x++)
                {
                    for (int y = 0; y < flags.length; y++)
                    {
                        System.out.print(flags[x][y]+" ");
                    }
                    System.out.println();
                }


            }
            else if(id == 3) // retrieve game
            {

            }

        }


    }

    public class TextChangedListern implements TextWatcher
    {
        private int x;
        private int y;

        public TextChangedListern(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @Override
        public void afterTextChanged(Editable s) //here
        {
            String input = getInput(x,y);

            //if input (edit text) is equal to ""
            //set the value on the board to a zero
            if(input.equals(""))
            {
                game.setValue(0,x,y);
            }
            //if input (edit text) is equal to "0"
            //clear x,y on the interface
            else if(input.equals("0"))
            {
                game.setValue(0,x,y);
                clear(x,y);
            }
            //if the input length is greater than 1
            //set 0 at x,y on board
            //clear x,y on interface
            else if(input.length() > 1)
            {
                game.setValue(0,x,y);
                clear(x,y);

            }
            else
            {
                //value = convert input to integer
                int value=0;
                //convert input to integer
                try
                {
                    value = Integer.parseInt(input);
                }
                catch (NumberFormatException e)
                {}

                //if value can be placed at x ,y on game board
                if(!game.check(value,x,y))
                {
                    //set value at x,y on board
                    game.setValue(value,x,y);
                }
                else
                {
                    //set 0 at x,y on board
                    game.setValue(0,x,y);

                    //clear x,y on interface
                    clear(x,y);

                }
            }



        }
    }
}
