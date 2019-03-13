package com.nathan.dev.pdfapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.Pair;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

public class TemplatePDF {


    private String tagLogErr = "openDocument";
    private Context context;
    private File pdfFile;
    private Document document;
    private Paragraph paragraph;
    private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font fSubTitle = new Font(Font.FontFamily.TIMES_ROMAN, 14);
    private Font fText = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK);
    private Font fTotalValue = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK);

    public TemplatePDF(Context context) {
        this.context = context;
    }

    public void openDocument() {

        try {
            createFile();
            document = new Document(PageSize.A4);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
        } catch (Exception e) {
            Log.e(tagLogErr, e.toString());
        }

    }


    private void createFile() {

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());

        if (!folder.exists())
            folder.mkdirs();


        if (folder.exists())
            pdfFile = new File(folder, "OrderPDF.pdf");

    }

    public void closeDocument() {
        document.close();
    }

    public void addMetaData(String title, String subject, String author) {
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void addTitles(String titulo, String subtitle, Pair<String, String> parceiro, Pair<String, String> contato, Pair<String, String> tipneg, String secondTitle) {

        try {

            paragraph = new Paragraph();
            addChield(new Paragraph(titulo.toUpperCase(), fTitle));
            addChield(new Paragraph(subtitle.toUpperCase(), fTitle));
            addChield(new Paragraph("  ", fTitle));

            addChield(new Paragraph(parceiro.first, fSubTitle));
            addChield(new Paragraph(parceiro.second, fHighText));

            addChield(new Paragraph(contato.first, fSubTitle));
            addChield(new Paragraph(contato.second, fHighText));

            addChield(new Paragraph(tipneg.first, fSubTitle));

            addChield(new Paragraph(contato.second, fHighText));

            addChield(new Paragraph("  ", fTitle));


            addChield(new Paragraph(secondTitle.toUpperCase(), fTitle));


            paragraph.setSpacingAfter(30);
            document.add(paragraph);

        } catch (Exception e) {
            Log.e(tagLogErr, e.toString());
        }

    }


    public void addElement(String text) {
        try {
            paragraph = new Paragraph(text, fTitle);
            paragraph.setSpacingAfter(35);
            paragraph.setSpacingBefore(35);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

        } catch (Exception e) {
            Log.e(tagLogErr, e.toString());
        }


    }

    public void addParagraph(String text) {

        try {
            paragraph = new Paragraph(text, fText);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

        } catch (Exception e) {
            Log.e(tagLogErr, e.toString());
        }

    }

    private void addChield(Paragraph childParagraph) {
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }


    public void viewPDF(Activity activity) {


        if (pdfFile != null && pdfFile.exists()) {


            try {

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "TemplatePDF.pdf");

                if (file.exists()) {
                    Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);


                    Intent intent = new Intent();
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                    intent.setAction(Intent.ACTION_SEND);
                    activity.startActivity(Intent.createChooser(intent, null));


//                        intent.setAction(Intent.ACTION_VIEW);
//                        activity.startActivity(intent);


                }


//                Uri uri = Uri.fromFile(outputFile);

//                Intent share = new Intent();
//                share.setAction(Intent.ACTION_SEND);
//                share.setType("application/pdf");
//                share.putExtra(Intent.EXTRA_STREAM, uri);
//                share.setPackage("com.whatsapp");
//
//                activity.startActivity(share);

//                Uri uri= FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID , pdfFile);
//

//                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.adobe.reader")));
            } catch (Exception e) {
                e.printStackTrace();
//                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.adobe.reader")));
                System.out.println("falha ao carregar");
            }

        }


//        Intent intent = new Intent(context, ViewPDFActivity.class);
//        intent.putExtra("path", pdfFile.getAbsolutePath());
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }

}
