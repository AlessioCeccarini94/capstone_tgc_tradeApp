package alessioceccarini.tgcapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CardTraderService {

	@Value("${cardtrader.apikey}")
	private String apikey;

	private RestTemplate restTemplate;

	@Autowired
	public CardTraderService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public Double getAvgPrice(Long blueprintId) {

		String url = "https://api.cardtrader.com/api/v2/marketplace/products?blueprint_id=" + blueprintId;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + apikey);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {

			ResponseEntity<Map<String, Object>> response =
					restTemplate.exchange(
							url,
							HttpMethod.GET,
							entity,
							new ParameterizedTypeReference<>() {
							}
					);

			Map<String, Object> body = response.getBody();
			if (body == null) return null;

			List<Map<String, Object>> listings =
					(List<Map<String, Object>>) body.get(blueprintId.toString());

			if (listings == null || listings.isEmpty()) return null;

			Map<String, Object> firstListing = listings.get(0);

			Number cents = (Number) firstListing.get("price_cents");

			return cents.doubleValue() / 100;

		} catch (Exception e) {
			return null;
		}
	}
}
