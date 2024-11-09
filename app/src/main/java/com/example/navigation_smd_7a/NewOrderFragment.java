package com.example.navigation_smd_7a;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOrderFragment extends Fragment {

    Context context;
    private ProductAdapter adapter;
    private ArrayList<Product> products;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }



    /**\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     *
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewOrderFragment newInstance(String param1, String param2) {
        NewOrderFragment fragment = new NewOrderFragment();
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
        return inflater.inflate(R.layout.fragment_new_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lvNewOrderList = view.findViewById(R.id.lvNewOrdersList);
        ProductDB productDB = new ProductDB(context);
        productDB.open();
        products = productDB.fetchProducts();
        productDB.close();

        adapter = new ProductAdapter(context, R.layout.product_item_design, products);
        lvNewOrderList.setAdapter(adapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        ProductDB productDB = new ProductDB(getContext());
        productDB.open();

        ArrayList<Product> updatedProducts = productDB.fetchProducts();
        productDB.close();

        products.clear();
        products.addAll(updatedProducts);
        adapter.notifyDataSetChanged();
    }

    public void refreshProductList() {
        ProductDB productDB = new ProductDB(context);
        productDB.open();
        products = productDB.fetchProducts();
        productDB.close();
        adapter.notifyDataSetChanged();
    }
}

//
//////////////////////////////////////////////
//2. Send Data from NewOrderFragment to Another Fragment in MainActivity
//To send data to another fragment within the same activity (like MainActivity), you can use FragmentManager and pass data via Bundle.
//
//In NewOrderFragment
//
//// Send data to another fragment in MainActivity
//Bundle bundle = new Bundle();
//bundle.putSerializable("productList", updatedProductList); // Assuming Product implements Serializable
//bundle.putString("stringData", someStringData);
//
//OtherFragment otherFragment = new OtherFragment();
//otherFragment.setArguments(bundle);
//
//getParentFragmentManager()
//    .beginTransaction()
//    .replace(R.id.fragment_container, otherFragment)
//    .addToBackStack(null)
//    .commit();
//In OtherFragment
//Retrieve the data in onCreate or onViewCreated:
//
//java
//Copy code
//// Inside OtherFragment
//@Override
//public void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    if (getArguments() != null) {
//        ArrayList<Product> productList = (ArrayList<Product>) getArguments().getSerializable("productList");
//        String stringData = getArguments().getString("stringData");
//    }
//}
//3. Send Data from NewOrderFragment to a Fragment in Another Activity
//To send data from a fragment in one activity to a fragment in another activity, you can use Intent to pass data from MainActivity to the target activity, which will then send it to the fragment in that activity.
//
//        In NewOrderFragment
//Start the other activity and add data to the Intent:
//
//java
//Copy code
//// Send data to another activity
//Intent intent = new Intent(getActivity(), OtherActivity.class);
//intent.putExtra("productList", updatedProductList);
//intent.putExtra("stringData", someStringData);
//startActivity(intent);
//In OtherActivity
//Receive the data and pass it to the target fragment:
//
//java
//Copy code
//// Inside OtherActivity
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_other);
//
//    ArrayList<Product> productList = (ArrayList<Product>) getIntent().getSerializableExtra("productList");
//    String stringData = getIntent().getStringExtra("stringData");
//
//    OtherFragment otherFragment = new OtherFragment();
//    Bundle bundle = new Bundle();
//    bundle.putSerializable("productList", productList);
//    bundle.putString("stringData", stringData);
//    otherFragment.setArguments(bundle);
//
//    getSupportFragmentManager()
//            .beginTransaction()
//            .replace(R.id.fragment_container, otherFragment)
//            .commit();
//}
