package fr.sparna.validator;

import java.io.File;
import java.io.IOException;
<<<<<<< HEAD
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import java.util.spi.ResourceBundleControlProvider;

import org.openrdf.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
=======
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.Rio;
>>>>>>> d5219b1f73d64c75d1a649ee5cd545e9467b6e72
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import at.ac.univie.mminf.qskos4j.issues.Issue;

@Controller
public class SkosValidatorController {

<<<<<<< HEAD
	@Autowired
	private ApplicationData applicationData;


=======
>>>>>>> d5219b1f73d64c75d1a649ee5cd545e9467b6e72
	@RequestMapping(value = "home")
	public ModelAndView upload(
			@RequestParam(value="lang", required=false) String lang,
			HttpServletRequest request
	) throws IOException{

		ValidatorData data = new ValidatorData();
<<<<<<< HEAD
		ResourceBundle b = ResourceBundle.getBundle(
							"Bundle",
							Locale.getDefault());
					
					
		if(lang!=null){
			Locale.setDefault(new Locale(lang));
=======
		if(lang!=null) {
			SessionData sessionData = new SessionData();
			sessionData.setUserLocale(lang);
			sessionData.store(request.getSession());
		} else {
			SessionData sessionData = new SessionData();
			String userLanguage = request.getLocale().getLanguage();
			if(userLanguage.startsWith("fr")) {
				sessionData.setUserLocale("fr");
			} else {
				sessionData.setUserLocale("en");
			}
			sessionData.store(request.getSession());
>>>>>>> d5219b1f73d64c75d1a649ee5cd545e9467b6e72
		}
		return new ModelAndView("home", ValidatorData.KEY, data);
	}

	@RequestMapping(value = "result")
	public ModelAndView uploadResult(
			@RequestParam(value="file", required=true) MultipartFile file,
			@RequestParam(value="rulesChoice", required=false) String choice,
			HttpServletRequest request
	) throws RepositoryException {

		ValidatorData data = new ValidatorData();		

		if(choice!=null){
			data.extractAndSetChoice(choice);
		}		

		try {
			ValidateSkosFile skos=new ValidateSkosFile(choice.replaceAll("-", ","));			
			Collection<Issue> qSkosResult = skos.validate(file.getInputStream(), Rio.getWriterFormatForFileName(file.getOriginalFilename()));
			Process process = new Process(SessionData.retrieve(request.getSession()).getUserLocale());
			data.setErrorList(process.createReport(qSkosResult));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			data.setMsg(e.getMessage());
			return new ModelAndView("home", ValidatorData.KEY, data);
		}

		return new ModelAndView("result", ValidatorData.KEY, data);
	}

	@Deprecated
	public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
		File convFile = new File( multipart.getOriginalFilename());
		multipart.transferTo(convFile);
		return convFile;
	}

}