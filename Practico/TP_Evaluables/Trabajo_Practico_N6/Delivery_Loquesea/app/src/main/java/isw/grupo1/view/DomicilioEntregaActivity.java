package isw.grupo1.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import isw.grupo1.R;
import isw.grupo1.controller.ControladorPedidoLoQueSea;
import isw.grupo1.model.Domicilio;

public class DomicilioEntregaActivity extends AppCompatActivity {

    private Button btnSiguiente;
    private Spinner spnCiudades;
    private EditText etCalle;
    private EditText etNro;
    private EditText etReferencia, etPiso, etDpto;
    private Button btnMapa;
    private TextInputLayout tilCalleEnvio, tilNroCalleEnvio, tilReferenciaEnvio;
    private ControladorPedidoLoQueSea controlador;
    private ImageView ivMapa;
    private ConstraintLayout clVistaDomicilio;
    private ActionBar actionBar;
    private Button btnAtras;

    private static long LAST_CLICK_TIME = 0;
    private final int mDoubleClickInterval = 400; // Milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        controlador = (ControladorPedidoLoQueSea) bundle.getSerializable("controlador");
        setContentView(R.layout.activity_domicilio_entrega);
        cargarVistas();
        cargarCiudades();
        agregarFiltros();
        etCalle.setOnFocusChangeListener((view, tieneFoco) -> cambioFocoEtCalle(tieneFoco));
        etNro.setOnFocusChangeListener(((view, b) -> cambioFocoNroCalle(b)));
        etReferencia.setOnFocusChangeListener(((view, b) -> cambioFocoReferencia(b)));
        btnSiguiente.setOnClickListener((view) -> mostrarMedioPago());
        btnMapa.setOnClickListener(view -> mostrarMapa());
        ivMapa.setOnClickListener((view) -> seleccionDomicilioMapa());
        actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        cargarActionBar();

    }

    private void cargarActionBar(){
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        View view =actionBar.getCustomView();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        btnAtras = findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(bView -> botonAtras());
    }
    private void botonAtras(){
        this.finish();
    }

    private void cargarCiudades(){
        List<String> ciudadesList =  new ArrayList<String>();
        ciudadesList.add("Carlos Paz");
        ciudadesList.add("San Francisco");
        ciudadesList.add("Va. Gral. Belgrano");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ciudadesList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCiudades.setAdapter(adapter);
    }

    private void cargarVistas(){
        spnCiudades = findViewById(R.id.spnCiudadesEnvio);
        btnSiguiente= findViewById(R.id.btnMostrarMedioP);
        etCalle = findViewById(R.id.etCalleEnvio);
        etNro = findViewById(R.id.etNumeroCalleEnvio);
        etReferencia = findViewById(R.id.etReferenciaEnvio);
        tilCalleEnvio = findViewById(R.id.tilCalleEnvio);
        tilNroCalleEnvio = findViewById(R.id.tilNroCalleEnvio);
        tilReferenciaEnvio = findViewById(R.id.tilReferenciaEnvio);
        etPiso = findViewById(R.id.etPisoEnvio);
        etDpto = findViewById(R.id.etDptoEnvio);
        ivMapa = findViewById(R.id.ivMapaEnvio);
        btnMapa = findViewById(R.id.btnMapaSelDomEnvio);
        clVistaDomicilio = findViewById(R.id.clDomicilioEnvioActivity);
    }

    private void mostrarMapa() {
        ivMapa.setVisibility(View.VISIBLE);
        clVistaDomicilio.setVisibility(View.GONE);
    }

    private void cambioFocoReferencia(boolean tieneFoco) {
        if(!tieneFoco){
            if(!controlador.validarReferenciaDomicilio(etReferencia.getText().toString().trim(), tilReferenciaEnvio.getId())){
                tilReferenciaEnvio.setError(controlador.getError(tilReferenciaEnvio.getId()));
                tilReferenciaEnvio.setErrorEnabled(true);
            }else{
                tilCalleEnvio.setError(null);
                tilReferenciaEnvio.setErrorEnabled(false);
            }
        }
    }

    private void cambioFocoNroCalle(boolean tieneFoco) {
        if(!tieneFoco){
            if(!controlador.validarNumeroCalle(etNro.getText().toString().trim(), tilNroCalleEnvio.getId())){
                tilNroCalleEnvio.setError(controlador.getError(tilNroCalleEnvio.getId()));
                tilNroCalleEnvio.setErrorEnabled(true);
            }else{
                tilNroCalleEnvio.setError(null);
                tilNroCalleEnvio.setErrorEnabled(false);
            }
        }
    }

    private void cambioFocoEtCalle(boolean tieneFoco) {
        if(!tieneFoco){
            if(!controlador.validarCalle(etCalle.getText().toString().trim(), tilCalleEnvio.getId())){
                tilCalleEnvio.setError(controlador.getError(tilCalleEnvio.getId()));
                tilCalleEnvio.setErrorEnabled(true);
                //Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
                //etCalle.startAnimation(animShake);
            }else{
                tilCalleEnvio.setError(null);
                tilCalleEnvio.setErrorEnabled(false);
            }
        }
    }
    private void agregarFiltros(){
        ArrayList<InputFilter> curInputFilters = new ArrayList<>(Arrays.asList(etCalle.getFilters()));
        curInputFilters.add(0, new ControladorPedidoLoQueSea.AlphaNumericInputFilter());
        curInputFilters.add(1, new InputFilter.AllCaps());
        InputFilter[] newInputFilters = curInputFilters.toArray(new InputFilter[curInputFilters.size()]);
        etCalle.setFilters(newInputFilters);
        ArrayList<InputFilter> dptoFilters = new ArrayList<>(Arrays.asList(etDpto.getFilters()));
        dptoFilters.add(0, new ControladorPedidoLoQueSea.AlphaNumericInputFilter());
        dptoFilters.add(1, new InputFilter.AllCaps());
        etDpto.setFilters(dptoFilters.toArray(new InputFilter[dptoFilters.size()]));
        ArrayList<InputFilter> pisoFilters = new ArrayList<>(Arrays.asList(etPiso.getFilters()));
        pisoFilters.add(0, new ControladorPedidoLoQueSea.AlphaNumericInputFilter());
        pisoFilters.add(1, new InputFilter.AllCaps());
        etPiso.setFilters(dptoFilters.toArray(new InputFilter[dptoFilters.size()]));
    }

    private void mostrarMedioPago(){

        String localidad = spnCiudades.getSelectedItem().toString();
        String calle = etCalle.getText().toString().trim();
        String numero = etNro.getText().toString().trim();
        String referencia = etReferencia.getText().toString().trim();
        String piso = etPiso.getText().toString().trim();
        String dpto = etDpto.getText().toString().trim();

        //Al registrar el domicilio de envío se calcula el costo.
        Map<Integer, String> errores = controlador.registrarDomicilioEnvio(localidad, calle, numero,
                referencia, piso, dpto, tilCalleEnvio.getId(), tilNroCalleEnvio.getId(),
                tilReferenciaEnvio.getId());

        if(errores != null){
            Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
            for(Map.Entry<Integer, String> entry : errores.entrySet()){
                try {
                    TextInputLayout til = findViewById(entry.getKey());
                    til.setError(entry.getValue());
                    til.setErrorEnabled(true);
                    til.startAnimation(animShake);
                }catch(Exception e){
                    continue;
                }
            }
            return;
        }

        try {
            Intent medioPagoActivity = new Intent(this, MedioPagoActivity.class);
            medioPagoActivity.putExtra("controlador", controlador);
            startActivity(medioPagoActivity);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void seleccionDomicilioMapa() {
        long doubleClickCurrentTime = System.currentTimeMillis();
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - LAST_CLICK_TIME <= mDoubleClickInterval){

            ivMapa.setVisibility(View.GONE);
            clVistaDomicilio.setVisibility(View.VISIBLE);
            Domicilio domicilio = controlador.obtenerDomicilioRandom();
            Random random = new Random();
            etCalle.setText(domicilio.getCalle());
            etNro.setText(String.valueOf(domicilio.getNumero()));
            etPiso.setText((domicilio.getPiso() != null) ? domicilio.getPiso().toString() : "");
            etDpto.setText((domicilio.getDpto() != null) ? domicilio.getDpto() : "");
            spnCiudades.setSelection(random.nextInt(spnCiudades.getAdapter().getCount()));
        }else {
            LAST_CLICK_TIME = System.currentTimeMillis();
            // !Warning, Single click action problem
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}