package com.GTI.Grupo1.IoT;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login();
    }

    private void login() {
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        //usuario.sendEmailVerification();
        if (usuario != null) {
            System.out.println("Dentro de la funcion login, usuario no null");
            if(usuario.isEmailVerified()){
                Toast.makeText(this, "inicia sesión: " +
                        usuario.getDisplayName() + " - " + usuario.getEmail() + " - " +
                        usuario.getProviders().get(0), Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }else{
                showUiLogin();
                Toast.makeText(this, "Correo electronico no verificado", Toast.LENGTH_LONG).show();
            }
        } else {

            showUiLogin();
            /*
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true)
                                    .build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build()    )) //<<<<<<<<<<<<<
                    .setIsSmartLockEnabled(false)
                    .setLogo(R.drawable.nublado)
                    .setTheme(R.style.TemaLogin)
                    .build(), RC_SIGN_IN);
                    */
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            System.out.println("**************************************************************");
            System.out.println("Objeto data: " + data + "\n");
            System.out.println(data.getExtras().get("extra_idp_response"));
            System.out.println();
            System.out.println("**************************************************************");

            //aqui comprobamos si es el primer inicio de esta cuenta y si es de correo o google
            //si es el primer inicio enviamos el email
            //si no, llamamos a login

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                sendEmailVerification(data);
                login();
                //finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "Sin conexión a Internet",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(this, "Error desconocido",
                        Toast.LENGTH_LONG).show();
                Log.e("Autentificación", "Sign-in error: ", response.getError());
            }
        }
    }

    private void sendEmailVerification(Intent data){

        IdpResponse respuesta = (IdpResponse) data.getExtras().get("extra_idp_response");
        //User user = respuesta.getUser();
        String provider = respuesta.getProviderType();
        String var = "password";
        System.out.println("Dentro de la funcion sendEmail");
        System.out.println(provider);

        usuario = FirebaseAuth.getInstance().getCurrentUser();

        if(provider.equals("password")){

            System.out.println ("El proveedor  es password");

            if (respuesta.isNewUser()) {

                System.out.println("El usuario es nuevo");

                System.out.println("Usuario contiene: "+ usuario);
                usuario.sendEmailVerification();
                Toast.makeText(LoginActivity.this, "Correo de verificación enviado", Toast.LENGTH_LONG);
                /*
                usuario.sendEmailVerification()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    System.out.println("task es exitoso, llamando a login");
                                    login();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Correo no válido", Toast.LENGTH_SHORT);
                                }
                            }
                        });
                        */
            }else{
                System.out.println("en el else del segundo if");

                return;
            }
        }else{
            System.out.println("en el else del primer if");
            return;
        }

    }

    public void showUiLogin (){
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true)
                                .build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()    )) //<<<<<<<<<<<<<
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.nublado)
                .setTheme(R.style.TemaLogin)
                .build(), RC_SIGN_IN);
    }
}
