package com.mywaytec.myway;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mywaytec.myway.model.db.ConnectedBleInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONNECTED_BLE_INFO".
*/
public class ConnectedBleInfoDao extends AbstractDao<ConnectedBleInfo, Long> {

    public static final String TABLENAME = "CONNECTED_BLE_INFO";

    /**
     * Properties of entity ConnectedBleInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property BleName = new Property(1, String.class, "bleName", false, "BLE_NAME");
        public final static Property Address = new Property(2, String.class, "address", false, "ADDRESS");
        public final static Property Time = new Property(3, String.class, "time", false, "TIME");
    }


    public ConnectedBleInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ConnectedBleInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONNECTED_BLE_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"BLE_NAME\" TEXT," + // 1: bleName
                "\"ADDRESS\" TEXT," + // 2: address
                "\"TIME\" TEXT);"); // 3: time
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONNECTED_BLE_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ConnectedBleInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bleName = entity.getBleName();
        if (bleName != null) {
            stmt.bindString(2, bleName);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(3, address);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(4, time);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ConnectedBleInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bleName = entity.getBleName();
        if (bleName != null) {
            stmt.bindString(2, bleName);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(3, address);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(4, time);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ConnectedBleInfo readEntity(Cursor cursor, int offset) {
        ConnectedBleInfo entity = new ConnectedBleInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // bleName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // address
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // time
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ConnectedBleInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBleName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAddress(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ConnectedBleInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ConnectedBleInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ConnectedBleInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
