package com.dekwin.autorep.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dekwin.autorep.entities.Organization;
import com.dekwin.autorep.entities.Repair;
import com.dekwin.autorep.entities.Responsible;
import com.dekwin.autorep.entities.Spare;
import com.dekwin.autorep.entities.Work;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "repair_db.sqlite";
    private static final String DATABASE_TABLE = "table_name";
    private static final int DATABASE_VERSION = 1;

    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String SPARES_TABLE_NAME="spares";
    public static final String SPARES_COLUMN_ID="_id";
    public static final String SPARES_COLUMN_NAME="name";
    public static final String SPARES_COLUMN_PRICE="price";

    public static final String WORKS_TABLE_NAME="works";
    public static final String WORKS_COLUMN_ID="_id";
    public static final String WORKS_COLUMN_NAME="name";
    public static final String WORKS_COLUMN_PRICE="price";
    public static final String WORKS_COLUMN_REPAIRSID="repairsid";

    public static final String REPAIRS_COLUMN_ID="_id";
    public static final String REPAIRS_COLUMN_NAME="name";
    public static final String REPAIRS_TABLE_NAME="repairs";

    public static final String WORKS_SPARES_TABLE_NAME="works_spares";
    public static final String WORKS_SPARES_WORK_ID="work_id";
    public static final String WORKS_SPARES_SPARE_ID="spare_id";

    public static final String RESPONSIBLE_TABLE_NAME="responsible";
    public static final String RESPONSIBLE_COLUMN_ID="_id";
    public static final String RESPONSIBLE_COLUMN_NAME="name";
    public static final String RESPONSIBLE_COLUMN_SURNAME="surname";

    public static final String ORGANIZATIONS_TABLE_NAME="organizations";
    public static final String ORGANIZATIONS_COLUMN_ID="_id";
    public static final String ORGANIZATIONS_COLUMN_NAME="name";
    public static final String ORGANIZATIONS_COLUMN_ACCOUNT="account";
    public static final String ORGANIZATIONS_COLUMN_PHONE="phone";



    public void initDB(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + "organizations");
        db.execSQL("DROP TABLE IF EXISTS " + "contracts");
        db.execSQL("DROP TABLE IF EXISTS " + "repair_shops");
        db.execSQL("DROP TABLE IF EXISTS " + "responsible");
        db.execSQL("DROP TABLE IF EXISTS " + "contracts_works");
        db.execSQL("DROP TABLE IF EXISTS " + "works");
        db.execSQL("DROP TABLE IF EXISTS " + "works_spares");
        db.execSQL("DROP TABLE IF EXISTS " + "spares");
        db.execSQL("DROP TABLE IF EXISTS " + "repair");
        db.execSQL("DROP TABLE IF EXISTS " + "works_repairs");


        //\
        //  String organizations ="CREATE TABLE organizations( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, account TEXT, phone INTEGER);";

        String organizations ="CREATE TABLE "+ORGANIZATIONS_TABLE_NAME+" (" +
                ORGANIZATIONS_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ORGANIZATIONS_COLUMN_NAME+" TEXT, " +
                ORGANIZATIONS_COLUMN_ACCOUNT+" TEXT, " +
                ORGANIZATIONS_COLUMN_PHONE+" TEXT " +
                ");";

        String contracts= "CREATE TABLE contracts (" +
                "_id integer primary key autoincrement," +
                "organization integer," +
                "repairshop_id integer," +
                "initial_date DATE," +
                "final_date integer," +
                "price double" +
                ");";

        String repair_shops= "CREATE TABLE repair_shops (" +
                "_id integer primary key autoincrement," +
                "responsible_id integer" +
                ");";

        String responsible = "CREATE TABLE "+RESPONSIBLE_TABLE_NAME+" (" +
                RESPONSIBLE_COLUMN_ID+" integer primary key autoincrement," +
                RESPONSIBLE_COLUMN_NAME+" varchar(255)," +
                RESPONSIBLE_COLUMN_SURNAME+" varchar(255)" +
                ")";

        String contracts_works= "CREATE TABLE contracts_works (" +
                "contract_id integer," +
                "work_id integer" +
                ");";

        String works= "CREATE TABLE "+WORKS_TABLE_NAME+" (" +
                WORKS_COLUMN_ID+" integer primary key autoincrement," +
                WORKS_COLUMN_NAME+" varchar(255)," +
                WORKS_COLUMN_PRICE+" real," +
                WORKS_COLUMN_REPAIRSID+" int," +
                "FOREIGN KEY("+WORKS_COLUMN_REPAIRSID+") REFERENCES "+REPAIRS_TABLE_NAME+"("+REPAIRS_COLUMN_ID+")"+

                ");";

        String works_spares= "CREATE TABLE "+WORKS_SPARES_TABLE_NAME+" (" +
                WORKS_SPARES_WORK_ID+" integer," +
                WORKS_SPARES_SPARE_ID+" integer," +
                "FOREIGN KEY("+WORKS_SPARES_WORK_ID+") REFERENCES "+WORKS_TABLE_NAME+"("+WORKS_COLUMN_ID+"),"+

                "FOREIGN KEY("+WORKS_SPARES_SPARE_ID+") REFERENCES "+SPARES_TABLE_NAME+"("+SPARES_COLUMN_ID+")"+
                ");";






        String spares= "CREATE TABLE "+SPARES_TABLE_NAME+" (" +
                SPARES_COLUMN_ID+" integer primary key autoincrement," +
                SPARES_COLUMN_NAME+" varchar(255)," +
                SPARES_COLUMN_PRICE+" real" +
                ");";


        String repair= "CREATE TABLE "+REPAIRS_TABLE_NAME+" (" +
                REPAIRS_COLUMN_ID+" integer primary key autoincrement," +
                REPAIRS_COLUMN_NAME+" varchar(255)" +
                ");";

        String works_repairs = "CREATE TABLE works_repairs (" +
                "repair_id integer," +
                "work_id integer" +
                ");";


        db.execSQL(organizations);
        db.execSQL(contracts);
        db.execSQL(repair_shops);
        db.execSQL(responsible);
        db.execSQL(contracts_works);
        db.execSQL(works);
        db.execSQL(works_spares);
        db.execSQL(spares);
        db.execSQL(repair);
        db.execSQL(works_repairs);

        for (int i=0; i<10;i++) {
            db.execSQL("INSERT INTO spares(name,price) VALUES('Dviglo',200);");
            db.execSQL("INSERT INTO spares(name,price) VALUES('Korobka',100);");
            db.execSQL("INSERT INTO spares(name,price) VALUES('Rama',800);");
        }

        db.execSQL("INSERT INTO works(name,price,repairsid) VALUES('Ремонт двигателя - картер',2000,1);");
        db.execSQL("INSERT INTO works(name,price,repairsid) VALUES('Ремонт коробки - передача',4000,2);");
        db.execSQL("INSERT INTO works(name,price,repairsid) VALUES('Ремонт коробки - рычаг',1300,2);");

        db.execSQL("INSERT INTO repairs(name) VALUES('Ремонт двигателя');");
        db.execSQL("INSERT INTO repairs(name) VALUES('Ремонт коробки');");

        db.execSQL("INSERT INTO "+RESPONSIBLE_TABLE_NAME+"("+RESPONSIBLE_COLUMN_NAME+","+RESPONSIBLE_COLUMN_SURNAME+") VALUES('Вася','Петров');");
        db.execSQL("INSERT INTO "+RESPONSIBLE_TABLE_NAME+"("+RESPONSIBLE_COLUMN_NAME+","+RESPONSIBLE_COLUMN_SURNAME+") VALUES('Иван','Иванов');");

        db.execSQL("INSERT INTO "+ORGANIZATIONS_TABLE_NAME+"("+ORGANIZATIONS_COLUMN_NAME+","
                +ORGANIZATIONS_COLUMN_ACCOUNT+","+ORGANIZATIONS_COLUMN_PHONE
                +") VALUES('Корпорация юмора','Петровская25','88005553535');");

        db.execSQL("INSERT INTO "+ORGANIZATIONS_TABLE_NAME+"("+ORGANIZATIONS_COLUMN_NAME+","
                +ORGANIZATIONS_COLUMN_ACCOUNT+","+ORGANIZATIONS_COLUMN_PHONE
                +") VALUES('Корпорация зла 2','киев борщаговская 334','+380953334598');");


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
initDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public static ArrayList<Work> selectWorks(String where,String sort){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        Cursor c1 = sdb.query(WORKS_TABLE_NAME, new String[]{WORKS_COLUMN_ID, WORKS_COLUMN_NAME,WORKS_COLUMN_PRICE},
                where, null,
                null, null, sort) ;
        ArrayList <Work> worksList=new ArrayList<>();
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                   Work work = new Work(Integer.parseInt(c1.getString(c1.getColumnIndex(WORKS_COLUMN_ID))),
                            c1.getString(c1.getColumnIndex(WORKS_COLUMN_NAME)),Float.parseFloat(c1.getString(c1.getColumnIndex(WORKS_COLUMN_PRICE))));

                   worksList.add(work);
                } while (c1.moveToNext());
            }
            c1.close();
        }

        sdb.close();
        return  worksList;
    }


    public static ArrayList<Repair> selectRepairs(String where,String sort){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        Cursor c1 = sdb.query(REPAIRS_TABLE_NAME, new String[]{REPAIRS_COLUMN_ID, REPAIRS_COLUMN_NAME},
                where, null,
                null, null, sort) ;
        ArrayList <Repair> list=new ArrayList<>();
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Repair repair = new Repair(Integer.parseInt(c1.getString(c1.getColumnIndex(WORKS_COLUMN_ID))),
                            c1.getString(c1.getColumnIndex(WORKS_COLUMN_NAME)));

                   list.add(repair);
                } while (c1.moveToNext());
            }
            c1.close();
        }

        sdb.close();
        return list;
    }

    public static ArrayList<Spare> selectSpares(String sort){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        Cursor c1 = sdb.query(SPARES_TABLE_NAME, new String[]{SPARES_COLUMN_ID, SPARES_COLUMN_NAME,SPARES_COLUMN_PRICE},
                null, null,
                null, null, sort) ;
        ArrayList <Spare> sparesList=new ArrayList<>();
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Spare contactListItems = new Spare(Integer.parseInt(c1.getString(c1.getColumnIndex(SPARES_COLUMN_ID))),
                            c1.getString(c1.getColumnIndex(SPARES_COLUMN_NAME)),Float.parseFloat(c1.getString(c1.getColumnIndex(SPARES_COLUMN_PRICE))));


                    sparesList.add(contactListItems);

                } while (c1.moveToNext());
            }
            c1.close();
        }

        sdb.close();
        return  sparesList;
    }

    public static ArrayList<Spare> selectSpares(String sort,int workId){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        Cursor c1=null;

        if(workId!=0) {
            String srt="";
            if (sort!=null){
                srt="ORDER BY "+sort;
            }
            String query = "SELECT " + SPARES_COLUMN_ID + ", " + SPARES_COLUMN_NAME + ", " + SPARES_COLUMN_PRICE
                    + " FROM " + SPARES_TABLE_NAME + " WHERE " + SPARES_COLUMN_ID
                    + " IN (SELECT " + WORKS_SPARES_SPARE_ID + " FROM " + WORKS_SPARES_TABLE_NAME
                    + " WHERE " + WORKS_SPARES_WORK_ID + " = " + workId + ") "+srt;

            c1 = sdb.rawQuery(query, null);

           Log.e("count ",c1.getCount()+"");
        }else {
            c1 = sdb.query(SPARES_TABLE_NAME, new String[]{SPARES_COLUMN_ID, SPARES_COLUMN_NAME, SPARES_COLUMN_PRICE},
                    null, null,
                    null, null, sort);
        }

        ArrayList <Spare> sparesList=new ArrayList<>();
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Spare contactListItems = new Spare(Integer.parseInt(c1.getString(c1.getColumnIndex(SPARES_COLUMN_ID))),
                            c1.getString(c1.getColumnIndex(SPARES_COLUMN_NAME)),Float.parseFloat(c1.getString(c1.getColumnIndex(SPARES_COLUMN_PRICE))));


                    sparesList.add(contactListItems);

                } while (c1.moveToNext());
            }
            c1.close();
        }

        sdb.close();
        return  sparesList;
    }

