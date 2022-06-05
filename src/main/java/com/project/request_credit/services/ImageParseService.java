package com.project.request_credit.services;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
@Transactional
public class ImageParseService {
    private static final DecimalFormat df = new DecimalFormat("0.00");

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
                String url = ocr.getImage();
                image.setUrl(url);
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

    public String cleanImage(String path, String type) {
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

        if (type.equals("destination")) {
            resDes.replace("destination", "");
            return resDes + resPath;
        } else if (type.equals("result")) {
            resDes.replace("result", "");
            return resRes + resPath;
        } else {
            return "Error";
        }
    }

    public String ocrNewCINVerso(String result) {
        String res = "";

        String cin = "aucun";
        String nom = "aucun";
        String prenom = "aucun";
        String adresse = "aucun";
        String sexe = "aucun";

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

        if (line1.length() > 0) {
            String[] line1Split = line1.split("<");
            cin = line1Split[1].substring(1);
        }

        if (line2.length() > 0) {
            String[] line2Split = line2.split("<<");
            nom = line2Split[0];
            prenom = line2Split[1];
            if (nom.contains("<") || nom.contains(">") || prenom.contains("<") || prenom.contains(">")) {
                nom = nom.replace("<", " ");
                nom = nom.replace(">", " ");
                prenom = prenom.replace("<", " ");
                prenom = prenom.replace(">", " ");
            }
        }

        if (line3.length() > 0) {
            String adr = "";
            String[] line3Split = line3.split(" ");
            for (int i = 1; i < line3Split.length; i++) {
                if (line3Split[i].length() > 0) {
                    adr += line3Split[i] + " ";
                }
            }
            adresse = adr;
        }

        if (line4.length() > 0) {
            if (line4.contains("M")) {
                sexe = "M";
            } else {
                sexe = "F";
            }
        }

        res = "CIN : " + cin + "\nNom : " + nom + "\nPrenom : " + prenom + "\nAdresse : " + adresse + "\nSexe : "
                + sexe;
        return res;
    }

