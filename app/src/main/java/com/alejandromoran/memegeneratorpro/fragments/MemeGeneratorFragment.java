package com.alejandromoran.memegeneratorpro.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.utils.BackendlessDBUtil;
import com.alejandromoran.memegeneratorpro.utils.Classic;
import com.alejandromoran.memegeneratorpro.utils.Meme;
import com.alejandromoran.memegeneratorpro.utils.Quote;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.io.ByteArrayOutputStream;

public class MemeGeneratorFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private Meme meme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meme_generator, container, false);

        final EditText topText = ((EditText) rootView.findViewById(R.id.topText));
        final EditText bottomText = ((EditText) rootView.findViewById(R.id.bottomText));

        ImageButton saveMeme = (ImageButton) rootView.findViewById(R.id.saveMeme);
        ImageButton shareMeme = (ImageButton) rootView.findViewById(R.id.shareMeme);
        ImageView memePreview = (ImageView) rootView.findViewById(R.id.memePreview);


        meme = new Classic(getContext().getAssets());
        topText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                meme.setTopText(s.toString());
                previewImage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bottomText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                meme.setBottomText(s.toString());
                previewImage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveMeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeme();
            }
        });

        shareMeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMeme();
            }
        });

        topText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && topText.getText().toString().equals(getString(R.string.topText))) {
                    topText.setText("");
                }
            }
        });

        bottomText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && bottomText.getText().toString().equals(getString(R.string.bottomText))) {
                    bottomText.setText("");
                }
            }
        });

        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.classicMeme) {
                    Meme newMeme = new Classic(getContext().getAssets());
                    newMeme.setImage(meme.getImage());
                    newMeme.setTopText(meme.getTopText());
                    newMeme.setBottomText(meme.getBottomText());
                    meme = newMeme;
                    previewImage();
                } else if (checkedId == R.id.quoteMeme) {
                    Meme newMeme = new Quote();
                    newMeme.setImage(meme.getImage());
                    newMeme.setTopText(meme.getTopText());
                    newMeme.setBottomText(meme.getBottomText());
                    meme = newMeme;
                    previewImage();
                }
            }
        });

        memePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CharSequence colors[] = new CharSequence[]{"Predefined Images", "Gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select image from:");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1) {
                            selectImage();
                        }
                    }
                });
                builder.show();*/
                selectImage();
            }
        });


        return rootView;
    }

    private void saveMeme() {

        CharSequence colors[] = new CharSequence[]{"Yes", "No"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.publicMeme));
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isPublic = false;
                if (which == 0) {
                    isPublic = true;
                } else
                {
                    isPublic = false;
                }

                /*ParseFile file = new ParseFile("meme.png", image);
                file.saveInBackground();
                ParseObject imgupload = ParseObject.create("Memes");
                imgupload.put("userId", ParseUser.getCurrentUser().getObjectId());
                imgupload.put("name", meme.getTopText());
                imgupload.put("quote", meme.getTopText());
                imgupload.put("author", meme.getBottomText());
                imgupload.put("image", file);
                imgupload.put("isPublic", isPublic);
                imgupload.saveInBackground();*/

                BackendlessDBUtil.saveImage(getActivity(), "x", meme.getMeme());

            }
        });
        builder.show();


    }

    private void shareMeme() {

        String pathofBmp = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), meme.getMeme(), "meme", null);
        Uri bmpUri = Uri.parse(pathofBmp);
        final Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/png");
        startActivity(shareIntent);

    }

    private void selectImage() {

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        getIntent.setType("image/*");
        pickIntent.setType("image/*");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            meme.setImage(BitmapFactory.decodeFile(picturePath));
            previewImage();

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
