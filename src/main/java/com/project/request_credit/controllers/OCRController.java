package com.project.request_credit.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.project.request_credit.entities.Scanner;
import com.project.request_credit.entities.User;
import com.project.request_credit.models.OCR;
import com.project.request_credit.services.AccountService;
import com.project.request_credit.services.ImageParseService;
import com.project.request_credit.services.ScannerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.TesseractException;

@RequestMapping("/api/ocr")
@RestController
@CrossOrigin(origins = "http://localhost:8100")
public class OCRController {

	@Autowired
	private ImageParseService imageParseService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ScannerService scannerService;

	@PostMapping({ "parse" })
	public ResponseEntity<?> OCR(@RequestParam MultipartFile file, @RequestParam String lang,
			@RequestParam String id_user) throws IOException,
			TesseractException {
		try {
			String res = "";
			String path = imageParseService.getPathImage(file);
			String resPath = imageParseService.cleanImage(path);
			System.out.println(resPath);
			OCR ocr = new OCR();
			ocr.setId_user(Long.parseLong(id_user.toString()));
			ocr.setDestinationLanguage(lang);
			ocr.setImage(resPath);

			Scanner newScanner = new Scanner();
			Scanner scannerExist = scannerService.getScannerByUrl(ocr.getImage());

			if (scannerExist != null) {
				res = imageParseService.saveImageOCR(scannerExist, ocr);
				return new ResponseEntity<>(res, HttpStatus.OK);
			} else {
				res = imageParseService.saveImageOCR(newScanner, ocr);
				if (res.equals("Error")) {
					return new ResponseEntity<>("Error", HttpStatus.BAD_GATEWAY);
				} else {
					return new ResponseEntity<>(res, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping({ "scan" })
	public ResponseEntity<?> scan(@RequestParam("file") MultipartFile file) {
		String path = imageParseService.getPathImage(file);
		imageParseService.cleanImage(path);
		return new ResponseEntity<>("Scan done", HttpStatus.OK);
	}

	@PostMapping({ "upload" })
	public ResponseEntity<?> pictureupload(@RequestParam("file") MultipartFile file) {
		String path = imageParseService.getPathImage(file);
		if (path.isEmpty()) {
			return new ResponseEntity<>("Error while saving image", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>("Image saved " + path, HttpStatus.CREATED);
		}
	}

	@GetMapping({ "all" })
	public ResponseEntity<?> allOCR() {
		List<Scanner> images = scannerService.getScanners();
		return new ResponseEntity<>(images, HttpStatus.OK);
	}

	@DeleteMapping({ "delete/{id}" })
	public ResponseEntity<?> deleteOCR(@PathVariable Long id) {
		Scanner image = scannerService.findById(id);
		if (image == null) {
			return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
		} else {
			scannerService.delete(image);
			return new ResponseEntity<>("Image deleted", HttpStatus.OK);
		}
	}

	@PostMapping({ "OCR_NEW_CIN_Verso" })
	public ResponseEntity<?> OCRNewCINVerso(@RequestBody OCR ocr) {
		Scanner image = scannerService.getScannerByUrl(ocr.getImage());

		if (image == null) {
			return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
		} else {
			User user = accountService.findById(ocr.getId_user());
			if (user == null) {
				return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
			} else {
				String res = imageParseService.ocrNewCINVerso(user, image.getResult());
				return new ResponseEntity<>("CIN Verso \n" + res + "\n\n" + user.toString(), HttpStatus.OK);
			}
		}
	}

	@PostMapping({ "OCR_NEW_CIN_Recto" })
	public ResponseEntity<?> OCRNewCINRecto(@RequestBody OCR ocr) throws ParseException {
		Scanner image = scannerService.getScannerByUrl(ocr.getImage());

		if (image == null) {
			return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
		} else {
			User user = accountService.findById(ocr.getId_user());
			if (user == null) {
				return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
			} else {
				String res = imageParseService.ocrNewCINRecto(user, image.getResult());
				return new ResponseEntity<>("CIN Recto \n" + res + "\n\n" + user.toString(), HttpStatus.OK);
			}
		}
	}

	@PostMapping({ "OCR_Old_CIN_Verso" })
	public ResponseEntity<?> OCROldCINVerso(@RequestBody OCR ocr) {
		Scanner image = scannerService.getScannerByUrl(ocr.getImage());

		if (image == null) {
			return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
		} else {
			User user = accountService.findById(ocr.getId_user());
			if (user == null) {
				return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
			} else {
				String res = imageParseService.OCROldCINVerso(user, image.getResult());
				return new ResponseEntity<>("CIN Verso \n" + res + "\n\n" + user.toString(), HttpStatus.OK);
			}
		}
	}

	@PostMapping({ "OCR_Bulletin_Paie" })
	public ResponseEntity<?> ocrBulletinPaie(@RequestBody OCR ocr) {
		Scanner image = scannerService.getScannerByUrl(ocr.getImage());

		if (image == null) {
			return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
		} else {
			String res = imageParseService.parseImage(image.getUrl(), ocr.getDestinationLanguage());
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
	}

	@PostMapping({ "OCR_RIB" })
	public ResponseEntity<?> ocrRIB(@RequestBody OCR ocr) {
		Scanner image = scannerService.getScannerByUrl(ocr.getImage());
		String res = imageParseService.saveImageOCR(image, ocr);
		if (res.equals("Error")) {
			return new ResponseEntity<>("Error while parsing image", HttpStatus.BAD_REQUEST);
		} else {
			String res2 = imageParseService.ocrRIB(null, res);
			return new ResponseEntity<>(res2, HttpStatus.OK);
		}
	}
}
