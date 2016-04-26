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
import android.widget.Toast;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button registerButton;
    User myUser;
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

        myUser = User.getInstance(getApplicationContext());

        //Set EditTexts if user exists
        if (myUser != null) {
            etTitle.setText(myUser.getTitle());
            etForename.setText(myUser.getForename());
            etMiddlenames.setText(myUser.getMiddlenames());
            etSurname.setText(myUser.getSurname());
            etBirthday.setText(myUser.getBirthday());
            etAddress1.setText(myUser.getAddress1());
            etAddress2.setText(myUser.getAddress2());
            etCity.setText(myUser.getCity());
            etCounty.setText(myUser.getCounty());
            etEmail.setText(myUser.getEmail());
            etTel.setText(myUser.getTel());
            etMobile.setText(myUser.getMobile());
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
                myUser.setTitle(etTitle.getText().toString());
                myUser.setForename(etForename.getText().toString());
                myUser.setMiddlenames(etMiddlenames.getText().toString());
                myUser.setSurname(etSurname.getText().toString());
                myUser.setBirthday(etBirthday.getText().toString());
                myUser.setAddress1(etAddress1.getText().toString());
                myUser.setAddress2(etAddress2.getText().toString());
                myUser.setCity(etCity.getText().toString());
                myUser.setCounty(etCounty.getText().toString());
                myUser.setEmail(etEmail.getText().toString());
                myUser.setTel(etTel.getText().toString());
                myUser.setMobile(etMobile.getText().toString());

                myUser.saveInstance();
                setResult(Activity.RESULT_OK);
                finish();

//                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}