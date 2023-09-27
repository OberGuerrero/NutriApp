package com.example.nutriapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tlRegistro = findViewById(R.id.tlRegistro);

        SpannableString spannableString = new SpannableString("Ingresando en este enlace");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(MainActivity.this, Registro.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 0, spannableString.length(), 0);

        tlRegistro.setText(spannableString);

        tlRegistro.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

        tlRegistro.setLinkTextColor(Color.BLUE);
    }
}
