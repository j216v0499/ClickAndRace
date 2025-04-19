package com.dearos.clickandrace.database
//
//import io..jan.supabase.SupabaseClient
//import io..jan.supabase.createSupabaseClient
//import io..jan.supabase.gotrue.GoTrue
//import io.jan.supabase.gotrue.gotrue
//
//object Supabase {
//    lateinit var client: SupabaseClient
//    val auth: GoTrue get() = client.gotrue
//
//    fun init() {
//        client = createSupabaseClient(
//            supabaseUrl = "https://YOUR_PROJECT_ID.supabase.co",
//            supabaseKey = "YOUR_ANON_KEY"
//        ) {
//            install(io.github.jan.supabase.gotrue.gotrue)
//        }
//    }
//}
