package alessioceccarini.tgcapp.specifications;

import alessioceccarini.tgcapp.entities.Card;
import org.springframework.data.jpa.domain.Specification;

public class CardSpecification {
	public static Specification<Card> hasGame(Long gameId) {
		return (root, query, cb) -> {
			if (gameId == null) return cb.conjunction();
			return cb.equal(root.join("expansion").join("game").get("id"), gameId);
		};
	}

	public static Specification<Card> hasExpansion(Long expansionId) {
		return (root, query, cb) -> {
			if (expansionId == null) return cb.conjunction();
			return cb.equal(root.join("expansion").get("cardTraderId"), expansionId);
		};
	}

	public static Specification<Card> priceGreaterThan(Double price) {
		return (root, criteriaQuery, criteriaBuilder) ->
		{
			if (price < 0) return criteriaBuilder.conjunction();
			return criteriaBuilder.greaterThanOrEqualTo(root.get("avgPrice"), price);
		};
	}

	public static Specification<Card> priceLessThan(Double price) {
		return (root, criteriaQuery, criteriaBuilder) ->
		{
			if (price < 0) return criteriaBuilder.conjunction();
			return criteriaBuilder.lessThanOrEqualTo(root.get("avgPrice"), price);
		};
	}
}
