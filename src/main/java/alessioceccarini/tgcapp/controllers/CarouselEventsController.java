package alessioceccarini.tgcapp.controllers;

import alessioceccarini.tgcapp.entities.EventsCarouselImg;
import alessioceccarini.tgcapp.services.CarouselImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/carousels")
public class CarouselEventsController {

	private final CarouselImgService carouselImgService;

	@Autowired
	public CarouselEventsController(CarouselImgService carouselImgService) {
		this.carouselImgService = carouselImgService;
	}

	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public List<EventsCarouselImg> getAllCarouselImg() {
		return carouselImgService.getAll();
	}

	@PostMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public EventsCarouselImg newCarouselImg(@RequestBody EventsCarouselImg eventsCarouselImg) {
		return carouselImgService.createNewCarouselImg(eventsCarouselImg);
	}

	@PatchMapping("/{id}")
	public EventsCarouselImg updateCarouselImg(@PathVariable UUID id, @RequestParam MultipartFile file) throws IOException {
		return carouselImgService.updateCarouselImg(id, file);
	}

	@DeleteMapping("/{id}")
	public void deleteCarouselImg(@PathVariable UUID id) {
		carouselImgService.deleteCarouselImg(id);
	}
}
