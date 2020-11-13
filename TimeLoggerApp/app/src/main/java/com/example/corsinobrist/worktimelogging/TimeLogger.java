package com.example.corsinobrist.worktimelogging;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.MoreObjects;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TimeLogger extends AppCompatActivity {
    public static final String DATE_KEY = "date";
    public static final String TIME_KEY = "time";
    public static final String TAG = "FireBase";
    Calendar actualCalender = Calendar.getInstance();


    String docID = "";
    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("test/timeData");
    private DocumentReference mDocRefUser = FirebaseFirestore.getInstance().document("user/Corsin");
    private CollectionReference mCollRefUser = FirebaseFirestore.getInstance().collection("user");
    private FirebaseFirestore db = FirebaseFirestore.getInstance();//.collection("user/");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
    String currentDateandTime = sdf.format(new Date());
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    Map<String, Object> dataToSave = new HashMap<String, Object>();
    Map<String, Object> testdataToSave = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView timeField =(TextView) findViewById(R.id.timeField);
        timeField.setText(currentTime);
        TextView dateField =(TextView) findViewById(R.id.dateField);
        dateField.setText(currentDate);
        dataToSave.put(DATE_KEY, currentDate);
        dataToSave.put(TIME_KEY, currentTime);



    }


    public void onClick_breakBTN(View view) {
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        TextView timeField =(TextView) findViewById(R.id.timeField);
        timeField.setText(currentTime);
        TextView dateField =(TextView) findViewById(R.id.dateField);
        dateField.setText(currentDate);

        mCollRefUser.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> docID = new ArrayList<>();
                    List<String> docID2 = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        docID.add(document.getId());

                    }
                    Log.d(TAG, docID.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    public void onClick_startBTN(View view) {
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        TextView timeField =(TextView) findViewById(R.id.timeField);
        timeField.setText(currentTime);
        TextView dateField =(TextView) findViewById(R.id.dateField);
        dateField.setText(currentDate);
        dataToSave.put(DATE_KEY, currentDate);
        dataToSave.put(TIME_KEY, currentTime);


        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FireBase","Document has been saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FireBase","Document could NOT been saved!!!");
            }
        });
    }


    public void onClick_finishBTN(View view) {



       creatUser(mCollRefUser);


    /*
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String dateText =documentSnapshot.getString(DATE_KEY);
                    String timeText =documentSnapshot.getString(TIME_KEY);
                    Map<String, Object> myData = documentSnapshot.getData();
                    TextView timeField =(TextView) findViewById(R.id.timeField);
                    timeField.setText(timeText);
                    TextView dateField =(TextView) findViewById(R.id.dateField);
                    dateField.setText(dateText);
                }
                Log.d("FireBase","Document read sucessfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FireBase","Document could NOT read from Firebase!!!");
            }
        });*/

    }



    public boolean creatCollection(String  coll, DocumentReference docRef) {
        return false;
    }
    public boolean creatDocument(HashMap data, CollectionReference colRef) {
        return false;
    }

    public Map creatUserHashmap(Integer numFields) {
        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "Strauch");
        data1.put("surname", "Beere");
        data1.put("position", "Wiss. Assistent");
        data1.put("employment", "50");
        data1.put("username", "StB");
        data1.put("password", "strauch");
        return data1;
    }

    public boolean creatUser(CollectionReference colRef) {
      final boolean successFlag = false;
       /* mCollRefUser.document().set(creatUserHashmap(2))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });*/
        creatUserDatatree(colRef, "StB");
        return successFlag;
    }

    public boolean creatUserDatatree(final CollectionReference colRef, String username){

        mCollRefUser.whereEqualTo("username", "StB") .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> docIDs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        docIDs.add(document.getId());

                    }
                    Log.d(TAG, docIDs.toString());
                    docID = docIDs.get(0).toString();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                CollectionReference accountsColRef = colRef.document(docID).collection("accounts");
                CollectionReference yearRefCol= colRef.document(docID).collection("year");


                //Add documents to new collections
                Map<String, Object> accounts = new HashMap<>();
                accounts.put("111222", "CCISN Labor");
                accountsColRef.document().set(accounts)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "accounts successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document accounts", e);
                            }
                        });

                Map<String, Object> years = new HashMap<>();
                String actualYear = String.valueOf(actualCalender.get(Calendar.YEAR));
                years.put(actualYear, actualYear);
                yearRefCol.document().set(years)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot years successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });




                //Add collections month
                CollectionReference monthRefCol = yearRefCol.document(actualYear).collection("month");
                Map<String, Object> months = new HashMap<>();
                String actualMonth = String.valueOf(actualCalender.get(Calendar.MONTH));
                months.put(actualYear, actualYear);
                //Add documents to new collections
                monthRefCol.document().set(months)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot months successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });


                //Add collections day
                CollectionReference dayRefCol = monthRefCol.document(actualMonth).collection("days");
                Map<String, Object> days = new HashMap<>();
                String actualDay = String.valueOf(actualCalender.get(Calendar.DAY_OF_MONTH));
                days.put(actualDay, actualDay);
                //Add documents to new collections
                dayRefCol.document().set(days)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot days successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });


                //Add collections stamps & bookings
                CollectionReference stampsRefCol = dayRefCol.document(actualDay).collection("stamps");
                CollectionReference bookingsRefCol = dayRefCol.document(actualDay).collection("bookings");



                Map<String, Object> stamps = new HashMap<>();

                stamps.put("Test", "");
                //Add documents to new collections
                stampsRefCol.document().set(stamps)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot stamps successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });


                Map<String, Object> bookings = new HashMap<>();
                bookings.put("Test", "");
                //Add documents to new collections
                bookingsRefCol.document().set(bookings)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot bookings successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        }, 5000);   //5 seconds




        return true;
    }
}