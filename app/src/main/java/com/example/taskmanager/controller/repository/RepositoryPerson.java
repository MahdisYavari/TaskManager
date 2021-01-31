package com.example.taskmanager.controller.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.controller.greenDao.TaskOpenHelper;
import com.example.taskmanager.controller.model.DaoMaster;
import com.example.taskmanager.controller.model.DaoSession;
import com.example.taskmanager.controller.model.Person;
import com.example.taskmanager.controller.model.PersonDao;
import com.example.taskmanager.controller.model.Task;
import com.example.taskmanager.controller.model.TaskDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.UUID;

public class RepositoryPerson {
    private static RepositoryPerson instance;
    private Context mContext;
    private PersonDao mPersonDao;
    private TaskDao mTaskDao;


    public static RepositoryPerson getInstance(Context context){
        if (instance == null)
            instance = new RepositoryPerson(context);
        return instance;
    }

    public RepositoryPerson(Context context) {
        mContext = context.getApplicationContext();
        SQLiteDatabase db = new TaskOpenHelper(mContext).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        mPersonDao = daoSession.getPersonDao();
        mTaskDao = daoSession.getTaskDao();
    }

    public void add(Person person){
        mPersonDao.insert(person);
    }

    public List<Person> getAllUser(){
        return  mPersonDao.loadAll();
    }

    public Person getPerson(String username){
        return mPersonDao.queryBuilder().where(PersonDao.Properties.MUser.eq(username)).unique();
    }

    public Person getPerson(UUID mId){
        return mPersonDao.queryBuilder().where(PersonDao.Properties.MID.eq(mId.toString())).unique();
    }

    public boolean existPerson(String username){
        if(mPersonDao.queryBuilder().where(PersonDao.Properties.MUser.eq(username)).unique() != null)
            return true;
        return false;
    }
    public boolean validateUserAndPass(String username, String password){
        QueryBuilder<Person> qb = mPersonDao.queryBuilder();
        qb.where(qb.and(PersonDao.Properties.MUser.eq(username), PersonDao.Properties.MPass.eq(password)));
        if (qb.unique() != null)
            return true;
        return false;
    }
    public List<Task> getAllTaskPerUser(UUID mId) {
        return mTaskDao.queryBuilder().where(TaskDao.Properties.MUUID.eq(mId.toString())).list();
    }

    public int getTaskCount(UUID id){
        return getPerson(id).getTaskCount();
    }

    //delete all users
    public void deleteAllUser(){
        mPersonDao.deleteAll();
    }

    //Delete
    public void deleteUser (UUID mId) {
        mPersonDao.delete(mPersonDao.queryBuilder().where(PersonDao.Properties.MID.eq(mId.toString())).unique());
    }
}
