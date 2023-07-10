package com.azhar.reportapps.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.azhar.reportapps.dao.DatabaseDao2;
import com.azhar.reportapps.database.DatabaseClient;
import com.azhar.reportapps.model.Modeldatabase2;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Azhar Rivaldi on 19-11-2021
 * Youtube Channel : https://bit.ly/2PJMowZ
 * Github : https://github.com/AzharRivaldi
 * Twitter : https://twitter.com/azharrvldi_
 * Instagram : https://www.instagram.com/azhardvls_
 * LinkedIn : https://www.linkedin.com/in/azhar-rivaldi
 */

public class InputDataViewModeluser extends AndroidViewModel {



    DatabaseDao2 databaseDao2;

    public InputDataViewModeluser(@NonNull Application application) {
        super(application);

        databaseDao2 = (DatabaseDao2) DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
    }

    public void adduser(final String Username, final  String Password) {
        Completable.fromAction(() -> {
            Modeldatabase2 modelDatabase2 = new Modeldatabase2();
            modelDatabase2.Username = Username;
            modelDatabase2.Password = Password;

            databaseDao2.insertDatauser(modelDatabase2);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


}
