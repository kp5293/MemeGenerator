package com.alejandromoran.memegeneratorpro.utils;

import android.util.Log;

import com.alejandromoran.memegeneratorpro.entities.*;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.List;

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

        List<Meme> memesList = new ArrayList<Meme>();
        List<Favourite> favouritesList = getMyFavourites();

        for (Favourite favourite : favouritesList) {
            memesList.add(getMemeById(favourite.getMemeId()));
        }

        return memesList;

    }

    public static void markAsFavourite(String memeId, String userId) {

        Favourite favourite = new Favourite();
        favourite.setMemeId(memeId);
        favourite.setUserId(userId);;
        Backendless.Data.of(Favourite.class).save(favourite);

    }

    public static void deleteMeme(String objectId) {

        Backendless.Persistence.of(Meme.class).remove(getMemeById(objectId));

    }

    public static Meme getMemeById(String objectId) {

        return Backendless.Persistence.of(Meme.class).findById(objectId);

    }

}
