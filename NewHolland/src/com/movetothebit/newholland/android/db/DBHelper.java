package com.movetothebit.newholland.android.db;

import java.io.File;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.movetothebit.newholland.android.model.AnswerItem;
import com.movetothebit.newholland.android.model.AnswerWinItem;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.model.ModelItem;
import com.movetothebit.newholland.android.model.ObjetiveItem;
import com.movetothebit.newholland.android.utils.lConstants;

public class DBHelper extends OrmLiteSqliteOpenHelper implements lConstants{

	public static final String TAG = "DBhelper";
	private static final String DATABASE_NAME = "nw_ormlite.db";
	private static final int DATABASE_VERSION = 1;

	private static Dao<InscriptionData, Integer> dataDao;
	private static Dao<AnswerItem, Integer> answersDao;
	private static Dao<AnswerWinItem, Integer> answersWinDao;
	private static Dao<ModelItem, Integer> modelsDao;
	private static Dao<ObjetiveItem, Integer> objetiveDao;
	private ConnectionSource connectionSource = null;

	public DBHelper(Context context) {
		
		super(context, Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + DATABASE_NAME, null,
				DATABASE_VERSION);
		
		
	}
	
	@Override
	public ConnectionSource getConnectionSource() {
	    if (connectionSource == null) {
	        connectionSource = super.getConnectionSource();
	    }
	    return connectionSource;
	}
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, InscriptionData.class);
			TableUtils.createTable(connectionSource, ModelItem.class);
			TableUtils.createTable(connectionSource, AnswerItem.class);
			TableUtils.createTable(connectionSource, AnswerWinItem.class);
			TableUtils.createTable(connectionSource, ObjetiveItem.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		onCreate(db, connectionSource);
	}

	public Dao<InscriptionData, Integer> getInscriptionsDao()
			throws SQLException {
		if (dataDao == null) {
			dataDao = getDao(InscriptionData.class);
		}
		return dataDao;
	}

	public  Dao<ModelItem, Integer> getModelsDao() throws SQLException {
		if (modelsDao == null) {
			modelsDao = getDao(ModelItem.class);
		}
		return modelsDao;
	}

	public  Dao<AnswerItem, Integer> getAnswersDao() throws SQLException {
		if (answersDao == null) {
			answersDao = getDao(AnswerItem.class);
		}
		return answersDao;
	}

	public  Dao<AnswerWinItem, Integer> getAnswersWinDao()
			throws SQLException {
		if (answersWinDao == null) {
			answersWinDao = getDao(AnswerWinItem.class);
		}
		return answersWinDao;
	}
	public  Dao<ObjetiveItem, Integer> getObjetivesDao()
			throws SQLException {
		if (objetiveDao == null) {
			objetiveDao= getDao(ObjetiveItem.class);
		}
		return objetiveDao;
	}
	public void dropAllTables(){
		
		try {
			TableUtils.clearTable(getConnectionSource(), InscriptionData.class);
			TableUtils.clearTable(getConnectionSource(), ModelItem.class);
			TableUtils.clearTable(getConnectionSource(), AnswerItem.class);
			TableUtils.clearTable(getConnectionSource(), AnswerWinItem.class);
			TableUtils.clearTable(getConnectionSource(), ObjetiveItem.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
	

	@Override
	public void close() {
		super.close();
		dataDao = null;
		answersDao = null;
		answersWinDao = null;
		modelsDao = null;
		objetiveDao = null;
	}

}