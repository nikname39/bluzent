/**
 * work_start and end
 * by jh
 */
package com.minewbeacon.blescan.demo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuliwuli.blescan.demo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mDisplayDate, mOnworkView, mOffworkView;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private Button mBtnOnWork;
    private Button mBtnOffWork;
    private String Name = "";
    private String Work = "";


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //.. DAte
        mDisplayDate = (TextView) v.findViewById(R.id.tvDate);
        mOnworkView = (TextView) v.findViewById(R.id.OnworkView);
        mOffworkView = (TextView) v.findViewById(R.id.OffworkView);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        String date = year + "-" + (month+1) + "-" + day;
        String Timedate = hour + "시" + minute + "분";


        mDisplayDate.setText(date);


        //출퇴근 시간 가져오기


//        mDatabaseRef.child("Attendance").child(date).child(mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("name").toString()).child("work_start").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot datasnapshot1) {
//                String value1 = datasnapshot1.getValue(String.class);
//                Work = value1;
//                mOnworkView.setText("오늘의 출근 시간:"+Work);
//                Toast.makeText(getActivity(), Work, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
//            }
//        });

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                UserAccount account = new UserAccount();
                account.setIdToken(firebaseUser.getUid());
                account.setEmailId(firebaseUser.getEmail());
                account.setWork_start(Timedate);


                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        Name= value;

                        mDatabaseRef.child("Attendance").child(date).child(Name).child("work_start").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot1) {
                                String value1 = datasnapshot1.getValue(String.class);
                                Work = value1;
                                mOnworkView.setText("오늘의 출근 시간: "+Work);
                                Toast.makeText(getActivity(), Work, Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });

                //딜레이 후 시작할 코드 작성
            }
        }, 600);// 0.6초 정도 딜레이를 준 후 시작



        //..

        //출퇴근
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("bluzent");

        mBtnOnWork= v.findViewById(R.id.OnWork);
        // mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
        try {
            mBtnOnWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                    UserAccount account = new UserAccount();
                    account.setIdToken(firebaseUser.getUid());
                    account.setEmailId(firebaseUser.getEmail());
                    account.setWork_start(Timedate);


                    mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            Name = value;

                            mDatabaseRef.child("Attendance").child(date).child(Name).child("work_start").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot datasnapshot1) {
                                    String value1 = datasnapshot1.getValue(String.class);
                                    Work = value1;
                                    mOnworkView.setText("오늘의 출근 시간:" + Work);
                                    Toast.makeText(getActivity(), Work, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            mDatabaseRef.child("Attendance").child(date).child(Name).setValue(account);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                        }
                    });


                    Toast.makeText(getActivity(), "출근 성공", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
        }

//        mBtnOffWork= v.findViewById(R.id.OffWork);
//        // mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
//        mBtnOffWork.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
//                UserAccount account = new UserAccount();
//                account.setIdToken(firebaseUser.getUid());
//                account.setEmailId(firebaseUser.getEmail());
//
//                account.setWork_end(Timedate);
//
//
//                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("name").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String value = dataSnapshot.getValue(String.class);
//                        Name= value;
//
//                        mDatabaseRef.child("Attendance").child(date).child(Name).child("work_end").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot datasnapshot1) {
//                                String value1 = datasnapshot1.getValue(String.class);
//                                Work = value1;
//                                mOffworkView.setText("오늘의 퇴근 시간:"+Work);
//                                Toast.makeText(getActivity(), Work, Toast.LENGTH_SHORT).show();
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                        mDatabaseRef.child("Attendance").child(date).child(Name).(account);
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
//                    }
//                });
//
//
//
//                Toast.makeText(getActivity(), "출근 성공", Toast.LENGTH_SHORT).show();
//            }
//        });



        return v;
    }
}