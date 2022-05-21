package com.example.creditproject.services;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.example.creditproject.entities.Imagetest;
import com.example.creditproject.entities.User;
import com.example.creditproject.models.OCR;
import com.example.creditproject.repositories.ImageRespository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class ImageParseService {

    @Autowired
    private ImageRespository imageRespository;

    public String parseImage(String filePath, String lang) {

        File imageFile = new File(filePath);
        ITesseract instance = new Tesseract();

        try {
            BufferedImage in = ImageIO.read(imageFile);

            BufferedImage newImage = new BufferedImage(
                    in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = newImage.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();

            instance.setDatapath("./tessdata");
            instance.setLanguage(lang);
            String result = instance.doOCR(newImage);
            return result;

        } catch (TesseractException | IOException e) {
            System.err.println(e.getMessage());
            return "Error while reading image";
        }
    }

    public Imagetest save(Imagetest image) {
        imageRespository.save(image);
        return image;
    }

    public String saveImageOCR(Imagetest image, OCR ocr) {
        String result = parseImage(ocr.getImage(), ocr.getDestinationLanguage());
        if (result == null || result.equals("Error while reading image")) {
            return "Error";
        } else {
            image.setUrl(ocr.getImage());
            image.setResult(result.trim());
            image.setUserId(ocr.getId_user());
            save(image);
            return result;
        }
    }

    public Imagetest findByUrl(String url) {
        return imageRespository.findByUrl(url);
    }

    public List<Imagetest> findAll() {
        return imageRespository.findAll();
    }

    public void delete(Imagetest image) {
        imageRespository.delete(image);
    }

    public Imagetest findById(Long id) {
        return imageRespository.findById(id).orElse(null);
    }

    public String ocrCINVerso(User user, String result) {

        String cin = "";
        String nom = "";
        String prenom = "";
        String adresse = "";

        String[] lines = result.split("\n");
        String line1 = "";
        String line2 = "";
        String line3 = "";

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("<")) {
                line1 = lines[i];
                line2 = lines[i + 2];
                break;
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Adr")) {
                line3 += lines[i];
                break;
            }
        }

        System.out.println("***" + line1);
        System.out.println("***" + line2);
        System.out.println("***" + line3);

        String[] line1Split = line1.split("<");
        cin = line1Split[1].substring(1);

        String[] line2Split = line2.split("<<");
        nom = line2Split[0];
        prenom = line2Split[1];

        if (line3.isEmpty()) {
            adresse = "";
        } else {
            adresse = line3.substring(8);
        }

        System.out.println("***" + cin);
        System.out.println("***" + nom);
        System.out.println("***" + prenom);
        System.out.println("***" + adresse);

        User user2 = new User();
        user2.setCin(cin);
        user2.setFirstName(prenom);
        user2.setLastName(nom);
        user2.setAddress(adresse);

        if (user.getFirstName().equals(user2.getFirstName())
                && user.getLastName().equals(user2.getLastName()) && user.getCin().equals(user2.getCin())
                && user.getAddress().equals(user2.getAddress())) {
            return "OK \n\n" + user2.toString();
        } else {
            return "KO \n\n" + user2.toString();
        }
    }

    public String ocrCINRecto(User user, String result) {

        String dateNaiss = "";
        String lieuNaiss = "";

        String[] lines = result.split("\n");
        String line1 = "";
        String line2 = "";

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("à ")) {
                line1 = lines[i];
                break;
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Néle")) {
                line2 = lines[i];
                break;
            }
        }

        System.out.println("***" + line1);
        System.out.println("***" + line2);

        String[] line1Split = line1.split("à ");
        lieuNaiss = line1Split[1];

        String[] line2Split = line2.split("Néle ");
        String[] line2Split2 = line2Split[1].split(" ");
        dateNaiss = line2Split2[0].replace(".", "/");

        System.out.println("***" + dateNaiss);
        System.out.println("***" + lieuNaiss);

        User user2 = new User();
        user2.setDateNai(dateNaiss);
        //user2.setLieuNai(lieuNaiss);

        if (user.getDateNai().equals(user2.getDateNai())){// && user.getLieuNai().equals(user2.getLieuNai())) {
            return "OK \n\n" + user2.toString();
        } else {
            return "KO \n\n" + user2.toString();
        }
    }

    public String ocrRIB(User user, String result) {
        String nom = "";
        String prenom = "";
        String rib = "";
        String banque = "";

        String line1 = "";
        String line2 = "";
        String line3 = "";

        String[] lines = result.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Intitulé du compte")) {
                line1 = lines[i];
            } else if (lines[i].contains("RIB")) {
                line2 = lines[i + 1];
            } else if (lines[i].contains("BANK")) {
                line3 = lines[i];
            }
        }
        String[] line1Split = line1.split(":");
        nom = line1Split[1].trim().split(" ")[0].trim();
        prenom = line1Split[1].trim().split(" ")[1].trim();
        rib = line2.replaceAll("[^0-9]+", " ");

        System.out.println("***" + line3);

        String[] line2Split = line3.split(" ");

        for (int i = 0; i < line2Split.length; i++) {
            if (line2Split[i].contains("BANK")) {
                banque += line2Split[i - 1] + " " + line2Split[i].replace(",", " ");
                break;
            }
        }

        System.out.println("\n nom : " + nom + "\n prenom : " + prenom + "\n rib : " + rib + "\n banque : " + banque);
        return result + "\n\n nom : " + nom + "\n prenom : " + prenom + "\n rib : " + rib + "\n banque : " + banque;
    }

}
