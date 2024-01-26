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
import androidx.compose.material3.Slider
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
import com.example.tipcalculater.util.calculateTip
import com.example.tipcalculater.util.calculateTotalPerPerson

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
        .padding(15.dp)
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
    Column(modifier = Modifier.padding(all=12.dp)) {
        BillForm { billAmt->
            Log.d("AMT" , "MainContent:${billAmt.toInt()*100}")
        }

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

                val  sliderPositionState = remember{
                    mutableStateOf(value = 0f)
                }
                val tipPercentage =(sliderPositionState.value*100).toInt()
                val splitByState= remember {
                    mutableStateOf(1)
                }
                val  range=IntRange(start=1, endInclusive = 100)

                val tipAmountState= remember {
                    mutableStateOf(value = 0.0)
                }
                val totalPerPersonState= remember {
                    mutableStateOf(value = 0.0)
                }

                TopHeader(totalPerPerson=totalPerPersonState.value)

                Surface(modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth(),
                    shape = RoundedCornerShape(corner = CornerSize(8.dp)),
                    border= BorderStroke(width = 1.dp, color = Color.LightGray) ) {

                    Column(
                        modifier = Modifier.padding(6.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        InputField(
                            valueState = totalBillState,
                            labelId = "Enter Bill",
                            enabled = true,
                            isSingleLine = true,
                            onAction = KeyboardActions {
                                if (!validState) return@KeyboardActions
                                onValChange(totalBillState.value.trim())

                                keyboardController?.hide()
                            })
                        //  if (validState){
                        Row(
                            modifier = Modifier.padding(3.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Split",
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.width(120.dp))
                            Row(
                                modifier = Modifier.padding(horizontal = 3.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                RoundIconbtn(
                                    imageVector = Icons.Default.Remove,
                                    onClick = {
                                        splitByState.value =
                                            if (splitByState.value > 1) splitByState.value - 1
                                            else 1

                                        totalPerPersonState.value=
                                            calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
                                                splitBy = splitByState.value,
                                                tipPercentage=tipPercentage)
                                    })
                                Text(
                                    text = "${splitByState.value}",
                                    modifier = Modifier
                                        .align(alignment = Alignment.CenterVertically)
                                        .padding(start = 9.dp, end = 9.dp)
                                )
                                RoundIconbtn(
                                    imageVector = Icons.Default.Add,
                                    onClick = {
                                        if (splitByState.value < range.last) {
                                            splitByState.value = splitByState.value + 1
                                            totalPerPersonState.value=
                                                calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
                                                    splitBy = splitByState.value,
                                                    tipPercentage=tipPercentage)
                                        }
                                    })
                            }
                        }
                        //Tip Row
                        Row(
                            modifier = Modifier
                                .padding(
                                    horizontal = 3.dp,
                                    vertical = 12.dp
                                )
                        ) {
                            Text(
                                text = "Tip",
                                modifier = Modifier.align(alignment = Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.width(200.dp))
                            Text(
                                text = "$ ${tipAmountState.value}",
                                modifier = Modifier.align(alignment = Alignment.CenterVertically)
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "$tipPercentage %")

                            //Slider
                            Slider(value = sliderPositionState.value,
                                onValueChange = { newVal ->
                                    sliderPositionState.value = newVal
                                    tipAmountState.value =
                                        calculateTip(totalBill =totalBillState.value.toDouble(),
                                            tipPercentage=tipPercentage)

                                    totalPerPersonState.value=
                                        calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
                                             splitBy = splitByState.value,
                                            tipPercentage=tipPercentage)
                                },
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                                steps = 5,
                                onValueChangeFinished = {

                                })
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