public static void updateSpares(String table,ContentValues cv, String field, String[] bind){
    SQLiteDatabase sdb= sInstance.getWritableDatabase();
    sdb.update(SPARES_TABLE_NAME, cv, field,
            bind);

    sdb.close();
}

    public static void deleteSpares(String table,String whereClause, String whereArgs[]){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        sdb.delete(table, whereClause, whereArgs);

        sdb.close();
    }

    public static long addSpares(String  columnHack, ContentValues cv ){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        long lastId= sdb.insert(SPARES_TABLE_NAME, columnHack, cv);

        sdb.close();

        return lastId;
    }

    public static long addWorksSpares(String  columnHack, ContentValues cv ){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
      long lastId= sdb.insert(WORKS_SPARES_TABLE_NAME, columnHack,cv);

        sdb.close();
        return lastId;
    }

    /**
     * responsible part
     * @param sort
     * @return
     */
    public static ArrayList<Responsible> selectResponsible(String sort){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        Cursor c1 = sdb.query(RESPONSIBLE_TABLE_NAME, new String[]{RESPONSIBLE_COLUMN_ID, RESPONSIBLE_COLUMN_NAME, RESPONSIBLE_COLUMN_SURNAME},
                null, null,
                null, null, sort) ;
        ArrayList <Responsible> responsibleList=new ArrayList<>();
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Responsible responsible = new Responsible(Integer.parseInt(c1.getString(c1.getColumnIndex(RESPONSIBLE_COLUMN_ID))),
                            c1.getString(c1.getColumnIndex(RESPONSIBLE_COLUMN_NAME)),c1.getString(c1.getColumnIndex(RESPONSIBLE_COLUMN_SURNAME)));


                    responsibleList.add(responsible);

                } while (c1.moveToNext());
            }
        }
        c1.close();
        sdb.close();
        return  responsibleList;
    }

    public static long addResponsible(String  columnHack, ContentValues cv ){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        long lastId= sdb.insert(RESPONSIBLE_TABLE_NAME, columnHack, cv);

        sdb.close();

        return lastId;
    }


    public static void deleteResponsible(String whereClause, String whereArgs[]){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        sdb.delete(RESPONSIBLE_TABLE_NAME, whereClause, whereArgs);

        sdb.close();
    }
    public static void updateResponsible(ContentValues cv, String field, String[] bind){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        sdb.update(RESPONSIBLE_TABLE_NAME, cv, field,
                bind);

        sdb.close();
    }


    /**
     * Organizations part
     */

    public static ArrayList<Organization> selectOrganizations(String sort){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        Cursor c1 = sdb.query(ORGANIZATIONS_TABLE_NAME, new String[]{ORGANIZATIONS_COLUMN_ID, ORGANIZATIONS_COLUMN_NAME, ORGANIZATIONS_COLUMN_ACCOUNT, ORGANIZATIONS_COLUMN_PHONE},
                null, null,
                null, null, sort) ;
        ArrayList <Organization> organizationsList=new ArrayList<>();
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                   Organization organization = new Organization(Integer.parseInt(c1.getString(c1.getColumnIndex(ORGANIZATIONS_COLUMN_ID))),
                            c1.getString(c1.getColumnIndex(ORGANIZATIONS_COLUMN_NAME)),c1.getString(c1.getColumnIndex( ORGANIZATIONS_COLUMN_ACCOUNT)),c1.getString(c1.getColumnIndex( ORGANIZATIONS_COLUMN_PHONE)));


                    organizationsList.add(organization);

                } while (c1.moveToNext());
            }
        }
        c1.close();
        sdb.close();
        return organizationsList;
    }

    public static long addOrganization(String  columnHack, ContentValues cv ){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        long lastId= sdb.insert(ORGANIZATIONS_TABLE_NAME, columnHack, cv);

        sdb.close();

        return lastId;
    }


    public static void deleteOrganization(String whereClause, String whereArgs[]){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        sdb.delete(ORGANIZATIONS_TABLE_NAME, whereClause, whereArgs);

        sdb.close();
    }
    public static void updateOrganization(ContentValues cv, String field, String[] bind){
        SQLiteDatabase sdb= sInstance.getWritableDatabase();
        sdb.update(ORGANIZATIONS_TABLE_NAME, cv, field,
                bind);

        sdb.close();
    }
}