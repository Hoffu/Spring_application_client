package com.example.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ClientApplication {
	public static void main(String[] args) {
		SpringApplication app = new
				SpringApplication(ClientApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}

	private Logger log = LoggerFactory.getLogger(ClientApplication.class);

	@Bean
	public CommandLineRunner process(ConsultationRestClient client){
		return args -> {
			Iterable<Consultation> toDos = client.findAll();
			assert toDos != null;
			toDos.forEach( toDo -> log.info(toDo.toString()));
//			toDos.forEach(toDo -> client.delete(toDo.getId()));
			ParseTxt parseTxt = new ParseTxt();
			for (Consultation consultation : parseTxt.getConsultationsFromFile("C:\\Users\\Hoffu\\IdeaProjects\\client\\schedule.txt")) {
				Consultation newToDo = client.upsert(consultation);
				assert newToDo != null;
				log.info(newToDo.toString());
				Consultation toDo = client.findById(newToDo.getId());
				log.info(toDo.toString());
				Consultation completed = client.setCompleted(newToDo.getId());
				assert completed.isCompleted();
				log.info(completed.toString());
//				client.delete(newToDo.getId());
				assert client.findById(newToDo.getId()) == null;
			}
		};
	}
}

