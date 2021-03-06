package com.calculator.LeonardWojtczak;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import org.mariuszgromada.math.mxparser.Expression;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView result;
    private boolean isResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.result = findViewById(R.id.result);
        initButtonsListeners();

        if(savedInstanceState != null) {
            this.result.setText(savedInstanceState.getString("result"));
            this.isResult = savedInstanceState.getBoolean("isResult");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("result", this.result.getText().toString());
        outState.putBoolean("isResult", this.isResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but0:
                addSingToResult("0");
                break;
            case R.id.but1:
                addSingToResult("1");
                break;
            case R.id.but2:
                addSingToResult("2");
                break;
            case R.id.but3:
                addSingToResult("3");
                break;
            case R.id.but4:
                addSingToResult("4");
                break;
            case R.id.but5:
                addSingToResult("5");
                break;
            case R.id.but6:
                addSingToResult("6");
                break;
            case R.id.but7:
                addSingToResult("7");
                break;
            case R.id.but8:
                addSingToResult("8");
                break;
            case R.id.but9:
                addSingToResult("9");
                break;
            case R.id.add:
                addSpecialSingToResult("+");
                break;
            case R.id.sub:
                addSpecialSingToResult("-");
                break;
            case R.id.div:
                addSpecialSingToResult("/");
                break;
            case R.id.mul:
                addSpecialSingToResult("*");
                break;
            case R.id.comma:
                addSpecialSingToResult(".");
                break;
            case R.id.equals:
                calculateTheEquation();
                break;
            case R.id.change:
                changeSign();
                break;
            case R.id.del:
                this.result.setText(null);
                break;
            case R.id.c:
                this.result.setText(removeLastCharacter(this.result.getText()));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scientific:
                startActivity(new Intent(this, Scientific.class));
                return true;
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;
            case R.id.exit:
                onFinishClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private CharSequence removeLastCharacter(CharSequence text) {
        return text.length() > 0 ? text.subSequence(0, text.length() - 1) : null;
    }

    private void calculateTheEquation() {
        Expression expression = new Expression(this.result.getText().toString());
        double result = expression.calculate();
        if(result % 1 == 0)
            this.result.setText(String.valueOf((int)result));
        else
            this.result.setText(String.valueOf(result));

        if(!this.isResult)
            this.isResult = true;
    }

    private void addSingToResult(String sign) {
        if(this.isResult) {
            this.result.setText(null);
            this.isResult = false;
        }
        this.result.append(sign);
    }

    private void changeSign() {
        StringBuilder equation = new StringBuilder(this.result.getText());
        int lenght = equation.length();
        int minus, plus, mul, div;
        minus = equation.lastIndexOf("-");
        plus =  equation.lastIndexOf("+");
        mul = equation.lastIndexOf("*");
        div = equation.lastIndexOf("/");
        if(minus != -1 || plus != -1 || mul != -1 || div != -1) {
            if ((mul < plus && div < plus) || (mul < minus && div < minus)) {
                if (minus > plus) {
                    if (minus == 0)
                        equation.replace(minus,minus + 1, "");
                    else if (minus != lenght - 1)
                        equation.replace(minus, minus + 1, "+");
                } else {
                    if (plus != lenght - 1)
                        equation.replace(plus, plus + 1, "-");
                }
            }
        }
        else {
            equation.reverse();
            equation.append("-");
            equation.reverse();
        }
        this.result.setText(equation.toString());
    }

    private void addSpecialSingToResult(String sign) {

        if(this.isResult)
            this.isResult = false;

        if(sign.equals("-")) {
            if(isSameSignOnEnd(sign)) {
                if(this.result.getText().length() > 1)
                    addOnEnd("+");
            }
            else if(isSameSignOnEnd("+")) {
                addOnEnd(sign);
            }
            else
                this.result.append(sign);
        }
        else {
            if(isSpecialSignOnEnd()) {
                if(this.result.getText().length() > 1)
                    addOnEnd(sign);
            }
            else
                if(this.result.getText().length() > 0)
                    this.result.append(sign);
        }
    }

    private void addOnEnd(String sign) {
        CharSequence charSequence = this.result.getText().subSequence(0, this.result.getText().length() - 1);
        this.result.setText(charSequence);
        this.result.append(sign);
    }

    private boolean isSameSignOnEnd(String sign) {
        String text = this.result.getText().toString();
        if(text.endsWith(sign))
            return true;
        return false;
    }

    private boolean isSpecialSignOnEnd() {
        String text = this.result.getText().toString();
        if(text.endsWith("+") || text.endsWith("-") || text.endsWith("*") || text.endsWith("/"))
            return true;
        return false;
    }

    protected void onFinishClick() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initButtonsListeners(){
        Button but0 = findViewById(R.id.but0);
        Button but1 = findViewById(R.id.but1);
        Button but2 = findViewById(R.id.but2);
        Button but3 = findViewById(R.id.but3);
        Button but4 = findViewById(R.id.but4);
        Button but5 = findViewById(R.id.but5);
        Button but6 = findViewById(R.id.but6);
        Button but7 = findViewById(R.id.but7);
        Button but8 = findViewById(R.id.but8);
        Button but9 = findViewById(R.id.but9);

        Button add = findViewById(R.id.add);
        Button sub = findViewById(R.id.sub);
        Button equals = findViewById(R.id.equals);
        Button change = findViewById(R.id.change);
        Button div = findViewById(R.id.div);
        Button mul = findViewById(R.id.mul);
        Button comma = findViewById(R.id.comma);
        Button del = findViewById(R.id.del);
        Button c = findViewById(R.id.c);

        but0.setOnClickListener(this);
        but1.setOnClickListener(this);
        but2.setOnClickListener(this);
        but3.setOnClickListener(this);
        but4.setOnClickListener(this);
        but5.setOnClickListener(this);
        but6.setOnClickListener(this);
        but7.setOnClickListener(this);
        but8.setOnClickListener(this);
        but9.setOnClickListener(this);

        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        equals.setOnClickListener(this);
        change.setOnClickListener(this);
        div.setOnClickListener(this);
        mul.setOnClickListener(this);
        comma.setOnClickListener(this);
        del.setOnClickListener(this);
        c.setOnClickListener(this);
    }

}
