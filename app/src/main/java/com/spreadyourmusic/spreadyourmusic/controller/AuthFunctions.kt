package com.spreadyourmusic.spreadyourmusic.controller

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.spreadyourmusic.spreadyourmusic.models.User

/**
 * Created by Abel
 * On 27/02/18.
 */

// Comprueba si el usuario y contraseña existen, si lo hacen, almacena en elmacenamiento persistente
// ambos datos y devuelve true
// En caso contrario devuelve false
fun doLogin(user: User): Boolean {
    // TODO: Falta implementar
    // Si el login es correcto se sobreescribe session
    // Y preferences y demas
    Thread.sleep(3000)
    return true
}

fun isLogin(): Boolean{
    // TODO: Falta implementar
    return false
}

fun doGoogleLogin(user: GoogleSignInAccount): Boolean {
    // TODO: Falta implementar
    // Si el login es correcto se sobreescribe session
    // Y preferences y demas
    Thread.sleep(3000)
    return false
}