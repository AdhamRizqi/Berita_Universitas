package com.azhar.reportapps.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class Modeldatabase2 {
    public String Username;
    public String Password;

    @Entity(tableName = "tbl_user")
    public class ModelDatabase implements Serializable {

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "uid")
        public int uid;

        @ColumnInfo(name = "Username")
        public String Username;

        @ColumnInfo(name = "Password")
        public String Password;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return Username;
        }

        public void setUsername(String Username) {
            this.Username = Username;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }


    }
}
