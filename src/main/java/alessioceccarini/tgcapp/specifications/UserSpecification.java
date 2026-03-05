package alessioceccarini.tgcapp.specifications;

import alessioceccarini.tgcapp.entities.user_entities.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class UserSpecification {

	public static Specification<User> hasCity(UUID cityId) {
		return (root, query, criteriaBuilder) ->
				criteriaBuilder.equal(root.get("city").get("id"), cityId);
	}

	public static Specification<User> hasProvince(UUID provinceId) {
		return (root, query, criteriaBuilder) ->
				criteriaBuilder.equal(root.get("province").get("id"), provinceId);
	}

	public static Specification<User> hasUsername(String username) {
		return (root, query, criteriaBuilder) ->
				criteriaBuilder.equal(root.get("username").get(username), username);
	}
}
