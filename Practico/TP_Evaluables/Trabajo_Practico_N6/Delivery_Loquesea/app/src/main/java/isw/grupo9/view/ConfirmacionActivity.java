package isw.grupo9.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import isw.grupo9.MainActivity;
import isw.grupo9.R;

public class ConfirmacionActivity extends AppCompatActivity {

    private Button btnInicio;
    private ActionBar actionBar;
    private Button btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion);
        btnInicio = findViewById(R.id.btnInicio);
        btnInicio.setOnClickListener(view -> mostrarInicio());
        cargarActionBar();
    }

    private void mostrarInicio() {
        //Intent intentInicio = new Intent(Intent.ACTION_MAIN);
        //intentInicio.addCategory(Intent.CATEGORY_HOME);
        //intentInicio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intentInicio);
        //finish();
        navigateUpTo(new Intent(getBaseContext(), MainActivity.class));
    }
    private void cargarActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        View view =actionBar.getCustomView();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        btnAtras = findViewById(R.id.btnAtras);
        btnAtras.setVisibility(View.GONE);

    }
}