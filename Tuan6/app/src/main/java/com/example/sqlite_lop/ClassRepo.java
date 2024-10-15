package com.example.sqlite_lop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ClassRepo extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Class.db";

    // Bảng lớp học
    public static final String TABLE_CLASSES = "classes";
    public static final String COLUMN_CLASS_ID = "class_id";
    public static final String COLUMN_CLASS_NAME = "class_name";

    // Bảng sinh viên
    public static final String TABLE_STUDENTS = "students";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_STUDENT_NAME = "student_name";
    public static final String COLUMN_STUDENT_DOB = "student_dob";
    public static final String COLUMN_CLASS_FOREIGN_KEY = "class_id";

    // Tạo bảng lớp học
    public static final String SQL_CREATE_TABLE_CLASSES = "CREATE TABLE " + TABLE_CLASSES + " ("
            + COLUMN_CLASS_ID + " TEXT PRIMARY KEY, "
            + COLUMN_CLASS_NAME + " TEXT)";

    // Tạo bảng sinh viên
    public static final String SQL_CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS + " ("
            + COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_STUDENT_NAME + " TEXT, "
            + COLUMN_STUDENT_DOB + " TEXT, "
            + COLUMN_CLASS_FOREIGN_KEY + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_CLASS_FOREIGN_KEY + ") REFERENCES " + TABLE_CLASSES + "(" + COLUMN_CLASS_ID + "))";

    // Xóa bảng lớp học và sinh viên
    public static final String SQL_DELETE_TABLE_CLASSES = "DROP TABLE IF EXISTS " + TABLE_CLASSES;
    public static final String SQL_DELETE_TABLE_STUDENTS = "DROP TABLE IF EXISTS " + TABLE_STUDENTS;

    public ClassRepo(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_CLASSES);  // Tạo bảng lớp học
        db.execSQL(SQL_CREATE_TABLE_STUDENTS); // Tạo bảng sinh viên
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_STUDENTS);
        db.execSQL(SQL_DELETE_TABLE_CLASSES);
        onCreate(db);
    }

    // Thêm lớp học mới
    public void addClass(String classId, String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_ID, classId);
        values.put(COLUMN_CLASS_NAME, className);
        db.insert(TABLE_CLASSES, null, values);
        db.close();
    }

    // Xóa lớp học
    public boolean deleteClass(String classId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowAffected = db.delete(TABLE_CLASSES, COLUMN_CLASS_ID + "=?", new String[]{classId});
        db.close();
        return rowAffected > 0;
    }

    // Lấy danh sách lớp học
    public ArrayList<ClassItem> loadAllClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_CLASS_ID,
                COLUMN_CLASS_NAME
        };
        Cursor cursor = db.query(TABLE_CLASSES, projection, null, null, null, null, null);
        ArrayList<ClassItem> classItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_NAME));
            classItems.add(new ClassItem(id, name));
        }
        cursor.close();
        db.close();
        return classItems;
    }

    // Thêm sinh viên vào lớp
    public void addStudent(String studentName, String studentDob, String classId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, studentName);
        values.put(COLUMN_STUDENT_DOB, studentDob);
        values.put(COLUMN_CLASS_FOREIGN_KEY, classId);
        db.insert(TABLE_STUDENTS, null, values);
        db.close();
    }

    // Xóa sinh viên khỏi cơ sở dữ liệu
    public boolean deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowAffected = db.delete(TABLE_STUDENTS, COLUMN_STUDENT_ID + "=?", new String[]{String.valueOf(studentId)});
        db.close();
        return rowAffected > 0;
    }


    // Lấy danh sách sinh viên của một lớp
    public ArrayList<StudentItem> loadStudentsByClass(String classId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<StudentItem> studentList = new ArrayList<>();
        String[] projection = {
                COLUMN_STUDENT_ID,    // Lấy ID của sinh viên
                COLUMN_STUDENT_NAME,
                COLUMN_STUDENT_DOB
        };
        Cursor cursor = db.query(TABLE_STUDENTS, projection, COLUMN_CLASS_FOREIGN_KEY + "=?", new String[]{classId}, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID)); // Lấy ID sinh viên
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_NAME));
            String dob = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_DOB));
            studentList.add(new StudentItem(id, name, dob));
        }
        cursor.close();
        db.close();
        return studentList;
    }

}
