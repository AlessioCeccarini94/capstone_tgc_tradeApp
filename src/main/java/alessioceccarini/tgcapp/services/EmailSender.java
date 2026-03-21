package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.payloads.UserDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EmailSender {

	private final String apiKey;
	private final String domain;
	private final String from;
	private final String baseUrl;

	public EmailSender(
			@Value("${mg.apikey}") String apiKey,
			@Value("${mg.domain}") String domain,
			@Value("${mg.from}") String from,
			@Value("${mg.base-url}") String baseUrl
	) {
		this.apiKey = apiKey;
		this.domain = domain;
		this.from = from;
		this.baseUrl = baseUrl;
	}

	public String sendWelcomeEmail(UserDTO user) throws UnirestException {
		String userinfo = user.username();

		if (user.firstName() != null && !user.firstName().isBlank()) {
			userinfo = user.firstName();

			if (user.lastName() != null && !user.lastName().isBlank()) {
				userinfo += " " + user.lastName();
			}
		}

		String url = baseUrl + "/v3/" + domain + "/messages";

		HttpResponse<String> request = Unirest.post(url)
				.basicAuth("api", apiKey)
				.field("from", "TCG Trade <" + from + ">")
				.field("to", user.email())
				.field("subject", "Welcome " + userinfo)
				.field("html",
						"<div style='font-family: Arial; text-align:center;'>"
								+ "<img src='https://i.pinimg.com/736x/f9/16/52/f91652de1ebcf7ea875d8e5422803f1c.jpg' width='120' style='margin-bottom:20px;'/>"
								+ "<h2>Welcome " + userinfo + "</h2>"
								+ "<p>Your account has been created successfully!</p>"
								+ "<a href='https://google.com' "
								+ "style='display:inline-block; padding:10px 20px; background:#007bff; color:white; text-decoration:none; border-radius:5px;'>"
								+ "Go to Login"
								+ "</a>"
								+ "</div>"
				)
				.asString();

		if (request.getStatus() >= 400) {
			throw new RuntimeException("Mailgun Error: " + request.getBody());
		}

		return request.getBody();
	}
}