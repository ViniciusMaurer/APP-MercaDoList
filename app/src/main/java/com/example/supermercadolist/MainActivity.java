package com.example.supermercadolist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ScrollView scrollView;
    private FloatingActionButton btAdicionar;
    private TextView tvTotalCompra, tvTotalItens;
    Banco banco = new Banco();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        banco.abrirBanco(this);
        banco.abrirTabela(this);
        banco.fecharBanco();

        linearLayout = findViewById(R.id.linearLayout);
        scrollView = findViewById(R.id.scrollView);

        tvTotalCompra = findViewById(R.id.tvTotalCompra);
        tvTotalItens = findViewById(R.id.tvTotalItens);

        carregaInformacoes();

        btAdicionar = findViewById(R.id.btAdicionar);
        btAdicionar.setOnClickListener((v) -> {abrirTelaAdicionar();});
    }

    private void abrirTelaAdicionar() {
        Intent intent = new Intent(MainActivity.this, InsertInfo.class);
        startActivity(intent);
    }

    public void calculaTotalCompra(int id, boolean isChecked, double preco, String unidade, double quantidade){
        if(isChecked) {
            if(preco > 0){
                String textoTotalCompra = tvTotalCompra.getText().toString();
                textoTotalCompra = textoTotalCompra.replace(".", "");
                textoTotalCompra = textoTotalCompra.replace(",", ".");
                double precoTotalCompra = Double.parseDouble(textoTotalCompra);

                if(quantidade > 0 && (unidade.equals("dz") || unidade.equals("un") || unidade.equals("Caixa") || unidade.equals("Embalagem") || unidade.equals("Galão") || unidade.equals("Garrafa") || unidade.equals("Lata") || unidade.equals("Pacote"))){
                    precoTotalCompra += (preco * quantidade);
                }else{
                    precoTotalCompra += preco;
                }

                String textoComVirgula = formatarValor(precoTotalCompra);
                tvTotalCompra.setText(textoComVirgula);
                banco.setarEstadoCheckbox(this, id, 1);
            }
        }else{
            if(preco > 0){
                String textoTotalCompra = tvTotalCompra.getText().toString();
                textoTotalCompra = textoTotalCompra.replace(".", "");
                textoTotalCompra = textoTotalCompra.replace(",", ".");
                double precoTotalCompra = Double.parseDouble(textoTotalCompra);

                if(quantidade > 0 && (unidade.equals("dz") || unidade.equals("un") || unidade.equals("Caixa") || unidade.equals("Embalagem") || unidade.equals("Galão") || unidade.equals("Garrafa") || unidade.equals("Lata") || unidade.equals("Pacote"))){
                    precoTotalCompra -= (preco * quantidade);
                }else{
                    precoTotalCompra -= preco;
                }

                String textoComVirgula = formatarValor(precoTotalCompra);
                tvTotalCompra.setText(textoComVirgula);
                banco.setarEstadoCheckbox(this, id, 0);
            }
        }
    }

    public void inicializaTotalCompra(int id, boolean isChecked, double preco, String unidade, double quantidade){
        if(isChecked) {
            if(preco > 0){
                String textoTotalCompra = tvTotalCompra.getText().toString();
                textoTotalCompra = textoTotalCompra.replace(".", "");
                textoTotalCompra = textoTotalCompra.replace(",", ".");
                double precoTotalCompra = Double.parseDouble(textoTotalCompra);

                if (quantidade > 0 && (unidade.equals("dz") || unidade.equals("un") || unidade.equals("Caixa") || unidade.equals("Embalagem") || unidade.equals("Galão") || unidade.equals("Garrafa") || unidade.equals("Lata") || unidade.equals("Pacote"))) {
                    precoTotalCompra += (preco * quantidade);
                } else {
                    precoTotalCompra += preco;
                }

                String textoComVirgula = formatarValor(precoTotalCompra);
                tvTotalCompra.setText(textoComVirgula);
            }
        }
    }

    private void carregaInformacoes() {
        Cursor cursor = buscarDados();
        String aux = "";
        double precoTotalItens = 0.0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String descricao = cursor.getString(1);
                double preco = cursor.getDouble(2);
                double preco_total = cursor.getDouble(3);
                double quantidade = cursor.getDouble(4);
                String unidadeAux = cursor.getString(5);
                String unidade = unidadeAux != null ? unidadeAux : "";
                String categoria = cursor.getString(6);
                String categoriaAux = categoria != null ? categoria : "Categoria";
                int verifica_selecionado = Integer.parseInt(cursor.getString(7));
                boolean checkboxSelecionado = (verifica_selecionado == 1);

                if(quantidade > 0 && (unidade.equals("dz") || unidade.equals("un") || unidade.equals("Caixa") || unidade.equals("Embalagem") || unidade.equals("Galão") || unidade.equals("Garrafa") || unidade.equals("Lata") || unidade.equals("Pacote"))){
                    precoTotalItens += (preco * quantidade);
                }else{
                    precoTotalItens += preco;
                }

                // Coloca categoria
                if(!aux.equalsIgnoreCase(categoriaAux)){
                    TextView textView = new TextView(this);
                    textView.setText(categoriaAux);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView.setTextColor(Color.parseColor("#808080"));
                    linearLayout.addView(textView);
                }

                aux = categoriaAux;

                // Criar layout horizontal para o item
                LinearLayout horizontalLayout = new LinearLayout(this);
                horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 30);
                horizontalLayout.setLayoutParams(params);
                horizontalLayout.setGravity(Gravity.CENTER);

                CheckBox checkBox = new CheckBox(this);
                checkBox.setChecked(checkboxSelecionado);
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                horizontalLayout.addView(checkBox);

                inicializaTotalCompra(id, checkboxSelecionado, preco, unidade, quantidade);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        calculaTotalCompra(id, isChecked, preco, unidade, quantidade);
                    }
                });

                // Monta texto do botão
                StringBuilder stringBotao = new StringBuilder();
                stringBotao.append(descricao != null ? descricao : "");
                if(descricao != null && (preco != 0 || quantidade != 0 || !unidade.equals("")))
                    stringBotao.append("\n");
                DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("pt", "BR"));
                simbolos.setGroupingSeparator('.');
                simbolos.setDecimalSeparator(',');
                DecimalFormat formato = new DecimalFormat("#,##0.00", simbolos);
                String textoComVirgula = formato.format(preco);
                stringBotao.append(preco != 0 ? "R$ " + textoComVirgula : "");
                if(preco != 0 && (quantidade != 0 || !unidade.equals("")))
                    stringBotao.append("  -  ");
                stringBotao.append(quantidade != 0 ? String.valueOf(new DecimalFormat("#,##0.00").format(quantidade).replace('.', ',')) : "");
                if(quantidade != 0 && !unidade.equals(""))
                    stringBotao.append("  ");
                stringBotao.append(unidade);

                // Criar o botão para o item
                Button button = new Button(this);
                button.setText(stringBotao);
                button.setId(id);
                button.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                horizontalLayout.addView(button);

                button.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, UpdateInfo.class);
                    intent.putExtra("item_id", id);
                    intent.putExtra("item_descricao", descricao);
                    intent.putExtra("item_preco", preco);
                    intent.putExtra("item_preco_total", preco_total);
                    intent.putExtra("item_quantidade", quantidade);
                    intent.putExtra("item_unidade", unidade);
                    intent.putExtra("item_categoria", categoria);
                    startActivity(intent);
                });

                ImageButton imageButton = new ImageButton(this);
                imageButton.setImageResource(android.R.drawable.ic_menu_delete);
                imageButton.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                imageButton.setBackground(null);
                horizontalLayout.addView(imageButton);

                imageButton.setOnClickListener((v) -> {
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Atenção")
                            .setMessage("Tem certeza que deseja excluir esse item?")
                            .setPositiveButton("OK", (dialog, which) -> {
                                if(banco.excluirInformacao(this, id)){
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                                else{
                                    CaixaMensagem.mostrar("Erro ao atualizar dados!", this);
                                }
                            })
                            .setNegativeButton("Cancelar", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                });

                linearLayout.addView(horizontalLayout);

            } while (cursor.moveToNext());

            String textoComVirgula = formatarValor(precoTotalItens);
            tvTotalItens.setText(textoComVirgula);

            cursor.close();
        }else{
            //CaixaMensagem.mostrar("Nenhuma informação encontrada!", this);
        }
        banco.fecharBanco();
    }

    private Cursor buscarDados() {
        banco.abrirBanco(this);
        Cursor cursor = null;

        cursor = banco.db.query(
                "mercado",
                new String[]{"id", "descricao", "preco", "preco_total", "quantidade", "unidade", "categoria", "verifica_selecionado"},
                null,
                null,
                null,
                null,
                "categoria",
                null
        );

        if(cursor.getCount() != 0){
            return cursor;
        }
        else{
            return null;
        }
    }

    private String formatarValor(double valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("pt", "BR"));
        simbolos.setGroupingSeparator('.');
        simbolos.setDecimalSeparator(',');
        DecimalFormat formato = new DecimalFormat("#,##0.00", simbolos);
        return formato.format(valor);
    }
}