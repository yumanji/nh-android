package com.movetothebit.newholland.android.db;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import android.accounts.Account;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.movetothebit.newholland.android.helpers.DataHelper;
import com.movetothebit.newholland.android.model.AnswerItem;
import com.movetothebit.newholland.android.model.AnswerWinItem;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.model.ModelItem;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.utils.lConstants;

public class DBHelper extends OrmLiteSqliteOpenHelper implements lConstants{

	public static final String TAG = "DBhelper";
	private static final String DATABASE_NAME = "nw_ormlite.db";
	private static final int DATABASE_VERSION = 1;

	private static Dao<InscriptionData, Integer> dataDao;
	private static Dao<AnswerItem, Integer> answersDao;
	private static Dao<AnswerWinItem, Integer> answersWinDao;
	private static Dao<ModelItem, Integer> modelsDao;

	public DBHelper(Context context) {

		super(context, Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, InscriptionData.class);
			TableUtils.createTable(connectionSource, ModelItem.class);
			TableUtils.createTable(connectionSource, AnswerItem.class);
			TableUtils.createTable(connectionSource, AnswerWinItem.class);
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

	public void syncAllData(Context ctx) throws SQLException,ServerException{
		
		if(DataHelper.sendInscriptions(ctx, getInscriptionsFilled())){
			downloadAllData(ctx);
		}
		
	}

	
	

	
	public void downloadAllData(Context ctx) throws SQLException,ServerException{	
		
			syncInscriptions(ctx);
			syncAnswers(ctx);
			syncAnswersWin(ctx);
			syncModels(ctx);
		
	}
	
	public void syncInscriptions(Context ctx) throws SQLException,ServerException{
		
		List<InscriptionData> list = null;
		List<InscriptionData> tempList = null;
		Dao<InscriptionData, Integer> dao = null;
		
		try {
			
			dao = getInscriptionsDao();
			tempList = getInscriptions();
			list = DataHelper.getIncriptionsFromServer(ctx);
			
			dao.deleteBuilder().where();
			dao.deleteBuilder().delete();
			
			insertInscriptions(ctx, list);
			
			
		} catch (SQLException e) {
			
			insertInscriptions(ctx, tempList);
			throw e;
		} catch (ServerException e) {
			insertInscriptions(ctx, tempList);
			throw e;
			
		}
		
	}
	public void syncAnswers(Context ctx) throws SQLException,ServerException{
		
		List<AnswerItem> list = null;
		List<AnswerItem> tempList = null;
		Dao<AnswerItem, Integer> dao = null;
		
		try {
			
			dao = getAnswersDao();
			tempList = getAnswers();
			list = DataHelper.getAnswersFromServer(ctx);
			
			dao.deleteBuilder().where();
			dao.deleteBuilder().delete();
			
			insertAnswers(ctx, list);
			
			
		} catch (SQLException e) {
			insertAnswers(ctx, tempList);
			throw e;
		} catch (ServerException e) {
			insertAnswers(ctx, tempList);
			throw e;
		}
		
	}
	
	public List<InscriptionData> getComunValuesFromInscription(Context ctx, String columnName){
		
		List<InscriptionData> results = null;
		
		try {
			results = getInscriptionsDao().queryBuilder()
		    .distinct().selectColumns(columnName).query();			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	
	public String[] getBrandValues(Context ctx){
		
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, BRAND);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getBrand();
			    
			}
		}else{
			result = new String[1];
			result[1] = "No hay datos";
		}
		return result;
	}
	
	public String[] getPeriodValues(Context ctx){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, MONTH);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getMonth()+ " - 2012" ;
			    
			}
		}else{
			result = new String[1];
			result[1] = "No hay datos";
		}
		return result;
	}
	public String[] getPopulationValues(Context ctx){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, POPULATION);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getPopulation();
			    
			}
		}else{
			result = new String[1];
			result[1] = "No hay datos";
		}
		return result;
	}
	public String[] getModelsCompValues(Context ctx){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, MODEL_EQUAL);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getModeloComparable();
			    
			}
		}else{
			result = new String[1];
			result[1] = "No hay datos";
		}
		return result;
	}
	public String[] getModelsValues(Context ctx){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, COMMERCIAL_MODEL);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getCommercialModel();
			    
			}
		}else{
			result = new String[1];
			result[1] = "No hay datos";
		}
		return result;
	}
	public String[] getSalesmanValues(Context ctx){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, SALESMAN_NAME);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getSalesmanName();
			    
			}
		}else{
			result = new String[1];
			result[1] = "No hay datos";
		}
		return result;
	}
	public String[] getDealerValues(Context ctx){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, DEALER_NAME);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getDealerName();
			    
			}
		}else{
			result = new String[1];
			result[1] = "No hay datos";
		}
		return result;
	}
	public String[] getAreaValues(Context ctx){		
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, AREA);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getArea();
			    
			}
		}else{
			result = new String[1];
			result[1] = "No hay datos";
		}
		return result;
	}
	public void syncModels(Context ctx) throws SQLException,ServerException{
		
		List<ModelItem> list = null;
		List<ModelItem> tempList = null;
		Dao<ModelItem, Integer> dao = null;
		
		try {
			
			dao = getModelsDao();
			tempList = getModels();
			list = DataHelper.getModelsFromServer(ctx);
			
			dao.deleteBuilder().where();
			dao.deleteBuilder().delete();
			
			insertModels(ctx, list);
			
			
			
		} catch (SQLException e) {
			insertModels(ctx, tempList);
			throw e;
			
		} catch (ServerException e) {
			insertModels(ctx, tempList);
			throw e;
			
		}
		
	}
	public void syncAnswersWin(Context ctx) throws SQLException,ServerException{
		
		List<AnswerWinItem> list = null;
		List<AnswerWinItem> tempList = null;
		Dao<AnswerWinItem, Integer> dao = null;
		
		try {
			
			dao = getAnswersWinDao();
			tempList = getAnswersWin();
			list = DataHelper.getAnswersWinFromServer(ctx);
			
			dao.deleteBuilder().where();
			dao.deleteBuilder().delete();
			
			insertAnswersWin(ctx, list);
			
			
			
		} catch (SQLException e) {
			
			insertAnswersWin(ctx, tempList);
			throw e;
			
		} catch (ServerException e) {
			insertAnswersWin(ctx, tempList);
			throw e;
			
		}
		
	}
	
