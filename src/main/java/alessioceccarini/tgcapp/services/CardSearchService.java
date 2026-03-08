//package alessioceccarini.tgcapp.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class CardSearchService {
//
//	private final RestTemplate restTemplate;
//
//	@Value("${cardtrader.apikey}")
//	private String apikey;
//
//	@Autowired
//	public CardSearchService(RestTemplate restTemplate) {
//		this.restTemplate = restTemplate;
//	}
//
//	public List<Map<String, Object>> searchCards(String name) {
//
//		String URL = "https://api.cardtrader.com/api/v2/blueprints?q=" + name;
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Authorization", "Bearer " + apikey);
//
//		HttpEntity<String> entity = new HttpEntity<>(headers);
//
//		ResponseEntity<List<Map<String, Object>>> response =
//				restTemplate.exchange(
//						URL,
//						HttpMethod.GET,
//						entity,
//						new ParameterizedTypeReference<>() {
//						}
//				);
//
//		return response.getBody().forEach(card ->
//				System.out.println(card.get("name")));
//
/// /				.stream()
/// /				.filter(card -> card.get("name")
/// /						.toString()
/// /						.toLowerCase()
/// /						.contains(name.toLowerCase())
/// /				).toList();
//	}
//}
