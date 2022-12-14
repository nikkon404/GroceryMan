package com.nikkon.groceryman.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.nikkon.groceryman.Activities.GroceryInputFormActivity;
import com.nikkon.groceryman.Activities.LoadDataActivity;
import com.nikkon.groceryman.Models.Item;
import com.nikkon.groceryman.R;
import com.nikkon.groceryman.Utils.AppSnackBar;
import com.nikkon.groceryman.Utils.Utilities;

import java.io.IOException;


public class ScannerFragment extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 200;
    SurfaceView surfaceView;
    View view;

    //
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_scanner, container, false);
        checkCameraPermission();
        surfaceView = view.findViewById(R.id.surfaceView);
                 view.findViewById(R.id.btnManualAdd).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), GroceryInputFormActivity.class);

                 intent.putExtra("item", Item.emptyItem());
                    startActivity(intent);
             }
         });
        initialiseDetectorsAndSources();
//        openDataLoader("041383090219");
        return view;
    }


    //check camera permission
    void checkCameraPermission(){

        //check camera permission
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            //permission not granted
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
        }
        else{
            //permission granted
        }

    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(view.getContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(view.getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        //showing camera preview
        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(view.getContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    String  receivedData = barcodes.valueAt(0).displayValue;
                    if(receivedData.isEmpty()){
                        Toast.makeText(view.getContext(), "No data received", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        openDataLoader(receivedData);

                    }

                }
            }
        });
    }

    void openDataLoader(String data){
        if(!Utilities.hasInternet(view.getContext())){
            AppSnackBar.showSnack(view, "Please check your internet connection and try again");
            return;
        }
        Intent intent = new Intent(getActivity(), LoadDataActivity.class);
        intent.putExtra("data", data);
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (cameraSource != null) {
                        cameraSource.release();

//                        cameraSource.stop();
                    }
                    startActivity(intent);

                }
            });


        }
        catch (Exception e){
            e.printStackTrace();
        }




    }



}