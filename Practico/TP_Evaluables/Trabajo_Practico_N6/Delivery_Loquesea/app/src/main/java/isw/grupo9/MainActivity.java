package isw.grupo9;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import isw.grupo9.controller.ControladorPedidoLoQueSea;
import isw.grupo9.model.Domicilio;
import isw.grupo9.view.DatePickerFragment;
import isw.grupo9.view.DescripcionPedidoActivity;
import isw.grupo9.view.TimePickerFragment;

public class MainActivity extends AppCompatActivity{

    private  Button btnSiguiente;
    private Spinner spnCiudades;
    private EditText etCalle;
    private EditText etNro;
    private EditText etReferencia;
    private Button imgBtnMapa;
    private ControladorPedidoLoQueSea controlador;
    private RadioButton rdBtnAntesPosible;
    private RadioButton rdBtnEntregaProgramada;
    private EditText etFechaEntregaSel, etPisoRetiro, etDptoRetiro;
    private TextInputLayout tilCalleRetiro, tilNroCalleRetiro, tilReferenciaRetiro, tilFechaEnvio;
    private TextInputLayout tilPisoRetiro, tilDptoRetiro;
    private ImageView ivMapa;
    private ConstraintLayout clVistaMain;
    private ActionBar actionBar;
    private Button btnAtras;

