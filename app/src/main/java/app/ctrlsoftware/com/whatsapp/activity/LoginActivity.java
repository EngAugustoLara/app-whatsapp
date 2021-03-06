package app.ctrlsoftware.com.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import app.ctrlsoftware.com.whatsapp.R;
import app.ctrlsoftware.com.whatsapp.config.ConfiguracaoFirebase;
import app.ctrlsoftware.com.whatsapp.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginSenha;
    private Button btnLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        loginEmail = (EditText) findViewById(R.id.editLoginEmailId);
        loginSenha = (EditText) findViewById(R.id.editLoginSenhaId);
        btnLogar = (Button) findViewById(R.id.btnLogarId);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario = new Usuario();
                usuario.setEmail( loginEmail.getText().toString() );
                usuario.setSenha( loginSenha.getText().toString() );

                validarLogin();
            }
        });
    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ) {
                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login", Toast.LENGTH_SHORT).show();

                } else{
                    String erroExecao = "";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        erroExecao = "A conta de email não existe ou foi desativada";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExecao = "Senha incorreta";
                    } catch (Exception e) {
                        erroExecao = "Ao efeturar login";
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "Erro: "+erroExecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity( intent );
        finish();
    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }
}
