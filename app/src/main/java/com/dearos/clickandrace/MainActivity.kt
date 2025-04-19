package com.dearos.clickandrace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import com.dearos.clickandrace.ui.theme.ClickAndRaceTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

//val supabase = createSupabaseClient(
//    supabaseUrl = "https://YOUR_PROJECT_ID.supabase.co",
//    supabaseKey = "YOUR_ANON_KEY"
//
//
//){
//    install(Auth)
//    install(Postgrest)
//    //intall others
//}


class MainActivity : ComponentActivity() {

    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                ClickAndRaceTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                     //   InstrumentsList()
                    }
                }
            }
        }
    }
}

@Composable
fun GoogleSignInButton(){
    val context = LocalContext.current;

    val olClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)


        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("")
            .setNonce("")
            .build()

    }

    Button(onClick = {}) {
        Text(text = "Sign in with Google")

    }

}