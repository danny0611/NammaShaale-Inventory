package com.example.nammashaaleinventory.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class IssueDao_Impl implements IssueDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Issue> __insertionAdapterOfIssue;

  public IssueDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfIssue = new EntityInsertionAdapter<Issue>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `issues` (`issueId`,`assetId`,`issueDescription`,`issueDate`,`imagePath`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Issue entity) {
        statement.bindLong(1, entity.getIssueId());
        statement.bindLong(2, entity.getAssetId());
        statement.bindString(3, entity.getIssueDescription());
        statement.bindString(4, entity.getIssueDate());
        statement.bindString(5, entity.getImagePath());
      }
    };
  }

  @Override
  public Object insert(final Issue issue, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfIssue.insertAndReturnId(issue);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Issue>> observeIssues() {
    final String _sql = "SELECT * FROM issues ORDER BY issueDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"issues"}, new Callable<List<Issue>>() {
      @Override
      @NonNull
      public List<Issue> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfIssueId = CursorUtil.getColumnIndexOrThrow(_cursor, "issueId");
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfIssueDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "issueDescription");
          final int _cursorIndexOfIssueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "issueDate");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final List<Issue> _result = new ArrayList<Issue>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Issue _item;
            final long _tmpIssueId;
            _tmpIssueId = _cursor.getLong(_cursorIndexOfIssueId);
            final long _tmpAssetId;
            _tmpAssetId = _cursor.getLong(_cursorIndexOfAssetId);
            final String _tmpIssueDescription;
            _tmpIssueDescription = _cursor.getString(_cursorIndexOfIssueDescription);
            final String _tmpIssueDate;
            _tmpIssueDate = _cursor.getString(_cursorIndexOfIssueDate);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            _item = new Issue(_tmpIssueId,_tmpAssetId,_tmpIssueDescription,_tmpIssueDate,_tmpImagePath);
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
