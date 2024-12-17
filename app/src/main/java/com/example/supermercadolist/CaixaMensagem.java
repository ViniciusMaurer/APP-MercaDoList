package com.example.supermercadolist;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.app.Activity;
import android.content.DialogInterface;

public class CaixaMensagem {

    public static void mostrar (String txt, Activity act){
        MaterialAlertDialogBuilder adb = new MaterialAlertDialogBuilder(act);
        adb.setMessage(txt)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ação ao clicar no botão OK
                    }
                })
                .show();
    }
}