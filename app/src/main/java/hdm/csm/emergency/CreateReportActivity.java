package hdm.csm.emergency;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateReportActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1337;

    ImageView myImageView;
    Button takePictureButton;
    Button createReportButton;
    Button cancelButton;
    LinearLayout imageContainer;

    Uri mImageURI;

    Report report;

    ArrayList<String> roadReportImageURIs = new ArrayList<String>();
    ArrayList<String> vehicleReportImageURIs = new ArrayList<String>();
    ArrayList<String> witnessReportImageURIs = new ArrayList<String>();

    EditText etRoadReportComment;
    EditText etVehicleReportComment;
    EditText etWitnessReportComment;

    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager = DataManager.getInstance(getApplicationContext());

        setContentView(R.layout.activity_create_report);

        takePictureButton = (Button) findViewById(R.id.button_takePicture);
        takePictureButton.setOnClickListener(this);

        createReportButton = (Button) findViewById(R.id.button_createReport);
        createReportButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

        imageContainer = (LinearLayout) findViewById(R.id.imageContainer);

        etRoadReportComment = (EditText) findViewById(R.id.et_roadReportComment);
        etVehicleReportComment = (EditText) findViewById(R.id.et_vehicleReportComment);
        etWitnessReportComment = (EditText) findViewById(R.id.et_witnessReportComment);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_takePicture:

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.i("exceptioncreatefile", ex.toString());
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mImageURI = Uri.fromFile(photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
                break;
            case R.id.button_cancel:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.button_createReport:
                Toast.makeText(this, "Report created", Toast.LENGTH_SHORT).show();
                createReport();
                setResult(Activity.RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {

                ImageView tempImageView = new ImageView(this);

                Bitmap imageBitmap = null;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;

                imageBitmap = BitmapFactory.decodeFile(mImageURI.getPath(), options);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(15, 0, 15, 0);
                tempImageView.setLayoutParams(lp);
                tempImageView.setImageBitmap(imageBitmap);

                imageContainer.addView(tempImageView);
                roadReportImageURIs.add(mImageURI.toString());
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    private void createReport() {
        report = new Report();
        report.setRoadReportImageURIs(this.roadReportImageURIs);
        report.setRoadReportComment(etRoadReportComment.getText().toString());
        report.setVehicleReportComment(etVehicleReportComment.getText().toString());

        //report.setWitnessReportImageURIs(this.witnessReportImageURIs);
        //report.setWitnessReportComment(etWitnessReportComment.getText().toString());
        report.setUser(User.getInstance(getApplicationContext()));

        //Createfile and save
        String filename = "report_" + System.currentTimeMillis();
        dataManager.saveReport(filename, report);
    }
}
