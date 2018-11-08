package com.GTI.Grupo1.IoT;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class UsuariosFragment extends Fragment {

    private RecyclerView recyclerView;
    public AdaptadorUsuarios adaptador;
    private RecyclerView.LayoutManager layoutManager;
    public static UsuarioInterface usuarios = new UsuariosVector();

    public UsuariosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.usuarios_fragment, container, false);

        // Inflate the layout for this fragment
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adaptador = new AdaptadorUsuarios(getActivity(), usuarios);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }


}
