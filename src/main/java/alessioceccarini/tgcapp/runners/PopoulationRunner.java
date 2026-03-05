package alessioceccarini.tgcapp.runners;

import alessioceccarini.tgcapp.entities.user_entities.City;
import alessioceccarini.tgcapp.entities.user_entities.Province;
import alessioceccarini.tgcapp.repositories.CityRepo;
import alessioceccarini.tgcapp.repositories.ProvinceRepo;
import alessioceccarini.tgcapp.services.CardImporterService;
import alessioceccarini.tgcapp.services.UserService;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class PopoulationRunner implements CommandLineRunner {

	private final UserService userService;
	private final CityRepo cityRepo;
	private final ProvinceRepo provinceRepo;
	private final CardImporterService cardImporterService;

	@Value("${admin.firstName}")
	private String adminFirstName;
	@Value("${admin.lastName}")
	private String adminLastName;
	@Value("${admin.email}")
	private String adminEmail;
	@Value("${admin.username}")
	private String adminUsername;
	@Value("${admin.password}")
	private String adminPassword;

	@Autowired
	public PopoulationRunner(UserService userService, PasswordEncoder passwordEncoder, CityRepo cityRepo, ProvinceRepo provinceRepo, CardImporterService cardImporterService) {
		this.userService = userService;
		this.cityRepo = cityRepo;
		this.provinceRepo = provinceRepo;
		this.cardImporterService = cardImporterService;


	}

	@Override
	public void run(String @NonNull ... args) throws Exception {

		//-------------------- CREATE PROVINCES AND CITIES ------------------------

		if (provinceRepo.count() == 0) {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("province-italiane.csv");
			try {
				assert inputStream != null;
				CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream))
						.withSkipLines(1).withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build();

				String[] line;
				while ((line = reader.readNext()) != null) {
					Province province = new Province(line[0], line[1], line[2]);
					provinceRepo.save(province);
				}
			} catch (Exception e) {
				e.printStackTrace();
				e.getMessage();
				System.out.println(e.getMessage());
			}
			try {
				InputStream inputStream1 = getClass().getClassLoader().getResourceAsStream("comuni-italiani.csv");

				assert inputStream1 != null;
				CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream1))
						.withSkipLines(1)
						.withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build();
				String[] line;
				while ((line = reader.readNext()) != null) {
					while ((line = reader.readNext()) != null) {
						if (line[3].equals("Verbano-Cusio-Ossola")) line[3] = ("Verbania");
						if (line[3].equals("Valle d'Aosta/Vallée d'Aoste")) line[3] = ("Aosta");
						if (line[3].equals("Monza e della Brianza")) line[3] = ("Monza-Brianza");
						if (line[3].equals("Bolzano/Bozen")) line[3] = ("Bolzano");
						if (line[3].equals("La Spezia")) line[3] = ("La-Spezia");
						if (line[3].equals("Reggio nell'Emilia")) line[3] = ("Reggio-Emilia");
						if (line[3].equals("Forlì-Cesena")) line[3] = ("Forli-Cesena");
						if (line[3].equals("Pesaro e Urbino")) line[3] = ("Pesaro-Urbino");
						if (line[3].equals("Ascoli Piceno")) line[3] = ("Ascoli-Piceno");
						if (line[3].equals("Reggio Calabria")) line[3] = ("Reggio-Calabria");
						if (line[3].equals("Vibo Valentia")) line[3] = ("Vibo-Valentia");
						if (line[3].equals("Sud Sardegna")) line[3] = ("Carbonia Iglesias");
						Province province = provinceRepo.findByProvince(line[3]).orElseThrow(() -> new IllegalArgumentException("Province not found"));
						City city = new City(line[2], province);
						cityRepo.save(city);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}

		//----------------------------- CREATE ADMIN -----------------------------

		if (!userService.existsByEmail(adminEmail)) {
			userService.createAdmin(adminFirstName, adminLastName, adminUsername, adminEmail, adminPassword);
			System.out.println(adminEmail + " has been created");
		}

		//-------------------------------------------- CARD IMPORT---------------------------------

		cardImporterService.importCards();

	}
}
