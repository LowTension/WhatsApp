package br.com.whatsappandroid.cursoandroid.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes){

        // VERIFICAR VERSÃO DO SDK
        if(Build.VERSION.SDK_INT >= 23){

            List<String> listaPermissoes = new ArrayList<String>();

            /*
            * PERCORRE AS PERMISSÕES NECESSÁRIAS, VERIFICANDO
            * UMA A UMA SE JÁ TEM A PERMISSÃO LIBERADA
            * */
            for(String permissao: permissoes){

                Boolean validaPermissao =
                        ContextCompat.checkSelfPermission(activity, permissao) ==
                        PackageManager.PERMISSION_GRANTED;

                // SE NÃO TIVER PERMISSÃO, SEGUE ENTÃO PARA A LISTA DE PERMISSÕES
                if(!validaPermissao){
                    listaPermissoes.add(permissao);
                }
            }

            // CASO A LISTA ESTEJA VAZIA, NÃO É NECESSÁRIO SOLICITAR PERMISSÃO
            if(listaPermissoes.isEmpty()){
                return true;
            }

            // SOLICITAR PERMISSÃO
            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

        }

        return true;
    }
}
