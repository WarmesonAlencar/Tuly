package com.example.tuly.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtils {

    // ============================================================
    //                  REQUEST CODES
    // ============================================================
    public static final int REQUEST_GALLERY = 2001;
    public static final int REQUEST_CAMERA = 3001;

    // URI da última foto tirada pela câmera
    private static Uri cameraImageUri = null;


    // ============================================================
    //                  📌 ABRIR A GALERIA
    // ============================================================
    public static void openGallery(Activity activity) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        activity.startActivityForResult(intent, REQUEST_GALLERY);
    }


    // ============================================================
    //        📌 TRATAR RESULTADO DA GALERIA
    // ============================================================
    public static Uri handleGalleryResult(Activity activity, int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK && data != null) {

            Uri uri = data.getData();

            if (uri != null) {
                int flags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                try {
                    activity.getContentResolver().takePersistableUriPermission(uri, flags);
                } catch (Exception ignored) {}

                return uri;
            }
        }

        return null;
    }



    // ============================================================
    //                  📸 ABRIR A CÂMERA
    // ============================================================
    public static void openCamera(Activity activity) {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(activity.getPackageManager()) == null) {
            Toast.makeText(activity, "Nenhum app de câmera encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        File imageFile = createImageFile(activity);
        if (imageFile == null) {
            Toast.makeText(activity, "Erro ao criar arquivo de imagem", Toast.LENGTH_SHORT).show();
            return;
        }

        cameraImageUri = FileProvider.getUriForFile(
                activity,
                activity.getPackageName() + ".provider",
                imageFile
        );

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cameraImageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }



    // ============================================================
    //        📌 TRATAR RESULTADO DA CÂMERA
    // ============================================================
    public static Uri handleCameraResult(int requestCode, int resultCode) {

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            return cameraImageUri;
        }

        return null;
    }



    // ============================================================
    //       📄 CRIAR UM ARQUIVO INTERNAMENTE PARA A FOTO
    // ============================================================
    private static File createImageFile(Activity activity) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (storageDir == null) return null;

        return new File(storageDir, "IMG_" + timeStamp + ".jpg");
    }
}
