package com.project.request_credit.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OCR {
	private String destinationLanguage;
	private String image;
	private Long id_user;
	public String getDestinationLanguage() {
		return destinationLanguage;
	}
	public void setDestinationLanguage(String destinationLanguage) {
		this.destinationLanguage = destinationLanguage;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Long getId_user() {
		return id_user;
	}
	public void setId_user(Long id_user) {
		this.id_user = id_user;
	}
	
}
