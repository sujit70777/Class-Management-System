package com.myclasshelper.sujit007.myclasshelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.myclasshelper.sujit007.myclasshelper.AttendanceInformation.AttendanceDatabaseAdapter;
import com.myclasshelper.sujit007.myclasshelper.StudentInformation.StudentAdapter;
import com.myclasshelper.sujit007.myclasshelper.StudentInformation.StudentDatabaseAdapter;
import com.myclasshelper.sujit007.myclasshelper.StudentInformation.StudentInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassActivity extends AppCompatActivity {
    ImageButton addStudents, attendence , allClass , Allstudents;
    int ClassID , batch , semester;
    TextView num , numClass;
    private ArrayList<StudentInfo> studentInfoArrayList;
    private StudentAdapter studentAdapter;
    private StudentDatabaseAdapter studentDatabaseAdapter;

    private AttendanceDatabaseAdapter databaseAdapter;
    private TakingClassAdapter takingClassAdapter;
    private ArrayList<AttendanceInfo> arrayList;
    String url = "http://themughalsbd.com/classManager/students.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        Bundle bundle = getIntent().getExtras();


        String className = bundle.getString("ClassName");
        ClassID = bundle.getInt("ClassID");
        batch = bundle.getInt("batch");
        semester = bundle.getInt("semester");
        //   Toast.makeText(getApplicationContext(), ClassID+" bb", Toast.LENGTH_LONG).show();
        TextView clsname = (TextView) findViewById(R.id.clsName);
        clsname.setText(className);

        //  Toast.makeText(getApplicationContext(), className+" o" , Toast.LENGTH_LONG).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        num = (TextView) findViewById(R.id.NumberOfStudent);
        numClass = (TextView) findViewById(R.id.numClass);
        studentInfoArrayList = new ArrayList<StudentInfo>();
        studentDatabaseAdapter = new StudentDatabaseAdapter(this);
        studentInfoArrayList = studentDatabaseAdapter.getStudentData(String.valueOf(ClassID));
        studentAdapter = new StudentAdapter(this , studentInfoArrayList);
        arrayList = new ArrayList<AttendanceInfo>();
        databaseAdapter = new AttendanceDatabaseAdapter(this);
        arrayList = databaseAdapter.getTakingClasses(String.valueOf(ClassID));
        takingClassAdapter = new TakingClassAdapter(this, arrayList);


        numClass.setText(String.valueOf(takingClassAdapter.getCount()));


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

      //  addStudents = (ImageButton) findViewById(R.id.ibAddStudent);
        attendence = (ImageButton) findViewById(R.id.ibAttendence);
        allClass = (ImageButton) findViewById(R.id.ibAllClass);
        Allstudents = (ImageButton) findViewById(R.id.ibAllstudents);
//        addStudents.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ClassActivity.this, AddStudent.class);
//                intent.putExtra("ClassTypeId", String.valueOf(ClassID));
//                startActivity(intent);
//
//            }
//        });
        attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassActivity.this, TakeAttendenceActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("batch", batch);
                bundle1.putInt("semester", semester);
                bundle1.putString("ClassTypeId", String.valueOf(ClassID));

                intent.putExtras(bundle1);

                startActivity(intent);

            }
        });

        allClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassActivity.this, TakingClassList.class);
                intent.putExtra("ClassTypeId", String.valueOf(ClassID));
                startActivity(intent);
            }
        });

         Allstudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassActivity.this, StudentListActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("batch", batch);
                bundle1.putInt("semester", semester);
                bundle1.putString("ClassTypeId", String.valueOf(ClassID));

                intent.putExtras(bundle1);

                startActivity(intent);
            }
        });

        new Backtask().execute();



    }

    public class Backtask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                num.setText(String.valueOf(jsonArray.length()));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            new Backtask().execute();
                        }
                    }

            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("batch", String.valueOf(batch));
                    params.put("semester", String.valueOf(semester));

                    return params;
                }
            };
            MySingletone.getmInstance(ClassActivity.this).addTorequestQue(stringRequest);



            return null;
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}

