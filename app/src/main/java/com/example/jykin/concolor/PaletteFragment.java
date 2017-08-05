package com.example.jykin.concolor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by seppc_laptop.
 * http://android-er.blogspot.com/2015/09/extract-prominent-colors-from-image.html
 */

public class PaletteFragment extends DialogFragment implements View.OnClickListener {
    private Button b_open_image, b_camera;
    private ImageView iv_image;
    private TextView tv_vibrant;
    private View v_vibrant;
    private Button b_cancel, b_ok;

    private int initialColor, primaryColor;

    private static final int REQUEST_OPEN_IMAGE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    Boolean value = true;


    Uri target_uri = null;

    public PaletteFragment(){

    }

    //New instance of the palette fragment, gets the initial color from argb in mainactivity
    public static PaletteFragment newInstance(int color){
        PaletteFragment newPalette = new PaletteFragment();

        Bundle args = new Bundle();
        args.putInt("initialColor", color);
        newPalette.setArguments(args);

        return newPalette;
    }



    //Interface with mainactivity to set the color
    public interface OnDialogCloseListener {
        void closeDialog(int color);
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.palette, container, false);
        initialColor = getArguments().getInt("initialColor");

        b_open_image = (Button) view.findViewById(R.id.b_open_image);
        b_open_image.setOnClickListener(this);

        b_camera = (Button) view.findViewById(R.id.b_camera);
        b_camera.setOnClickListener(this);

        iv_image = (ImageView) view.findViewById(R.id.iv_image);

        tv_vibrant = (TextView) view.findViewById(R.id.tv_vibrant);
        v_vibrant = (View) view.findViewById(R.id.v_vibrant);

        b_cancel = (Button) view.findViewById(R.id.b_palette_cancel);
        b_cancel.setOnClickListener(this);

        b_ok = (Button) view.findViewById(R.id.b_palette_ok);
        b_ok.setOnClickListener(this);

        return view;
    }

    //Gets uri of the image selected
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int request_code, int result_code, Intent data) {
    if(value == true) {

        if (result_code == Activity.RESULT_OK) {

            Uri data_uri = data.getData();

            if (request_code == REQUEST_OPEN_IMAGE) {
                target_uri = data_uri;
                updateImage(data_uri);
            }
        }
    }
    else
        galleryAddPic();
        value = true;


    }

    //Creates a bitmap of the image uri and sets the imageview
    private void updateImage(Uri uri){

        if (uri != null){
            Bitmap bm;
            try {
                bm = BitmapFactory.decodeStream(
                        getActivity().getContentResolver()
                                .openInputStream(uri));
                iv_image.setImageBitmap(bm);

                extractPrimaryColor(bm);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //Gets the primary color of the image
    private void extractPrimaryColor(Bitmap bitmap){
        if(bitmap == null) return; // stops camera crash
        int defaultColor = 0x000000;
        Palette p = Palette.from(bitmap).generate();

        int VibrantColor = p.getVibrantColor(defaultColor);
        tv_vibrant.setText("Primary Color: " + String.format("#%X", VibrantColor));
        v_vibrant.setBackgroundColor(VibrantColor);
        primaryColor = VibrantColor;
    }

    //sets the size of the dialogfragment to match parent
    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        //added to preview and give result right away
        updateImage(contentUri);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);

    }

    //was added to have everything in one place rather than scattered
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_open_image:
                Intent intent = new Intent();

                //uses ACTION_OPEN_DOCUMENT if latest build of android otherwise uses
                //ACTION_GET_CONTENT if older than kitkat
                if (Build.VERSION.SDK_INT >=
                        Build.VERSION_CODES.KITKAT) {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                } else {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                }

                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // set MIME type for image
                intent.setType("image/*");

                startActivityForResult(intent, REQUEST_OPEN_IMAGE);
                break;

            case R.id.b_camera:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        ex.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getContext(),
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        value = false;
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
                break;

            case R.id.b_palette_ok:
                PaletteFragment.OnDialogCloseListener activity = (PaletteFragment.OnDialogCloseListener) getActivity();
                activity.closeDialog(primaryColor);
                PaletteFragment.this.dismiss();
                break;

            case R.id.b_palette_cancel:
                activity = (PaletteFragment.OnDialogCloseListener) getActivity();
                activity.closeDialog(-1);
                PaletteFragment.this.dismiss();
                break;
        }
    }

}