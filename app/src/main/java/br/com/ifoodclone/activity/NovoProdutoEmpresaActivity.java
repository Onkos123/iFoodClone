package br.com.ifoodclone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import br.com.ifoodclone.R;
import br.com.ifoodclone.activity.model.Empresa;
import br.com.ifoodclone.activity.model.Produto;
import br.com.ifoodclone.databinding.ActivityNovoProdutoEmpresaBinding;
import br.com.ifoodclone.helper.UsuarioFirebase;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {
    private ActivityNovoProdutoEmpresaBinding binding;
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNovoProdutoEmpresaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Configuracoes iniciais
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //Configurações Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void validarDadosProduto(View view){

        //Valida se os campos foram preenchidos
        String nome = binding.editProdutoNome.getText().toString();
        String taxa = binding.editProdutoTaxa.getText().toString();
        String categoria = binding.editProdutoCategoria.getText().toString();


        if( !nome.isEmpty()){
            if( !taxa.isEmpty()){
                if( !categoria.isEmpty()){
                    Produto produto = new Produto();
                    produto.setIdUsuario(idUsuarioLogado);
                    produto.setNome(nome);
                    produto.setDescricao(categoria);
                    produto.setPreco(taxa);
                    produto.salvar();
                    
                    finish();
                    Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    exibirMensagem("Digite uma categoria");
                }
            }else{
                exibirMensagem("Digite uma taxa de entrega");
            }
        }else{
            exibirMensagem("Digite um nome para a empresa");
        }

    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }
}
