package com.example.nammashaaleinventory.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class AssetDao_Impl implements AssetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Asset> __insertionAdapterOfAsset;

  private final InventoryConverters __inventoryConverters = new InventoryConverters();

  private final EntityDeletionOrUpdateAdapter<Asset> __updateAdapterOfAsset;

  public AssetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAsset = new EntityInsertionAdapter<Asset>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `assets` (`assetId`,`assetName`,`serialNumber`,`category`,`purchaseDate`,`condition`,`imagePath`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Asset entity) {
        statement.bindLong(1, entity.getAssetId());
        statement.bindString(2, entity.getAssetName());
        statement.bindString(3, entity.getSerialNumber());
        statement.bindString(4, entity.getCategory());
        statement.bindString(5, entity.getPurchaseDate());
        final String _tmp = __inventoryConverters.fromAssetCondition(entity.getCondition());
        statement.bindString(6, _tmp);
        statement.bindString(7, entity.getImagePath());
      }
    };
    this.__updateAdapterOfAsset = new EntityDeletionOrUpdateAdapter<Asset>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `assets` SET `assetId` = ?,`assetName` = ?,`serialNumber` = ?,`category` = ?,`purchaseDate` = ?,`condition` = ?,`imagePath` = ? WHERE `assetId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Asset entity) {
        statement.bindLong(1, entity.getAssetId());
        statement.bindString(2, entity.getAssetName());
        statement.bindString(3, entity.getSerialNumber());
        statement.bindString(4, entity.getCategory());
        statement.bindString(5, entity.getPurchaseDate());
        final String _tmp = __inventoryConverters.fromAssetCondition(entity.getCondition());
        statement.bindString(6, _tmp);
        statement.bindString(7, entity.getImagePath());
        statement.bindLong(8, entity.getAssetId());
      }
    };
  }

  @Override
  public Object insert(final Asset asset, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAsset.insertAndReturnId(asset);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Asset asset, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAsset.handle(asset);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Asset>> observeAssets() {
    final String _sql = "SELECT * FROM assets ORDER BY assetName";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"assets"}, new Callable<List<Asset>>() {
      @Override
      @NonNull
      public List<Asset> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfAssetName = CursorUtil.getColumnIndexOrThrow(_cursor, "assetName");
          final int _cursorIndexOfSerialNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "serialNumber");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "condition");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final List<Asset> _result = new ArrayList<Asset>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Asset _item;
            final long _tmpAssetId;
            _tmpAssetId = _cursor.getLong(_cursorIndexOfAssetId);
            final String _tmpAssetName;
            _tmpAssetName = _cursor.getString(_cursorIndexOfAssetName);
            final String _tmpSerialNumber;
            _tmpSerialNumber = _cursor.getString(_cursorIndexOfSerialNumber);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getString(_cursorIndexOfPurchaseDate);
            final AssetCondition _tmpCondition;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCondition);
            _tmpCondition = __inventoryConverters.toAssetCondition(_tmp);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            _item = new Asset(_tmpAssetId,_tmpAssetName,_tmpSerialNumber,_tmpCategory,_tmpPurchaseDate,_tmpCondition,_tmpImagePath);
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

  @Override
  public Object getAsset(final long assetId, final Continuation<? super Asset> $completion) {
    final String _sql = "SELECT * FROM assets WHERE assetId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, assetId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Asset>() {
      @Override
      @Nullable
      public Asset call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfAssetName = CursorUtil.getColumnIndexOrThrow(_cursor, "assetName");
          final int _cursorIndexOfSerialNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "serialNumber");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "condition");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final Asset _result;
          if (_cursor.moveToFirst()) {
            final long _tmpAssetId;
            _tmpAssetId = _cursor.getLong(_cursorIndexOfAssetId);
            final String _tmpAssetName;
            _tmpAssetName = _cursor.getString(_cursorIndexOfAssetName);
            final String _tmpSerialNumber;
            _tmpSerialNumber = _cursor.getString(_cursorIndexOfSerialNumber);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getString(_cursorIndexOfPurchaseDate);
            final AssetCondition _tmpCondition;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCondition);
            _tmpCondition = __inventoryConverters.toAssetCondition(_tmp);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            _result = new Asset(_tmpAssetId,_tmpAssetName,_tmpSerialNumber,_tmpCategory,_tmpPurchaseDate,_tmpCondition,_tmpImagePath);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateConditions(final List<Long> assetIds, final AssetCondition condition,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("UPDATE assets SET condition = ");
        _stringBuilder.append("?");
        _stringBuilder.append(" WHERE assetId IN (");
        final int _inputSize = assetIds.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        final String _tmp = __inventoryConverters.fromAssetCondition(condition);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        for (long _item : assetIds) {
          _stmt.bindLong(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
