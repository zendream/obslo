package com.odvarkajak.oslol.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.odvarkajak.oslol.domain.DataFile;
import com.odvarkajak.oslol.domain.Observation;
import com.odvarkajak.oslol.domain.Project;
import com.odvarkajak.oslol.repository.DataFileRepository;
import com.odvarkajak.oslol.repository.ObservationRepository;
import com.odvarkajak.oslol.repository.ProjectRepository;
import com.odvarkajak.oslol.repository.UserRepository;
import com.odvarkajak.oslol.service.ObservationService;
import com.odvarkajak.oslol.web.form.ObservationForm;
import com.odvarkajak.oslol.web.form.SettingsForm;

@Controller
public class ObservationController {
	
	static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${fileDir.mainFolder}")
    private String mainFolder;
    @Value("${fileDir.observationImagesFolder}")
    private String observationImagesFolder;
    @Value("${fileDir.observationDataFolder}")
    private String observationDataFolder;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    ProjectRepository projectRepository;
    
    @Autowired
    DataFileRepository dataFileRepository;
    
	@Autowired
	ObservationRepository observationRepository;
	@Autowired
	ObservationService observationService;
	
	 @SuppressWarnings("unchecked")
		@ModelAttribute("allObservations")
	    public List<Observation> populateObservations() {
	        return (List<Observation>) this.observationRepository.loadObservations();
	    }
	 
	 @RequestMapping(value = "/observation/listAll")
	    public String observationList() {
	        return "view/observation/list";
	    }
	 
	 @RequestMapping(value = "/getObservationsByName", method = RequestMethod.POST, produces = "application/json")
	 public @ResponseBody List<Observation> getObservations(@RequestParam String term, HttpServletResponse response) {
		 //TODO clean input
		 return observationRepository.findObservationsByname(term);	 
	}
	 
	 @RequestMapping(value = "/observation/detail/{observationId}", method=RequestMethod.GET)
	    public String observationView(Model model,@PathVariable("observationId") Long observationId) {	    	
	    	User loggedUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	String viewing = loggedUser.getUsername();
	        model.addAttribute("viewing",viewing);
	    	Observation observation = observationRepository.findObservationById(observationId);	    	
	        model.addAttribute("observation", observation);	 
	        List<Project> projects = new LinkedList<Project>();
	        logger.debug("Building list of projects");
	        for (Project observationProject : observation.getProjects()){
	        	projects.add(observationProject);
	        }
	        for(Project project : projects){
	        	logger.debug("Found observation - " + project.getName());
	        }
	        model.addAttribute("projects",projects);
	        List<com.odvarkajak.oslol.domain.User> users = new LinkedList<com.odvarkajak.oslol.domain.User>();
	        logger.debug("Building list of associated users");
	        for (com.odvarkajak.oslol.domain.User observationUser : observation.getUsers()){
	        	users.add(observationUser);
	        }
	        for(com.odvarkajak.oslol.domain.User usr  : users){
	        	logger.debug("Found observation - " + usr.getUsername());
	        }
	        model.addAttribute("allowedUsers",users);
	        List<String> directAccessUsers = new LinkedList<String>();
	        logger.debug("Building list of direct access users");
	        directAccessUsers.addAll(observationService.getUserNamesWithDirectAccess(observation));
	        model.addAttribute("directAccessUsers",directAccessUsers);
	        if (!model.containsAttribute("settingsForm")) {
	        	model.addAttribute("settingsForm",new SettingsForm());
	        }
	        return "view/observation/detail";
	    }


	    @RequestMapping("/observation/create")
	    public String create(Model model) {
	        logger.debug("Now: Create observation");
	        if (!model.containsAttribute("observation")) {
	            model.addAttribute("observation", new ObservationForm());
	        }        
	        return "view/observation/create";
	    }
	    
