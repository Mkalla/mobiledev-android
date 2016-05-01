package hdm.csm.emergency;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button registerButton;
    User user;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener datePickerDialog;

    //Inputfields
    EditText etTitle;
    EditText etForename;
    EditText etMiddlenames;
    EditText etSurname;
    EditText etBirthday;
    EditText etAddress1;
    EditText etAddress2;
    EditText etCity;
    EditText etCounty;
    EditText etEmail;
    EditText etTel;
    EditText etMobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        etTitle = (EditText) findViewById(R.id.title);
        etForename = (EditText) findViewById(R.id.forename);
        etMiddlenames = (EditText) findViewById(R.id.middlenames);
        etSurname = (EditText) findViewById(R.id.surname);
        etBirthday = (EditText) findViewById(R.id.birthday);
        etAddress1 = (EditText) findViewById(R.id.address1);
        etAddress2 = (EditText) findViewById(R.id.address2);
        etCity = (EditText) findViewById(R.id.city);
        etCounty = (EditText) findViewById(R.id.county);
        etEmail = (EditText) findViewById(R.id.email);
        etTel = (EditText) findViewById(R.id.tel);
        etMobile = (EditText) findViewById(R.id.mobile);

        user = User.getInstance(getApplicationContext());

        //Set EditTexts if user exists
        if (user != null) {
            etTitle.setText(user.getTitle());
            etForename.setText(user.getForename());
            etMiddlenames.setText(user.getMiddlenames());
            etSurname.setText(user.getSurname());
            etBirthday.setText(user.getBirthday());
            etAddress1.setText(user.getAddress1());
            etAddress2.setText(user.getAddress2());
            etCity.setText(user.getCity());
            etCounty.setText(user.getCounty());
            etEmail.setText(user.getEmail());
            etTel.setText(user.getTel());
            etMobile.setText(user.getMobile());
        }

        datePickerDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etBirthday.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }
        };

        etBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.birthday:
                        Log.i("Clicked", "clicked");
                        DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, datePickerDialog, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH));

                        dialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis()); //Birthday not earlier than current date TODO: Doesn't work on Android 5.0.x
                        dialog.show();

                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton:
                user.setTitle(etTitle.getText().toString());
                user.setForename(etForename.getText().toString());
                user.setMiddlenames(etMiddlenames.getText().toString());
                user.setSurname(etSurname.getText().toString());
                user.setBirthday(etBirthday.getText().toString());
                user.setAddress1(etAddress1.getText().toString());
                user.setAddress2(etAddress2.getText().toString());
                user.setCity(etCity.getText().toString());
                user.setCounty(etCounty.getText().toString());
                user.setEmail(etEmail.getText().toString());
                user.setTel(etTel.getText().toString());
                user.setMobile(etMobile.getText().toString());

                user.saveInstance();
                setResult(Activity.RESULT_OK);
                finish();

//                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}