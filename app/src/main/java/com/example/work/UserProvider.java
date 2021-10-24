package com.example.work;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class UserProvider extends ContentProvider {
    public static final String AUTHORITY = "com.ryan.work";
    public final static String TABLE_NAME = "user";
    public final static String USER_ID = "id";
    public final static String USER_USERNAME = "username";
    public final static String USER_PASSWORD = "password";
    public final static String USER_GENDER = "gender";
    public final static String USER_CITY = "city";
    public final static String USER_BIRTHDAY = "birthday";
    public final static String USER_AVATAR = "avatar";

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int INSERT = 10;
    private static final int DELETE = 20;
    private static final int DELETE_ALL = 21;
    private static final int UPDATE = 30;
    private static final int QUERY = 40;
    private static final int QUERY_ALL = 41;
    private static final int QUERY_BY_NAME = 42;

    static {
        matcher.addURI(AUTHORITY,"insert",INSERT);
        matcher.addURI(AUTHORITY,"delete/#",DELETE);
        matcher.addURI(AUTHORITY,"delete",DELETE_ALL);
        matcher.addURI(AUTHORITY,"update/#",UPDATE);
        matcher.addURI(AUTHORITY,"query/#",QUERY);
        matcher.addURI(AUTHORITY,"queryByName",QUERY_BY_NAME);
        matcher.addURI(AUTHORITY,"query",QUERY_ALL);
    }

    private DBHelper mDBHelper;
    private SQLiteDatabase db;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = matcher.match(uri);
        int flag = 0;
        switch (code) {
            case DELETE:
                long id = ContentUris.parseId(uri);
                selection = selection == null ? "id=" + id : selection + "AND id=" + id;
                flag = db.delete(TABLE_NAME,selection,selectionArgs);
                break;
            case DELETE_ALL:
                flag = db.delete(TABLE_NAME,null,null);
                break;
            default:
                throw new RuntimeException("uri不能识别");
        }
        return flag;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code = matcher.match(uri);
        switch (code) {
            case INSERT:
                long id = db.insert(TABLE_NAME, null, values);
                return ContentUris.withAppendedId(uri,id);
            default:
                throw new RuntimeException("uri不能识别");
        }
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext(),"db",null,1);
        db = mDBHelper.getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int code = matcher.match(uri);
        Cursor cursor = null;
        switch (code) {
            case QUERY:
                long id = ContentUris.parseId(uri);
                selection = selection == null ? "id=" + id : selection + "AND id=" + id;
                cursor = db.query(TABLE_NAME,null,selection,selectionArgs,null,null,sortOrder);
                break;
            case QUERY_ALL:
                cursor = db.query(TABLE_NAME,null,null,null,null,null,null);
                break;
            case QUERY_BY_NAME:
                cursor = db.query(TABLE_NAME,null,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new RuntimeException("uri不能识别");
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int code = matcher.match(uri);
        int flag = 0;
        switch (code) {
            case UPDATE:
                long id = ContentUris.parseId(uri);
                selection = selection == null ? "id=" + id : selection + "AND id=" + id;
                flag = db.update(TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new RuntimeException("uri不能识别");
        }
        return flag;
    }
}