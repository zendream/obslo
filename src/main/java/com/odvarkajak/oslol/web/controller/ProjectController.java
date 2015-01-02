package com.odvarkajak.oslol.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import org.springframework.web.servlet.ModelAndView;

import com.odvarkajak.oslol.domain.Observation;
import com.odvarkajak.oslol.domain.Project;
import com.odvarkajak.oslol.domain.Role;
import com.odvarkajak.oslol.domain.SecurityCode;
import com.odvarkajak.oslol.repository.ProjectRepository;
import com.odvarkajak.oslol.repository.UserRepository;
import com.odvarkajak.oslol.service.ProjectService;
import com.odvarkajak.oslol.utility.SecureUtility;
import com.odvarkajak.oslol.utility.TypeActivationEnum;
import com.odvarkajak.oslol.web.form.ProjectForm;
import com.odvarkajak.oslol.web.form.UserForm;

@Controller
public class ProjectController {
    static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${fileDir.mainFolder}")
    private String mainFolder;
    @Value("${fileDir.projectImagesFolder}")
    private String projectImagesFolder;
    
    @Autowired
    ProjectRepository projectRepository;    
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    ProjectService projectService;
    
    @SuppressWarnings("unchecked")
	@ModelAttribute("allProjects")
    public List<Project> populateProjects() {
        return (List<Project>) this.projectRepository.loadProjects();
    }
    
    @RequestMapping(value = "/project/listAll")
    public String projectList() {
        return "view/project/list";
    }
    
    @RequestMapping(value = "/user/searchProject")
    public String searchProject() {
        return "view/user/searchProject";
    }
    
    
    @RequestMapping(value = "/project/detail/{projectId}", method=RequestMethod.GET)
    public ModelAndView projectView(@PathVariable("projectId") Long projectId) {
    	ModelAndView mav = new ModelAndView("view/project/detail");
    	Project project = projectRepository.findProjectById(projectId);
        mav.addObject("project", project);
        User loggedUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String viewing = loggedUser.getUsername();
        mav.addObject("viewing",viewing);
        List<String> directAccessUsers = new LinkedList<String>();
        logger.debug("Building list of allowed users");
        directAccessUsers.addAll(projectService.getUserNamesWithAccess(project));
        mav.addObject("directAccessUsers",directAccessUsers);
        List<Observation> observations = new LinkedList<Observation>();
        logger.debug("Building list of observations");
        for (Observation observation : project.getObservations()){
        	observations.add(observation);
        }
        for(Observation observation : observations){
        	logger.debug("Found observation - " + observation.getName());
        }
        mav.addObject("observations",observations);
        return mav;
    }


    @RequestMapping("/project/create")
    public String create(Model model) {
        logger.debug("Now: Create project");
        if (!model.containsAttribute("project")) {
        	ProjectForm newForm = new ProjectForm();
            model.addAttribute("project", newForm);
        }        
        return "view/project/create";
    }
    //TODO secure
    @RequestMapping("/project/edit/{projectId}")
    public String edit(Model model, @PathVariable("projectId") Long projectId) {
        logger.debug("Now: Edit project");
        Project project = projectRepository.findProjectById(projectId);
        projectRepository.update(project);
        ProjectForm form = new ProjectForm();
        if (project != null){  
        	form.setName(project.getName());
        	form.setDescription(project.getDescription());
        	form.setHasFile(project.getPictureFile() == "");
        	form.setModifId(projectId);        
        	logger.debug("Editing project Id - " + form.getModifId());
        	form.setPhase(project.getPhase());
        }
        model.addAttribute("project", form);
        return "view/project/create";
    }


    @RequestMapping(value = "/project/create_confirm", method = RequestMethod.POST)
    @Transactional
    public String createProject(Model model, @ModelAttribute("project") @Valid ProjectForm form, BindingResult result, ServletRequest servletRequest) {
        logger.debug("Now: createProject form");
        User loggedUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();               
        if (!result.hasErrors()) {  
        	if (projectRepository.isNameAlreadyExists(form.getName())) {
                FieldError fieldError = new FieldError("name", "name", "This name already exists");
                result.addError(fieldError);
                return "view/project/create";
            }
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();                       
            Project project = new Project();
            logger.debug("kurvadrat");
            project.setName(form.getName());
            project.setDescription(form.getDescription());
            project.setCreated(new java.sql.Date(date.getTime()));
            project.setPhase(form.getPhase());
            project.setModified(date);
            project.setAuthor(userRepository.findUserByUsername(loggedUser.getUsername()));
            if (form.getFile() == null){
            	logger.debug("Project CreateOrEdit - Null file!");
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
                    	String target = projectImagesFolder + "\\" + form.getName() + "\\";
        				Path path = Paths.get(target + filename);
        				File dir = path.toFile();
        				dir.getParentFile().mkdirs();
        				file.transferTo(path.toFile());
        				logger.debug("FileIO - Saving file:  " + file.getOriginalFilename() + 
        						" as " + filename + 
        						" to " + target.toString());
        				logger.debug("You successfully uploaded " + filename + "!");
        				project.setPicture(file.getOriginalFilename());
        				project.setPictureFile(file.getOriginalFilename());
                    }
                    catch (RuntimeException e) {
                    	logger.debug("You failed to upload " + filename + " => " + e.getMessage());
                    	FieldError fieldError = new FieldError("file", "file", "File too large or not .JPG");
                        result.addError(fieldError);
                        return "view/project/create";
                    	
                    }
                    catch (Exception e) {
                    	logger.debug("You failed to upload " + filename + " => " + e.getMessage());
                    }
                } else {
                	logger.debug("You failed to upload " + filename + " because the file was empty.");                	
                }
            }
            
            projectRepository.saveProject(project);            
            logger.debug("End: createProject form success");
            logger.debug("End: createProject success");        
            return ("redirect:listAll/") ;
        } 
        else {
            logger.debug(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            this.create(model);
            return "view/project/create";

        }
    }
    
    @RequestMapping(value = "/project/getImage/{projectId}")
    public @ResponseBody
    HttpEntity<byte[]> getImage(@PathVariable Long projectId)  {
    	Project project = projectRepository.findProjectById(projectId);
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); //or what ever type it is
        
    	byte[] buffer = new byte[1024];
    	int b;
    	
    	logger.debug("Retrieving picture for projectId = " + projectId);
    	
    	String target = projectImagesFolder + "\\" + project.getName() + "\\" + project.getPictureFile();
		Path path;
		File imageFile;
		if ((projectId == null) || (project.getPictureFile() == null)){
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
			logger.debug("IOException encountered while retrieving file");
			e.printStackTrace();
		}
		catch (Exception e)	{
			logger.debug("Something very bad happened when retrieving file");
			e.printStackTrace();
		}
		return new HttpEntity<byte[]>(buffer, headers);
    }
    
//TODO
    @RequestMapping(value = "/user/get_projects_list",
            method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Object[] getProjectsList(@RequestParam("term") String query) {
        List<String> usernameList = userRepository.findUsername(query);

        return usernameList.toArray();
    }
}
