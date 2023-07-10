package com.azhar.reportapps.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.azhar.reportapps.model.ModelDatabase;
import com.azhar.reportapps.model.Modeldatabase2;

import java.util.List;

/**
 * Created by Azhar Rivaldi on 19-11-2021
 * Youtube Channel : https://bit.ly/2PJMowZ
 * Github : https://github.com/AzharRivaldi
 * Twitter : https://twitter.com/azharrvldi_
 * Instagram : https://www.instagram.com/azhardvls_
 * LinkedIn : https://www.linkedin.com/in/azhar-rivaldi
 */

@Dao
public interface DatabaseDao2 {

    @Query("SELECT * FROM tbl_user")
    LiveData<List<ModelDatabase>> getAlluser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDatauser(Modeldatabase2 modelDatabases);

    @Query("DELETE FROM tbl_user")
    void deleteAlluser();

    @Query("DELETE FROM tbl_user WHERE uid= :uid")
    void deleteSingleuser(int uid);

}
