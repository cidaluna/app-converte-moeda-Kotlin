package com.example.monetario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var result: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            // buscar todos os componenetes para poder manipular eles

            result = findViewById<TextView>(R.id.txt_result)

            //pegar referencia do botao
            val buttonConverter = findViewById<Button>(R.id.btn_converter)

            // quando clicar no botao apresenta o texto
            buttonConverter.setOnClickListener {
                converter()
            }

        }

        private fun converter(){
            // verificar quem foi selecionado
            val selectedCurrency = findViewById<RadioGroup>(R.id.radio_group)

            val checked = selectedCurrency.checkedRadioButtonId

            val currency = when(checked){
                R.id.radio_usd ->{
                    "USD"
                }
                R.id.radio_eur ->{
                    "EUR"
                }else -> {
                    "CLP"
                }
            }

            val editField = findViewById<EditText>(R.id.edit_field)

            val value = editField.text.toString()

            // se nao for digitado nada no campo, eh aplicado um return que nao envia nada
            if(value.isEmpty())
                return

            result.text = value
            result.visibility = View.VISIBLE

            // abrir um processo paralelo para chamada a Internet e consultar o valor da moeda
            // este processo paralelo precisa delegar a resposta para o desenho da tela
            Thread {
                val url = URL("https://free.currconv.com/api/v7/convert?q=${currency}_BRL&compact=ultra&apiKey=c5f3b7fc79c839ce6e96")

                // abrir conexao de rede
                val conn = url.openConnection() as HttpURLConnection

                try{
                    // pegar os bits e transformar em dados, terei a chave e o valor {"asdasd":1234123}
                    val data = conn.inputStream.bufferedReader().readText()

                    val obj = JSONObject(data)

                    runOnUiThread{
                        val res = obj.getDouble("${currency}_BRL")

                        result.text = "R$ ${"%.4f".format(value.toDouble() * res)} "
                        result.visibility = View.VISIBLE
                    }
                }finally{
                    conn.disconnect()
                }
            }.start()

        }



    }
