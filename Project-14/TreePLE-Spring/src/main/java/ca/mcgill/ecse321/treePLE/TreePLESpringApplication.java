package ca.mcgill.ecse321.treePLE;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import ca.mcgill.ecse321.treePLE.controller.configuration.AndroidProperties;
import ca.mcgill.ecse321.treePLE.controller.configuration.WebFrontendProperties;
import ca.mcgill.ecse321.treePLE.model.CarbonSequestrationManager;
import ca.mcgill.ecse321.treePLE.model.TreePLEManager;
import ca.mcgill.ecse321.treePLE.persistence.PersistenceDensity;
import ca.mcgill.ecse321.treePLE.persistence.PersistenceXStream;

@SpringBootApplication
public class TreePLESpringApplication extends SpringBootServletInitializer {	

	@Autowired
	private AndroidProperties androidProperties;

	@Autowired
	private WebFrontendProperties webFrontendProperties;
	
	public static void main(String[] args) {
		SpringApplication.run(TreePLESpringApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		// Let the model matcher map corresponding fields by name
		modelMapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
		modelMapper.getConfiguration().setSourceNamingConvention(NamingConventions.NONE)
				.setDestinationNamingConvention(NamingConventions.NONE);
		return modelMapper;
	}
	
	//Bean to provide a TreePLE manager
	@Bean
	public TreePLEManager treeMan() {
		return PersistenceXStream.initializeModelManager(PersistenceXStream.getFilename());
	}
	
	@Bean
	public CarbonSequestrationManager csm() {
		return PersistenceDensity.initializeModelManager(PersistenceDensity.getFilename());
	}
		
	// Enable CORS globally
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// Allow web client
				String frontendUrl = "http://" + webFrontendProperties.getIp() + ":" + webFrontendProperties.getPort();
				// Allow android client
				String androidUrl = "http://" + androidProperties.getIp() + ":" + androidProperties.getPort();
				// For debug purposes, allow connecting  from localhost as well
				registry.addMapping("/**");
			}
		};
	}
}