    private static long LAST_CLICK_TIME = 0;
    private final int mDoubleClickInterval = 400; // Milliseconds


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlador = new ControladorPedidoLoQueSea();
        setContentView(R.layout.activity_main);
        cargarVistas();
        cargarCiudades();
        cargarListeners();
        agregarFiltros();
        rdBtnAntesPosible.setChecked(true);
        etFechaEntregaSel.setText("");
        tilFechaEnvio.setVisibility(View.GONE);
        etFechaEntregaSel.setVisibility(View.GONE);
        cargarActionBar();

    }

    private void cargarActionBar(){
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        View view =actionBar.getCustomView();
        btnAtras = findViewById(R.id.btnAtras);
        btnAtras.setVisibility(View.GONE);
    }

    private void cargarVistas(){
        btnSiguiente= findViewById(R.id.btnSiguiente);
        etCalle = findViewById(R.id.etCalleRetiro);
        etNro = findViewById(R.id.etNumeroCalleRetiro);
        etReferencia = findViewById(R.id.etReferenciaRetiro);
        tilCalleRetiro = findViewById(R.id.tilCalleRetiro);
        tilNroCalleRetiro = findViewById(R.id.tilNumCalleRetiro);
        tilReferenciaRetiro = findViewById(R.id.tilReferenciaRetiro);
        tilFechaEnvio = findViewById(R.id.tilFechaSel);
        rdBtnAntesPosible = findViewById(R.id.rdBtnAntesPosible);
        rdBtnEntregaProgramada = findViewById(R.id.rdBtnEntregaProgramada);
        etFechaEntregaSel = findViewById(R.id.etFechaSeleccionada);
        spnCiudades = findViewById(R.id.spnCiudadesRetiro);
        imgBtnMapa = findViewById(R.id.btnMapaSelDomRetiro);
        ivMapa = findViewById(R.id.ivMapaRetiro);
        clVistaMain = findViewById(R.id.clVistaMain);
        etPisoRetiro = findViewById(R.id.etPisoRetiro);
        etDptoRetiro = findViewById(R.id.etDptoRetiro);
        tilPisoRetiro = findViewById(R.id.tilPisoRetiro);
        tilDptoRetiro = findViewById(R.id.tilDptoRetiro);
        actionBar = getSupportActionBar();

    }
    private void cargarListeners(){
        btnSiguiente.setOnClickListener((view) -> mostrarDescripcionPedido());
        rdBtnAntesPosible.setOnClickListener(view -> ocultarFechaSeleccionada());
        rdBtnEntregaProgramada.setOnClickListener(view -> mostrarSelectorFecha());
        etCalle.setOnFocusChangeListener((view, tieneFoco) -> cambioFocoEtCalle(tieneFoco));
        etNro.setOnFocusChangeListener((view, tieneFoco) -> cambioFocoNroCalle(tieneFoco));
        etReferencia.setOnFocusChangeListener((view, tieneFoco) -> cambioFocoReferencia(tieneFoco));
        imgBtnMapa.setOnClickListener((view) -> mostrarMapa());
        ivMapa.setOnClickListener((view) -> seleccionDomicilioMapa());

    }
    private void agregarFiltros(){
        ArrayList<InputFilter> curInputFilters = new ArrayList<>(Arrays.asList(etCalle.getFilters()));
        curInputFilters.add(0, new ControladorPedidoLoQueSea.AlphaNumericInputFilter());
        curInputFilters.add(1, new InputFilter.AllCaps());
        InputFilter[] newInputFilters = curInputFilters.toArray(new InputFilter[curInputFilters.size()]);
        etCalle.setFilters(newInputFilters);
        ArrayList<InputFilter> dptoFilters = new ArrayList<>(Arrays.asList(etDptoRetiro.getFilters()));
        dptoFilters.add(0, new ControladorPedidoLoQueSea.AlphaNumericInputFilter());
        dptoFilters.add(1, new InputFilter.AllCaps());
        etDptoRetiro.setFilters(dptoFilters.toArray(new InputFilter[dptoFilters.size()]));
        //etDptoRetiro.addTextChangedListener(new ControladorPedidoLoQueSea.DptoWatcher());
        ArrayList<InputFilter> pisoFilters = new ArrayList<>(Arrays.asList(etPisoRetiro.getFilters()));
        pisoFilters.add(0, new ControladorPedidoLoQueSea.AlphaNumericInputFilter());
        pisoFilters.add(1, new InputFilter.AllCaps());
        etPisoRetiro.setFilters(dptoFilters.toArray(new InputFilter[dptoFilters.size()]));
        //Filtros para referencia de domicilio
        ArrayList<InputFilter> refInputFilters = new ArrayList<>(Arrays.asList(etReferencia.getFilters()));
        refInputFilters.add(0, new ControladorPedidoLoQueSea.AlphaNumericInputFilter());
        refInputFilters.add(1, new InputFilter.AllCaps());
        InputFilter[] newRefInputFilters = refInputFilters.toArray(new InputFilter[refInputFilters.size()]);
        etReferencia.setFilters(newRefInputFilters);
    }

    private void seleccionDomicilioMapa() {
        long doubleClickCurrentTime = System.currentTimeMillis();
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - LAST_CLICK_TIME <= mDoubleClickInterval){

            ivMapa.setVisibility(View.GONE);
            clVistaMain.setVisibility(View.VISIBLE);
            Domicilio domicilio = controlador.obtenerDomicilioRandom();
            Random random = new Random();
            etCalle.setText(domicilio.getCalle());
            etNro.setText(String.valueOf(domicilio.getNumero()));
            etPisoRetiro.setText((domicilio.getPiso() != null) ? domicilio.getPiso() : "");
            etDptoRetiro.setText((domicilio.getDpto() != null) ? domicilio.getDpto() : "");
            spnCiudades.setSelection(random.nextInt(spnCiudades.getAdapter().getCount()));
        }else {
            LAST_CLICK_TIME = System.currentTimeMillis();
            // !Warning, Single click action problem
        }
    }

    private void mostrarMapa() {
        ivMapa.setVisibility(View.VISIBLE);
        clVistaMain.setVisibility(View.GONE);
    }

    private void cambioFocoReferencia(boolean tieneFoco) {
        if(!tieneFoco){
            if(!controlador.validarReferenciaDomicilio(etReferencia.getText().toString().trim(), tilReferenciaRetiro.getId())){
                tilReferenciaRetiro.setError(controlador.getError(tilReferenciaRetiro.getId()));
                tilReferenciaRetiro.setErrorEnabled(true);
            }else{
                tilCalleRetiro.setError(null);
                tilReferenciaRetiro.setErrorEnabled(false);
            }
        }
    }

    private void cambioFocoNroCalle(boolean tieneFoco) {
        if(!tieneFoco){
            if(!controlador.validarNumeroCalle(etNro.getText().toString().trim(), tilNroCalleRetiro.getId())){
                tilNroCalleRetiro.setError(controlador.getError(tilNroCalleRetiro.getId()));
                tilNroCalleRetiro.setErrorEnabled(true);
            }else{
                tilNroCalleRetiro.setError(null);
                tilNroCalleRetiro.setErrorEnabled(false);
            }
        }
    }

    private void cambioFocoEtCalle(boolean tieneFoco) {
        if(!tieneFoco){
            if(!controlador.validarCalle(etCalle.getText().toString().trim(), tilCalleRetiro.getId())){
                tilCalleRetiro.setError(controlador.getError(tilCalleRetiro.getId()));
                tilCalleRetiro.setErrorEnabled(true);
                //Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
                //etCalle.startAnimation(animShake);
            }else{
                tilCalleRetiro.setError(null);
                tilCalleRetiro.setErrorEnabled(false);
            }
        }
    }


    private void mostrarDescripcionPedido(){
        String localidad = spnCiudades.getSelectedItem().toString();
        String calle = etCalle.getText().toString().trim();
        String numero = etNro.getText().toString().trim();
        String referencia = etReferencia.getText().toString().trim();
        String piso = etPisoRetiro.getText().toString().trim();
        String dpto = etDptoRetiro.getText().toString().trim();
        boolean loAntesPosible =rdBtnAntesPosible.isChecked();
        LocalDateTime fechaEnvio = (!etFechaEntregaSel.getText().toString().isEmpty())
                ? LocalDateTime.parse(etFechaEntregaSel.getText().toString(),
                                                        DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"))
                : null;

        Map<Integer, String> errores = controlador.registrarDomicilioRetiro(localidad, calle,
                numero, piso, dpto, referencia, loAntesPosible,
                fechaEnvio,spnCiudades.getId(), tilCalleRetiro.getId(), tilNroCalleRetiro.getId(),
                tilReferenciaRetiro.getId(),tilFechaEnvio.getId());

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
            Intent descripcionPedidoIntent = new Intent(this, DescripcionPedidoActivity.class);
            descripcionPedidoIntent.putExtra("controlador", controlador);
            startActivity(descripcionPedidoIntent);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void cargarCiudades(){
        List<String> ciudadesList =  new ArrayList<String>();
        ciudadesList.add("Córdoba");
        ciudadesList.add("Unquillo");
        ciudadesList.add("Villa Allende");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ciudadesList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnCiudades.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void mostrarSelectorFecha() {

        etFechaEntregaSel.setVisibility(View.VISIBLE);
        tilFechaEnvio.setVisibility(View.VISIBLE);
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance((datePicker, anio, mes, dia) -> {
                seleccionarFecha(anio, mes, dia);},
                (view) -> {cancalarSeleccionFecha();});
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");

    }

    private void cancalarSeleccionFecha() {
        rdBtnAntesPosible.setChecked(true);
        etFechaEntregaSel.setText("");
        etFechaEntregaSel.setVisibility(View.GONE);
        tilFechaEnvio.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void seleccionarFecha(int anio, int mes, int dia) {

        //etFechaEntregaSel.setText(LocalDate.of(anio,mes+1,dia).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        mostrarSelectorHora(anio, mes, dia);
        //etFechaEntregaSel.setVisibility(View.VISIBLE);
        //tilFechaEnvio.setVisibility(View.VISIBLE);
        /*
        try{
            if(!controlador.validarFechaEnvio(LocalDateTime
                            .parse(etFechaEntregaSel.getText().toString(),
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                    tilFechaEnvio.getId())){
                tilFechaEnvio.setError(controlador.getError(tilFechaEnvio.getId()));
                tilFechaEnvio.setErrorEnabled(true);

            }else{
                tilFechaEnvio.setError(null);
                tilFechaEnvio.setErrorEnabled(false);
                mostrarSelectorHora(anio, mes, dia);
            }
        }catch(Exception e){
            rdBtnAntesPosible.setChecked(true);
        }*/

    }

    private void mostrarSelectorHora(int anio, int mes, int dia) {
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance((picker, hora, minuto)
                                                     -> seleccionarHora(anio, mes, dia,hora, minuto),
                (view) -> cancalarSeleccionFecha());

        timePickerFragment.show(getSupportFragmentManager(), "timePicker");

    }

    private void seleccionarHora(int anio, int mes, int dia, int hora, int minuto) {

        LocalDateTime fechaHoraSeleccionada = LocalDateTime.of(anio, mes+1, dia, hora, minuto);
        etFechaEntregaSel.setText(fechaHoraSeleccionada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));
        if(!controlador.validarFechaEnvio(fechaHoraSeleccionada, tilFechaEnvio.getId())){
            tilFechaEnvio.setError(controlador.getError(tilFechaEnvio.getId()));
            tilFechaEnvio.setErrorEnabled(true);
        }else{
            tilFechaEnvio.setError(null);
            tilFechaEnvio.setErrorEnabled(false);

        }

    }

    private void ocultarFechaSeleccionada() {
        etFechaEntregaSel.setText("");
        etFechaEntregaSel.setVisibility(View.GONE);
        tilFechaEnvio.setVisibility(View.GONE);
        tilFechaEnvio.setErrorEnabled(false);
    }
}