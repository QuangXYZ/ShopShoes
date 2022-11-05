package com.example.shopshoes.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopshoes.Adapter.OrderAdapter;
import com.example.shopshoes.Model.Order;
import com.example.shopshoes.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCustomerOder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCustomerOder extends Fragment {

    private OrderAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Order> orderArrayList;
    private TextView noOder;
    DatabaseReference myRootRef;
    private ProgressBar progressBar;
    FragmentActivity c;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCustomerOder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCustomerOder.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCustomerOder newInstance(String param1, String param2) {
        FragmentCustomerOder fragment = new FragmentCustomerOder();
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
        View view = inflater.inflate(R.layout.fragment_customer_oder, container, true);;
        orderArrayList =new ArrayList<Order>();
        recyclerView =view.findViewById(R.id.customer_order_list);
        progressBar = view.findViewById(R.id.spin_progress_bar_order);
        noOder = view.findViewById(R.id.no_order);
        myRootRef = FirebaseDatabase.getInstance().getReference();
        c = getActivity();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(c,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new OrderAdapter(orderArrayList,c, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(c.getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
//        getDataFromFirebase();
        //Toast.makeText(getActivity(),orderArrayList.size()+" size", Toast.LENGTH_LONG).show();
        return view;
    }

    public void getDataFromFirebase() {

        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post : snapshot.getChildren()){
                    Order order = post.getValue(Order.class);
                    orderArrayList.add(order);
                }
                mAdapter.notifyDataSetChanged();
                setData();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                noOder.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(c,"Error", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setData() {
        if(orderArrayList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            noOder.setVisibility(View.GONE);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            noOder.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }
}