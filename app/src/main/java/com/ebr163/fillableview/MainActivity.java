package com.ebr163.fillableview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ebr163.view.FilledView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Random rnd = new Random();
    private FilledView filledViewLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filledViewLeft = findViewById(R.id.fillable_view_left);

        Button changeColorButton = findViewById(R.id.changeColorBtn);
        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                filledViewLeft.setFillColor(color);
            }
        });

        Button changeTextButton = findViewById(R.id.changeTextBtn);
        changeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                filledViewLeft.setText(String.valueOf(color));
            }
        });

        SeekBar seekbar = findViewById(R.id.seekBar);
        filledViewLeft.setProgress(seekbar.getProgress() / 100F);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                filledViewLeft.setProgress(progress / 100F);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.left:
                filledViewLeft.setStartMode(FilledView.StartMode.LEFT);
                return true;
            case R.id.top:
                filledViewLeft.setStartMode(FilledView.StartMode.TOP);
                return true;
            case R.id.right:
                filledViewLeft.setStartMode(FilledView.StartMode.RIGHT);
                return true;
            case R.id.bottom:
                filledViewLeft.setStartMode(FilledView.StartMode.BOTTOM);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
