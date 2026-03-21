package alessioceccarini.tgcapp.services;

import alessioceccarini.tgcapp.entities.EventsCarouselImg;
import alessioceccarini.tgcapp.exceptions.NotFoundException;
import alessioceccarini.tgcapp.repositories.CarouselRepo;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class CarouselImgService {

	private final CarouselRepo carouselRepo;
	private final Cloudinary cloudinary;

	@Autowired
	public CarouselImgService(CarouselRepo carouselRepo, Cloudinary cloudinary) {
		this.carouselRepo = carouselRepo;
		this.cloudinary = cloudinary;
	}

	//----------------------------------------------- G E T ------------------------------------------------------------

	public List<EventsCarouselImg> getAll() {
		return carouselRepo.findAll();
	}

	public EventsCarouselImg getById(UUID id) {
		return carouselRepo.findById(id).orElseThrow(() -> new NotFoundException("Carousel Img Not Found"));
	}

	//----------------------------------------------- P O S T ----------------------------------------------------------

	public EventsCarouselImg createNewCarouselImg(EventsCarouselImg eventsCarouselImg) {
		return carouselRepo.save(eventsCarouselImg);
	}

	//---------------------------------------------- P A T C H ---------------------------------------------------------

	public EventsCarouselImg updateCarouselImg(UUID uuid, MultipartFile file) throws IOException {

		EventsCarouselImg carouselImg = this.getById(uuid);

		String newImg = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
		carouselImg.setImage(newImg);
		return carouselRepo.save(carouselImg);
	}

	//---------------------------------------------- D E L E T E -------------------------------------------------------

	public void deleteCarouselImg(UUID id) {
		EventsCarouselImg eventsCarouselImg = this.getById(id);
		carouselRepo.delete(eventsCarouselImg);
	}
}
