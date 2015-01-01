package com.odvarkajak.oslol.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DataFileController {
	
	static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private void validateImage(MultipartFile image) {
		if (!image.getContentType().equals("image/jpeg")) {
			throw new RuntimeException("Only JPG images are accepted");
		}
	}
	private void saveImage(String filename, String path, MultipartFile image)
			throws RuntimeException, IOException {
			try {			
				path = "C:\\TEMP\\images\\users\\";
				Path target = Paths.get(path + filename);
				image.transferTo(target.toFile()); 
				System.out.println("FileIO - Saving file:  " + image.getOriginalFilename() + 
						" as " + filename + 
						" to " + target.toString());
				} 
			catch (IOException e) {
					throw e;
				}
		}
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file){		
        if (!file.isEmpty()) {
            try {
            	String target = "C:\\TEMP\\images\\users\\";
				Path path = Paths.get(target + filename);
				file.transferTo(path.toFile());
				logger.debug("FileIO - Saving file:  " + file.getOriginalFilename() + 
						" as " + filename + 
						" to " + target.toString());
				return "You successfully uploaded " + filename + "!";
            } catch (Exception e) {
                return "You failed to upload " + filename + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + filename + " because the file was empty.";
        }
        
    }
}
