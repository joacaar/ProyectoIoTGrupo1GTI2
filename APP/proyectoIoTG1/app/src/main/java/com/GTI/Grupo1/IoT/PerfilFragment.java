package com.GTI.Grupo1.IoT;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class PerfilFragment extends Fragment {


    private FirebaseUser user;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.perfil, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        TextView nombre = view.findViewById(R.id.nombreUsuario);
        TextView correo = view.findViewById(R.id.correoUsuario);

        nombre.setText(user.getDisplayName());
        correo.setText(user.getEmail());

        return view;
    }

}
