package com.example.hw3q5;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    //the size of the board
    private final int SIZE = 9;

    //game
    private Game game;

    //app interface
    private AppInterface appInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        int screenSize = getWindowManager().getCurrentWindowMetrics().getBounds().width();
        int width = screenSize/SIZE;


        //create game
        game = new Game();

        //create app interface
        appInterface = new AppInterface(this,SIZE,width);

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