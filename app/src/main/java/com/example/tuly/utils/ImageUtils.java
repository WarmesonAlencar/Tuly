package com.example.tuly.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class ImageUtils {

    public static final int REQUEST_CAMERA = 100;
    public static final int REQUEST_GALLERY = 200;

    /**
     * Abre a câmera para tirar uma foto
     */
    public static void openCamera(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * Abre a galeria para escolher uma imagem
     */
    public static void openGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_GALLERY);
    }

    /**
     * Trata o resultado da câmera ou galeria e retorna a URI da imagem
     */
    public static Uri handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CAMERA) {
                return data.getData(); // foto tirada
            } else if (requestCode == REQUEST_GALLERY) {
                return data.getData(); // imagem escolhida
            }
        }
        return null;
    }
}
