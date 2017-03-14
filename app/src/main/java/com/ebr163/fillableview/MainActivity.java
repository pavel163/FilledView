package com.ebr163.fillableview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.ebr163.view.FilledView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Random rnd = new Random();
    private FilledView filledViewLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filledViewLeft = (FilledView) findViewById(R.id.fillable_view_left);

        Button changeColorButton = (Button) findViewById(R.id.changeColorBtn);
        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                filledViewLeft.setFillColor(color);
                filledViewLeft.setText(String.valueOf(color));
            }
        });

        SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
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
}
