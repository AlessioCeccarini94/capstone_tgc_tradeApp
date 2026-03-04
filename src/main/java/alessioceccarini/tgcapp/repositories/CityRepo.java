package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.City;
import alessioceccarini.tgcapp.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CityRepo extends JpaRepository<City, UUID> {
	Optional<City> findByCityName(String name);

	List<City> findByProvince(Province province);

}
