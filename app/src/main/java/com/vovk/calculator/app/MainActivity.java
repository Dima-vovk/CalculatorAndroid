package com.vovk.calculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.EnumMap;


public class MainActivity extends Activity {

    private EditText editText;

    private Button btnAdd;
    private Button btnMinus;
    private Button btnMultiply;
    private Button btnDivide;

    private OperationType operationType;

    private EnumMap <Symbol, Object> comands = new EnumMap<Symbol, Object>(Symbol.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.txtEdit);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnMultiply = (Button) findViewById(R.id.btnMultiply);
        btnDivide = (Button) findViewById(R.id.btnDivide);

        btnAdd.setTag(OperationType.ADD);
        btnMinus.setTag(OperationType.MINUS);
        btnMultiply.setTag(OperationType.MULTIPLY);
        btnDivide.setTag(OperationType.DIVIDE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void SwitchButton(View view) {

        switch (view.getId()) {

            case R.id.btnAdd:
            case R.id.btnMinus:
            case R.id.btnMultiply:
            case R.id.btnDivide:
                operationType = (OperationType) view.getTag();
                if (!comands.containsKey(Symbol.OPERATION)) {
                    if (!comands.containsKey(Symbol.FIRST_DIGIT)) {
                        comands.put(Symbol.FIRST_DIGIT, editText.getText());
                    }
                    comands.put(Symbol.OPERATION, operationType);
                }
                else if (!comands.containsKey(Symbol.SECOND_DIGIT)) {
                    comands.put(Symbol.SECOND_DIGIT, editText.getText());
                    doEquelse();
                    comands.put(Symbol.OPERATION, operationType);
                    comands.remove(Symbol.SECOND_DIGIT);
                }
                break;
            case R.id.btnDel: {
                editText.setText(editText.getText().delete(editText.getText().length() - 1, editText.getText().length()));

                if (editText.getText().toString().trim().length() == 0) {
                    editText.setText("0");
                }
                break;
            }


//            case R.id.btnOne:
//                editText.setText(editText.getText().toString() + view.getContentDescription().toString());
//                break;
//            case R.id.btnTwo:
//                editText.setText(editText.getText().toString() + "2");
//                break;
//            case R.id.btnThree:
//                editText.setText(editText.getText().toString() + "3");
//                break;
//            case R.id.btnFour:
//                editText.setText(editText.getText().toString() + "4");
//                break;
//            case R.id.btnFive:
//                editText.setText(editText.getText().toString() + "5");
//                break;
//            case R.id.btnSix:
//                editText.setText(editText.getText().toString() + "6");
//                break;
//            case R.id.btnSeven:
//                editText.setText(editText.getText().toString() + "7");
//                break;
//            case R.id.btnEight: {
//                editText.setText(editText.getText().toString() + "8");
//                break;
//            }
//            case R.id.btnNine: {
//                editText.setText(editText.getText().toString() + "9");
//                break;
//            }
            case R.id.btnZero: {
                if (editText.getText().toString().equals("0") | editText.getText().toString().isEmpty()) {
                    break;
                } else {
                    editText.setText(editText.getText().toString() + "0");
                    break;
                }
            }
            case R.id.btnClean: {
                editText.setText("0");
                comands.clear();
                break;
            }
            case R.id.btnEquels: {
                if (comands.containsKey(Symbol.FIRST_DIGIT) && comands.containsKey(Symbol.OPERATION)) {
                    comands.put(Symbol.SECOND_DIGIT, editText.getText());

                    doEquelse();

                    comands.put(Symbol.OPERATION, operationType);
                    comands.remove(Symbol.SECOND_DIGIT);
                }
                break;
            }
            case R.id.btnDouble: {
                if (comands.containsKey(Symbol.FIRST_DIGIT)
                        && getDouble(editText.getText().toString()) == getDouble(comands.get(Symbol.FIRST_DIGIT).toString())) {
                    editText.setText("0" + view.getContentDescription().toString());
                }
                if (!editText.getText().toString().contains(",")) {
                    editText.setText(editText.getText() + ",");
                }
                break;

            }

            default:
                if (editText.getText().toString().equals("0") ||
                        (comands.containsKey(Symbol.FIRST_DIGIT) && getDouble(editText.getText()) == getDouble(comands.get(Symbol.FIRST_DIGIT)))) {
                    editText.setText(view.getContentDescription().toString());
                } else
                    editText.setText(editText.getText() + view.getContentDescription().toString());
        }
    }


    private void doEquelse( ) {

        OperationType operationType1 = (OperationType) comands.get(Symbol.OPERATION);

        double result = calc(operationType1,
                                getDouble(comands.get(Symbol.FIRST_DIGIT)),
                                getDouble(comands.get(Symbol.SECOND_DIGIT)));

        if (result %1 == 0) {
            editText.setText(String.valueOf((int)result));
        }
        else {
            editText.setText(String.valueOf(result));
        }
        comands.put(Symbol.FIRST_DIGIT, result);

    }



        private Double calc(OperationType operationType, Double a, Double b){
        switch (operationType) {
            case ADD:
                return Operation.add(a, b);
                case MINUS:
                    return Operation.minus(a, b);
                case MULTIPLY:
                    return Operation.multiply(a, b);
                case DIVIDE:
                    return Operation.divide(a, b);

        }
            return null;

        }

    private double getDouble(Object v){
        double result = 0;
        try {
            result = Double.valueOf(v.toString().replace(',', '.')).doubleValue();
        }
        catch (Exception e){
            e.printStackTrace();
            result = 0;
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
        return result;
    }
}