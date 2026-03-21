package alessioceccarini.tgcapp.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

	@Value("${cloudinary.apikey}")
	private String apiKey;
	@Value("${cloudinary.secret}")
	private String apiSecret;
	@Value("${cloudinary.name}")
	private String cloudinaryName;

	@Bean
	public Cloudinary cloudinary() {
		Map<String, String> config = new HashMap<>();
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		config.put("cloud_name", cloudinaryName);
		return new Cloudinary(config);
	}
}


