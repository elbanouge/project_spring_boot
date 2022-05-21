package com.example.creditproject.controllers;

import java.io.IOException;
import java.util.List;

import com.example.creditproject.entities.Imagetest;
import com.example.creditproject.entities.User;
import com.example.creditproject.models.OCR;
import com.example.creditproject.services.ImageParseService;
import com.example.creditproject.services.UserService;

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
import org.springframework.web.bind.annotation.RestController;

import net.sourceforge.tess4j.TesseractException;

@RequestMapping("/api/ocr")
@RestController
@CrossOrigin(origins = "http://localhost:8100")
public class OCRController {
	@Autowired
	private ImageParseService imageParseService;
	@Autowired
	private UserService userService;

	@PostMapping("parse")
	public ResponseEntity<?> OCR(@RequestBody OCR ocr) throws IOException,
			TesseractException {
		Imagetest image = new Imagetest();
		Imagetest imagePath = imageParseService.findByUrl(ocr.getImage());

		if (imagePath != null) {
			String res = imageParseService.saveImageOCR(imagePath, ocr);
			return new ResponseEntity<>("Image found and update \n" + res, HttpStatus.FOUND);

		} else {
			String res = imageParseService.saveImageOCR(image, ocr);
			if (res.equals("Error")) {
				return new ResponseEntity<>("Error while saving image", HttpStatus.BAD_REQUEST);
			} else {
				return new ResponseEntity<>("Image saved \n " + res, HttpStatus.CREATED);
			}
		}
	}

	@PostMapping("OCR_CIN_Verso")
	public ResponseEntity<?> OCRCINVerso(@RequestBody OCR ocr) {
		Imagetest image = imageParseService.findByUrl(ocr.getImage());

		if (image == null) {
			return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
		} else {
			User user = userService.findById(image.getUserId());
			if (user == null) {
				return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
			} else {
				String res = imageParseService.ocrCINVerso(user, image.getResult());
				return new ResponseEntity<>("CIN Verso \n" + res + "\n\n" + user.toString(), HttpStatus.OK);
			}
		}
	}

	@PostMapping("OCR_CIN_Recto")
	public ResponseEntity<?> OCRCINRecto(@RequestBody OCR ocr) {
		Imagetest image = imageParseService.findByUrl(ocr.getImage());

		if (image == null) {
			return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
		} else {
			User user = userService.findById(image.getUserId());
			if (user == null) {
				return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
			} else {
				String res = imageParseService.ocrCINRecto(user, image.getResult());
				return new ResponseEntity<>("CIN Recto \n" + res + "\n\n" + user.toString(), HttpStatus.OK);
			}
		}
	}

	@GetMapping("all")
	public ResponseEntity<?> allOCR() {
		List<Imagetest> images = imageParseService.findAll();
		return new ResponseEntity<>(images, HttpStatus.OK);
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteOCR(@PathVariable Long id) {
		Imagetest image = imageParseService.findById(id);
		if (image == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			imageParseService.delete(image);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	@PostMapping({ "/OCR_Bulletin_Paie" })
	public ResponseEntity<?> ocrBulletinPaie(@RequestBody OCR ocr) {
		Imagetest image = imageParseService.findByUrl(ocr.getImage());

		if (image == null) {
			return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
		} else {
			String res = imageParseService.parseImage(image.getUrl(), ocr.getDestinationLanguage());
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
	}

	@PostMapping({ "/OCR_RIB" })
	public ResponseEntity<?> ocrRIB(@RequestBody OCR ocr) {
		Imagetest image = imageParseService.findByUrl(ocr.getImage());
		String res = imageParseService.saveImageOCR(image, ocr);
		if (res.equals("Error")) {
			return new ResponseEntity<>("Error while parsing image", HttpStatus.BAD_REQUEST);
		} else {
			String res2 = imageParseService.ocrRIB(null, res);

			return new ResponseEntity<>(res2, HttpStatus.OK);
		}

	}
}
