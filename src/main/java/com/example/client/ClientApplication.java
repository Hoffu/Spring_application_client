package com.example.client;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.Temporal;
import java.util.GregorianCalendar;
import java.util.UUID;


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
//			ParseTxt parseTxt = new ParseTxt();
//			for (Consultation consultation : parseTxt.getConsultationsFromFile("C:\\Users\\Hoffu\\IdeaProjects\\client\\schedule.txt")) {
//				Consultation newToDo = client.upsert(consultation);
//				assert newToDo != null;
//				log.info(newToDo.toString());
//				Consultation toDo = client.findById(newToDo.getId());
//				log.info(toDo.toString());
//				Consultation completed = client.setCompleted(newToDo.getId());
//				assert completed.isCompleted();
//				log.info(completed.toString());
////				client.delete(newToDo.getId());
//				assert client.findById(newToDo.getId()) == null;
//			}

			Calendar icsCalendar = new Calendar();
			icsCalendar.add(new ProdId("-//Consultations Calendar//iCal4j 1.0//EN"));
			icsCalendar.add(Version.VERSION_2_0);
			icsCalendar.add(CalScale.GREGORIAN);

			toDos.forEach( toDo -> {
				VEvent meeting = new VEvent();
				Summary summary = new Summary(toDo.getDescription());
				Categories categories = new Categories("Консультация");
				LocalDateTime startDate = toDo.getDate();
				LocalDateTime endDate = toDo.getDate();
				meeting.add(new DtStart<Temporal>(startDate));
				meeting.add(new DtEnd<Temporal>(endDate));
				meeting.add(summary);
				meeting.add(categories);

				RandomUidGenerator rug = new RandomUidGenerator();
				meeting.add(rug.generateUid());

				icsCalendar.add(meeting);
			});
			System.out.println(icsCalendar);
			try {
				FileOutputStream fout = new FileOutputStream("mycalendar.ics");
				CalendarOutputter outputter = new CalendarOutputter();
				outputter.output(icsCalendar, fout);
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
	}
}

