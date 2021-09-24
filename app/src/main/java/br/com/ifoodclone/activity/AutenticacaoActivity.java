package br.com.ifoodclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import br.com.ifoodclone.R;
import br.com.ifoodclone.databinding.ActivityAutenticacaoBinding;
import br.com.ifoodclone.helper.ConfiguracaoFirebase;
import br.com.ifoodclone.helper.UsuarioFirebase;

public class AutenticacaoActivity extends AppCompatActivity {
    private ActivityAutenticacaoBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAutenticacaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        inicializarComponentes();

        verificaUsuarioLogado();

        binding.switchAcesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){//Cadastrar
                    binding.linearTipoUsuario.setVisibility(View.VISIBLE);

                }else {//Logar
                    binding.linearTipoUsuario.setVisibility(View.GONE);
                }

            }
        });

        binding.btnAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editLoginEmail.getText().toString();
                String senha = binding.editLoginSenha.getText().toString();

                if (!email.isEmpty()){
                    if (!senha.isEmpty()){
                        //Verifica estado do swith
                        if (binding.switchAcesso.isChecked()){//Cadastro
                            auth.createUserWithEmailAndPassword(
                                    email,
                                    senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AutenticacaoActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                                        String tipoUsuario = getTipoUsuario();
                                        UsuarioFirebase.atualizarTipoUsuario(tipoUsuario);
                                        abrirTelaPrincipal(tipoUsuario);
                                    }else {
                                        String excessao = "";

                                        try {
                                            throw task.getException();
                                        }catch (FirebaseAuthWeakPasswordException e){
                                            excessao = "Digite uma senha mais forte!";
                                        }catch (FirebaseAuthInvalidCredentialsException e){
                                            excessao = "Digite um e-mail valido";
                                        }catch (FirebaseAuthUserCollisionException e){
                                            excessao = "Esta conja fa foi cadastrada";
                                        }catch (Exception e){
                                            excessao = "Erro ao cadastrar usuario: "+e.getMessage();
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(AutenticacaoActivity.this, excessao, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {//Login
                            auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AutenticacaoActivity.this, "Logado com sucesso!", Toast.LENGTH_SHORT).show();
                                        String tipoUsuario = auth.getCurrentUser().getDisplayName();
                                        abrirTelaPrincipal(tipoUsuario);
                                    }else {
                                        Toast.makeText(AutenticacaoActivity.this, "Erro ao fazer login: "+task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }else {
                        binding.editLoginSenha.setError(getString(R.string.obrigatorio));
                    }
                }else {
                    binding.editLoginEmail.setError(getString(R.string.obrigatorio));
                }

            }
        });
    }
    private String getTipoUsuario(){
        return binding.switchTipoUsuario.isChecked() ? "E" : "U";
    }

    private void verificaUsuarioLogado() {
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual != null){
            String tipoUsuario = usuarioAtual.getDisplayName();
            abrirTelaPrincipal(tipoUsuario);
        }
    }

    private void abrirTelaPrincipal(String tipoUsuario) {
        if (tipoUsuario.equals("E")){//Empresa
            startActivity(new Intent(getApplicationContext(),EmpresaActivity.class));
        }else{//Usuario
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
    }

    private void inicializarComponentes(){
        auth = ConfiguracaoFirebase.getAuth();
    }

}