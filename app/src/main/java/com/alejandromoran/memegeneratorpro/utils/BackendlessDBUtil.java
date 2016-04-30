package com.alejandromoran.memegeneratorpro.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;
import com.alejandromoran.memegeneratorpro.entities.*;
import com.alejandromoran.memegeneratorpro.entities.Meme;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ale on 06/02/2016.
 */
public class BackendlessDBUtil {

    public static List<Meme> getMyMemes() {

        String whereClause = String.format("objectId = %s", Backendless.UserService.CurrentUser().getObjectId());
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        BackendlessCollection<Meme> result = Backendless.Persistence.of(Meme.class).find(dataQuery);

        return result.getCurrentPage();

    }

    private static List<Favourite> getMyFavourites() {

        String whereClause = String.format("userId = %s", Backendless.UserService.CurrentUser().getObjectId());
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        BackendlessCollection<Favourite> result = Backendless.Persistence.of(Favourite.class).find(dataQuery);

        return result.getCurrentPage();

    }

    public static List<Meme> getMyFavouriteMemes() {

        List<Meme> memeList = new ArrayList<Meme>();
        List<Favourite> favouritesList = getMyFavourites();

        for (Favourite favourite : favouritesList) {
            memeList.add(getMemeById(favourite.getMemeId()));
        }

        return memeList;

    }

    public static void markAsFavourite(String memeId, String userId) {

        Favourite favourite = new Favourite();
        favourite.setMemeId(memeId);
        favourite.setUserId(userId);;
        Backendless.Data.of(Favourite.class).save(favourite);

    }

    public static Meme getLastMeme(int skip) {

        //String whereClause = String.format("isPublic = %s", "true");
        //QueryOptions queryOptions = new QueryOptions();
        //queryOptions.addSortByOption("created");
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        //dataQuery.setWhereClause(whereClause);
       // dataQuery.setQueryOptions(queryOptions);
        Backendless.Persistence.of( Meme.class).find( new AsyncCallback<BackendlessCollection<Meme>>(){
            @Override
            public void handleResponse( BackendlessCollection<Meme> foundContacts )
            {
                // all Contact instances have been found
                Log.d("BACKEND", "DATA FOUDN!!! " + String.valueOf(foundContacts.getCurrentPage().size()));
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.d("BACKEND", " " + fault.getMessage() + " " + fault.getCode());
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });

        //return (Meme) result.getCurrentPage().get(0);
        return null;

    }

    public static void deleteMeme(String objectId) {

        Backendless.Persistence.of(Meme.class).remove(getMemeById(objectId));

    }

    public static Meme getMemeById(String objectId) {

        return Backendless.Persistence.of(Meme.class).findById(objectId);

    }

    public static void saveImage(final Context context, String imageName, Bitmap image) {

        Backendless.Files.Android.upload( image, Bitmap.CompressFormat.PNG, 100, imageName, Backendless.UserService.CurrentUser().getObjectId(), new AsyncCallback<BackendlessFile>()
        {
            @Override
            public void handleResponse( final BackendlessFile backendlessFile )
            {
                Toast.makeText(context, "Meme saved successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                Toast.makeText(context, backendlessFault.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
