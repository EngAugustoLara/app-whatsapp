package app.ctrlsoftware.com.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wesley Ramos on 07/06/2017.
 */

public class Permissao {

    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes){

        List<String> listaPermissoes = new ArrayList<String>();

        if (Build.VERSION.SDK_INT >= 23) {
            /*
               * Percorre as permissões passadas, verificando uma a uma
               * se já tem a permissão liberada
             */


            for (String permissao : permissoes){
                boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                //!validaPermissao = se não tem
                if ( !validaPermissao ) {
                    listaPermissoes.add(permissao);
                }
            }

            //Caso a lista esteja vazia não é necessario solicitar permissão
            if ( listaPermissoes.isEmpty() ) {
                return true;
            }

            //Converter tipo List para String
            String[] novasPermissoes = new String[ listaPermissoes.size() ];
            listaPermissoes.toArray( novasPermissoes );

            //Solicita Permissão
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

        }
        return true;
    }


}
