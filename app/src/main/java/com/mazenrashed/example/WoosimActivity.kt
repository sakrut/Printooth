package com.mazenrashed.example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.converter.Utf8Converter
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.PrintingCallback
import kotlinx.android.synthetic.main.activity_main.*

class WoosimActivity : AppCompatActivity() {

    private val printing = Printooth.printer(WoosimPrinter())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_woosim)
        initViews()
        initListeners()
    }

    private fun initViews() {
        btnPiarUnpair.text = if (Printooth.hasPairedPrinter()) "Un-pair ${Printooth.getPairedPrinter()?.name}" else "Pair with printer"
    }

    private fun initListeners() {
        btnPrint.setOnClickListener {
            if (!Printooth.hasPairedPrinter()) startActivityForResult(Intent(this,
                    ScanningActivity::class.java),
                    ScanningActivity.SCANNING_FOR_PRINTER)
            else printSomePrintable()
        }

        btnPrintImages.setOnClickListener {
            if (!Printooth.hasPairedPrinter()) startActivityForResult(Intent(this,
                    ScanningActivity::class.java),
                    ScanningActivity.SCANNING_FOR_PRINTER)
            else printSomeImages()
        }

        btnPiarUnpair.setOnClickListener {
            if (Printooth.hasPairedPrinter()) Printooth.removeCurrentPrinter()
            else startActivityForResult(Intent(this, ScanningActivity::class.java),
                    ScanningActivity.SCANNING_FOR_PRINTER)
            initViews()
        }

        printing.printingCallback = object : PrintingCallback {
            override fun connectingWithPrinter() {
                Toast.makeText(this@WoosimActivity, "Connecting with printer", Toast.LENGTH_SHORT).show()
            }

            override fun printingOrderSentSuccessfully() {
                Toast.makeText(this@WoosimActivity, "Order sent to printer", Toast.LENGTH_SHORT).show()
            }

            override fun connectionFailed(error: String) {
                Toast.makeText(this@WoosimActivity, "Failed to connect printer", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: String) {
                Toast.makeText(this@WoosimActivity, error, Toast.LENGTH_SHORT).show()
            }

            override fun onMessage(message: String) {
                Toast.makeText(this@WoosimActivity, "Message: $message", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun printSomePrintable() {
        val printables = getSomePrintables()
        printing.print(printables)
    }

    private fun printSomeImages() {
        val printables = arrayListOf<Printable>(
                ImagePrintable.Builder(R.drawable.image1, resources)
                        .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
                        .build(),
                ImagePrintable.Builder(R.drawable.image2, resources).build(),
                ImagePrintable.Builder(R.drawable.image3, resources).build()
        )

        printing.print(printables)
    }

    private fun getSomePrintables() = ArrayList<Printable>().apply {

        add(TextPrintable.Builder()
                .setText("aą zż xź cć eę lł oó sś")
                .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                //.setUnderlined(DefaultPrinter.UNDERLINED_MODE_ON)
                .setCharacterCode(DefaultPrinter.CHARCODE_PC866)
                .setNewLinesAfter(1)
                .setCustomConverter(Utf8Converter())
                .build())


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK)
            printSomePrintable()
        initViews()
    }
}