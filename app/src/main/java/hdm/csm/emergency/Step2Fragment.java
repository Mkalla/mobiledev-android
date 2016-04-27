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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Step2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Step2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step2Fragment extends Fragment implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1337;

    Button takePictureButton;
    LinearLayout vehicleImageContainer;

    Uri mImageURI;

    EditText etVehicleReportComment;

    private OnFragmentInteractionListener mListener;

    public Step2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Step2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Step2Fragment newInstance() {
        Step2Fragment fragment = new Step2Fragment();
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

        View fragmentView = inflater.inflate(R.layout.fragment_step2, container, false);

        takePictureButton = (Button) fragmentView.findViewById(R.id.button_takePicture);
        takePictureButton.setOnClickListener(this);

        vehicleImageContainer = (LinearLayout) fragmentView.findViewById(R.id.vehicleImageContainer);

        etVehicleReportComment = (EditText) fragmentView.findViewById(R.id.et_vehicleReportComment);

        // Inflate the layout for this fragment
        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

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
                        photoFile = ((RegisterFragementActivity)getActivity()).createImageFile();
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

                vehicleImageContainer.addView(tempImageView);
                ((RegisterFragementActivity)getActivity()).vehicleReportImageURIs.add(mImageURI.toString());
            }
        }
    }
}