	    @RequestMapping(value = "/observation/addSettings/{observationId}", method = RequestMethod.POST)
	    @Transactional(readOnly = false)
	    public String addSettingsToObservation(@PathVariable Long observationId,Model model, @ModelAttribute("settingsForm") @Valid SettingsForm form, BindingResult result, ServletRequest servletRequest) {
	    	Observation observation = observationRepository.findObservationById(observationId);
	    	User loggedUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	//TODO USER
	    	if (!form.getUser().equals("")){
	    		try{
	    			com.odvarkajak.oslol.domain.User user = userRepository.findUserByScreenname(form.getUser());
	    			logger.debug("Found user with name -" + form.getUser() + " current observationId is = " + observation.getId());	
	    			logger.debug("check");
	    			observation.getUsers().add(user);
	    			user.getAccessibleObservations().add(observation);
	    	    	logger.debug("check");
	    	    	observationRepository.update(observation);
	    	    	logger.debug("check");
	    	    	
	    		}
	    		catch (Exception e){
	    			logger.debug("No user found with name -" + form.getUser());
	    			FieldError fieldError = new FieldError("user", "user", "Username not found");
                    result.addError(fieldError);
	    		}
	    	}
	    	
	    	if (!form.getProject().equals("")){
	    		try{
	    			Project targetProject = projectRepository.findProjectByName(form.getProject());
	    			if (targetProject == null) throw new NoResultException(); 
	    			logger.debug("Found project with id - " + targetProject.getId() + " attempting to add observation " + observation.getId());	    				    				    		    			
	    	    	targetProject.getObservations().add(observation);
	    	    	projectRepository.saveProject(targetProject);
	    	    	
	    	    	
	    	    	
	    		}
	    		catch (NoResultException e){
	    			logger.debug("Failed to find -" + form.getProject());
	    			FieldError fieldError = new FieldError("project", "project", "Project name not found");
                    result.addError(fieldError);
	    		}
	    	}	  
	    	
	    	if (form.getDataFile() == null){
            	logger.debug("Observation Settings - no file");
            }            
            else{
    	    	DataFile dataFile = new DataFile();
            	MultipartFile file = form.getDataFile();
            	String filename = file.getOriginalFilename();
            	logger.debug("Upload requested filename = " + file.getOriginalFilename());
            	if (!file.isEmpty()) {
                    try {
                    	//if (!file.getContentType().equals("text/txt")) {
                		//	throw new RuntimeException("Only TXT files are accepted");
                		//}
                    	Calendar cal = Calendar.getInstance();
        	            Date date = cal.getTime();
        	            String target = observationDataFolder + "\\" + observation.getId() + "\\";
        				Path path = Paths.get(target + filename);
        				
        	            dataFile.setCreated(new java.sql.Date(date.getTime()));
        	            
        	            if(form.getDescription() == null) 
        	            		dataFile.setDescription("N/A");
        	            	else 
        	            		dataFile.setDescription(form.getDescription());
        	            
        	            dataFile.setName(file.getOriginalFilename());
        	            dataFile.setParentObservation(observation);
        	            dataFile.setPath(path.toString());
        	            dataFile.setSize(file.getSize());
        	            dataFile.setParentObservation(observation);
        	            dataFileRepository.saveDataFile(dataFile);        	            
                    	
        				File dir = path.toFile();
        				dir.getParentFile().mkdirs();
        				file.transferTo(path.toFile());
        				logger.debug("FileIO - Saving file:  " + file.getOriginalFilename() + 
        						" of observation " + observation.getId() + 
        						" to " + target.toString());
        				logger.debug("You successfully uploaded " + filename + "!");
        				
                    }
                    catch (RuntimeException e) {
                    	logger.debug("You failed to upload " + filename + " => " + e.getMessage());
                    	FieldError fieldError = new FieldError("file", "file", "Only .txt files < 1 MB are accepted");
                        result.addError(fieldError);
                    	
                    }
                    catch (Exception e) {
                    	logger.debug("You failed to upload " + filename + " => " + e.getMessage());
                    }
                } else {
                	logger.debug("You failed to upload " + filename + " because the file was empty.");                	
                }
            }
	        logger.debug("Back to observation detail");
	        return ("redirect:/observation/detail/"+observationId.toString());
	        
	    }
	    //TODO secure
	    @RequestMapping("/observation/edit/{observationId}")
	    public String edit(Model model, @PathVariable("observationId") Long observationId) {
	        logger.debug("Now: Edit observation");
	        Observation observation = observationRepository.findObservationById(observationId);
	        observationRepository.update(observation);
	        ObservationForm form = new ObservationForm();
	        if (observation != null){  
	        	form.setName(observation.getName());
	        	form.setDescription(observation.getDescription());
	        	form.setHasFile(observation.getPictureFile() == "");
	        	form.setModifId(observationId);        
	        	logger.debug("Editing observation Id - " + form.getModifId());
	        }
	        model.addAttribute("observation", form);
	        return "view/observation/create";
	    }


