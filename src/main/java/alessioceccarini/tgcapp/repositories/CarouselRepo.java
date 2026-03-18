package alessioceccarini.tgcapp.repositories;

import alessioceccarini.tgcapp.entities.EventsCarouselImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarouselRepo extends JpaRepository<EventsCarouselImg, UUID> {

}
