package com.zwstudio.lolly.data;
import android.content.Context;

public class Repo {
	
	DatabaseHelper db;

	public RepoDictAll repoDictAll;
	public RepoDictionary repoDictionary;
	public RepoLanguage repoLanguage;

	public Repo(Context context)
	{
		DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
		db = manager.getHelper(context, DatabaseHelper.class);

		repoDictAll = new RepoDictAll(db);
		repoDictionary = new RepoDictionary(db);
		repoLanguage = new RepoLanguage(db);

	}	
	
}
