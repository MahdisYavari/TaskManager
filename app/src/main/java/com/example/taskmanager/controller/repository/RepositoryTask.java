package com.example.taskmanager.controller.repository;

import android.content.ContentValues;
import android.content.Context;

import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.controller.greenDao.TaskOpenHelper;
import com.example.taskmanager.controller.model.DaoMaster;
import com.example.taskmanager.controller.model.DaoSession;
import com.example.taskmanager.controller.model.Task;
import com.example.taskmanager.controller.model.TaskDao;

import java.io.File;
import java.util.List;
import java.util.UUID;


public class RepositoryTask {

    private static RepositoryTask instance;
    private Context mContext;
    private TaskDao mTaskDao;

    private RepositoryTask(Context context) {
        mContext = context.getApplicationContext();
        SQLiteDatabase db = new TaskOpenHelper(mContext).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
       mTaskDao = daoSession.getTaskDao();
    }


    public static RepositoryTask getInstance(Context context) {
        if (instance == null)
            instance = new RepositoryTask(context);
        return instance;
    }

    public Task getTask(UUID id) {
         return mTaskDao.queryBuilder().where(TaskDao.Properties.MUUID.eq(id)).unique();
    }

    public void update(Task task) {
        mTaskDao.update(task);
    }

    public void deleteTask(Task task) {
          mTaskDao.delete(task);
    }

    public void deleteAllTask(Task task) {
      mTaskDao.deleteAll();
    }

    public List<Task> getTasks() {
        return mTaskDao.loadAll();
    }


    public void addTask(Task task) {
        mTaskDao.insert(task);
    }

    public File getPhotoFile(Task task){
        return new File(mContext.getFilesDir(),task.getPhotoName());

    }




}