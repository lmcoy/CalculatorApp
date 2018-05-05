package de.lennart_oymanns.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ArrayList<String> listItems = null;
    ArrayAdapter<String> adapter;

    private ListView lview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Intent intent = getIntent();
        listItems = intent.getStringArrayListExtra(MainActivity.HISTORY);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        lview = (ListView) findViewById(R.id.historylist);
        lview.setAdapter(adapter);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parentView, View childView,
                                    int position, long id) {
                Intent in = new Intent();
                String expr = lview.getItemAtPosition(position).toString();
                in.putExtra(MainActivity.EXPR_FROM_HISTORY, expr);
                setResult(Activity.RESULT_OK,
                        in);
                finish();
            }

            public void onNothingSelected(AdapterView parentView) {

            }
        });

        Button button_clear = (Button) findViewById(R.id.button_clear);
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.putExtra(MainActivity.CLEAR_HISTORY, true);
                setResult(Activity.RESULT_OK,
                        in);
                finish();
            }
        });
    }
}
