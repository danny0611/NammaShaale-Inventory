package com.example.nammashaaleinventory.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RepairDao_Impl implements RepairDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Repair> __insertionAdapterOfRepair;

  private final InventoryConverters __inventoryConverters = new InventoryConverters();

  private final EntityDeletionOrUpdateAdapter<Repair> __updateAdapterOfRepair;

  public RepairDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRepair = new EntityInsertionAdapter<Repair>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `repairs` (`repairId`,`assetId`,`repairStatus`,`assignedTo`,`priority`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Repair entity) {
        statement.bindLong(1, entity.getRepairId());
        statement.bindLong(2, entity.getAssetId());
        final String _tmp = __inventoryConverters.fromRepairStatus(entity.getRepairStatus());
        statement.bindString(3, _tmp);
        statement.bindString(4, entity.getAssignedTo());
        final String _tmp_1 = __inventoryConverters.fromRepairPriority(entity.getPriority());
        statement.bindString(5, _tmp_1);
      }
    };
    this.__updateAdapterOfRepair = new EntityDeletionOrUpdateAdapter<Repair>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `repairs` SET `repairId` = ?,`assetId` = ?,`repairStatus` = ?,`assignedTo` = ?,`priority` = ? WHERE `repairId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Repair entity) {
        statement.bindLong(1, entity.getRepairId());
        statement.bindLong(2, entity.getAssetId());
        final String _tmp = __inventoryConverters.fromRepairStatus(entity.getRepairStatus());
        statement.bindString(3, _tmp);
        statement.bindString(4, entity.getAssignedTo());
        final String _tmp_1 = __inventoryConverters.fromRepairPriority(entity.getPriority());
        statement.bindString(5, _tmp_1);
        statement.bindLong(6, entity.getRepairId());
      }
    };
  }

  @Override
  public Object insert(final Repair repair, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRepair.insertAndReturnId(repair);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Repair repair, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRepair.handle(repair);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Repair>> observeRepairs() {
    final String _sql = "SELECT * FROM repairs ORDER BY repairId DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"repairs"}, new Callable<List<Repair>>() {
      @Override
      @NonNull
      public List<Repair> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRepairId = CursorUtil.getColumnIndexOrThrow(_cursor, "repairId");
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfRepairStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "repairStatus");
          final int _cursorIndexOfAssignedTo = CursorUtil.getColumnIndexOrThrow(_cursor, "assignedTo");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final List<Repair> _result = new ArrayList<Repair>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Repair _item;
            final long _tmpRepairId;
            _tmpRepairId = _cursor.getLong(_cursorIndexOfRepairId);
            final long _tmpAssetId;
            _tmpAssetId = _cursor.getLong(_cursorIndexOfAssetId);
            final RepairStatus _tmpRepairStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfRepairStatus);
            _tmpRepairStatus = __inventoryConverters.toRepairStatus(_tmp);
            final String _tmpAssignedTo;
            _tmpAssignedTo = _cursor.getString(_cursorIndexOfAssignedTo);
            final RepairPriority _tmpPriority;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfPriority);
            _tmpPriority = __inventoryConverters.toRepairPriority(_tmp_1);
            _item = new Repair(_tmpRepairId,_tmpAssetId,_tmpRepairStatus,_tmpAssignedTo,_tmpPriority);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
