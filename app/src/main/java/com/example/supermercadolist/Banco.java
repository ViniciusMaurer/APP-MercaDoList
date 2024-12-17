package com.example.supermercadolist;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class Banco {

    public SQLiteDatabase db;

    public void abrirBanco(Activity act){
        try{
            db = act.openOrCreateDatabase("mercado", Context.MODE_PRIVATE, null);
            //CaixaMensagem.mostrar("Banco de dados acessado com sucesso!", act);
        }
        catch (Exception ex){
            CaixaMensagem.mostrar("Erro ao abrir ou criar banco de dados!", act);
        }
    }

    public void fecharBanco(){
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void abrirTabela(Activity act){
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS mercado ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "descricao VARCHAR(100), "
                    + "quantidade DECIMAL(10, 2), "
                    + "unidade VARCHAR(50), "
                    + "preco DECIMAL(10, 2), "
                    + "preco_total DECIMAL(10, 2), "
                    + "categoria VARCHAR(100), "
                    + "verifica_selecionado INTEGER DEFAULT 0"
                    + ");");
            //CaixaMensagem.mostrar("Tabela criada com sucesso!", act);
        }
        catch(Exception ex){
            CaixaMensagem.mostrar("Erro ao criar a tabela!", act);
        }
    }

    public boolean inserirInformacoes(Activity act, String descricao, double quantidade, double preco, double preco_total, String unidade, String categoria){
        abrirBanco(act);

        try{
            String sql = "INSERT INTO mercado (descricao, quantidade, unidade, preco, preco_total, categoria) VALUES (?, ?, ?, ?, ?, ?)";

            SQLiteStatement statement = db.compileStatement(sql);

            if (descricao.isEmpty())
                statement.bindNull(1);
            else
                statement.bindString(1, descricao);

            if (quantidade == 0)
                statement.bindNull(2);
            else
                statement.bindDouble(2, quantidade);

            if (unidade.equalsIgnoreCase("Selecione alguma unidade..."))
                statement.bindNull(3);
            else
                statement.bindString(3, unidade);

            if (preco == 0)
                statement.bindNull(4);
            else
                statement.bindDouble(4, preco);

            if (preco_total == 0)
                statement.bindNull(5);
            else
                statement.bindDouble(5, preco_total);

            if (categoria.equalsIgnoreCase("Selecione alguma categoria..."))
                statement.bindNull(6);
            else
                statement.bindString(6, categoria);

            statement.executeInsert();

            return true;
        }
        catch (Exception ex){
            return false;
        }
        finally {
            fecharBanco();
        }
    }

    public boolean atualizarInformacoes(Activity act, int id, String descricao, double preco, double preco_total, double quantidade, String unidade, String categoria){
        abrirBanco(act);

        try {
            String sql = "UPDATE mercado SET descricao = ?, quantidade = ?, unidade = ?, preco = ?, preco_total = ?, categoria = ? WHERE id = ?";

            SQLiteStatement statement = db.compileStatement(sql);

            if (descricao.isEmpty())
                statement.bindNull(1);
            else
                statement.bindString(1, descricao);

            if (quantidade == 0)
                statement.bindNull(2);
            else
                statement.bindDouble(2, quantidade);

            if (unidade.equalsIgnoreCase("Selecione alguma unidade..."))
                statement.bindNull(3);
            else
                statement.bindString(3, unidade);

            if (preco == 0)
                statement.bindNull(4);
            else
                statement.bindDouble(4, preco);

            if (preco_total == 0)
                statement.bindNull(5);
            else
                statement.bindDouble(5, preco_total);

            if (categoria.equalsIgnoreCase("Selecione alguma categoria..."))
                statement.bindNull(6);
            else
                statement.bindString(6, categoria);

            statement.bindLong(7, id);

            statement.executeUpdateDelete();

            return true;
            //CaixaMensagem.mostrar("Dados atualizados com sucesso!", act);
        }
        catch (Exception ex){
            CaixaMensagem.mostrar("Erro ao atualizar dados!", act);
            return false;
        }
        finally {
            fecharBanco();
        }
    }

    public void setarEstadoCheckbox(Activity act, int id, int valorEstado){
        abrirBanco(act);

        try {
            String sql = "UPDATE mercado SET verifica_selecionado = ? WHERE id = ?";

            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindLong(1, valorEstado);
            statement.bindLong(2, id);

            statement.executeUpdateDelete();

            //CaixaMensagem.mostrar("Estado da checkbox atualizada com sucesso!", act);
        }
        catch (Exception ex){
            CaixaMensagem.mostrar("Erro ao atualizar dados!", act);
        }
        finally {
            fecharBanco();
        }
    }

    public boolean excluirInformacao(Activity act, int id){
        abrirBanco(act);

        try{
            String sql = "DELETE FROM mercado WHERE id = ?";

            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindLong(1, id);

            statement.executeUpdateDelete();

            return true;
        }
        catch (Exception ex){
            return false;
        }
        finally {
            fecharBanco();
        }
    }
}