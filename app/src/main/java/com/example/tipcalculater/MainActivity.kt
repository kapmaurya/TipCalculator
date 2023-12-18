package com.example.tipcalculater

import RoundIconbtn
import android.graphics.drawable.Icon
import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.MovableContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculater.components.InputField
import com.example.tipcalculater.ui.theme.TipCalculaterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
               //TopHeader()
                MainContent()
            }

        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {

    TipCalculaterTheme {
        Surface(
            color = MaterialTheme.colorScheme.background) {
            content()
        }
    }

}

//@Preview
@Composable
fun TopHeader(totalPerPerson:Double=134.0){
    Surface (modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
        color= Color(color = 0Xffe9d7f7)
       // .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp )))
    ){
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total ="%.2f".format(totalPerPerson)
           Text(text = "Total Per Person",
               style = MaterialTheme.typography.headlineMedium)
            Text(text = "$$total",
                style =MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold)

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent()
{
BillForm(){billAmt->
    Log.d("AMT" , "MainContent:${billAmt.toInt()*100}")


}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier=Modifier,
             onValChange:(String)->Unit={})
            {
                val totalBillState= remember {
                    mutableStateOf(value = "")
                }
                val validState= remember (totalBillState.value){
                    totalBillState.value.trim().isNotEmpty()

                }
                val keyboardController=LocalSoftwareKeyboardController.current

                Surface(modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth(),
                    shape = RoundedCornerShape(corner = CornerSize(8.dp)),
                    border= BorderStroke(width = 1.dp, color = Color.LightGray) ) {

                    Column(modifier=Modifier.padding(6.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start) {
                        InputField(
                            valueState = totalBillState,
                            labelId = "Enter Bill",
                            enabled = true,
                            isSingleLine =true,
                            onAction = KeyboardActions{
                                if(!validState) return@KeyboardActions
                                onValChange(totalBillState.value.trim())

                                keyboardController?.hide()
                            })
                        if (validState){
                            Row(modifier=Modifier.padding(3.dp),
                               horizontalArrangement = Arrangement.Start ,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(text = "Split",
                                    modifier = Modifier)
                                Spacer(modifier = Modifier.width(120.dp))
                                Row(modifier=Modifier.padding(horizontal = 3.dp),
                                    horizontalArrangement = Arrangement.End) {
                                    RoundIconbtn(
                                        imageVector = Icons.Default.Remove,
                                        onClick = { })
                                    RoundIconbtn(

                                        imageVector = Icons.Default.Add,
                                        onClick = {  })

                                }
                            }
                        }
                        else{
                            Box(){

                            }
                        }
                    }
                }

}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipCalculaterTheme {
        MyApp {
            Text(text = "Hello Again")
        }
    }
}