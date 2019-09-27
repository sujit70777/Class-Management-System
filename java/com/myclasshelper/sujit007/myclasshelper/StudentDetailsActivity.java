package com.myclasshelper.sujit007.myclasshelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.myclasshelper.sujit007.myclasshelper.AttendanceInformation.AttendanceDatabaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentDetailsActivity extends AppCompatActivity {
    private String ClassTypeID;
    private int sid;
    private ArrayList<AttendanceInfo> attendanceInfoArrayList;
    private AttendanceAdapter2 attendanceAdapter;
    private AttendanceDatabaseAdapter attendanceDatabaseAdapter;
    GridView DetailsGrid;
    int atdID;
    String type, marksType, givenmarks;
    Dialog dialog;
    private int size;
    TextView presentP, absentP, leaveP, Total;
    TextView atdM, assM, midM, fM, TM, ctM;
    ImageButton BatdM, BassM, BmidM, BfM, BctM;
    CardView c1, c2, c3, c4, c5, c6;
    float a, p, sum, l;
    float pp, ap, lp;
    int i;
    Button more;
    AlertDialog.Builder builder;
    String url = "http://themughalsbd.com/classManager/updateMarks.php";
    String url2 = "http://themughalsbd.com/classManager/studentData.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        new backdata().execute();

        presentP = (TextView) findViewById(R.id.textViewPP);
        absentP = (TextView) findViewById(R.id.textViewAP);
        leaveP = (TextView) findViewById(R.id.textViewLP);
        Total = (TextView) findViewById(R.id.textViewTS);
        atdM = (TextView) findViewById(R.id.attendenceMarks);
        assM = (TextView) findViewById(R.id.assignmentMarks);
        midM = (TextView) findViewById(R.id.midMarks);
        fM = (TextView) findViewById(R.id.finalMarks);
        TM = (TextView) findViewById(R.id.totalMarks);
        ctM = (TextView) findViewById(R.id.ctMarks);
        more = (Button) findViewById(R.id.buttonmore);


        builder = new AlertDialog.Builder(StudentDetailsActivity.this);

        c1 = (CardView) findViewById(R.id.card1);
        c2 = (CardView) findViewById(R.id.card2);
        c3 = (CardView) findViewById(R.id.card3);
        c4 = (CardView) findViewById(R.id.card4);
        c5 = (CardView) findViewById(R.id.card5);
        c6 = (CardView) findViewById(R.id.card6);

        c1.setVisibility(View.GONE);
        c2.setVisibility(View.GONE);
        c3.setVisibility(View.GONE);
        c4.setVisibility(View.GONE);
        c5.setVisibility(View.GONE);
        c6.setVisibility(View.GONE);

        atdM.setVisibility(View.GONE);
        assM.setVisibility(View.GONE);
        midM.setVisibility(View.GONE);
        fM.setVisibility(View.GONE);
        TM.setVisibility(View.GONE);
        ctM.setVisibility(View.GONE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getExtras();
        ClassTypeID = bundle.getString("ClassTypeID");
        sid = bundle.getInt("sid");
        attendanceInfoArrayList = new ArrayList<AttendanceInfo>();
        attendanceDatabaseAdapter = new AttendanceDatabaseAdapter(this);
        DetailsGrid = (GridView) findViewById(R.id.gridVTakeClasses);
        setGridView();
        getSupportActionBar().setTitle("" + sid);

        BatdM = (ImageButton) findViewById(R.id.attendenceButton);
        BassM = (ImageButton) findViewById(R.id.assignemtButton);
        BmidM = (ImageButton) findViewById(R.id.midButton);
        BfM = (ImageButton) findViewById(R.id.finalButton);
        BctM = (ImageButton) findViewById(R.id.ctButton);

        BatdM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                givingMarks("1");
            }
        });
        BassM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                givingMarks("2");
            }
        });
        BctM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                givingMarks("3");
            }
        });
        BmidM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                givingMarks("4");
            }
        });
        BfM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                givingMarks("5");
            }
        });



        /*

        1 attendance mark
        2 Assignment mark
        3 ct marks
        4 mid marks
        5 final marks


         */


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fM.getVisibility() == View.VISIBLE) {
                    atdM.setVisibility(View.GONE);
                    assM.setVisibility(View.GONE);
                    midM.setVisibility(View.GONE);
                    fM.setVisibility(View.GONE);
                    TM.setVisibility(View.GONE);
                    ctM.setVisibility(View.GONE);
                    c1.setVisibility(View.GONE);
                    c2.setVisibility(View.GONE);
                    c3.setVisibility(View.GONE);
                    c4.setVisibility(View.GONE);
                    c5.setVisibility(View.GONE);
                    c6.setVisibility(View.GONE);
                    more.setText("All Marks");
                    DetailsGrid.setVisibility(View.VISIBLE);

                } else {
                    atdM.setVisibility(View.VISIBLE);
                    assM.setVisibility(View.VISIBLE);
                    midM.setVisibility(View.VISIBLE);
                    fM.setVisibility(View.VISIBLE);
                    TM.setVisibility(View.VISIBLE);
                    ctM.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.VISIBLE);
                    c5.setVisibility(View.VISIBLE);
                    c6.setVisibility(View.VISIBLE);
                    more.setText("All Attendence");
                    DetailsGrid.setVisibility(View.GONE);


                }
            }
        });


        DetailsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                type = attendanceAdapter.get_Std_A_Type(position);
                String CLASSid = attendanceInfoArrayList.get(position).getAttendanceDate();
                atdID = attendanceAdapter.get_Std_Aid(position);
                //      Toast.makeText(getApplicationContext(), type, Toast.LENGTH_LONG).show();
                dialog = new Dialog(StudentDetailsActivity.this);
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

    private void UpdateSattendance(String typeOFad, int adID) {

        attendanceDatabaseAdapter.updateStudentAttendance(String.valueOf(adID), typeOFad);
        setGridView();
    }

    private void setGridView() {
        attendanceInfoArrayList = attendanceDatabaseAdapter.getAttendanceData2(ClassTypeID, String.valueOf(sid));
        attendanceAdapter = new AttendanceAdapter2(StudentDetailsActivity.this, attendanceInfoArrayList);
        DetailsGrid.setAdapter(attendanceAdapter);
        size = attendanceInfoArrayList.size();
        setPercent();
    }

    private void setPercent() {
        a = 0;
        p = 0;
        l = 0;
        pp = 0;
        lp = 0;
        ap = 0;


        for (i = 0; i < size; i++) {

            if (attendanceAdapter.get_Std_A_Type(i).equals("P")) {
                p++;
            } else if (attendanceAdapter.get_Std_A_Type(i).equals("A")) {
                a++;
            } else if (attendanceAdapter.get_Std_A_Type(i).equals("L")) {
                l++;
            }

        }
        sum = a + p + l;

        pp = sum / p;
        ap = sum / a;
        lp = sum / l;
        pp = 100 / pp;
        ap = 100 / ap;
        lp = 100 / lp;

        Total.setText("Total Class: " + String.valueOf((int) sum));
        presentP.setText("Total Present: " + String.valueOf((int) p) + " (" + String.format("%.2f", pp) + "%)");
        absentP.setText("Total Absent: " + String.valueOf((int) a) + " (" + String.format("%.2f", ap) + "%)");
        leaveP.setText("Total Leave: " + String.valueOf((int) l) + " (" + String.format("%.2f", lp) + "%)");
        //    String.format("%.2f",pp);

    }

    public void givingMarks(final String type) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.d_marks_update);

        final EditText editText = (EditText) dialog.findViewById(R.id.marksEt);
        Button D_cancel = (Button) dialog.findViewById(R.id.cancelbtns);
        Button D_save = (Button) dialog.findViewById(R.id.savebtns);

        editText.setFocusable(true);
        if (type.equals("1")) {
            editText.setHint("Enter Attendance Marks");
        } else if (type.equals("2")) {
            editText.setHint("Enter Assignment Marks");
        } else if (type.equals("3")) {
            editText.setHint("Enter Class Test Marks");
        } else if (type.equals("4")) {
            editText.setHint("Enter Mid Exam Marks");
        } else if (type.equals("5")) {
            editText.setHint("Enter Final Exam Marks");
        }

        D_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marksType = type;
                givenmarks = editText.getText().toString();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                new backdata().execute();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }

                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", String.valueOf(sid));
                        params.put("marks", givenmarks);
                        params.put("type", type);

                        return params;
                    }
                };
                MySingletone.getmInstance(StudentDetailsActivity.this).addTorequestQue(stringRequest);

                dialog.cancel();
            }
        });


        D_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();

    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("Login failed")) {

                }


            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public class backdata extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        //    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject o = jsonArray.getJSONObject(0);


                                atdM.setText("Attendance Marks:" + " " + o.getString("attendence_marks"));

                                assM.setText("Assignment Marks:" + " " + o.getString("assignment_marks"));

                                ctM.setText("Class Test Marks:" + " " + o.getString("classtest_marks"));

                                midM.setText("Mid Exam Marks:" + " " + o.getString("mid_marks"));

                                fM.setText("Final Exam Marks:" + " " + o.getString("final_marks"));
                                TM.setText("Total Marks:" + " " + o.getString("total_marks"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            new backdata().execute();
                        }
                    }

            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", String.valueOf(sid));

                    return params;
                }
            };
            MySingletone.getmInstance(StudentDetailsActivity.this).addTorequestQue(stringRequest);


            return null;
        }
    }
}
