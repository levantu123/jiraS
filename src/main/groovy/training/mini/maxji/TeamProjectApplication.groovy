package training.mini.maxji

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class TeamProjectApplication {

	static void main(String[] args) {
		
		if(System.getProperty("appPass").equals("123")) {
			SpringApplication.run(TeamProjectApplication, args)
		}
	}

}
