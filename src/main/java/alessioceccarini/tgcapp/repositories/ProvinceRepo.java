package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProvinceRepo extends JpaRepository<Province, UUID> {
	Optional<Province> findByShortName(String shortName);

	Optional<Province> findByProvince(String province);

	List<Province> findByRegion(String region);
}
