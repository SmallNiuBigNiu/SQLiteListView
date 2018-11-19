package cn.gdcp.sqlitelistview;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IOnListener{

    private EditText edtKey1;
    private EditText edtKey2;
    private EditText edtKey3;
    private ImageView ivSearch;
    private ListView lvStu;
    private Button btnAdd;
    private ArrayList<Student> studentArrayList=new ArrayList<Student>();
    private StuAdater adater;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //查找首页各种组件
        findViews();
        adater = new StuAdater(studentArrayList,MainActivity.this , MainActivity.this);
        readDataFromDB();
        lvStu.setAdapter(adater);
        lvStu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Student student = studentArrayList.get(i);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EditActivity.class);
                intent.putExtra("STUNO", student.getStuno());
                intent.putExtra("NAME", student.getName());
                intent.putExtra("AGE", student.getAge());
                startActivityForResult(intent, 1002);
            }
        });
    }

    //初始化所有数据
    private void readDataFromDB() {

        createDB();

        String sql = "create table if not exists student (stuno varchar(20), name varchar(20), age int)";
        db.execSQL(sql);

        studentArrayList.clear();
        Cursor cursor = db.query("student", null,null,
                null,null,null,null,null);

        while (cursor.moveToNext()){
                String stuno = cursor.getString(0);
                String name = cursor.getString(1);
                int age = cursor.getInt(2);

                Student  student = new Student(stuno, name, age);
                studentArrayList.add(student);
        }

        adater.notifyDataSetChanged();

        cursor.close();
        db.close();
    }

    //创建或打开数据库
    private void createDB() {
        String path = getFilesDir().getAbsolutePath() + File.separator + "stu.db";
        db = SQLiteDatabase.openOrCreateDatabase(path, null);
    }

    //接收来自其他页面的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data==null){
            return;
        }
        if(resultCode == 2001) {
            String stuno = data.getStringExtra("STUNO");
            String name = data.getStringExtra("NAME");
            int age = data.getIntExtra("AGE", 0);

            Student student = new Student(stuno, name, age);
            addStudentToDB(student);
        }
        if(resultCode == 3001) {
            String stuno = data.getStringExtra("STUNO");
            String name = data.getStringExtra("NAME");
            int age = data.getIntExtra("AGE", 0);

            Student student =  new Student(stuno, name, age);
            updateStudentToDB(student);
        }
    }

    //更新学生
    private void updateStudentToDB(Student student) {
        createDB();
        String sql = "update student set name = '"+student.getName()+"'," +
                " age = "+student.getAge()+" where stuno = '"+student.getStuno()+"'";
        db.execSQL(sql);
        db.close();
        readDataFromDB();
    }

    //添加学生
    private void addStudentToDB(Student student) {
        createDB();
        ContentValues values = new ContentValues();
        values.put("stuno", student.getStuno());
        values.put("name", student.getName());
        values.put("age", student.getAge());

        db.insert("student", null, values);
        db.close();
        readDataFromDB();
    }

    //查找首页各种组件
    private void findViews() {
        lvStu = (ListView) findViewById(R.id.lv_stu);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 1001);
            }
        });
        edtKey1 = (EditText) findViewById(R.id.edt_key1);
        edtKey2 = (EditText) findViewById(R.id.edt_key2);
        edtKey3 = (EditText) findViewById(R.id.edt_key3);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key1 = edtKey1.getText().toString();
                String key2 = edtKey2.getText().toString();
                String key3 = edtKey3.getText().toString();
                searchFromDB(key1,key2,key3);
            }
        });
    }

    //搜索学生
    private void searchFromDB(String key1,String key2,String key3) {
        createDB();
        String where = "name like '%" + key2 +"%' and stuno like '%" + key1 +"%' " +
                "and age like '%" + key3+"%'";

//        Log.e("顶顶顶顶",where);
        studentArrayList.clear();
        Cursor cursor = db.query("student", null,where,
                null,null,null,null,null);
//        Log.e("打哈哈的好",String.valueOf(cursor.getCount()));
        while (cursor.moveToNext()){
            String stuno = cursor.getString(0);
            String name = cursor.getString(1);
            int age = cursor.getInt(2);

            Student  student = new Student(stuno, name, age);
            studentArrayList.add(student);
        }

        adater.notifyDataSetChanged();

        cursor.close();
        db.close();
    }

    //删除学生
    @Override
    public void delete(Student student) {
        createDB();
        String where = "stuno = '" + student.getStuno() + "'";
        db.delete("student", where, null);
        db.close();
        readDataFromDB();
    }
}
