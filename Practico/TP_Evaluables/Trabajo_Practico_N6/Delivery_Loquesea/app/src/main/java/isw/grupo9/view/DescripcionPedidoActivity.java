package isw.grupo9.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import isw.grupo9.R;
import isw.grupo9.controller.ControladorPedidoLoQueSea;
import isw.grupo9.utils.FileMetadata;

public class DescripcionPedidoActivity extends AppCompatActivity {

    private ControladorPedidoLoQueSea controlador;
    private EditText etDescripcionPedido;
    private TextInputLayout tilDescripcionPedido;
    private TextView tvPathImagen;
    private Button btnSiguienteActivity;
    private Button btnCargarImagen;
    private Button btnQuitarImagen;
    private ImageView ivImagen;
    private Uri uriImagen;
    private ActionBar actionBar;
    private Button btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_pedido);
        Bundle bundle = getIntent().getExtras();
        controlador = (ControladorPedidoLoQueSea)bundle.getSerializable("controlador");

        etDescripcionPedido = findViewById(R.id.etDescripcionPedido);
        tilDescripcionPedido = findViewById(R.id.tilDescripcionPedido);
        btnSiguienteActivity = findViewById(R.id.btnCargarDomEntrega);
        tvPathImagen = findViewById(R.id.tvRutaImagen);
        tvPathImagen.setVisibility(View.GONE);
        ivImagen = findViewById(R.id.ivImagenSeleccionada);
        btnCargarImagen = findViewById(R.id.btnCargarImagen);
        btnQuitarImagen = findViewById(R.id.imgBtnQuitarImagen);
        btnQuitarImagen.setVisibility(View.GONE);
        ivImagen.setVisibility(View.GONE);
        etDescripcionPedido.setOnFocusChangeListener((vista, tieneFoco) -> cambioFocoDescripcion(tieneFoco));
        btnSiguienteActivity.setOnClickListener((view -> mostrarDomicilioEntrega()));
        btnQuitarImagen.setOnClickListener(view -> quitarImagen());
        btnCargarImagen.setOnClickListener(view -> cargarImagen());
        actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        cargarActionBar();
        agregarFiltros();

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

    private void agregarFiltros(){
        //Filtros para referencia de domicilio
        ArrayList<InputFilter> refInputFilters = new ArrayList<>(Arrays.asList(etDescripcionPedido.getFilters()));
        refInputFilters.add(0, new ControladorPedidoLoQueSea.AlphaNumericInputFilter());
        refInputFilters.add(1, new InputFilter.AllCaps());
        InputFilter[] newRefInputFilters = refInputFilters.toArray(new InputFilter[refInputFilters.size()]);
        etDescripcionPedido.setFilters(newRefInputFilters);
    }

    private void cargarImagen() {
        Intent intentoGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentoGaleria, 3);
    }

    private void mostrarDomicilioEntrega() {

        String descripcionPedido = etDescripcionPedido.getText().toString();
        Map<Integer, String> errores = controlador.registrarDescripcionPedido(descripcionPedido,
                uriImagen, tilDescripcionPedido.getId());
        if(errores != null){
            Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
            try {
                for (Map.Entry<Integer, String> entry : errores.entrySet()) {
                    TextInputLayout til = findViewById(entry.getKey());
                    til.setError(entry.getValue());
                    til.setErrorEnabled(true);
                    til.startAnimation(animShake);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return;
        }
        //Cambio de activity
        Intent intentoDireccionEnvio = new Intent(this, DomicilioEntregaActivity.class);
        intentoDireccionEnvio.putExtra("controlador", controlador);
        startActivity(intentoDireccionEnvio);
    }

    private void cambioFocoDescripcion(boolean tieneFoco) {
        if(!tieneFoco){
            if(!controlador
                    .validarDescripcionPedido(etDescripcionPedido.getText().toString(),
                            tilDescripcionPedido.getId())){
                tilDescripcionPedido.setError(controlador.getError(tilDescripcionPedido.getId()));
                tilDescripcionPedido.setErrorEnabled(true);

            } else{
                tilDescripcionPedido.setError(null);
                tilDescripcionPedido.setErrorEnabled(false);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            uriImagen = data.getData();
            FileMetadata metadata = FileMetadata.getFileMetaData(getBaseContext(), uriImagen);
            if(metadata.getSize() < 5*1024*1024) {
                tvPathImagen.setText(".../" + metadata.getDisplayName());
                tvPathImagen.setVisibility(View.VISIBLE);
                ivImagen.setImageURI(uriImagen);
                ivImagen.setVisibility(View.VISIBLE);
                btnQuitarImagen.setVisibility(View.VISIBLE);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Entrada Inválida").setMessage("La imagen debe ser menor a 5MB");
                builder.setPositiveButton("Aceptar",(dialod, id) -> {});
                AlertDialog dialogo = builder.create();
                dialogo.show();

            }
        }
    }

    private void quitarImagen(){
        uriImagen = null;
        ivImagen.setImageURI(null);
        ivImagen.setVisibility(View.GONE);
        btnQuitarImagen.setVisibility(View.GONE);
        tvPathImagen.setText("");
        tvPathImagen.setVisibility(View.GONE);
    }

    private void botonAtras(){
        this.finish();
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