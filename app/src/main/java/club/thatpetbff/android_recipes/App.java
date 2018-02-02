package club.thatpetbff.android_recipes;

import android.app.Application;

//import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.*;

import club.thatpetbff.android_recipes.DaoMaster.DevOpenHelper;
//import org.greenrobot.greendao.example.DaoMaster.DevOpenHelper;

/**
 * Created by rtom on 1/16/18.
 */

public class App extends Application {
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();



        DevOpenHelper helper = new DevOpenHelper(this, ENCRYPTED ? "fragment_recipes-db-encrypted" : "fragment_recipes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

/*
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "fragment_recipes-db", null);

        //Database dataBase1 = helper.getWritableDb();
        Database database = helper.getEncryptedWritableDb("123");

        daoSession=new DaoMaster(database).newSession();
*/
        //DaoMaster.dropAllTables(db, true);
        //DaoMaster.createAllTables(db, true);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

