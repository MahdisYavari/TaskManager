package com.example.taskmanager.controller.greenDao;

import android.content.Context;

import com.example.taskmanager.controller.model.DaoMaster;

public class TaskOpenHelper extends DaoMaster.OpenHelper {
    public static final String NAME = "Task";
    public TaskOpenHelper(Context context) {
        super(context, NAME);
    }
}
