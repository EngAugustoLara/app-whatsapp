package app.ctrlsoftware.com.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import app.ctrlsoftware.com.whatsapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText nome;
    private EditText fone;
    private EditText foneCodigoDdd;
    private EditText foneCodigoPais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nome = (EditText) findViewById(R.id.caixaNomeId);
        foneCodigoPais = (EditText) findViewById(R.id.caixaFoneCodigoPaisId);
        foneCodigoDdd = (EditText) findViewById(R.id.caixaFoneCodigoDddId);
        fone = (EditText) findViewById(R.id.caixaFoneId);



        SimpleMaskFormatter simpleMaskCodigoPais = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMaskCodigoDdd = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");

        MaskTextWatcher maskCodigoPais = new SimpleMaskTextWatcher(foneCodigoPais, simpleMaskCodigoPais);
        MaskTextWatcher maskCodigoDdd = new SimpleMaskTextWatcher(foneCodigoDdd, simpleMaskCodigoDdd);
        MaskTextWatcher maskTelefone = new MaskTextWatcher(fone, simpleMaskTelefone);

        foneCodigoPais.addTextChangedListener(maskCodigoPais);
        foneCodigoDdd.addTextChangedListener(maskCodigoDdd);
        fone.addTextChangedListener(maskTelefone);

    }
}
