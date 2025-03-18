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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppInterface extends RelativeLayout
{
    private final int BORDSIZE = 9;
    private EditText[][] board;
    private GridLayout boardGrid;
    private Button newPuzzleButton;
    private Button savePuzzleButton;
    private Button retrievePuzzleButton;
    private boolean watcher = false;

    //file name that will save the current puzzle
    private final String CURRENT_PUZZLE = "gameInputs";

    //file name the will save the flags of the game
    private final String PUZZLE_FLAGS = "gameFlags";

    //game object
    private Game game ;

    public AppInterface(Context context, int size, int width )
    {
        super(context);

        game = new Game();

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
        LayoutParams gridParams = new LayoutParams(
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
        LayoutParams newPuzzleBtnLayout = new LayoutParams(0,0);
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
        LayoutParams savePuzzleBtnLayout = new LayoutParams(0,0);
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
        LayoutParams retrievePuzzleBtnLayout = new LayoutParams(0,0);
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
    public void drawInitialBoard()
    {
        int[][] brd = game.getBoard();

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

    //method return the current input in given edit text
    public String getInput(int x, int y)
    {
        return board[x][y].getText().toString();
    }

    //method clears the board
    public void clear(int x, int y)
    {
        board[x][y].setText("");
    }

    //method clears the board
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

    //method set textChange handler to edit texts
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


    //method draws new board to the screen
    public void drawNewBoard(int[][] brd , int[][] flags)
    {
        //create the board
        for (int x = 0; x < BORDSIZE; x++)
        {
            for (int y = 0; y < BORDSIZE; y++)
            {
                if(flags[x][y]== 1)
                {
                    board[x][y].setText(brd[x][y]+"");
                    board[x][y].setBackgroundColor(Color.parseColor("#606060"));
                    board[x][y].setEnabled(false);
                }
                else
                {
                    if(brd[x][y]==0)
                    {
                        board[x][y].setText("");
                        board[x][y].setBackgroundColor(Color.parseColor("#A8A8A8"));
                        board[x][y].setEnabled(true);
                    }
                    else
                    {
                        board[x][y].setText(brd[x][y]+"");
                        board[x][y].setBackgroundColor(Color.parseColor("#A8A8A8"));
                        board[x][y].setEnabled(true);
                    }

                }
            }
        }
    }

    //event handler that handles the button
    public class ButtonListener implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            int id = findButton((Button) v);

            if(id == 1) // new game
            {
                //create a new game
                game = new Game();

                //clear the board
                clearBoard();

                //turn off the text watcher
                watcher = true;

                //displaying the new board
                drawNewBoard(game.getBoard(),game.getFlags());

                //turn on the text watcher
                watcher = false;
            }
            else if (id == 2) // save current game
            {
                //save the current puzzle in a game
                try
                {
                    //open file for writing edit text status
                    FileOutputStream fout =getContext().openFileOutput(CURRENT_PUZZLE, Context.MODE_PRIVATE );

                    //String builder to hold the current Puzzle
                    StringBuilder stringBuilder = new StringBuilder();

                    //the current board
                    int[][] currentBoard = new int[BORDSIZE][BORDSIZE];

                    //reading the current board from the edit texts
                    String input ="";
                    for(int x = 0 ; x<BORDSIZE ; x++)
                    {
                        for(int y = 0 ; y< BORDSIZE ; y++)
                        {
                            input = getInput(x,y);
                            if(input.equals(""))
                                input = "0";
                            currentBoard[x][y] = Integer.parseInt(input);
                        }
                    }

                    //write the current board to the file
                    for (int x = 0; x < BORDSIZE; x++)
                    {
                        for (int y = 0; y < BORDSIZE; y++)
                        {
                            stringBuilder.append(currentBoard[x][y]);
                            if (y < BORDSIZE - 1)
                                stringBuilder.append(","); // Separate numbers by commas
                        }
                        stringBuilder.append("\n");
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
                    FileOutputStream fout =getContext().openFileOutput(PUZZLE_FLAGS, Context.MODE_PRIVATE );

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
                        stringBuilder.append("\n");
                    }

                    //write string builder to the file
                    fout.write(stringBuilder.toString().getBytes());
                    fout.close();
                }
                catch (IOException e)
                {

                }
            }
            else if(id == 3) // retrieve game
            {
                // board from the file
                int[][] boardFromFile = new int[BORDSIZE][BORDSIZE];
                int[][] flagsFromFile = new int[BORDSIZE][BORDSIZE];

                //open , retrieve the saved puzzle
                try
                {
                    FileInputStream fin = getContext().openFileInput(CURRENT_PUZZLE);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fin));


                    String line;
                    int row = 0 ;

                    // Read the file line by line
                    while ((line = reader.readLine()) != null && row < BORDSIZE)
                    {
                        String[] values = line.split(","); // Split by commas
                        for (int col = 0; col < BORDSIZE; col++)
                        {
                            boardFromFile[row][col] = Integer.parseInt(values[col].trim());
                        }
                        row++;
                    }

                    // Close the reader
                    reader.close();
                }
                catch (IOException e)
                {

                }

                //open, retrieve the saved flags
                try
                {
                    FileInputStream fin = getContext().openFileInput(PUZZLE_FLAGS);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fin));


                    String line;
                    int row = 0 ;

                    // Read the file line by line
                    while ((line = reader.readLine()) != null && row < BORDSIZE)
                    {
                        String[] values = line.split(","); // Split by commas
                        for (int col = 0; col < BORDSIZE; col++)
                        {
                            flagsFromFile[row][col] = Integer.parseInt(values[col].trim());
                        }
                        row++;
                    }

                    // Close the reader
                    reader.close();
                }
                catch (IOException e)
                {

                }

                game.setBoard(boardFromFile);
                game.setFlags(flagsFromFile);

                watcher = true;
                clearBoard();
                drawNewBoard(game.getBoard(), game.getFlags());
                watcher = false;
            }

        } //end of on click method


    }// end of button listener class

    //text watcher watches the edit texts changes
    public class TextChangedListener implements TextWatcher
    {
        private int x;
        private int y;

        public TextChangedListener(int x, int y)
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
            if(watcher)
                return;

            //get the input from the edit text
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
                    clear(x,y);
                }
                else
                {
                    //set 0 at x,y on board
                    game.setValue(value,x,y);

                    //clear x,y on interface
                   // clear(x,y);

                }
            }
        }
    } //end of text watcher class


}//end of class


