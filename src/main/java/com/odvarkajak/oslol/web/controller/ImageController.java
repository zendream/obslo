package com.odvarkajak.oslol.web.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ImageController {

	//TODO
	/*
	 @RequestMapping(value = "/image/{image_id}", produces = MediaType.IMAGE_PNG_VALUE)
	    public ResponseEntity<byte[]> getImage(@PathVariable("image_id") Long imageId) throws IOException {

	        byte[] imageContent = //get image from DAO based on id
	        final HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.IMAGE_PNG);
	        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	    }
	    */
}