    public String ocrNewCINRecto(String result) throws ParseException {
        String res = "";

        String dateNaiss = "aucun";
        String lieuNaiss = "aucun";
        String dateValidite = "aucun";

        String[] lines = result.split("\n");
        String line1 = "";
        String line2 = "";
        String line3 = "";

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("à ")) {
                line1 = lines[i];
                break;
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("N") && lines[i].contains("le")
                    || lines[i].contains("Né")) {
                line2 = lines[i];
                break;
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("au ")) {
                line3 = lines[i];
                break;
            }
        }

        System.out.println("line1 : " + line1);
        System.out.println("line2 : " + line2);
        System.out.println("line3 : " + line3);

        if (line1.length() > 0) {
            lieuNaiss = line1.replaceAll("[^A-Z]+", " ");
        }

        if (line2.length() > 0) {
            String date = "";

            String[] line2Split = line2.replaceAll("[^0-9]+", " ").split(" ");
            for (int i = 0; i < line2Split.length; i++) {
                if (line2Split[i].length() > 0 && line2Split[i].length() > 1) {
                    if (i != line2Split.length - 1) {
                        date += line2Split[i].trim() + "/";
                    } else {
                        date += line2Split[i].trim();
                    }
                }
            }
            dateNaiss = date;
        }

        if (line3.length() > 0) {
            line3 = line3.split("au ")[1];
            dateValidite = line3.replaceAll("[^0-9]+", " ").trim();

            for (int i = 0; i < dateValidite.length(); i++) {
                if (dateValidite.charAt(i) == ' ') {
                    dateValidite = dateValidite.replace(" ", "/");
                    break;
                }
            }
        }

        res = "Lieu de naissance : " + lieuNaiss + "\nDate de naissance : " + dateNaiss + "\nDate de validité : "
                + dateValidite;
        return res;
    }

    public String OCROldCINRecto(String result) {
        String res = "";
        String nom = "aucun";
        String prenom = "aucun";
        String dateNaiss = "aucun";
        String lieuNaiss = "aucun";
        String dateCINVal = "aucun";

        String[] lines = result.split("\n");
        String line1 = "";
        String line2 = "";
        String line3 = "";
        String line4 = "";

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("le") && lines[i].contains("N")) {
                line1 = lines[i];
                break;
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("à ")) {
                line2 = lines[i];
                break;
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("au ")) {
                line3 = lines[i];
                break;
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("[A-Z]+")) {
                line4 = lines[i];
                break;
            }
        }

        System.out.println("line1 : " + line1);
        System.out.println("line2 : " + line2);
        System.out.println("line3 : " + line3);
        System.out.println("line4 : " + line4);

        if (line1.length() > 0) {
            String date = "";

            String[] line1Split = line1.replaceAll("[^0-9]+", " ").split(" ");
            for (int i = 0; i < line1Split.length; i++) {
                if (line1Split[i].length() > 0 && line1Split[i].length() > 1) {
                    if (i != line1Split.length - 1) {
                        date += line1Split[i].trim() + "/";
                    } else {
                        date += line1Split[i].trim();
                    }
                }
            }
            dateNaiss = date;
        }

        if (line2.length() > 0) {
            lieuNaiss = line2.replaceAll("[^A-Z]+", " ");
        }

        if (line3.length() > 0) {
            dateCINVal = line3.replaceAll("[^0-9]+", " ").trim();

            for (int i = 0; i < dateCINVal.length(); i++) {
                if (dateCINVal.charAt(i) == ' ') {
                    dateCINVal = dateCINVal.replace(" ", "/");
                    break;
                }
            }
        }

        res = "Date de naissance : " + dateNaiss + "\nLieu de naissance : " + lieuNaiss + "\nDate de validité : "
                + dateCINVal + "\nNom : " + nom + "\nPrenom : " + prenom;
        return res;
    }

    public String OCROldCINVerso(String result) {
        String res = "";
        String cin = "aucun";
        String adresse = "aucun";
        String sexe = "aucun";

        String[] lines = result.split("\n");
        String line1 = "";
        String line2 = "";
        String line3 = "";

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("N°")) {
                line1 = lines[i];
                break;
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Adresse") || lines[i].contains("Adr")) {
                line2 = lines[i];
                break;
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Sexe") || lines[i].contains("exe")) {
                line3 = lines[i];
                break;
            }
        }

        System.out.println("line1 : " + line1);
        System.out.println("line2 : " + line2);
        System.out.println("line3 : " + line3);

        if (line1.length() > 0) {
            String[] line1Split = line1.split(" ");
            for (int i = 0; i < line1Split.length; i++) {
                if (line1Split[i].length() > 0 && line1Split[i].startsWith("N")) {
                    if (line1Split[i + 1].length() == 0) {
                        cin = line1Split[i + 2];
                    } else {
                        cin = line1Split[i + 1];
                    }
                }
            }
        }

        if (line2.length() > 0) {
            String adr = "";
            String[] line2Split = line2.split(" ");
            for (int i = 1; i < line2Split.length; i++) {
                if (line2Split[i].length() > 0) {
                    adr += line2Split[i] + " ";
                }
            }
            adresse = adr;
        }

        if (line3.length() > 0) {
            if (line3.contains("M")) {
                sexe = "M";
            } else {
                sexe = "F";
            }
        }

        res = "CIN : " + cin + "\nAdresse : " + adresse + "\nSexe : " + sexe;
        return res;
    }

    public String ocrRIB(String result) {
        String res = "";

        String nom = "";
        String prenom = "";
        String rib = "";
        String banque = "";

        String line1 = "";
        String line2 = "";
        String line3 = "";

        String[] lines = result.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Intitul")) {
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

        res += "nom : " + nom + "\n" + "prenom : " + prenom + "\n" + "rib : " + rib + "\n" + "banque : " + banque;

        System.out.println("***" + res);
        return res;
    }

    public String ocrAttestationSalaire(String result) {
        String res = "";
        String nom = "";
        String prenom = "";
        String sexe = "";
        String cin = "";
        String salaire = "";
        String date = "";

        String[] lines = result.split("\n");
        String line1 = "";
        String line2 = "";
        String line3 = "";
        String line4 = "";

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].toLowerCase().contains("ATTESTE PAR LA PRESENTE QUE".toLowerCase())) {
                line1 = lines[i];
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].toLowerCase().contains("CIN N°".toLowerCase())) {
                line2 = lines[i];
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].toLowerCase().contains("SALAIRE ANNUEL NET DE".toLowerCase())) {
                line3 = lines[i];
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].toLowerCase().contains("ATTESTATION DE SALAIRE DU ".toLowerCase())) {
                line4 = lines[i];
            }
        }

        if (line1.length() > 0) {
            String[] line1Split = line1.split("ATTESTE PAR LA PRESENTE QUE");
            sexe = line1Split[1].trim().split(" ")[0].trim();
            if (sexe.toLowerCase().equals("MR".toLowerCase())) {
                sexe = "M";
            } else {
                sexe = "F";
            }

            nom = line1Split[1].trim().split(" ")[1].trim();
            prenom = line1Split[1].trim().split(" ")[2].trim();
        }

        if (line2.length() > 0) {
            String[] line2Split = line2.split(" ");
            for (int i = 0; i < line2Split.length; i++) {
                if (line2Split[i].contains("CIN")) {
                    cin = line2Split[i + 2];
                    break;
                }
            }
        }

        if (line3.length() > 0) {
            salaire = line3.replaceAll("[^0-9]+", " ").trim();
            salaire = salaire.split(" ")[0] + "." + salaire.split(" ")[1];
            salaire = df.format(Double.parseDouble(salaire) / 12) + "";
        }

        if (line4.length() > 0) {
            date = line4.replaceAll("[^0-9]+", " ").trim();
            date = "DE " + date.split(" ")[0] + "/" + date.split(" ")[1] + "/" + date.split(" ")[2] + "AU "
                    + date.split(" ")[3] + "/" + date.split(" ")[4] + "/" + date.split(" ")[5];
        }

        System.out.println("line1 : " + line1);
        System.out.println("line2 : " + line2);
        System.out.println("line3 : " + line3);
        System.out.println("line4 : " + line4);

        res = "nom : " + nom + "\n" + "prenom : " + prenom + "\n" + "sexe : " + sexe + "\n" + "cin : " + cin + "\n"
                + "salaire : " + salaire + "\n" + "date : " + date;
        return res;
    }

    public String compareInfoUserOCR(String info, Long id_user, String type) {
        String res = "";
        boolean bol = false;
        User user = accountService.findById(id_user);
        if (user != null) {
            String CIN = "";
            String Nom = "";
            String Prenom = "";
            String Adresse = "";
            String Sexe = "";

            String dateNaiss = "";
            String lieuNaiss = "";
            String dateCINVal = "";

            if (type.equals("CIN_NEW_Verso")) {

                CIN = info.split("\n")[0].split(":")[1].trim();
                Nom = info.split("\n")[1].split(":")[1].trim();
                Prenom = info.split("\n")[2].split(":")[1].trim();
                Adresse = info.split("\n")[3].split(":")[1].trim();
                Sexe = info.split("\n")[4].split(":")[1].trim();

                if (CIN.equals(user.getCin())) {
                    res += "CIN user and CIN OCR are the same \n";
                    bol = true;
                }
                if (Nom.toLowerCase().equals(user.getLastName().toLowerCase())) {
                    res += "Nom user and Nom OCR are the same \n";
                    bol = true;
                }
                if (Prenom.toLowerCase().equals(user.getFirstName().toLowerCase())) {
                    res += "Prenom user and Prenom OCR are the same \n";
                    bol = true;
                }
                if (Adresse.toLowerCase().equals(user.getAddress().toLowerCase())) {
                    res += "Adresse user and Adresse OCR are the same \n";
                    bol = true;
                }
                if (Sexe.equals("M") && user.getSexe() == true || Sexe.equals("F") && user.getSexe() == false) {
                    res += "Sexe user and Sexe OCR are the same \n";
                    bol = true;
                }

            } else if (type.equals("CIN_NEW_Recto")) {

                lieuNaiss = info.split("\n")[0].split(":")[1].trim();
                dateNaiss = info.split("\n")[1].split(":")[1].trim();
                dateCINVal = info.split("\n")[2].split(":")[1].trim();

                if (dateNaiss.toLowerCase().trim().equals(user.getDate_naissance().toLowerCase().trim())) {
                    res += "Date de naissance user and Date de naissance OCR are the same \n";
                    bol = true;
                }
                if (lieuNaiss.toLowerCase().trim().equals(user.getLieu_naissance().toLowerCase().trim())) {
                    res += "Lieu Naissance user and Lieu Naissance OCR are the same \n";
                    bol = true;
                }
                if (user.getDate_validite_cin() != null) {
                    if (dateCINVal.toLowerCase().trim().equals(user.getDate_validite_cin().toLowerCase().trim())) {
                        res += "Date CIN Val user and Date CIN Val OCR are the same \n";
                        bol = true;
                    }
                }

            } else if (type.equals("CIN_Old_Verso")) {

                CIN = info.split("\n")[0].split(":")[1].trim();
                Adresse = info.split("\n")[1].split(":")[1].trim();
                Sexe = info.split("\n")[2].split(":")[1].trim();

                if (CIN.trim().toLowerCase().equals(user.getCin().trim().toLowerCase())) {
                    res += "CIN user and CIN OCR are the same \n";
                    bol = true;
                }
                if (Adresse.trim().toLowerCase().equals(user.getAddress().trim().toLowerCase())) {
                    res += "Adresse user and Adresse OCR are the same \n";
                    bol = true;
                }
                if (Sexe.equals("M") && user.getSexe() == true || Sexe.equals("F") && user.getSexe() == false) {
                    res += "Sexe user and Sexe OCR are the same \n";
                    bol = true;
                }

            } else if (type.equals("CIN_Old_Recto")) {

                dateNaiss = info.split("\n")[0].split(":")[1].trim();
                lieuNaiss = info.split("\n")[1].split(":")[1].trim();
                dateCINVal = info.split("\n")[2].split(":")[1].trim();

                if (dateNaiss.toLowerCase().trim().equals(user.getDate_naissance().toLowerCase().trim())) {
                    res += "Date de naissance user and Date de naissance OCR are the same \n";
                    bol = true;
                }
                if (lieuNaiss.toLowerCase().trim().equals(user.getLieu_naissance().toLowerCase().trim())) {
                    res += "Lieu Naissance user and Lieu Naissance OCR are the same \n";
                    bol = true;
                }
                if (user.getDate_validite_cin() != null) {
                    if (dateCINVal.toLowerCase().trim().equals(user.getDate_validite_cin().toLowerCase().trim())) {
                        res += "Date CIN Validity user and Date CIN Validity OCR are the same \n";
                        bol = true;
                    }
                }

            }

            if (bol == false) {
                res = "Info OCR and user are different \n";
            }
        }
        return res;
    }
}
