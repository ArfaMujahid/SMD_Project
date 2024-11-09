package com.example.navigation_smd_7a;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveredFragment extends Fragment {

    //ARG_PARAM1 and ARG_PARAM2 are used to pass data to a Fragment when it is created.
    // These are static final variables that define the keys for the parameters that the fragment can accept.

    //They’re used in the newInstance method to bundle arguments (param1 and param2) so that when
    //Fragment is instantiated, it can hold onto and access these parameters.

    // Purpose of Parameters: If the parameters being passed to the fragment represent different data, you should rename ARG_PARAM1 and ARG_PARAM2 to better reflect their purpose. For instance, if they represent a user ID and an order status, you could rename them to ARG_USER_ID and ARG_ORDER_STATUS for better readability.
    //
    //Data Types: If the types of param1 and param2 change, say from String to int or any other data type, you’ll also need to adjust the code in newInstance() and onCreate() accordingly. This means using methods like args.putInt() instead of args.putString() and getArguments().getInt() to retrieve them.
    //
    //Fragment Reusability: If you intend to use the fragment with different parameter sets, you might add or remove parameters as needed. For example, if a fragment doesn’t need param2 for a certain use case, you can modify newInstance() to accept only param1, or create a new newInstance() method that only uses one paramete


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private DeliveredAdapter deliveredAdapter;
    private ArrayList<Product> products;

    public DeliveredFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveredFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveredFragment newInstance(String param1, String param2) {
        DeliveredFragment fragment = new DeliveredFragment();
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
        View view = inflater.inflate(R.layout.fragment_delivered, container, false);

        ListView listView = view.findViewById(R.id.productListView);
        ProductDB productDB = new ProductDB(getContext());
        productDB.open();

        products = productDB.fetchProductsWithStatus("delivered");
        productDB.close();

        deliveredAdapter = new DeliveredAdapter(getContext(), products);
        listView.setAdapter(deliveredAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ProductDB productDB = new ProductDB(getContext());
        productDB.open();

        ArrayList<Product> updatedProducts = productDB.fetchProductsWithStatus("delivered");
        productDB.close();

        products.clear();
        products.addAll(updatedProducts);
        deliveredAdapter.notifyDataSetChanged();
    }

}