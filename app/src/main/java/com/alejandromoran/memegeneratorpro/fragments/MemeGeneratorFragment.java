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
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import com.alejandromoran.memegeneratorpro.R;
import com.alejandromoran.memegeneratorpro.entities.Memes;
import com.alejandromoran.memegeneratorpro.utils.Classic;
import com.alejandromoran.memegeneratorpro.utils.Meme;
import com.alejandromoran.memegeneratorpro.utils.Quote;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class MemeGeneratorFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private Meme meme;

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

        return rootView;
    }

    @OnClick(R.id.saveMeme)
    public void saveMeme() {

        CharSequence colors[] = new CharSequence[]{"Yes", "No"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.publicMeme));
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int isPublic) {

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                String imageName = Backendless.UserService.CurrentUser().getObjectId() + ts;
                Backendless.Files.Android.upload( meme.getMeme(), Bitmap.CompressFormat.PNG, 100, imageName, "memes" , new AsyncCallback<BackendlessFile>()
                {
                    @Override
                    public void handleResponse( final BackendlessFile backendlessFile )
                    {
                        Memes memes = new Memes();
                        memes.setTopText(meme.getTopText());
                        memes.setBottomText(meme.getBottomText());
                        memes.setName(meme.getTopText());
                        memes.setImage(backendlessFile.getFileURL());
                        memes.setUserId(Backendless.UserService.CurrentUser().getUserId());

                        Backendless.Persistence.save(memes, new AsyncCallback<Memes>() {
                            public void handleResponse( Memes savedMeme )
                            {
                                Log.d("DEBUG", "savedMeme: " + savedMeme.toString());
                                Backendless.Persistence.save( savedMeme, new AsyncCallback<Memes>() {
                                    @Override
                                    public void handleResponse(Memes response)
                                    {
                                        Log.d("DEBUG", "Memes!!" + response.toString());
                                    }
                                    @Override
                                    public void handleFault( BackendlessFault fault )
                                    {
                                        Log.d("DEBUG", "fault!!" + fault.toString());
                                    }
                                } );
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
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        meme.setTopText(s.toString());
        previewImage();
    }

    @OnTextChanged(R.id.bottomText)
    public void onBottomTextChanged(CharSequence s, int start, int before, int count) {
        meme.setBottomText(s.toString());
        previewImage();
    }

    @OnClick(R.id.shareMeme)
    public void shareMeme() { meme.share(getActivity(), meme.getMeme()); }

    @OnClick(R.id.memePreview)
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
        if (requestCode == PICK_IMAGE && resultCode == FragmentActivity.RESULT_OK && null != data) {
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
