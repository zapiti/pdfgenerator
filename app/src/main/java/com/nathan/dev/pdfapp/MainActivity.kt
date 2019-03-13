package com.nathan.dev.pdfapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Pair
import android.view.View
import com.example.dev.pdfapp.R

class MainActivity : AppCompatActivity() {


    lateinit var templatePDF: TemplatePDF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }


    fun pdfView(view: View) {


        requestPermission()

        templatePDF = TemplatePDF(applicationContext)
        templatePDF.openDocument()

        val text = "Parceiro"

        val text2 = "CONTATO"

        val text3 = "Pagamento"

        val title = "Sankhya Order"

        val subtitle = "Comprovante de venda"

        val prodTitle = "1x PRODUTO XPTO AZUL "

        val prodDescri = "R$ 50,00"

        val complement = "SANKHYA GESTAO DE NEGOCIOS"

        val value = getValueTitleSize(text.toUpperCase(), 70)

        val value2 = getValueTitleSize(text2.toUpperCase(), 70)

        val value3 = getValueTitleSize(text3.toUpperCase(), 70)

        val titlesecundary = "Itens do pedido"

        val total = "TOTAL  R$500,00"

        val prodlist = ArrayList<Pair<String,String>>()

        for (i in 0..20) {
            prodlist.add(Pair(prodTitle,prodDescri))
        }

        creatHeaderPDF(title, subtitle, value, complement, value2, value3, titlesecundary)
        creatBottomContent(prodlist, total)

        templatePDF.closeDocument()


        if (isExternalStorageWritable()) {
            templatePDF.viewPDF(this)
        } else {
            println("nao escreveu")
        }


    }

    private fun creatBottomContent(prodlist: ArrayList<Pair<String, String>>, total: String) {
        for (itens in prodlist) {
            val prodline = getValueProdSize(itens.first, itens.second, 140)
            templatePDF.addParagraph(prodline)
        }

        templatePDF.addElement(total)
    }

    private fun creatHeaderPDF(title: String, subtitle: String, value: String, complement: String, value2: String, value3: String, titlesecundary: String) {
        templatePDF.addMetaData(title, subtitle, title)
        templatePDF.addTitles(title, subtitle, Pair(value, complement), Pair(value2, complement), Pair(value3, complement), titlesecundary)
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
            )
        }

        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
            )
        }
    }

    private fun getValueTitleSize(text: String, size: Int): String {

        var sizeValue = text.count()


        if (sizeValue > size) {
            sizeValue = 0
        }

        var value = "";
        for (i in 0..((size - sizeValue) / 2)) {

            value += "_"
        }


        return "$value$text$value"
    }

    private fun getValueProdSize(title: String, descripton: String, size: Int): String {

        var sizeValue = (title + descripton).count()


        if (sizeValue > size) {
            sizeValue = 0
        }

        var value = "";
        for (i in 0..((size - sizeValue) / 2)) {

            value += "-"
        }


        return "$title$value$descripton"
    }


    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

}
