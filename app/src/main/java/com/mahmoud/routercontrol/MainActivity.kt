package com.mahmoud.routercontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class MainActivity: ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { MaterialTheme { RouterApp() } } }
}

@Composable fun RouterApp(){
 var user by remember { mutableStateOf("admin") }; var pass by remember { mutableStateOf("") }; var status by remember { mutableStateOf("جاهز للاتصال بـ 192.168.0.1") }; var result by remember { mutableStateOf("") }; var busy by remember { mutableStateOf(false) }
 val scope=rememberCoroutineScope()
 Surface(Modifier.fillMaxSize()) { Column(Modifier.padding(20.dp).verticalScroll(rememberScrollState()), verticalArrangement=Arrangement.spacedBy(12.dp)) {
   Text("Router Control", style=MaterialTheme.typography.headlineMedium); Text("NETGEAR DGN2200M • V1 Prototype")
   OutlinedTextField(user,{user=it},label={Text("Router username")}, modifier=Modifier.fillMaxWidth())
   OutlinedTextField(pass,{pass=it},label={Text("Router password")}, modifier=Modifier.fillMaxWidth())
   Button(enabled=!busy,onClick={busy=true; scope.launch { val r=readRouter(user,pass); status=r.first; result=r.second; busy=false }},modifier=Modifier.fillMaxWidth()){Text(if(busy)"جاري القراءة..." else "اتصال وقراءة Router Status")}
   Card(Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp)){Text("الحالة",style=MaterialTheme.typography.titleMedium); Text(status)}}
   if(result.isNotBlank()) Card(Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp)){Text("قراءة أولية",style=MaterialTheme.typography.titleMedium); Text(result)}}
   Text("V1: قراءة فقط في النسخة الأولية. لا يغيّر Wi‑Fi أو WAN أو إعدادات الراوتر.", style=MaterialTheme.typography.bodySmall)
 }}
}

suspend fun readRouter(user:String, pass:String):Pair<String,String> = withContext(Dispatchers.IO){
 try {
  val client=OkHttpClient.Builder().build(); val req=Request.Builder().url("http://192.168.0.1/start.htm").header("Authorization",Credentials.basic(user,pass)).get().build();
  client.newCall(req).execute().use { r ->
   if(!r.isSuccessful) return@withContext Pair("فشل الاتصال: HTTP ${r.code}","")
   val html=r.body?.string().orEmpty(); val doc=Jsoup.parse(html); val title=doc.title().ifBlank{"NETGEAR Router"}; val text=doc.text().take(1200)
   Pair("متصل بالراوتر ✓", "$title\n\n$text")
  }
 } catch(e:Exception){ Pair("تعذر الاتصال بالراوتر", e.message ?: "خطأ غير معروف") }
}
