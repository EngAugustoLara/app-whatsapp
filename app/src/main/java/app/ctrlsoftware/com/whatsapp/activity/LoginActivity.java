package app.ctrlsoftware.com.whatsapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import app.ctrlsoftware.com.whatsapp.R;
import app.ctrlsoftware.com.whatsapp.helper.Permissao;
import app.ctrlsoftware.com.whatsapp.helper.Preferencias;

public class LoginActivity extends AppCompatActivity {

    private EditText nome;
    private EditText fone;
    private EditText foneCodigoDdd;
    private EditText foneCodigoPais;
    private Button btnCadastrar;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Permissao.validaPermissoes(1, this, permissoesNecessarias); // Não precisa instanciar a classe pq o método é static


        nome = (EditText) findViewById(R.id.caixaNomeId);
        foneCodigoPais = (EditText) findViewById(R.id.caixaFoneCodigoPaisId);
        foneCodigoDdd = (EditText) findViewById(R.id.caixaFoneCodigoDddId);
        fone = (EditText) findViewById(R.id.caixaFoneId);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrarId);

        //Definindo as máscaras
        SimpleMaskFormatter simpleMaskCodigoPais = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMaskCodigoDdd = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");

        MaskTextWatcher maskCodigoPais = new SimpleMaskTextWatcher(foneCodigoPais, simpleMaskCodigoPais);
        MaskTextWatcher maskCodigoDdd = new SimpleMaskTextWatcher(foneCodigoDdd, simpleMaskCodigoDdd);
        MaskTextWatcher maskTelefone = new MaskTextWatcher(fone, simpleMaskTelefone);

        foneCodigoPais.addTextChangedListener(maskCodigoPais);
        foneCodigoDdd.addTextChangedListener(maskCodigoDdd);
        fone.addTextChangedListener(maskTelefone);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomeUsuario = nome.getText().toString();
                String telefoneCompleto =
                        foneCodigoPais.getText().toString() +
                        foneCodigoDdd.getText().toString() +
                        fone.getText().toString();

                //Retirando as mascaras e deixando o telefone sem formatação

                String telefoneSemFormatacao;
                telefoneSemFormatacao = telefoneCompleto.replace("-", "").replace("+", "");

                //Gerar Token
                Random randomico = new Random();
                int numeroRandomico = randomico.nextInt(9999 - 1000) + 1000;
                String token = String.valueOf(numeroRandomico);
                String mensagemEnvio = "WhatsApp Código de confirmação: "+token;

                //Salvar os dados para validação
                Preferencias preferencias = new Preferencias(LoginActivity.this);
                preferencias.salvarUsuarioPreferencias(nomeUsuario, telefoneSemFormatacao, token);

                //Envio de SMS
                //telefoneSemFormatacao = "5554";
                boolean enviadoSMS = enviaSMS("+" +telefoneSemFormatacao, mensagemEnvio);

                /*
                HashMap<String, String> usuario = preferencias.getDadosUsuario();
                Log.i("TOKEN", "T:" +usuario.get("token") + " FONE:" +usuario.get("telefone"));
                Toast.makeText(getApplicationContext(), "Clicado em Cadastrar", Toast.LENGTH_SHORT).show();
                */
            }
        });

    }

    //Envio SMS
    private boolean enviaSMS(String telefone, String mensagem){

        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);

            return true;
        } catch (Exception e){
            e.printStackTrace();

            return false;
        }

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissons, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissons, grantResults);

        for(int resultado : grantResults){
            if (resultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar esse app, é necessário aceitar as permissões");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
