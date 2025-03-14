package com.example.hw3q5;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //the size of the board
    private final int SIZE = 9;

    //game
    private Game game;

    //app interface
    private AppInterface appInterface;

    //name of the file to save the puzzle
    private final String FILE_NAME ="sudokuGame";

    //name of the file that will store the current status of edit text
    private final String CURRENT_EDIT_TEXT = "gameInputs";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //setting layout
        int screenSize = getWindowManager().getCurrentWindowMetrics().getBounds().width();
        int width = screenSize/SIZE;

        //create a button handler object
        ButtonHandler handler = new ButtonHandler();

        //create game
        game = new Game();

        //create app interface
        appInterface = new AppInterface(this,SIZE,width,handler);

        //set content view of the screen
        setContentView(appInterface);

        //get initial board and display in appInterface
        int[][] board = game.getBoard();
        appInterface.drawInitialBoard(board);

        //attach event handler to all edit text
        for(int x = 0 ; x< board.length ; x++)
        {
            for(int y = 0 ; y < board.length ; y++)
            {
                TextChangeHandler temp = new TextChangeHandler(x,y);
                appInterface.setTextChangeHandler(temp,x,y);
            }
        }

    }//end of constructor

    //event handler that handles the button
    public class ButtonHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {

            //get the button id
            int id=0;
            id = appInterface.findButton((Button) v);

            if(id == 1) //new game
            {
               //create a new game
                game = new Game();

                //display the new game
                appInterface.drawInitialBoard(game.getBoard());

                //resetting the text change handlers for the new edit texts
                for(int x = 0; x < SIZE; x++)
                {
                    for(int y = 0; y < SIZE; y++)
                    {
                        TextChangeHandler temp = new TextChangeHandler(x, y);
                        appInterface.setTextChangeHandler(temp, x, y);
                    }
                }

            }

            if(id == 2) //save game
            {
                //save the current game in a file
                try
                {
                    //open file for write
                    FileOutputStream fout = openFileOutput(FILE_NAME, Context.MODE_PRIVATE | Context.MODE_APPEND);

                    //String builder to hold the current Puzzle
                    StringBuilder stringBuilderb = new StringBuilder();

                    //get the current board
                    int[][] borad = game.getBoard();

                    // Convert board to a string format (e.g., CSV style)
                    for (int x = 0; x < SIZE; x++)
                    {
                        for (int y = 0; y < SIZE; y++)
                        {
                            stringBuilderb.append(borad[x][y]);
                            if (y < SIZE - 1)
                                stringBuilderb.append(","); // Separate numbers by commas
                        }
                        stringBuilderb.append("\n"); // New row
                    }

                    //write the current bord to the file
                    fout.write(stringBuilderb.toString().getBytes());
                    fout.close();

                }
                catch (IOException e)
                {

                }

                //save the current status of the current edit texts
                try
                {
                    //open file for writing edit text status
                    FileOutputStream fout = openFileOutput(CURRENT_EDIT_TEXT, Context.MODE_PRIVATE | Context.MODE_APPEND);

                    //String builder to hold the current Puzzle
                    StringBuilder stringBuilderb = new StringBuilder();


                    //get the current board
                    int[][] borad = game.getBoard();

                    for (int x = 0; x < SIZE; x++)
                    {
                        for (int y = 0; y < SIZE; y++)
                        {
                            if(borad[x][y] > 0)
                                stringBuilderb.append(1);
                            else
                                stringBuilderb.append(0);

                            if (y < SIZE - 1)
                                stringBuilderb.append(","); // Separate numbers by commas
                        }
                    }

                    //write string builder to the file
                    fout.write(stringBuilderb.toString().getBytes());
                    fout.close();
                }
                catch (IOException e)
                {

                }

            }

            if(id == 3) //retrieve game
            {

            }

        }
    }



    public class TextChangeHandler implements TextWatcher
    {
        private int x;
        private int y;

        public TextChangeHandler(int x, int y)
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
            String input = appInterface.getInput(x,y);

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
                appInterface.clear(x,y);
            }
            //if the input length is greater than 1
            //set 0 at x,y on board
            //clear x,y on interface
            else if(input.length() > 1)
            {
                game.setValue(0,x,y);
                appInterface.clear(x,y);

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
                    appInterface.clear(x,y);

                }
            }

            if(game.checkCompleted())
            {
                showDialogBox();
            }

        }
    }
    private void showDialogBox()
    {
        //create a dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        //setting the message of the dialog box
        dialog.setMessage("Success , Do you want to play again?");

        //creating a listener for the dialog box
        DialogBoxListener handler = new DialogBoxListener();

        //setting the buttons labels
        dialog.setPositiveButton("Yes",handler);
        dialog.setNegativeButton("No",handler);
        dialog.setNeutralButton("Cancel",handler);

        //show the dialog box
        dialog.show();
    }

    private class DialogBoxListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int id)
        {

            //if the positive button is clicked, play a new game ;
            if(id == -1)
            {
                //create a new game
                game = new Game();
                int[][] newBoard = game.getBoard();


                //display the new game to the screen
                appInterface.drawInitialBoard(newBoard);

                //resetting the text change handlers for the new edit texts
                for(int x = 0; x < SIZE; x++)
                {
                    for(int y = 0; y < SIZE; y++)
                    {
                        TextChangeHandler temp = new TextChangeHandler(x, y);
                        appInterface.setTextChangeHandler(temp, x, y);
                    }
                }

            }
            //if the negative button is clicked, destroy the app
            else if (id == -2)
            {
                MainActivity.this.finish();
            }
            else;
        }
    }
}