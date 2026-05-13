package com.example.nammashaaleinventory.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class InventoryDatabase_Impl extends InventoryDatabase {
  private volatile AssetDao _assetDao;

  private volatile IssueDao _issueDao;

  private volatile RepairDao _repairDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `assets` (`assetId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `assetName` TEXT NOT NULL, `serialNumber` TEXT NOT NULL, `category` TEXT NOT NULL, `purchaseDate` TEXT NOT NULL, `condition` TEXT NOT NULL, `imagePath` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `issues` (`issueId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `assetId` INTEGER NOT NULL, `issueDescription` TEXT NOT NULL, `issueDate` TEXT NOT NULL, `imagePath` TEXT NOT NULL, FOREIGN KEY(`assetId`) REFERENCES `assets`(`assetId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_issues_assetId` ON `issues` (`assetId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `repairs` (`repairId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `assetId` INTEGER NOT NULL, `repairStatus` TEXT NOT NULL, `assignedTo` TEXT NOT NULL, `priority` TEXT NOT NULL, FOREIGN KEY(`assetId`) REFERENCES `assets`(`assetId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_repairs_assetId` ON `repairs` (`assetId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '15f7b1be63be1ba6a273188278354122')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `assets`");
        db.execSQL("DROP TABLE IF EXISTS `issues`");
        db.execSQL("DROP TABLE IF EXISTS `repairs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAssets = new HashMap<String, TableInfo.Column>(7);
        _columnsAssets.put("assetId", new TableInfo.Column("assetId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("assetName", new TableInfo.Column("assetName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("serialNumber", new TableInfo.Column("serialNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("purchaseDate", new TableInfo.Column("purchaseDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("condition", new TableInfo.Column("condition", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("imagePath", new TableInfo.Column("imagePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAssets = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAssets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAssets = new TableInfo("assets", _columnsAssets, _foreignKeysAssets, _indicesAssets);
        final TableInfo _existingAssets = TableInfo.read(db, "assets");
        if (!_infoAssets.equals(_existingAssets)) {
          return new RoomOpenHelper.ValidationResult(false, "assets(com.example.nammashaaleinventory.data.Asset).\n"
                  + " Expected:\n" + _infoAssets + "\n"
                  + " Found:\n" + _existingAssets);
        }
        final HashMap<String, TableInfo.Column> _columnsIssues = new HashMap<String, TableInfo.Column>(5);
        _columnsIssues.put("issueId", new TableInfo.Column("issueId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIssues.put("assetId", new TableInfo.Column("assetId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIssues.put("issueDescription", new TableInfo.Column("issueDescription", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIssues.put("issueDate", new TableInfo.Column("issueDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIssues.put("imagePath", new TableInfo.Column("imagePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysIssues = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysIssues.add(new TableInfo.ForeignKey("assets", "CASCADE", "NO ACTION", Arrays.asList("assetId"), Arrays.asList("assetId")));
        final HashSet<TableInfo.Index> _indicesIssues = new HashSet<TableInfo.Index>(1);
        _indicesIssues.add(new TableInfo.Index("index_issues_assetId", false, Arrays.asList("assetId"), Arrays.asList("ASC")));
        final TableInfo _infoIssues = new TableInfo("issues", _columnsIssues, _foreignKeysIssues, _indicesIssues);
        final TableInfo _existingIssues = TableInfo.read(db, "issues");
        if (!_infoIssues.equals(_existingIssues)) {
          return new RoomOpenHelper.ValidationResult(false, "issues(com.example.nammashaaleinventory.data.Issue).\n"
                  + " Expected:\n" + _infoIssues + "\n"
                  + " Found:\n" + _existingIssues);
        }
        final HashMap<String, TableInfo.Column> _columnsRepairs = new HashMap<String, TableInfo.Column>(5);
        _columnsRepairs.put("repairId", new TableInfo.Column("repairId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepairs.put("assetId", new TableInfo.Column("assetId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepairs.put("repairStatus", new TableInfo.Column("repairStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepairs.put("assignedTo", new TableInfo.Column("assignedTo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepairs.put("priority", new TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRepairs = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRepairs.add(new TableInfo.ForeignKey("assets", "CASCADE", "NO ACTION", Arrays.asList("assetId"), Arrays.asList("assetId")));
        final HashSet<TableInfo.Index> _indicesRepairs = new HashSet<TableInfo.Index>(1);
        _indicesRepairs.add(new TableInfo.Index("index_repairs_assetId", false, Arrays.asList("assetId"), Arrays.asList("ASC")));
        final TableInfo _infoRepairs = new TableInfo("repairs", _columnsRepairs, _foreignKeysRepairs, _indicesRepairs);
        final TableInfo _existingRepairs = TableInfo.read(db, "repairs");
        if (!_infoRepairs.equals(_existingRepairs)) {
          return new RoomOpenHelper.ValidationResult(false, "repairs(com.example.nammashaaleinventory.data.Repair).\n"
                  + " Expected:\n" + _infoRepairs + "\n"
                  + " Found:\n" + _existingRepairs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "15f7b1be63be1ba6a273188278354122", "8c5be01052809ec20ef195e59cc79e57");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "assets","issues","repairs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `assets`");
      _db.execSQL("DELETE FROM `issues`");
      _db.execSQL("DELETE FROM `repairs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AssetDao.class, AssetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(IssueDao.class, IssueDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RepairDao.class, RepairDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AssetDao assetDao() {
    if (_assetDao != null) {
      return _assetDao;
    } else {
      synchronized(this) {
        if(_assetDao == null) {
          _assetDao = new AssetDao_Impl(this);
        }
        return _assetDao;
      }
    }
  }

  @Override
  public IssueDao issueDao() {
    if (_issueDao != null) {
      return _issueDao;
    } else {
      synchronized(this) {
        if(_issueDao == null) {
          _issueDao = new IssueDao_Impl(this);
        }
        return _issueDao;
      }
    }
  }

  @Override
  public RepairDao repairDao() {
    if (_repairDao != null) {
      return _repairDao;
    } else {
      synchronized(this) {
        if(_repairDao == null) {
          _repairDao = new RepairDao_Impl(this);
        }
        return _repairDao;
      }
    }
  }
}
