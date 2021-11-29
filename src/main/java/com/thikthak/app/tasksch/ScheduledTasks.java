package com.thikthak.app.tasksch;

import com.thikthak.app.acl.auth.domain.User;
import com.thikthak.app.repository.geoloc.UserLocationRepository;
import com.thikthak.app.util.place.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// https://www.callicoder.com/spring-boot-task-scheduling-with-scheduled-annotation/


@Configuration
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    UserLocationRepository userLocationRepository;


    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    public void scheduleTaskWithFixedRate() {}


//    @Scheduled(fixedDelay = 5000) // 5 second
    @Scheduled(fixedDelay = 30000) // 5 second
    public void scheduleTaskWithFixedDelay() {

        logger.info("Fixed Delay Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {

            double clientLat = 	23.810331;
            double clientLon = 	90.412521;
            PlaceService placeService = new PlaceService(this.userLocationRepository);
            Map<User, Double> nPeoples = placeService.getNearestPeoples(clientLat, clientLon, 5);
            System.out.println(nPeoples);

            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            logger.error("Ran into an error {}", ex);
            throw new IllegalStateException(ex);
        }

    }



    public void scheduleTaskWithInitialDelay() {}

    public void scheduleTaskWithCronExpression() {}



}
