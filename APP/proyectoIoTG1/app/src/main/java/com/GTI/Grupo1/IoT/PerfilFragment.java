package com.GTI.Grupo1.IoT;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class PerfilFragment extends Fragment {


    private FirebaseUser user;

    private static final String TAG = "PerfilFragment";
    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button btnGuardar;

    private String date;
    private SeekBar seekBar;
    private String nivelEjer;
    private TextView textViewEjer;




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

        if (proveedor.equals("google.com")) {
            String uri = user.getPhotoUrl().toString();
            Picasso.with(getActivity().getBaseContext()).load(uri).into(foto);
            System.out.println("dentro de getPhoto");
        }

//---------------------------  CALENARIO  ------------------------------------------------------------------------------------
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
                        year, month, day);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

//------------------------------  SEEKBAR  ---------------------------------------------------------------------------------
        seekBar = view.findViewById(R.id.seekBar);
        textViewEjer = view.findViewById(R.id.TextoEjer);

        if (seekBar != null) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // Write code to perform some action when progress is changed.
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Write code to perform some action when touch is started.
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Write code to perform some action when touch is stopped.
                    //Toast.makeText(getActivity(), "Nivel de ejercicio " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();

                    if (seekBar.getProgress() == 0){
                        nivelEjer = "Nada de ejercicio ";
                    }else if (seekBar.getProgress()== 1){
                        nivelEjer = "Poco ejercicio";
                    }else if (seekBar.getProgress()== 2){
                        nivelEjer = "Ejercicio una vez por semana";
                    }else if (seekBar.getProgress()== 3){
                        nivelEjer = "Bastante ejercicio";
                    }else if (seekBar.getProgress()== 4){
                        nivelEjer = "Mucho ejercicio";
                    }

                    textViewEjer.setText(nivelEjer);
                }
            });
        }

        //Guardar datos del usuario
        addListenerOnButton(view);

        return view;
    }//onCreate()

//-------------------  GUARDAR DATOS DEL USUARIO  -----------------------------------------------------------------------
    public void addListenerOnButton(View view) {

        radioSexGroup = view.findViewById(R.id.radioSex);
        btnGuardar = view.findViewById(R.id.guardar);


        btnGuardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioSexButton = getView().findViewById(selectedId);


                if(radioSexButton!=null || date!=null) {
                    Toast.makeText(getActivity(),
                            radioSexButton.getText(), Toast.LENGTH_SHORT).show();

                    Toast.makeText(getActivity(),
                            date, Toast.LENGTH_LONG).show();

                    Toast.makeText(getActivity(),
                            "Nivel de ejercicio " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();



//-------------------------GUARDAR LOS DATOS DEL USUARIO EN LA BASE DE DATOS---------------------------------------

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    // Create a new user with a first and last name
                    Map<String, Object> perfilUser = new HashMap<>();
                    perfilUser.put("Sexo", radioSexButton.getText());
                    perfilUser.put("Fecha de nacimiento", date);
                    perfilUser.put("Nivel de ejercicio", seekBar.getProgress());

                    // Add a new document with a generated ID
                    db.collection("perfilUser")
                            .add(perfilUser)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                }//if()



            }

        });

    }

}//()


