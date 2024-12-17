package com.example.supermercadolist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class InsertInfo extends AppCompatActivity {

    private EditText etDescricao, etQuantidade, etPreco, etPrecoTotal;
    private Spinner spUnidade, spCategoria;
    private Button btSalvar;
    private Toolbar toolbar;

    Banco banco = new Banco();

    String[] unidades = {"Selecione alguma unidade...", "un", "dz", "ml", "L", "kg", "g", "Caixa", "Embalagem", "Galão", "Garrafa", "Lata", "Pacote"};

    String[] categorias = {
            "Selecione alguma categoria...",
            "Bebidas",
            "Carnes",
            "Comidas Prontas e Congeladas",
            "Farmácia",
            "Frios, Leites e Derivados",
            "Frutas, ovos e verduras",
            "Higiene Pessoal",
            "Importados",
            "Limpeza",
            "Mercearia",
            "Padaria e Sobremesas",
            "Saúde e Beleza",
            "Sem Categoria",
            "Temperos"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_info);

        toolbar = findViewById(R.id.toolbarInsert);
        setSupportActionBar(toolbar);

        // Habilitando o botão de voltar (seta)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Adicionar Item");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponentes();

        preencherSpinners();

        spUnidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String unidadeSelecionada = (String) spUnidade.getItemAtPosition(position);
                unidadeAlterada(unidadeSelecionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Caso nenhum item seja selecionado
            }
        });

        etQuantidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                unidadeAlterada(spUnidade.getSelectedItem().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        etPreco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                unidadeAlterada(spUnidade.getSelectedItem().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        btSalvar.setOnClickListener((v) -> {salvarInformacoes();});
    }

    private void unidadeAlterada(String unidade){
        boolean multiplicaPrecoQuantidade = false;
        if(unidade.equals("dz") || unidade.equals("un") || unidade.equals("Caixa") || unidade.equals("Embalagem") || unidade.equals("Galão") || unidade.equals("Garrafa") || unidade.equals("Lata") || unidade.equals("Pacote")){
            multiplicaPrecoQuantidade = true;
        }

        try {
            double preco = !etPreco.getText().toString().isEmpty() ? Double.parseDouble(etPreco.getText().toString()) : 0;
            double precoTotal;

            if (multiplicaPrecoQuantidade && !etQuantidade.getText().toString().isEmpty()) {
                double quantidade = Double.parseDouble(etQuantidade.getText().toString());
                precoTotal = preco * quantidade;
            } else {
                precoTotal = preco;
            }

            if(precoTotal != 0){
                etPrecoTotal.setText(String.valueOf(precoTotal));
            }else{
                etPrecoTotal.setText("");
            }
        } catch (NumberFormatException e) {
            etPrecoTotal.setText("0,00");
        }
    }

    private void salvarInformacoes() {
        String descricao = etDescricao.getText().toString();
        String entradaQtd = etQuantidade.getText().toString();
        double quantidade = !entradaQtd.equals("") ? Double.parseDouble(entradaQtd) : 0;
        String entradaPreco = etPreco.getText().toString();
        double preco = !entradaPreco.equals("") ? Double.parseDouble(entradaPreco) : 0;
        String entradaPrecoTotal = etPrecoTotal.getText().toString();
        double preco_total = !entradaPrecoTotal.equals("") ? Double.parseDouble(entradaPrecoTotal) : 0;
        String unidade = spUnidade.getSelectedItem().toString();
        String categoria = spCategoria.getSelectedItem().toString();

        if(banco.inserirInformacoes(this, descricao, quantidade, preco, preco_total, unidade, categoria))
            carregaTelaInicial();
        else
            CaixaMensagem.mostrar("Erro ao inserir dados!", this);
    }

    private void carregaTelaInicial() {
        Intent intent = new Intent(InsertInfo.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void preencherSpinners() {
        ArrayAdapter<String> adapterUnidade = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unidades);
        adapterUnidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUnidade.setAdapter(adapterUnidade);

        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapterCategoria);
    }

    private void inicializarComponentes() {
        etDescricao = findViewById(R.id.etDescricaoInsert);
        etQuantidade = findViewById(R.id.etQuantidadeInsert);
        etPreco = findViewById(R.id.etPrecoInsert);
        etPrecoTotal = findViewById(R.id.etPrecoTotalInsert);
        spUnidade = findViewById(R.id.spUnidadeInsert);
        spCategoria = findViewById(R.id.spCategoriaInsert);
        btSalvar = findViewById(R.id.btSalvarInsert);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}