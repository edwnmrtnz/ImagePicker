/*
* ImagePickerDialog
*
* The camera module implemented for this project is from
* https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en
*
* Author: Edwin Martinez Jr
* Email: dev.edwinmartinez@gmail.com
* Date: December 05, 2017
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
* */

package com.github.em4rtinez.imagepicker.ImagePicker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.em4rtinez.imagepicker.BuildConfig;
import com.github.em4rtinez.imagepicker.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImagePickerDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "ImagePickerDialog";

    private String cameraPhotoFilePath;
    private ImageDataReceiver imageDataReceiver;

    private String dialogTitle;
    private String dialogTitleColor;

    private int cameraResource;
    private int cameraIconWidth;
    private int cameraIconHeight;

    private int galleryResource;
    private int galleryIconWidth;
    private int galleryIconHeight;

    private boolean pickFromGallery;
    private boolean pickFromCamera;
    private boolean pickFromAll;

    private String cameraText;
    private String galleryText;
    private String cameraTextColor;
    private String galleryTextColor;
    private String backgroundColor;


    private TextView tvTitle;
    private LinearLayout parentLayout;

    private LinearLayout btnGallery;
    private LinearLayout btnCamera;

    private ImageView ivCameraIcon;
    private ImageView ivGalleryIcon;

    private TextView tvCamera;
    private TextView tvGallery;


    public void setDialogTitle(String title){
        this.dialogTitle = title;
    }

    public void setDialogTitleColor(String color){
        this.dialogTitleColor = color;
    }

    public void setPickFromAll(){
        this.pickFromAll = true;
    }

    public void setPickFrom(boolean pickFromGallery, boolean pickFromCamera){
        this.pickFromGallery    = pickFromGallery;
        this.pickFromCamera     = pickFromCamera;
    }

    public void setCameraIcon(int resource){
        cameraResource = resource;
    }

    public void setCameraIconSize(int width, int height){
        cameraIconHeight = height;
        cameraIconWidth = width;
    }

    public void setGalleryIcon(int resource){
        galleryResource = resource;
    }

    public void setGalleryIconSize(int width, int height){
        galleryIconHeight = height;
        galleryIconWidth = width;
    }

    public void setCameraText(String cameraText) {
        this.cameraText = cameraText;
    }

    public void setGalleryText(String galleryText) {
        this.galleryText = galleryText;
    }

    public void setCameraTextColor(String cameraTextColor) {
        this.cameraTextColor = cameraTextColor;
    }

    public void setGalleryTextColor(String galleryTextColor) {
        this.galleryTextColor = galleryTextColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_image_picker, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(view);

        setRetainInstance(true);

        //Bind Views
        parentLayout    = view.findViewById(R.id.parentLayout);
        tvTitle         = view.findViewById(R.id.tvTitle);

        btnGallery      = view.findViewById(R.id.btnGallery);
        btnCamera       = view.findViewById(R.id.btnCamera);

        tvGallery       = view.findViewById(R.id.tvGallery);
        tvCamera        = view.findViewById(R.id.tvCamera);


        ivCameraIcon    = view.findViewById(R.id.ivCameraIcon);
        ivGalleryIcon   = view.findViewById(R.id.ivGalleryIcon);

        btnGallery.setOnClickListener(this);
        btnCamera.setOnClickListener(this);


        hideAllPicker();

        initUI();


        return dialog.create();
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    private void initUI() {
        tvTitle.setText((dialogTitle != null && !dialogTitle.isEmpty()) ? dialogTitle : "Pick from");

        showPickFrom();

        if(cameraResource != 0){
            ivCameraIcon.setImageResource(cameraResource);
        }

        if(cameraIconWidth != 0 && cameraIconHeight != 0){
            int widthdp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cameraIconWidth, getActivity().getResources().getDisplayMetrics());
            int heightdp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cameraIconHeight, getActivity().getResources().getDisplayMetrics());
            ivCameraIcon.getLayoutParams().height = heightdp;
            ivCameraIcon.getLayoutParams().width = widthdp;
            ivCameraIcon.requestLayout();
        }

        if(galleryResource != 0){
            ivGalleryIcon.setImageResource(galleryResource);
        }

        if(galleryIconWidth != 0 && galleryIconHeight != 0){
            int widthdp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, galleryIconWidth, getActivity().getResources().getDisplayMetrics());
            int heightdp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, galleryIconHeight, getActivity().getResources().getDisplayMetrics());
            ivGalleryIcon.getLayoutParams().height = heightdp;
            ivGalleryIcon.getLayoutParams().width = widthdp;
            ivGalleryIcon.requestLayout();
        }

        if(galleryText != null && !galleryText.isEmpty()) tvGallery.setText(galleryText);

        if(cameraText != null && !cameraText.isEmpty()) tvCamera.setText(cameraText);

        if(cameraTextColor != null && !cameraTextColor.isEmpty()) tvCamera.setTextColor(Color.parseColor(cameraTextColor));

        if(galleryTextColor != null && !galleryTextColor.isEmpty()) tvGallery.setTextColor(Color.parseColor(galleryTextColor));

        if(backgroundColor != null  && !backgroundColor.isEmpty()) parentLayout.setBackgroundColor(Color.parseColor(backgroundColor));

        if(dialogTitleColor != null && !dialogTitleColor.isEmpty()) tvTitle.setTextColor(Color.parseColor(dialogTitleColor));

    }

    private void showPickFrom() {
        if(pickFromAll){
            showAllPicker();
        }else{
            if(pickFromCamera)  btnCamera.setVisibility(View.VISIBLE);

            if(pickFromGallery) btnGallery.setVisibility(View.VISIBLE);
        }
    }

    private void showAllPicker() {
        btnGallery.setVisibility(View.VISIBLE);
        btnCamera.setVisibility(View.VISIBLE);
    }

    private void hideAllPicker() {
        btnGallery.setVisibility(View.GONE);
        btnCamera.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGallery:{
                launchGallery();
                break;
            }
            case R.id.btnCamera:{
                try {
                    launchCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void launchCamera() throws IOException {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ImagePickerConstant.REQUEST_CAMERA);
            getDialog().hide();
        }else{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    return;
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            createImageFile());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, ImagePickerConstant.REQUEST_CAMERA);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        cameraPhotoFilePath = "file:" + image.getAbsolutePath();
        return image;

    }

    private void launchGallery() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ImagePickerConstant.REQUEST_GALLERY);
        } else {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, ImagePickerConstant.REQUEST_GALLERY);
        }
        
        getDialog().hide();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case ImagePickerConstant.REQUEST_GALLERY:{
                if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchGallery();
                }else{
                    Toast.makeText(getContext(), "Please allow permission!", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
                break;
            }
            case ImagePickerConstant.REQUEST_CAMERA:{
                if(hasAllPermissionsGranted(grantResults)){
                    try {
                        launchCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(), "Please allow permission!", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
                break;
            }
        }
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ImagePickerConstant.REQUEST_GALLERY:{
                if(resultCode == Activity.RESULT_OK){
                    Uri imageUri = data.getData();
                    File file = new File(getRealPathFromURI(getContext(), imageUri));

                    imageDataReceiver.onImageReceived(file);
                }
                getDialog().dismiss();
                break;
            }
            case ImagePickerConstant.REQUEST_CAMERA:{
                if(resultCode == Activity.RESULT_OK){
                    Uri imageUri = Uri.parse(cameraPhotoFilePath);
                    File file = new File(imageUri.getPath());

                    imageDataReceiver.onImageReceived(file);

                    // ScanFile so it will be appeared on Gallery
                    MediaScannerConnection.scanFile(getActivity(),
                            new String[]{imageUri.getPath()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                }
                            });
                }
                getDialog().dismiss();
                break;
            }
        }
    }

    public void setImageDataReceiver(ImageDataReceiver imageDataReceiver) {
        this.imageDataReceiver = imageDataReceiver;
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
