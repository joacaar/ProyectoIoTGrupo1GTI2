package com.GTI.Grupo1.IoT;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;


public class PerfilFragment extends Fragment{


    private FirebaseUser user;

    private static final String TAG = "PerfilFragment";
    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

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
        ImageView foto = view.findViewById(R.id.fotoUsuario);


        nombre.setText(user.getDisplayName());
        correo.setText(user.getEmail());
        String proveedor = user.getProviders().get(0);

        if(proveedor.equals("google.com")){
            String uri = user.getPhotoUrl().toString();
            Picasso.with(getActivity().getBaseContext()).load(uri).into(foto);
            System.out.println("dentro de getPhoto");
        }

        //CALENARIO
        mDisplayDate = view.findViewById(R.id.editTextFecha);


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        mDateSetListener,
                        year,month,day);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        return view;

    }

}
