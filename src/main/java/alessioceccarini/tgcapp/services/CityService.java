package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.City;
import alessioceccarini.tgcapp.repositories.CityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
	private final CityRepo cityRepo;

	@Autowired
	public CityService(CityRepo cityRepo) {
		this.cityRepo = cityRepo;
	}

	public List<City> findAll() {
		return this.cityRepo.findAll();
	}
}
