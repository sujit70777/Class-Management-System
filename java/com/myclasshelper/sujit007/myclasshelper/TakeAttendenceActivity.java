package com.myclasshelper.sujit007.myclasshelper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.myclasshelper.sujit007.myclasshelper.AttendanceInformation.AttendanceAdapter;
import com.myclasshelper.sujit007.myclasshelper.AttendanceInformation.AttendanceDatabaseAdapter;
import com.myclasshelper.sujit007.myclasshelper.StudentInformation.StudentAdapter;
import com.myclasshelper.sujit007.myclasshelper.StudentInformation.StudentDatabaseAdapter;
import com.myclasshelper.sujit007.myclasshelper.StudentInformation.StudentInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TakeAttendenceActivity extends AppCompatActivity {
    private Dialog dialog;
    private DatePicker datePicker;
    private TextView mydate;
    private String date = null, dateNum;
    private String ClassTypeID;
    private DatePickerDialog datePickerDialog;
    private int batch, semester;

    private StudentInfo studentInfo;
    private ArrayList<StudentInfo> studentInfoArrayList;
    private StudentDatabaseAdapter studentDatabaseAdapter;
    private StudentAdapter studentAdapter;
    private int size, size2;
    private int position;
    private ArrayList<AttendanceInfo> attendanceInfoArrayList;
    private AttendanceAdapter attendanceAdapter;
    private AttendanceDatabaseAdapter attendanceDatabaseAdapter;
    int atdID;
    String type;
    FloatingActionButton absent, present, leave;
    Button absent2, present2, leave2;
    TextView mid, first, last;
    GridView attendanceGridView;
    boolean att = false;
    Button reset, undo;
    private static final String StudentAbsent = "A";
    private static final String StudentPresent = "P";
    private static final String StudentLeave = "L";
    String url = "http://themughalsbd.com/classManager/students.php";

    LinearLayout round , old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);
        mydate = (TextView) findViewById(R.id.mydate);
        absent = (FloatingActionButton) findViewById(R.id.floatingABSENT);
        present = (FloatingActionButton) findViewById(R.id.floatingPRESENT);
        leave = (FloatingActionButton) findViewById(R.id.floatingLEAVE);


        absent2 = (Button) findViewById(R.id.floatingABSENT2);
        present2 = (Button) findViewById(R.id.floatingPRESENT2);
        leave2 = (Button) findViewById(R.id.floatingLEAVE2);

        mid = (TextView) findViewById(R.id.textViewMid);
        last = (TextView) findViewById(R.id.textViewLast);
        first = (TextView) findViewById(R.id.textViewFirst);
        reset = (Button) findViewById(R.id.reset);
        undo = (Button) findViewById(R.id.undo);


        attendanceGridView = (GridView) findViewById(R.id.GridView_Attendence);

        dialog = new Dialog(TakeAttendenceActivity.this);
        dialog.setContentView(R.layout.d__get_date);
        datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);

        final Button getdate = (Button) dialog.findViewById(R.id.getdate);

        getdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = String.valueOf(datePicker.getDayOfMonth()) + "-" + String.valueOf(datePicker.getMonth() + 1) + "-" + String.valueOf(datePicker.getYear());
                dateNum = date;
                dialog.dismiss();
                mydate.setText("Date : " + date + "");
                setGridView();
                //  Toast.makeText(getApplicationContext(),  "" + dateNum, Toast.LENGTH_LONG).show();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        ClassTypeID = bundle.getString("ClassTypeId");
        batch = bundle.getInt("batch");
        semester = bundle.getInt("semester");

        studentInfoArrayList = new ArrayList<StudentInfo>();
        studentDatabaseAdapter = new StudentDatabaseAdapter(this);
        // studentInfoArrayList = studentDatabaseAdapter.getStudentData(ClassTypeID);
        new Backtask().execute();

        studentAdapter = new StudentAdapter(TakeAttendenceActivity.this, studentInfoArrayList);

        attendanceInfoArrayList = new ArrayList<AttendanceInfo>();
        attendanceDatabaseAdapter = new AttendanceDatabaseAdapter(this);




        position = 0;



        att = true;


        //   int id = studentAdapter.get_Std_Cid(1);
        //   Toast.makeText(getApplicationContext(),  "" + id, Toast.LENGTH_LONG).show();
        round = (LinearLayout) findViewById(R.id.roundButton);
        old = (LinearLayout) findViewById(R.id.oldButton);
        if(Build.VERSION.SDK_INT <22){

         //   Toast.makeText(getApplicationContext(), ""+ Build.VERSION.SDK_INT , Toast.LENGTH_LONG).show();
            round.setVisibility(View.GONE);

        }else {
        //    Toast.makeText(getApplicationContext(), ""+ Build.VERSION.SDK_INT , Toast.LENGTH_LONG).show();
            old.setVisibility(View.GONE);
        }


        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertAttendance(StudentPresent, v);
            }
        });
        absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertAttendance(StudentAbsent, v);
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertAttendance(StudentLeave, v);
            }
        });


 present2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertAttendance(StudentPresent, v);
            }
        });
        absent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertAttendance(StudentAbsent, v);
            }
        });
        leave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertAttendance(StudentLeave, v);
            }
        });


        //----------------------- UNDO AND RESET ----------------------


        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attendanceInfoArrayList.size() > 0 && attendanceInfoArrayList.size() < size) {
                    int aid = attendanceAdapter.get_Std_Aid(attendanceInfoArrayList.size() - 1);
                    attendanceDatabaseAdapter.deleteStudentAttendance(String.valueOf(aid));
                    position--;
                    setGridView();
                    SetStudentID();
                } else {
                    Snackbar.make(v, "No Attendance To Undo!!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (attendanceInfoArrayList.size() > 0) {

                    scroll = 0;

                    final AlertDialog.Builder builder = new AlertDialog.Builder(TakeAttendenceActivity.this);
                    builder.setTitle("Delete Class");
                    builder.setCancelable(false);
                    builder.setMessage("Are you sure to Reset All this Attendance ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            attendanceDatabaseAdapter.deleteStudentsAttendance(dateNum, ClassTypeID);
                            position = 0;
                            setGridView();
                            SetStudentID();
                            dialog.cancel();
                            Snackbar.make(v, "Attendance is Reset ", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                } else {
                    Snackbar.make(v, "No Attendance To Reset!!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                }

            }
        });

        attendanceGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                type = attendanceAdapter.get_Std_A_Type(position);
                int CLASSid = attendanceAdapter.get_Std_Cid(position);
                atdID = attendanceAdapter.get_Std_Aid(position);
                //      Toast.makeText(getApplicationContext(), type, Toast.LENGTH_LONG).show();
                dialog = new Dialog(TakeAttendenceActivity.this);
                dialog.setContentView(R.layout.d__class_long_press);
                ImageView close = (ImageView) dialog.findViewById(R.id.closeImg);
                TextView text = (TextView) dialog.findViewById(R.id.myDialog);
                Button delete = (Button) dialog.findViewById(R.id.deletebtn);
                Button update = (Button) dialog.findViewById(R.id.updatebtn);
                dialog.setCancelable(false);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }

                });
                if (type.equals("A")) {
                    delete.setText("Leave");
                    delete.setBackgroundColor(0xffffbb33);

                    update.setText("Present");
                    update.setBackgroundColor(0xff99cc00);

                    text.setText("" + CLASSid);
                    text.setTextSize((float) 40.0);
                    text.setTextColor(0xffffffff);
                    text.setBackgroundColor(0xffff4444);
                }

                if (type.equals("P")) {
                    delete.setText("Absent");
                    delete.setBackgroundColor(0xffff4444);

                    update.setText("Leave");
                    update.setBackgroundColor(0xffffbb33);

                    text.setText("" + CLASSid);
                    text.setTextSize((float) 40.0);
                    text.setTextColor(0xffffffff);
                    text.setBackgroundColor(0xff99cc00);
                }

                if (type.equals("L")) {
                    delete.setText("Absent");
                    delete.setBackgroundColor(0xffff4444);

                    update.setText("Present");
                    update.setBackgroundColor(0xff99cc00);

                    text.setText("" + CLASSid);
                    text.setTextSize((float) 40.0);
                    text.setTextColor(0xffffffff);
                    text.setBackgroundColor(0xffffbb33);
                }

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String send = null;
                        String tt = null;

                        if (type.equals("A")) {
                            send = "L";
                            tt = "Leave";
                        } else if (type.equals("P")) {
                            send = "A";
                            tt = "Absent";
                        } else if (type.equals("L")) {
                            send = "A";
                            tt = "Absent";
                        }


                        UpdateSattendance(send, atdID);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Attendance Updated to " + tt, Toast.LENGTH_LONG).show();
                    }
                });


                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String send = null;

                        String tt = null;

                        if (type.equals("A")) {
                            send = "P";
                            tt = "Present";

                        } else if (type.equals("P")) {
                            send = "L";

                            tt = "Leave";

                        } else if (type.equals("L")) {
                            send = "P";
                            tt = "Present";
                        }
                        UpdateSattendance(send, atdID);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Attendance Updated to " + tt, Toast.LENGTH_LONG).show();
                    }
                });


                dialog.show();
            }
        });


    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void UpdateSattendance(String typeOFad, int adID) {

        attendanceDatabaseAdapter.updateStudentAttendance(String.valueOf(adID), typeOFad);
        setGridView();
    }

    private void InsertAttendance(String str, View view) {
        if (position < size && size2 < size) {
            String sid = String.valueOf(studentAdapter.get_Std_Cid(position));

            long id = attendanceDatabaseAdapter.StudentInfoInsert(sid, dateNum, ClassTypeID, str);
            setGridView();
            position++;
            SetStudentID();
        } else {
            Snackbar.make(view, "Attendance is finish!!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            // Toast.makeText(getApplicationContext(), "Attendance is finish", Toast.LENGTH_LONG).show();
            setGridView();

        }



    }

    private void SetStudentID() {
        if (size > 0) {
            if (position < size) {
                mid.setText(String.valueOf(studentInfoArrayList.get(position).getStudent_Class_id()));
                if (position + 1 < size) {
                    last.setText("N: " + String.valueOf(studentInfoArrayList.get(position+1).getStudent_Class_id()));
                } else {
                    last.setText("  ");
                }
                if (position - 1 > -1) {
                    first.setText("P: " + String.valueOf(studentInfoArrayList.get(position-1).getStudent_Class_id()));
                } else {
                    first.setText("  ");
                }
            } else {
                mid.setText("");
                if (position - 1 > -1) {
                    first.setText("P: " + String.valueOf(studentInfoArrayList.get(position-1).getStudent_Class_id()));
                } else {
                    first.setText("  ");
                }
            }


        } else {
            Toast.makeText(getApplicationContext(), "Add Students First", Toast.LENGTH_LONG).show();
        }
    }

    int scroll =0;

    private void setGridView() {
        attendanceInfoArrayList = attendanceDatabaseAdapter.getAttendanceData(ClassTypeID, dateNum);
        size2 = attendanceInfoArrayList.size();
        if (size2 > size) {

            Toast.makeText(getApplicationContext(), "Attendance is Already taken", Toast.LENGTH_LONG).show();
            att = false;
        }
        attendanceAdapter = new AttendanceAdapter(TakeAttendenceActivity.this, attendanceInfoArrayList);


        attendanceGridView.setAdapter(attendanceAdapter);
        attendanceGridView.smoothScrollToPosition(scroll);

        scroll++;

    }

    public class Backtask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int count = 0;
                             //  Toast.makeText(getApplicationContext() , response+"" , Toast.LENGTH_LONG).show();

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


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("-----------------------", "Json ERROR:---------- ");
                            }

                            size = studentInfoArrayList.size();

                            if (size > 0) {
                                dialog.show();
                            }

                            SetStudentID();
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
            MySingletone.getmInstance(TakeAttendenceActivity.this).addTorequestQue(stringRequest);


            return null;
        }
    }

}
