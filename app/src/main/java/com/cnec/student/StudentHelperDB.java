package com.cnec.student;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StudentHelperDB{

    private final StudentActivity context;
    private final static String NAMEDATABASE = "Student.db";
    private SQLiteDatabase db;
    private static Integer[] ids;

    public StudentHelperDB(StudentActivity context) {
        this.context = context;
    }

    public void open() {
        this.db = context.getBaseContext().openOrCreateDatabase(
                NAMEDATABASE, Context.MODE_WORLD_WRITEABLE, null);

        this.createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE if not exists Student ( "+
                "id integer primary key autoincrement, "+
                "code text not null, "+
                "name text not null,"+
                "email text not null);" ;

        this.db.execSQL(sql);
    }

    public void removeAllStudents() {
        //this.db.delete("Student", null, null);
        String sql = "DROP TABLE if exists Student;";

        this.db.execSQL(sql);

        this.createTable();
    }

    public void insertStudentsSamples() {
        this.insertStudent("B01", "Beltrano", "beltrano@cnec.br");
        this.insertStudent("S02", "Sicrano", "sicrano@cnec.br");
        this.insertStudent("F03", "Fulano", "fulano@cnec.br");
    }

    public void insertStudent(String code, String name, String email) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("code", code);
        initialValues.put("name", name);
        initialValues.put("email", email);

        this.db.insert("Student", null, initialValues);

    }

    public String[] getListStudents() {
        String[] fields = {"name", "id"};
        Cursor ret =  db.query("Student", fields, null, null, null, null, null);

        String[] values = new String[ret.getCount()];
        ids = new Integer[ret.getCount()];

        boolean hasRecord = ret.moveToFirst();
        int position = 0;

        while ( hasRecord  ){
            ids[position] = ret.getInt(ret.getColumnIndex("id"));
            values[position++] = ret.getString(ret.getColumnIndex("name"));
            hasRecord  = ret.moveToNext();
        }

        return values;
    }

    public static Integer getIdStudent(int position) {
        return ids[position];
    }

    public Cursor getStudent(int id) {
        String[] fields = {"id", "code", "name", "email"};
        Cursor ret =  db.query("Student",
                fields, "id = " + id,
                null, null, null, null);

        ret.moveToFirst();

        return ret;
    }


    public void updateStudent(String id, String code,
                              String name, String email) {
        ContentValues values = new ContentValues();
        values.put("code", code);
        values.put("name", name);
        values.put("email", email);

        db.update("Student", values, "id = ?", new String[]{id});
    }

    public void deleteStudent(String id){
        db.delete("Student", "id = " + id, null);
    }

}
