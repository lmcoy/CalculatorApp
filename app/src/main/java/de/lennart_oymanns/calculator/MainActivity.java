package de.lennart_oymanns.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    final static String HISTORY = "de.lennart_oymanns.Calculator.history";
    final static String EXPR_FROM_HISTORY = "de.lennart_oymanns.Calculator.expr_from_history";
    final static String CLEAR_HISTORY = "de.lennart_oymanns.Calculator.clear_history";
    private static final int GET_EXPR_FROM_HISTORY = 0;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private HashMap<String, String> variables = new HashMap<String, String>();
    private LinkedList<Result> results = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ansbutton = (Button) findViewById(R.id.button_ans);
        ansbutton.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                try {
                    String val = variables.get("ANS").toString();
                    Toast.makeText(v.getContext(), "ANS = " + val, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
                return true;
            }
        });

        Button buttona = (Button) findViewById(R.id.button_a);
        buttona.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                try {
                    String val = variables.get("A").toString();
                    Toast.makeText(v.getContext(), "A = " + val, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
                return true;
            }
        });

        Button buttonb = (Button) findViewById(R.id.button_b);
        buttonb.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                try {
                    String val = variables.get("B").toString();
                    Toast.makeText(v.getContext(), "B = " + val, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
                return true;
            }
        });

        Button buttonc = (Button) findViewById(R.id.button_c);
        buttonc.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                try {
                    String val = variables.get("C").toString();
                    Toast.makeText(v.getContext(), "C = " + val, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
                return true;
            }
        });

        EditText eqtext = (EditText) findViewById(R.id.equation_input);
        eqtext.setText("");

        eqtext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                System.out.println("test");
                System.out.println(actionId);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    Button buttoneq = (Button) findViewById(R.id.button_equals);
                    buttoneq.callOnClick();
                    buttoneq.setText("l");
                    System.out.println("test");
                    handled = true;
                }
                return handled;
            }
        });


        /*SetVariable("ANS", new Complex(0.0, 0.0));
        SetVariable("A", new Complex(0.0, 0.0));
        SetVariable("B", new Complex(0.0, 0.0));
        SetVariable("C", new Complex(0.0, 0.0));*/

        Spinner spinner = (Spinner) findViewById(R.id.toggle_numeric);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mode, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    public void calc(android.view.View view) {
        EditText eqtext = (EditText) findViewById(R.id.equation_input);
        String expr = eqtext.getText().toString();
        WebView webview = (WebView) findViewById(R.id.result);
        webview.getSettings().setJavaScriptEnabled(true);

        Spinner mode = (Spinner) findViewById(R.id.toggle_numeric);
        boolean numeric = false;
        String m = mode.getSelectedItem().toString();
        if (m.equals("Numerical")) {
            numeric = true;
        }

        String result = stringFromJNI(expr, numeric);
        String summary = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<link rel=\"stylesheet\" href=\"file:///android_asset/katex/katex.min.css\" >\n" +
                "<script src=\"file:///android_asset/katex/katex.min.js\" ></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<span id=\"math\"></span>\n" +
                "<script type=\"text/javascript\">\n" +
                "katex.render(\"" +
                result +
                "\", math);\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        String errormsg = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<link rel=\"stylesheet\" href=\"file:///android_asset/katex/katex.min.css\" >\n" +
                "<script src=\"file:///android_asset/katex/katex.min.js\" ></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<span id=\"math\"><pre>"+result+"</pre></span>\n" +
                "</body>\n" +
                "</html>";
        if (result.startsWith("error")) {
            summary = errormsg;
        }
        webview.loadDataWithBaseURL("file:///android_asset/", summary, "text/html", "utf-8", "");

        SetVariable("ANS", "");


        String ans = GetAnsJNI();
        if (ans.isEmpty()) {
            return;
        }

        Result r = new Result(expr, ans);

        if (!results.isEmpty()) {
            Result old = results.getFirst();
            if (!old.equals(r)) {
                results.addFirst(r);
            }
        } else {
            results.addFirst(r);
        }

        Button button_hist = (Button) findViewById(R.id.button_history);
        button_hist.setEnabled(true);

    }

    public void showHistoryActivity(android.view.View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        ArrayList<String> history = new ArrayList<String>();
        for (Iterator<Result> it = results.iterator(); it.hasNext(); ) {
            Result r = it.next();
            history.add(r.GetExpression());
        }
        intent.putStringArrayListExtra(HISTORY, history);
        startActivityForResult(intent, GET_EXPR_FROM_HISTORY);
    }

    public void add(android.view.View view) {
        TextView test = (TextView) findViewById(R.id.equation_input);
        Button bt = (Button) view;
        int start = Math.max(test.getSelectionStart(), 0);
        int end = Math.max(test.getSelectionEnd(), 0);
        Editable text = (Editable) test.getText();
        text.replace(Math.min(start, end), Math.max(start, end),
                bt.getText(), 0, bt.getText().length());
    }

    public void clearInput(android.view.View view) {
        EditText eqtext = (EditText) findViewById(R.id.equation_input);
        eqtext.setText("");
        WebView webview = (WebView) findViewById(R.id.result);
        webview.loadData("<html><body>&nbsp;</body></html>", "text/html", null);
    }

    public void moveSelectionLeft(android.view.View view) {
        EditText eqtext = (EditText) findViewById(R.id.equation_input);
        int c1 = eqtext.getSelectionStart();
        int c2 = eqtext.getSelectionEnd();
        if (c1 == c2 && c1 >= 1) {
            eqtext.setSelection(c1 - 1);

        }
    }

    public void moveSelectionRight(android.view.View view) {
        EditText eqtext = (EditText) findViewById(R.id.equation_input);
        int c1 = eqtext.getSelectionStart();
        int c2 = eqtext.getSelectionEnd();
        if (c1 == c2 && c1 < eqtext.length()) {
            eqtext.setSelection(c1 + 1);

        }
    }

    public void bSpace(android.view.View view) {
        EditText eqtext = (EditText) findViewById(R.id.equation_input);
        int start = Math.max(eqtext.getSelectionStart(), 0);
        int end = Math.max(eqtext.getSelectionEnd(), 0);
        Editable text = (Editable) eqtext.getText();
        if (text.length() == 0) {
            return;
        }

        SpannableStringBuilder selectedStr = new SpannableStringBuilder(text);
        selectedStr.replace(end - 1, end, "");
        eqtext.setText(selectedStr);
        eqtext.setSelection(end - 1);
    }

    public void SetVariable(String name, String expr) {
        SetVariableJNI(name);
        variables.put(name, expr);
    }

    public void showStoreMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        popup.getMenu().add(0, 0, 0, "A");
        popup.getMenu().add(0, 1, 1, "B");
        popup.getMenu().add(0, 2, 2, "C");

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    public void showSinMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.trig, popup.getMenu());

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String t = item.getTitle().toString();

                TextView test = (TextView) findViewById(R.id.equation_input);

                int start = Math.max(test.getSelectionStart(), 0);
                int end = Math.max(test.getSelectionEnd(), 0);
                Editable text = (Editable) test.getText();
                text.replace(Math.min(start, end), Math.max(start, end),
                        t, 0, t.length());
                return true;
            }
        });
        popup.show();
    }

    public void showASinMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.atrig, popup.getMenu());

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String t = item.getTitle().toString();

                TextView test = (TextView) findViewById(R.id.equation_input);

                int start = Math.max(test.getSelectionStart(), 0);
                int end = Math.max(test.getSelectionEnd(), 0);
                Editable text = (Editable) test.getText();
                text.replace(Math.min(start, end), Math.max(start, end),
                        t, 0, t.length());
                return true;
            }
        });
        popup.show();
    }

    public void showSinhMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.trighyp, popup.getMenu());

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String t = item.getTitle().toString();

                TextView test = (TextView) findViewById(R.id.equation_input);

                int start = Math.max(test.getSelectionStart(), 0);
                int end = Math.max(test.getSelectionEnd(), 0);
                Editable text = (Editable) test.getText();
                text.replace(Math.min(start, end), Math.max(start, end),
                        t, 0, t.length());
                return true;
            }
        });
        popup.show();
    }

    public void showArSinhMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.artrighyp, popup.getMenu());

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String t = item.getTitle().toString();

                TextView test = (TextView) findViewById(R.id.equation_input);

                int start = Math.max(test.getSelectionStart(), 0);
                int end = Math.max(test.getSelectionEnd(), 0);
                Editable text = (Editable) test.getText();
                text.replace(Math.min(start, end), Math.max(start, end),
                        t, 0, t.length());
                return true;
            }
        });
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Complex value = new Complex(0.0, 0.0);
        /*try {
            value = Complex.parseComplex(result);
        } catch (NumberFormatException e) {
            View button = findViewById(R.id.button_store);
            Toast.makeText(button.getContext(), "error", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        String ans = GetAnsJNI();
        if (ans.isEmpty()) {
            return false;
        }
        switch (item.getItemId()) {
            case 0:
                SetVariable("A", ans);
                return true;
            case 1:
                SetVariable("B", ans);
                return true;
            case 2:
                SetVariable("C", ans);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_EXPR_FROM_HISTORY && resultCode == Activity.RESULT_OK) {


            if (data.hasExtra(EXPR_FROM_HISTORY)) {
                String expr = data.getStringExtra(EXPR_FROM_HISTORY);
                EditText eqtext = (EditText) findViewById(R.id.equation_input);
                eqtext.setText(expr);
                WebView webview = (WebView) findViewById(R.id.result);
                webview.loadData("<html><body>&nbsp;</body></html>", "text/html", null);
            }
            if (data.hasExtra(CLEAR_HISTORY)) {
                boolean clear = data.getBooleanExtra(CLEAR_HISTORY, false);
                if (clear) {
                    results.clear();
                    Button button_hist = (Button) findViewById(R.id.button_history);
                    button_hist.setEnabled(false);

                }
            }
        }
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String expr, boolean numeric);

    public native void SetVariableJNI(String name);

    public native String GetAnsJNI();
}
