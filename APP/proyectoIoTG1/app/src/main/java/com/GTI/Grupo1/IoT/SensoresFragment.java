package com.GTI.Grupo1.IoT;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class SensoresFragment extends Fragment {

    private RecyclerView recyclerView;
    public SensoresAdaptador adaptador;
    private RecyclerView.LayoutManager layoutManager;
    public static SensoresInterface sensoresInterface = new SensoresVector();

    public SensoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sensores_layout, container, false);

        // Inflate the layout for this fragment
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adaptador = new SensoresAdaptador(getActivity(), sensoresInterface);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }


}