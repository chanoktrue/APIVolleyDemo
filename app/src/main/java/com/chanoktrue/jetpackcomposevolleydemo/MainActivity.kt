package com.chanoktrue.jetpackcomposevolleydemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chanoktrue.jetpackcomposevolleydemo.models.UserData
import com.chanoktrue.jetpackcomposevolleydemo.models.UserDataItem
import com.chanoktrue.jetpackcomposevolleydemo.ui.theme.JetpackComposeVolleyDemoTheme
import com.google.gson.GsonBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeVolleyDemoTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {

    val context = LocalContext.current

    val baseUrl = "https://api.github.com/users"
    val userData = UserData()
    val datas = remember{ mutableStateOf<UserData>(UserData())}
    val stringRequest = StringRequest(
        baseUrl,
        Response.Listener {
            val gsonBuilder = GsonBuilder()
            val gson = gsonBuilder.create()
            gson.fromJson(it, Array<UserDataItem>::class.java)?.forEach {
                userData.add(it)
            }
            datas.value = userData
           Log.e("API","Complete: $it")
        },
        Response.ErrorListener {
            Log.e("API", "Error: ${it.toString()}")
        }.apply {
            LazyColumn(){
                items(datas.value) { data ->
                    OneRow(data = data, context)
                }
            }
        }
    )


    Volley.newRequestQueue(context).add(stringRequest)

}

@Composable
fun OneRow(data: UserDataItem, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(CornerSize(10.dp)),
        elevation = 2.dp
    ) {
        Row(

            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(data.avatar_url),
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .size(150.dp)
                    .clip(
                        RoundedCornerShape(CornerSize(10.dp))
                    )
            )
            
            Text(
                text = data.login,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .clickable {
                        Toast
                            .makeText(context, "You clicked on" + data.login, Toast.LENGTH_LONG)
                            .show()
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeVolleyDemoTheme {
        Greeting()
    }
}