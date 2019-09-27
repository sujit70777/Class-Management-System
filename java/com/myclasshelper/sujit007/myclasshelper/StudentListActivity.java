package com.myclasshelper.sujit007.myclasshelper;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.myclasshelper.sujit007.myclasshelper.StudentInformation.StudentAdapter;
import com.myclasshelper.sujit007.myclasshelper.StudentInformation.StudentDatabaseAdapter;
import com.myclasshelper.sujit007.myclasshelper.StudentInformation.StudentInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentListActivity extends AppCompatActivity {


    ListView listView;
    private ArrayList<StudentInfo> studentInfoArrayList;
    private StudentAdapter studentAdapter;
    private StudentDatabaseAdapter studentDatabaseAdapter;
    String ClassTypeID;
    int batch , semester;
    Dialog dialog;
    String url = "http://themughalsbd.com/classManager/students.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        ClassTypeID = bundle.getString("ClassTypeId");
        batch = bundle.getInt("batch");
        semester = bundle.getInt("semester");
        dialog = new Dialog(StudentListActivity.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new Backtask().execute();

        listView = (ListView) findViewById(R.id.studentListNew);

        studentInfoArrayList = new ArrayList<StudentInfo>();
        studentDatabaseAdapter = new StudentDatabaseAdapter(this);
   //     studentInfoArrayList = studentDatabaseAdapter.getStudentData(ClassTypeID);

        studentAdapter = new StudentAdapter(StudentListActivity.this, studentInfoArrayList);

        listView.setAdapter(studentAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int sid = studentInfoArrayList.get(position).getStudent_Class_id();
                Intent intent = new Intent(StudentListActivity.this , StudentDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ClassTypeID" , ClassTypeID);
                bundle.putInt("sid" , sid);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });




    }

    public class Backtask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int count = 0;
                            //    Toast.makeText(getApplicationContext() , response+"" , Toast.LENGTH_LONG).show();

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                while (count < response.length()) {
                                    JSONObject j = jsonArray.getJSONObject(count);

                                    StudentInfo info = new StudentInfo();
                                    info.setStudent_id(j.getInt("id"));
                                    info.setStudent_Class_id(j.getInt("studentId"));
                                    info.setStudent_Name(j.getString("name"));

                                    studentInfoArrayList.add(info);
                                    count++;
                                    listView.setAdapter(studentAdapter);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("-----------------------", "Json ERROR:---------- ");
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
            MySingletone.getmInstance(StudentListActivity.this).addTorequestQue(stringRequest);



            return null;
        }
    }

}
