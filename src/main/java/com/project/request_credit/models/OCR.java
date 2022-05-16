package com.project.request_credit.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OCR {
	private String destinationLanguage;
	private String image;
	private Long id_user;
}
