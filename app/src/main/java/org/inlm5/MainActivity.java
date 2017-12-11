package org.inlm5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textArea);
        Button start = findViewById(R.id.newGameButton);
        Button exit = findViewById(R.id.exitButton);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.append("aaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
        textView.append("bbbbbbbbbbbbbbbbbbbbbbbbb\n");

    }


    private void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
