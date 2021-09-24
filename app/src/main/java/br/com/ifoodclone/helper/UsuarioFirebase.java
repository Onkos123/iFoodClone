package br.com.ifoodclone.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {

    public static String getIdUsuario(){
        FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
        return auth.getCurrentUser().getUid();
    }

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
        return auth.getCurrentUser();
    }

    public static boolean atualizarTipoUsuario(String s){

        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(s)
                    .build();
            user.updateProfile(profile);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
