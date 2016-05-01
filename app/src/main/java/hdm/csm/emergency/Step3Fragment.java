package hdm.csm.emergency;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;


public class Step3Fragment extends Fragment implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1337;

    Button takePictureButton;
    LinearLayout witnessImageContainer;

    Uri mImageURI;

    private OnFragmentInteractionListener mListener;

    EditText etWitnessReportComment;

    public Step3Fragment() {
        // Required empty public constructor
    }

    public static Step3Fragment newInstance() {
        Step3Fragment fragment = new Step3Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_step3, container, false);


        takePictureButton = (Button) fragmentView.findViewById(R.id.button_takePicture);
        takePictureButton.setOnClickListener(this);

        witnessImageContainer = (LinearLayout) fragmentView.findViewById(R.id.witnessImageContainer);

        etWitnessReportComment = (EditText) fragmentView.findViewById(R.id.et_witnessReportComment);

        // Inflate the layout for this fragment
        return fragmentView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_takePicture:

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = ((CreateReportActivity) getActivity()).createImageFile();
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1337) { //REQUEST_IMAGE_CAPTURE
            if (resultCode == getActivity().RESULT_OK) {

                ImageView tempImageView = new ImageView(getActivity().getApplicationContext());

                Bitmap imageBitmap = null;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;

                imageBitmap = BitmapFactory.decodeFile(mImageURI.getPath(), options);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(15, 0, 15, 0);
                tempImageView.setLayoutParams(lp);
                tempImageView.setImageBitmap(imageBitmap);

                witnessImageContainer.addView(tempImageView);
                ((CreateReportActivity) getActivity()).witnessReportImageURIs.add(mImageURI.toString());
            }
        }
    }
}
