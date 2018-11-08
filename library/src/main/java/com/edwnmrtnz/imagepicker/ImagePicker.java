/*
* ImagePicker
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


package com.edwnmrtnz.imagepicker;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import java.io.File;

public class ImagePicker {
    private boolean pickFromGallery;
    private boolean pickFromCamera;
    private boolean pickFromFile;
    private boolean enableAll;

    private int galleryResourceIcon;
    private int galleryIconHeight;
    private int galleryIconWidth;

    private int cameraResourceIcon;
    private int cameraIconHeight;
    private int cameraIconWidth;

    private String backgroundColor;

    private String dialogTitle;
    private String dialogTitleTextColor;

    private String galleryText;
    private String cameraText;

    private String galleryTextColor;
    private String cameraTextColor;

    ImagePickerDialog imagePickerDialog;

    public ImagePicker(Builder builder) {
        this.pickFromGallery        = builder.fromGallery;
        this.pickFromCamera         = builder.fromCamera;
        this.enableAll              = builder.enableAll;
        this.dialogTitle            = builder.dialogTitle;
        this.imagePickerDialog      = builder.imagePickerDialog;
        this.galleryResourceIcon    = builder.galleryResourceIcon;
        this.galleryIconHeight      = builder.galleryIconHeight;
        this.galleryIconWidth       = builder.galleryIconWidth;
        this.cameraResourceIcon     = builder.cameraResourceIcon;
        this.cameraIconHeight       = builder.cameraIconHeight;
        this.cameraIconWidth        = builder.cameraIconWidth;
        this.dialogTitleTextColor   = builder.dialogTitleTextColor;
        this.backgroundColor        = builder.backgroundColor;
        this.galleryText            = builder.galleryText;
        this.cameraText             = builder.cameraText;
        this.galleryTextColor       = builder.galleryTextColor;
        this.cameraTextColor        = builder.cameraTextColor;
    }

    public boolean isPickFromGallery() {
        return pickFromGallery;
    }

    public boolean isPickFromCamera() {
        return pickFromCamera;
    }

    public boolean isPickFromFile() {
        return pickFromFile;
    }

    public boolean isEnableAll() {
        return enableAll;
    }

    public int getGalleryResourceIcon() {
        return galleryResourceIcon;
    }

    public int getGalleryIconHeight() {
        return galleryIconHeight;
    }

    public int getGalleryIconWidth() {
        return galleryIconWidth;
    }

    public int getCameraResourceIcon() {
        return cameraResourceIcon;
    }

    public int getCameraIconHeight() {
        return cameraIconHeight;
    }

    public int getCameraIconWidth() {
        return cameraIconWidth;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public String getDialogTitleTextColor() {
        return dialogTitleTextColor;
    }

    public String getGalleryText() {
        return galleryText;
    }

    public String getCameraText() {
        return cameraText;
    }

    public String getGalleryTextColor() {
        return galleryTextColor;
    }

    public String getCameraTextColor() {
        return cameraTextColor;
    }

    public ImagePickerDialog getImagePickerDialog() {
        return imagePickerDialog;
    }

    public static class Builder{
        private boolean fromGallery;
        private boolean fromCamera;
        private boolean enableAll;

        private String dialogTitle;
        private String dialogTitleTextColor;
        private String backgroundColor;

        private int galleryResourceIcon;
        private int galleryIconHeight;
        private int galleryIconWidth;

        private int cameraResourceIcon;
        private int cameraIconHeight;
        private int cameraIconWidth;

        private String galleryText;
        private String cameraText;
        private String galleryTextColor;
        private String cameraTextColor;

        ImagePickerDialog imagePickerDialog;

        Context context;
        private FragmentManager fragmentManager;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setDialogTitleTextColor(String titleTextColor) {
            this.dialogTitleTextColor = titleTextColor;
            return this;
        }

        public Builder setGalleryText(String galleryText) {
            this.galleryText = galleryText;
            return this;
        }

        public Builder setCameraText(String cameraText) {
            this.cameraText = cameraText;
            return this;
        }

        public Builder setGalleryTextColor(String galleryTextColor) {
            this.galleryTextColor = galleryTextColor;
            return this;
        }

        public Builder setCameraTextColor(String cameraTextColor) {
            this.cameraTextColor = cameraTextColor;
            return this;
        }

        public Builder setGalleryIcon(int resource){
            this.galleryResourceIcon = resource;
            return this;
        }

        public Builder setGalleryIconSize(int width, int height){
            this.galleryIconWidth = width;
            this.galleryIconHeight = height;
            return this;
        }

        public Builder setCameraIcon(int resource){
            this.cameraResourceIcon = resource;
            return this;
        }

        public Builder setCameraIconSize(int width, int height){
            this.cameraIconHeight = height;
            this.cameraIconWidth = width;
            return this;
        }

        public Builder enablePickImageFromGallery() {
            this.fromGallery = true;
            return this;
        }

        public Builder enablePickImageFromCamera() {
            this.fromCamera = true;
            return this;
        }


        public Builder enablePickFromAll() {
            this.enableAll = true;
            return this;
        }

        public Builder setDialogTitle(String dialogTitle) {
            this.dialogTitle = dialogTitle;
            return this;
        }

        private void setupDialog(){
            FragmentActivity activity = (FragmentActivity) context;
            fragmentManager = activity.getSupportFragmentManager();
            imagePickerDialog = new ImagePickerDialog();

            imagePickerDialog.setDialogTitle(dialogTitle);
            imagePickerDialog.setDialogTitleColor(dialogTitleTextColor);
            imagePickerDialog.setBackgroundColor(backgroundColor);

            if(enableAll){
                imagePickerDialog.setPickFromAll();
            }else{
                imagePickerDialog.setPickFrom(fromGallery, fromCamera);
            }

            imagePickerDialog.setCameraText(cameraText);
            imagePickerDialog.setCameraTextColor(cameraTextColor);
            imagePickerDialog.setCameraIcon(cameraResourceIcon);
            imagePickerDialog.setCameraIconSize(cameraIconWidth, cameraIconHeight);

            imagePickerDialog.setGalleryText(galleryText);
            imagePickerDialog.setGalleryTextColor(galleryTextColor);
            imagePickerDialog.setGalleryIcon(galleryResourceIcon);
            imagePickerDialog.setGalleryIconSize(galleryIconWidth, galleryIconHeight);

        }

        public Builder getFile(final ImageDataReceiver imageDataReceiver){
            setupDialog();
            imagePickerDialog.setImageDataReceiver(new ImageDataReceiver() {
                @Override
                public void onImageReceived(File file) {
                    imageDataReceiver.onImageReceived(file);
                }
            });
            return this;
        }

        public ImagePicker show(){
            if(imagePickerDialog == null) {
                throw new NullPointerException("Dialog can't be shown! You must call getFile() before attempting to call show()");
            }else{
                imagePickerDialog.show(fragmentManager, "dialog_file_picker");
                return new ImagePicker(this);
            }
        }
    }
}