public List<ModelItem> getModels() throws SQLException, ServerException{
		
		List<ModelItem> result  = null;
		
		
		try {
			result = getModelsDao().queryForAll();
			
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}
public List<AnswerWinItem> getAnswersWin() throws SQLException, ServerException{
		
		List<AnswerWinItem> result  = null;
	
		
		try {
			result = getAnswersWinDao().queryForAll();
			
			
			 
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}
public List<AnswerItem> getAnswers() throws SQLException, ServerException{
		
		List<AnswerItem> result  = null;
	
		try {
			result = getAnswersDao().queryForAll();			
			
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}
	public List<InscriptionData> getInscriptions() throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;
		
		
		try {
			result =  getInscriptionsDao().queryForAll();
		
			
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}

	public List<InscriptionData> getInscriptionsEmpty() throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		
		try {
			
		
			 queryBuilder = getInscriptionsDao().queryBuilder();
			 queryBuilder.setWhere(queryBuilder.where().eq(lConstants.FILL_DATA, "0"));
			    
			 result = getInscriptionsDao().query(queryBuilder.prepare());
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}
	public List<InscriptionData> getInscriptionsFilter(String[] areas, String[] dealer)throws SQLException, ServerException{
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		Where<InscriptionData, Integer> where= null;
		
		try {
			
			queryBuilder = getInscriptionsDao().queryBuilder();
			 // get the WHERE object to build our query
			where = queryBuilder.where();
			// the name field must be equal to "foo"
			if(areas.length>0){
				where.in(AREA, areas);
			}
			if(areas.length>0 && dealer.length>0){
				// and
				where.and();
			}
			// the password field must be equal to "_secret"
			if(dealer.length>0){
				where.in(DEALER_NAME, dealer);
			}
			
			result = getInscriptionsDao().query(queryBuilder.prepare());
		
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
	}
	public List<InscriptionData> getInscriptionsFilled() throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		
		try {
			
			 queryBuilder = getInscriptionsDao().queryBuilder();			 
			 queryBuilder.setWhere(queryBuilder.where().eq(lConstants.FILL_DATA, "1"));
			    
			 result = getInscriptionsDao().query(queryBuilder.prepare());
		
			
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}
	public void insertInscriptions(Context context,
			List<InscriptionData> list) throws SQLException {

		try {
			if(list!=null){
				for (InscriptionData item : list) {
					createOrUpdateInscription(item);
				}
			}
			

		} catch (SQLException e) {
			// dao.rollBack(helper.getConnectionSource());
			Log.e(TAG, "Error creando usuario");
			throw e;

		}
	}
	
	public void createOrUpdateInscription(InscriptionData item) throws SQLException{
		getInscriptionsDao().createOrUpdate(item);
	}
	
	public  void insertAnswers(Context context, List<AnswerItem> list)
			throws SQLException {

		try {
			if(list!=null){
			for (AnswerItem item : list) {
				getAnswersDao().createOrUpdate(item);
			}
			}

		} catch (SQLException e) {
			Log.e(TAG, e.toString());
			throw e;

		}
	}

	public  void insertAnswersWin(Context context,
			List<AnswerWinItem> list) throws SQLException {

		try {
			if(list!=null){
				for (AnswerWinItem item : list) {
					getAnswersWinDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			throw e;

		}

	}

	public  void insertModels(Context context, List<ModelItem> list)
			throws SQLException {

		try {
			if(list!=null){
				for (ModelItem item : list) {
					getModelsDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			throw e;

		}
	}

	public  void updateInscriptions(Context context,
			List<InscriptionData> list) throws SQLException {

		try {
			if(list!=null){
				for (InscriptionData item : list) {
					getInscriptionsDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			throw e;

		}
	}

	public  void updateAnswers(Context context, List<AnswerItem> list)
			throws SQLException {

		try {
			if(list!=null){
				for (AnswerItem item : list) {
					getAnswersDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			throw e;

		}
	}

	public  void updateAnswersWin(Context context,
			List<AnswerWinItem> list) throws SQLException {

		try {
			if(list!=null){
				for (AnswerWinItem item : list) {
					getAnswersWinDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			throw e;

		}
	}

	public  void updateModels(Context context, List<ModelItem> list)
			throws SQLException {

		try {
			if(list!=null){
				for (ModelItem item : list) {
					getModelsDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			Log.e(TAG, e.toString());
			throw e;

		}
	}

	@Override
	public void close() {
		super.close();
		dataDao = null;
		answersDao = null;
		modelsDao = null;
	}

}