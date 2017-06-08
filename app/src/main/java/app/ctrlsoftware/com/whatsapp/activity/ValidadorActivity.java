package app.ctrlsoftware.com.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import app.ctrlsoftware.com.whatsapp.R;
import app.ctrlsoftware.com.whatsapp.helper.Preferencias;

public class ValidadorActivity extends AppCompatActivity {

    private EditText caixaCodigoValidacaoSms;
    private Button btnValidarCodigoSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        caixaCodigoValidacaoSms = (EditText) findViewById(R.id.caixaCodigoValidacaoSmsId);
        btnValidarCodigoSms = (Button) findViewById(R.id.btnValidarCodigoSmsId);

        SimpleMaskFormatter simpleMaskCodigoValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraCodigoValidacao = new MaskTextWatcher(caixaCodigoValidacaoSms, simpleMaskCodigoValidacao);

        caixaCodigoValidacaoSms.addTextChangedListener(mascaraCodigoValidacao);

        btnValidarCodigoSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Recuperar dados das preferencias do usuario
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);

                HashMap<String, String> usuario = preferencias.getDadosUsuario();

                String tokenGerado = usuario.get("token");
                String tokenDigitado = caixaCodigoValidacaoSms.getText().toString();

                if (tokenDigitado.equals(tokenGerado)) {
                    Toast.makeText(ValidadorActivity.this, "Token VALIDADO", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(ValidadorActivity.this, "Token NÃO VALIDADO", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
