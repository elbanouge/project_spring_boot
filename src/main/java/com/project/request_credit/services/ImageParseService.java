package com.project.request_credit.services;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import com.project.request_credit.entities.Scanner;
import com.project.request_credit.entities.User;
import com.project.request_credit.models.OCR;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class ImageParseService {

    @Autowired
    private ScannerService scannerService;

    @Autowired
    private AccountService accountService;

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

    public String saveImageOCR(Scanner image, OCR ocr) {
        String result = this.parseImage(ocr.getImage(), ocr.getDestinationLanguage());
        if (result == null || result.equals("Error while reading image")) {
            return "Error";
        } else {
            User user = accountService.findById(ocr.getId_user());

            if (user != null) {
                image.setUrl(ocr.getImage());
                image.setResult(result.trim());
                Scanner scanner = scannerService.saveScanner(image);

                if (scanner != null) {
                    user.getScanners().add(scanner);
                    accountService.createNewUser(user);
                    return result;
                } else {
                    return "Error";
                }
            } else {
                return "Error";
            }
        }
    }

    public String getPathImage(MultipartFile file) {
        String res = "";
        try {
            if (file.getOriginalFilename() != null) {
                Path downloadedFile = Paths.get("src\\main\\resources\\images\\" + file.getOriginalFilename());
                Files.deleteIfExists(downloadedFile);
                Files.copy(file.getInputStream(), downloadedFile);
                res = "./" + downloadedFile.toString().replace("\\", "/");
                return res;
            } else {
                return res;
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public String cleanImage(String path) {
        String resPath = path.split("/")[path.split("/").length - 1];
        String resDes = "./src/main/resources/images/destination";
        String resRes = "./src/main/resources/images/result";

        Mat destination = new Mat();
        Mat source = Imgcodecs.imread(path);

        for (int i = 0; i < 4; i++) {
            destination = new Mat(source.rows(), source.cols(), source.type());
            Imgproc.GaussianBlur(source, destination, new Size(0, 0), 10);
            Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);

            Imgcodecs.imwrite(resDes + resPath,
                    destination);
            source = destination;
        }

        Mat resultMat = new Mat();
        Imgproc.threshold(destination, resultMat, 55, 255, Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite(resRes + resPath, resultMat);
        return resRes + resPath;
    }

    public String ocrNewCINVerso(User user, String result) {

        String cin = "";
        String nom = "";
        String prenom = "";
        String adresse = "";
        String sexe = "";

        String[] lines = result.split("\n");
        String line1 = "";
        String line2 = "";
        String line3 = "";
        String line4 = "";

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

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Sexe") || lines[i].contains("exe")) {
                line4 += lines[i];
                break;
            }
        }

        for (int i = 0; i < line4.length(); i++) {
            if (line4.charAt(i) == 'M') {
                sexe += "Masculin";
                break;
            } else if (line4.charAt(i) == 'F') {
                sexe += "Feminin";
                break;
            }
        }

        System.out.println("***" + line1);
        System.out.println("***" + line2);
        System.out.println("***" + line3);
        System.out.println("*****line4*****" + line4);

        if (line1.length() > 0 && line2.length() > 0 && line3.length() > 0 && line4.length() > 0) {
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
        }

        System.out.println("***" + cin);
        System.out.println("***" + nom);
        System.out.println("***" + prenom);
        System.out.println("***" + adresse);
        System.out.println("***" + sexe);

        User user2 = new User();
        user2.setCin(cin);
        user2.setFirstName(prenom);
        user2.setLastName(nom);
        user2.setAddress(adresse);

        if (user.getFirstName().equals(user2.getFirstName())
                && user.getLastName().equals(user2.getLastName()) &&
                user.getCin().equals(user2.getCin())
                && user.getAddress().equals(user2.getAddress())) {
            return "OK \n\n" + user2.toString();
        } else {
            return "KO \n\n" + user2.toString();
        }
    }

    public String ocrNewCINRecto(User user, String result) throws ParseException {

        String dateNaiss = "";
        String lieuNaiss = "";

        String[] lines = result.split("\n");
        String line1 = "";
        String line2 = "";

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("à ")) {
                line1 = lines[i + 1];
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

        if (line1.length() > 0 && line2.length() > 0) {
            lieuNaiss = line1.replaceAll("[^A-Z]+", " ");

            String[] line2Split = line2.replaceAll("[^0-9]+", " ").split(" ");
            dateNaiss = line2Split[1] + "/" + line2Split[2] + "/" + line2Split[3];
        }

        System.out.println("***" + dateNaiss);
        System.out.println("***" + lieuNaiss);

        User user2 = new User();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        user2.setDate_naissance(dateformat.parse(dateNaiss));
        user2.setLieu_naissance(lieuNaiss);

        int res = user.getDate_naissance().compareTo(user2.getDate_naissance());

        if (res == 0 && user.getLieu_naissance().trim().equals(user2.getLieu_naissance().trim())) {
            return "OK \n\n" + user2.toString();
        } else {
            return "KO \n\n" + user2.toString();
        }
    }

    public String OCROldCINVerso(User user, String result) {
        return null; // TODO : OCROldCINVerso
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

        System.out.println("\n nom : " + nom + "\n prenom : " + prenom + "\n rib : "
                + rib + "\n banque : " + banque);
        return result + "\n\n nom : " + nom + "\n prenom : " + prenom + "\n rib : " +
                rib + "\n banque : " + banque;
    }
}
