package com.rodrigograca.canvas2image;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.OutputStream;

/**
 * Canvas2ImagePlugin.java
 * <p>
 * Android implementation of the Canvas2ImagePlugin for iOS. Inspired by
 * Joseph's "Save HTML5 Canvas Image to Gallery" plugin
 * http://jbkflex.wordpress.com/2013/06/19/save-html5-canvas-image-to-gallery-phonegap-android-plugin/
 *
 * @author Vegard Løkken <vegard@headspin.no>
 */
public class Canvas2ImagePlugin extends CordovaPlugin {
    public static final String ACTION = "saveImageDataToLibrary";
    public static final int WRITE_PERM_REQUEST_CODE = 1;
    private final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private CallbackContext callbackContext;
    private String format;
    private Bitmap bmp;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        this.format = args.optString(1);

        if (action.equals(ACTION)) {

            String base64 = args.optString(0);
            if (base64.equals("")) // isEmpty() requires API level 9
                callbackContext.error("Missing base64 string");

            // Create the bitmap from the base64 string
            // Log.d("Canvas2ImagePlugin", base64);
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (bmp == null) {
                callbackContext.error("The image could not be decoded");
            } else {
                this.bmp = bmp;
                this.callbackContext = callbackContext;
                // Save the image
                askPermissionAndSave();
            }

            return true;
        } else {
            return false;
        }
    }

    private void askPermissionAndSave() {

        if (PermissionHelper.hasPermission(this, WRITE_EXTERNAL_STORAGE)) {
            Log.d("SaveImage", "Permissions already granted, or Android version is lower than 6");
            savePhoto();
        } else {
            Log.d("SaveImage", "Requesting permissions for WRITE_EXTERNAL_STORAGE");
            PermissionHelper.requestPermission(this, WRITE_PERM_REQUEST_CODE, WRITE_EXTERNAL_STORAGE);
        }
    }

    private void savePhoto() {

        Uri imageUri = null;
        // Bitmap bmp = this.bmp;
        CallbackContext callbackContext = this.callbackContext;

        try {
            ContentResolver contentResolver = this.cordova.getContext().getContentResolver();

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "c2i_" + System.currentTimeMillis() + (this.format.equals("png") ? ".png" : ".jpg"));
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, this.format.equals("png") ? "image/png" : "image/jpeg");

            imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            // long startTime = System.currentTimeMillis();
            OutputStream out = contentResolver.openOutputStream(imageUri);

            this.bmp.compress(this.format.equals("png") ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            // Log.d("Timestamp: ", ""+System.currentTimeMillis());
            // long difference = System.currentTimeMillis() - startTime;
            // Log.d("Time: " + Long.toString(difference), "whatever");
        } catch (Exception e) {
            Log.e("Canvas2ImagePlugin", "An exception occurred while saving image: " + e.toString());
        }

        if (imageUri == null) {
            callbackContext.error("Error while saving image");
        } else {
            // Update image gallery
            scanPhoto(imageUri);
            callbackContext.success(imageUri.getPath());
        }

    }

    /*
     * Invoke the system's media scanner to add your photo to the Media Provider's
     * database, making it available in the Android Gallery application and to other
     * apps.
     */
    private void scanPhoto(Uri imageUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        cordova.getActivity().sendBroadcast(mediaScanIntent);
    }

    /**
     * Callback from PermissionHelper.requestPermission method
     */
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults)
            throws JSONException {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                Log.d("SaveImage", "Permission not granted by the user");
                callbackContext.error("Permissions denied");
                return;
            }
        }

        switch (requestCode) {
            case WRITE_PERM_REQUEST_CODE:
                Log.d("SaveImage", "User granted the permission for WRITE_EXTERNAL_STORAGE");
                savePhoto();
                break;
        }
    }
}
