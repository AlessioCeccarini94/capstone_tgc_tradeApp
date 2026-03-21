package alessioceccarini.tgcapp.tools;


import org.springframework.beans.factory.annotation.Value;


public class EmailSender {
	private String apiKey;
	private String domain;

	public EmailSender(@Value("${mg.apikey}") String apiKey, @Value("${mg.domain}") String domain) {
		this.apiKey = apiKey;
		this.domain = domain;
	}

//	public void sendEmail(User user) { //TODO: METTERE IN FASE DI CREAZIONE NUOVO UTENTE E COMUNICAZIONE TRADE
//
//		HttpResponse<JsonNode> response = Uni(
//				"https://api.mailgun.net/v3/" + this.domain + "/email")
//				.basicAuth("api", this.apiKey)
//				.queryString("to", user.getEmail())
//				.queryString("from", user.getEmail())
//				.queryString("subject", user.getFirstName() + " " + user.getLastName())
//				.queryString("text", user.getFirstName() + " " + user.getLastName()).asJson();
//
//	}
}
