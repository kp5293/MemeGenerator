package com.alejandromoran.memegeneratorpro.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.entities.Memes;
import com.alejandromoran.memegeneratorpro.utils.Classic;
import com.alejandromoran.memegeneratorpro.utils.Meme;
import com.alejandromoran.memegeneratorpro.utils.Quote;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class MemeGeneratorFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private Meme meme;
    private ImagePickerCallback imagePickerCallback;
    private String outputPath;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraImagePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_meme_generator, container, false);
        ButterKnife.bind(this, rootView);
        meme = new Classic(getContext().getAssets());
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Meme newMeme = null;
                if (checkedId == R.id.classicMeme) {
                    newMeme = new Classic(getContext().getAssets());
                } else if (checkedId == R.id.quoteMeme) {
                    newMeme = new Quote();
                }
                newMeme.setImage(meme.getImage());
                newMeme.setTopText(meme.getTopText());
                newMeme.setBottomText(meme.getBottomText());
                meme = newMeme;
                previewImage();
            }
        });


        imagePickerCallback = new ImagePickerCallback(){
            @Override
            public void onImagesChosen(List<ChosenImage> images) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(images.get(0).getOriginalPath(), options);
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;
                String imageType = options.outMimeType;
                BitmapFactory.Options options2 = new BitmapFactory.Options();
                options2.inSampleSize = calculateInSampleSize(options,500,500);
                meme.setImage(BitmapFactory.decodeFile(images.get(0).getOriginalPath(), options2));
                previewImage();
            }

            @Override
            public void onError(String message) {
                Log.e("ERROR", "ERROR" + message);
            }
        };

        return rootView;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @OnClick(R.id.saveMeme)
    public void saveMeme() {

        CharSequence choices[] = new CharSequence[]{"No", "Yes"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.publicMeme));
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int choice) {

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                String imageName = Backendless.UserService.CurrentUser().getObjectId() + ts;
                Backendless.Files.Android.upload( meme.getMeme(), Bitmap.CompressFormat.PNG, 100, imageName, "memes" , new AsyncCallback<BackendlessFile>()
                {
                    @Override
                    public void handleResponse( final BackendlessFile backendlessFile )
                    {

                        boolean isPublic = false;
                        if (choice == 0) {
                            isPublic = false;
                        }
                        else {
                            isPublic = true;
                        }
                        Memes memes = new Memes();
                        memes.setTopText(meme.getTopText());
                        memes.setBottomText(meme.getBottomText());
                        memes.setName(meme.getTopText());
                        memes.setImage(backendlessFile.getFileURL());
                        memes.setUserId(Backendless.UserService.CurrentUser().getUserId());
                        memes.setPublic(isPublic);

                        Log.d("DEBUG", "isPublic -> " + isPublic);

                        Backendless.Persistence.save(memes, new AsyncCallback<Memes>() {
                            public void handleResponse( Memes savedMeme )
                            {
                                Toast.makeText(getActivity(), getString(R.string.memeCreatedSuccessfully), Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void handleFault( BackendlessFault fault )
                            {
                                Log.d("DEBUG", "fault!!" + fault.toString());
                            }
                        });
                    }

                    @Override
                    public void handleFault( BackendlessFault backendlessFault )
                    {
                        Log.e("ERROR", "fault!!" + backendlessFault.toString());
                    }
                });

            }
        });
        builder.show();

    }


    @OnFocusChange(R.id.topText)
    public void onTopTextFocusChange(View v, boolean hasFocus) {
        EditText sentence = (EditText) v;
        int viewId = v.getId();
        if (hasFocus && sentence.getText().toString().equals(getString(R.string.topText))) {
            sentence.setText("");
        }
    }

    @OnFocusChange(R.id.bottomText)
    public void onBottomTextFocusChange(View v, boolean hasFocus) {
        EditText sentence = (EditText) v;
        int viewId = v.getId();
        if (hasFocus && sentence.getText().toString().equals(getString(R.string.bottomText))) {
            sentence.setText("");
        }
    }

    @OnTextChanged(R.id.topText)
    public void onTopTextChanged(CharSequence s, int start, int before, int count) {
        if(!s.equals(getString(R.string.topText))){
            meme.setTopText(s.toString());
            previewImage();
        }
    }

    @OnFocusChange(R.id.topText)
    public void onTopTextFocusChanged(View view, boolean hasFocus) {
        if (!hasFocus) {
            EditText editText = (EditText) getActivity().findViewById(R.id.topText);
            String currentText = editText.getText().toString();
            String defaultText =  getString(R.string.topText);
            if (currentText.isEmpty()) {
                editText.setText(defaultText);
            }
        }
    }

    @OnTextChanged(R.id.bottomText)
    public void onBottomTextChanged(CharSequence s, int start, int before, int count) {
        if(!s.equals(getString(R.string.bottomText))){
            meme.setBottomText(s.toString());
            previewImage();
        }
    }

    @OnFocusChange(R.id.bottomText)
    public void onBottomTextFocusChanged(View view, boolean hasFocus) {
        if (!hasFocus) {
            EditText editText = (EditText) getActivity().findViewById(R.id.bottomText);
            String currentText = editText.getText().toString();
            String defaultText =  getString(R.string.bottomText);
            if (currentText.isEmpty()) {
                editText.setText(defaultText);
            }
        }
    }

    @OnClick(R.id.shareMeme)
    public void shareMeme() { meme.share(getActivity(), meme.getMeme()); }

    @OnClick(R.id.memePreview)
    public void onClick(View v) {
        CharSequence colors[] = new CharSequence[]{"Predefined Images", "Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select image from:");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selectedOption) {
                switch (selectedOption) {
                    case 0:
                        break;
                    case 1:
                        selectImageFromCamera();
                        break;
                    case 2:
                        selectImageFromGallery();
                        break;
                    default:

                }
            }
        });
        builder.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("picker_path", outputPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                outputPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void selectImageFromCamera() {
        cameraImagePicker = new CameraImagePicker(this);
        cameraImagePicker.setImagePickerCallback(imagePickerCallback);
        outputPath = cameraImagePicker.pickImage();
    }

    private void selectImageFromGallery() {
        ImagePicker imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(imagePickerCallback);
        imagePicker.pickImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DEBUG", "resultCode: " + resultCode);
        if(resultCode == FragmentActivity.RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(getActivity());
                    imagePicker.setImagePickerCallback(imagePickerCallback);
                }
                imagePicker.submit(data);
            }
            else {
                if(requestCode == Picker.PICK_IMAGE_CAMERA) {
                    if(cameraImagePicker == null) {
                        cameraImagePicker = new CameraImagePicker(getActivity());
                        cameraImagePicker.reinitialize(outputPath);
                        cameraImagePicker.setImagePickerCallback(imagePickerCallback);
                    }
                    cameraImagePicker.submit(data);
                }
            }
        }
    }

    private void previewImage() {
        meme.generateImage();
        ImageView imageView = ((ImageView) getActivity().findViewById(R.id.memePreview));
        if (meme.getMeme() != null) {
            imageView.setImageBitmap(meme.getMeme());
        }
    }

}
