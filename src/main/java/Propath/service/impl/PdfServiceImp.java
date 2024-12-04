package Propath.service.impl;

import Propath.service.PdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


@Service
public class PdfServiceImp implements PdfService {

    @Override
    public  String extractTextFromPdfUrl(String pdfUrl) {
        String extractedText = "";

        try {
            // Create a URL object
            URL url = new URL(pdfUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get input stream from the connection
            try (InputStream inputStream = connection.getInputStream();
                 PDDocument document = PDDocument.load(inputStream)) {

                // Extract text using PDFBox
                PDFTextStripper pdfStripper = new PDFTextStripper();
                extractedText = pdfStripper.getText(document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return extractedText;
    }
}