	    @RequestMapping(value = "/observation/create_confirm", method = RequestMethod.POST)
	    @Transactional
	    public String createObservation(Model model, @ModelAttribute("observation") @Valid ObservationForm form, BindingResult result, ServletRequest servletRequest) {
	        logger.debug("Now: createObservation form");
	        User loggedUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();               
	        if (!result.hasErrors()) {  
	        	if (observationRepository.isNameAlreadyExistsForThisUser(form.getName(),userRepository.findUserByUsername(loggedUser.getUsername()).getId())) {
	                FieldError fieldError = new FieldError("name", "name", "This name already exists");
	                result.addError(fieldError);
	                return "view/observation/create";
	            }
	            Calendar cal = Calendar.getInstance();
	            Date date = cal.getTime();                       
	            Observation observation = new Observation();
	            observation.setId(null);
	            observation.setName(form.getName());
	            observation.setDescription(form.getDescription());
	            observation.setCreated(new java.sql.Date(date.getTime()));
	            observation.setModified(date);
	            observation.setAuthor(userRepository.findUserByUsername(loggedUser.getUsername()));
	            if (form.getFile() == null){
	            	logger.debug("Observation CreateOrEdit - Null file!");
	            }            
	            else{
	            	MultipartFile file = form.getFile();
	            	String filename = file.getOriginalFilename();
	            	logger.debug("Upload requested filename = " + file.getOriginalFilename());
	            	if (!file.isEmpty()) {
	                    try {
	                    	if (!file.getContentType().equals("image/jpeg")) {
	                			throw new RuntimeException("Only JPG images are accepted");
	                		}
	                    	String target = observationImagesFolder + "\\" + userRepository.findUserByUsername(loggedUser.getUsername()).getUsername() + "\\" + form.getName() + "\\";
	        				Path path = Paths.get(target + filename);
	        				File dir = path.toFile();
	        				dir.getParentFile().mkdirs();
	        				file.transferTo(path.toFile());
	        				logger.debug("FileIO - Saving file:  " + file.getOriginalFilename() + 
	        						" as " + filename + 
	        						" to " + target.toString());
	        				logger.debug("You successfully uploaded " + filename + "!");
	        				observation.setPictureFile(file.getOriginalFilename());
	        				observation.setPictureDescription(form.getDescription());
	                    }
	                    catch (RuntimeException e) {
	                    	logger.debug("You failed to upload " + filename + " => " + e.getMessage());
	                    	FieldError fieldError = new FieldError("file", "file", "File too large or not .JPG");
	                        result.addError(fieldError);
	                        return "view/observation/create";
	                    	
	                    }
	                    catch (Exception e) {
	                    	logger.debug("You failed to upload " + filename + " => " + e.getMessage());
	                    }
	                } else {
	                	logger.debug("You failed to upload " + filename + " because the file was empty.");                	
	                }
	            }
	            if (form.getGraphFile() == null){
	            	logger.debug("Observation CreateOrEdit - Null file!");
	            }            
	            else{
	            	MultipartFile file = form.getGraphFile();
	            	String filename = file.getOriginalFilename();
	            	logger.debug("Upload graph requested filename = " + file.getOriginalFilename());
	            	if (!file.isEmpty()) {
	                    try {
	                    	if (!file.getContentType().equals("image/jpeg")) {
	                			throw new RuntimeException("Only JPG images are accepted");
	                		}
	                    	String target = observationImagesFolder + "\\" + userRepository.findUserByUsername(loggedUser.getUsername()).getUsername() + "\\" + form.getName() + "\\";
	        				Path path = Paths.get(target + filename);
	        				File dir = path.toFile();
	        				dir.getParentFile().mkdirs();
	        				file.transferTo(path.toFile());
	        				logger.debug("FileIO - Saving file:  " + file.getOriginalFilename() + 
	        						" as " + filename + 
	        						" to " + target.toString());
	        				logger.debug("You successfully uploaded " + filename + "!");
	        				observation.setGraphFile(file.getOriginalFilename());
	        				observation.setGraphDescription(file.getOriginalFilename());
	                    }
	                    catch (RuntimeException e) {
	                    	logger.debug("You failed to upload " + filename + " => " + e.getMessage());
	                    	FieldError fieldError = new FieldError("graphFile", "graphFile", "File too large or not .JPG");
	                        result.addError(fieldError);
	                        return "view/observation/create";
	                    	
	                    }
	                    catch (Exception e) {
	                    	logger.debug("You failed to upload " + filename + " => " + e.getMessage());
	                    }
	                } else {
	                	logger.debug("You failed to upload " + filename + " because the file was empty.");                	
	                }
	            }
	            
	            observationRepository.saveObservation(observation);  
	            
	            logger.debug("End: createObservation form success -- " + observation.getId());
	            logger.debug("End: createObservation success");        
	            return ("redirect:listAll/") ;
	        } 
	        else {
	            logger.debug(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
	            this.create(model);
	            return "view/observation/create";

	        }
	    }
	    
	    @RequestMapping(value = "/observation/getImage/{observationId}")
	    public @ResponseBody
	    HttpEntity<byte[]> getImage(@PathVariable Long observationId)  {
	    	Observation observation = observationRepository.findObservationById(observationId);
	    	String author = observation.getAuthor().getUsername();
	    	HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.IMAGE_JPEG); //or what ever type it is
	        
	    	byte[] buffer = new byte[1024];
	    	int b;
	    	
	    	logger.debug("Retrieving picture for observationId = " + observationId);
	    	
	    	String target = observationImagesFolder + "\\" + author + "\\" + observation.getName() + "\\" + observation.getPictureFile();
			Path path;
			File imageFile;
			if ((observationId == null) || (observation.getPictureFile() == null) || (observation.getPictureFile() == "")){
	    		logger.debug("Image = "+ observation.getPictureFile() +" not set or ID not found, returning placeholder");
	    		target = mainFolder + "notfound.jpg";
	    		path = Paths.get(target);
	    		imageFile = path.toFile();
	    	}
			else
			{
				path = Paths.get(target);
				imageFile = path.toFile();
			}
			try {
				
				FileInputStream fis=new FileInputStream(imageFile);
	    		ByteArrayOutputStream bos=new ByteArrayOutputStream();
	    	
	    		while((b=fis.read(buffer))!=-1){
	    			bos.write(buffer,0,b);
	    		}
			
	    		byte[] fileBytes=bos.toByteArray();
	    		fis.close();
	    		bos.close();
	            headers.setContentLength(fileBytes.length);
	            return new HttpEntity<byte[]>(fileBytes, headers);
			}
			catch (FileNotFoundException f) {
				logger.debug("File not found");
				f.printStackTrace();
			}
			catch(IOException e) {
				logger.debug("IOException encountered while retrieving file");
				e.printStackTrace();
			}
			catch (Exception e)	{
				logger.debug("Something very bad happened when retrieving file");
				e.printStackTrace();
			}
			return new HttpEntity<byte[]>(buffer, headers);
	    }
	    @RequestMapping(value = "/observation/getGraph/{observationId}")
	    public @ResponseBody
	    HttpEntity<byte[]> getGraph(@PathVariable Long observationId)  {
	    	Observation observation = observationRepository.findObservationById(observationId);
	    	String author = observation.getAuthor().getUsername();
	    	HttpHeaders headers = new HttpHeaders();	    	
	        headers.setContentType(MediaType.IMAGE_JPEG); //or what ever type it is
	        
	    	byte[] buffer = new byte[1024];
	    	int b;
	    	
	    	logger.debug("Retrieving picture for observationId = " + observationId);
	    	
	    	String target = observationImagesFolder + "\\" + author + "\\" + observation.getName() + "\\" + observation.getGraphFile();
			Path path;
			File imageFile;
			if ((observationId == null) || (observation.getGraphFile() == null)|| (observation.getGraphFile().equals(""))){
	    		logger.debug("Image not set or ID not found, returning placeholder");
	    		target = mainFolder + "notfound.jpg";
	    		path = Paths.get(target);
	    		imageFile = path.toFile();
	    	}
			else
			{
				path = Paths.get(target);
				imageFile = path.toFile();
			}
			try {
				
				FileInputStream fis=new FileInputStream(imageFile);
	    		ByteArrayOutputStream bos=new ByteArrayOutputStream();
	    	
	    		while((b=fis.read(buffer))!=-1){
	    			bos.write(buffer,0,b);
	    		}
			
	    		byte[] fileBytes=bos.toByteArray();
	    		fis.close();
	    		bos.close();
	            headers.setContentLength(fileBytes.length);
	            return new HttpEntity<byte[]>(fileBytes, headers);
			}
			catch (FileNotFoundException f) {
				logger.debug("File not found");
				f.printStackTrace();
			}
			catch(IOException e) {
				logger.debug("IOException encountered while retrieving graph");
				e.printStackTrace();
			}
			catch (Exception e)	{
				logger.debug("Something very bad happened when retrieving graph");
				e.printStackTrace();
			}
			return new HttpEntity<byte[]>(buffer, headers);
	    }
}
