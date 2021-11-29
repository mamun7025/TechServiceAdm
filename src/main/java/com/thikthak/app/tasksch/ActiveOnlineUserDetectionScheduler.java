package com.thikthak.app.tasksch;

import com.thikthak.app.acl.auth.domain.User;
import com.thikthak.app.acl.auth.repository.UserRepository;
import com.thikthak.app.domain.geoloc.UserLocation;
import com.thikthak.app.repository.geoloc.UserLocationRepository;
import com.thikthak.app.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class ActiveOnlineUserDetectionScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    UserRepository userRepository;
    UserLocationRepository userLocationRepository;

    public ActiveOnlineUserDetectionScheduler(UserLocationRepository userLocationRepository, UserRepository userRepository){
        this.userLocationRepository = userLocationRepository;
        this.userRepository = userRepository;
    }


    @Scheduled(fixedDelay = 10000) // 25 second
    public void scheduleTaskWithFixedDelay() {

        logger.info("Active Online User Detection Scheduler :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {

            List<UserLocation> userLocations;
            userLocations = this.userLocationRepository.loadAllUsersLocation("load");

            for(UserLocation userLocation : userLocations){
                // check
                Date thisUserLastActiveTime = userLocation.getLastUpdateDateTime();
                if(thisUserLastActiveTime == null) return;

                // compare
                Date currentTime = new Date();
                long[] result = DateTimeUtil.findDifference2(thisUserLastActiveTime, currentTime);

                long dayDiff = result[0];
                long houDiff = result[1];
                long minDiff = result[2];

                // display
                if(houDiff > 0) minDiff = minDiff + (houDiff * 60);
                if(dayDiff > 0) minDiff = minDiff + (dayDiff * 24 * 60);
                System.out.println("User: " + userLocation.getUser().getUsername());
                System.out.println("In-Active Min: " + minDiff);

                // action
                User thisUser =  userLocation.getUser();
                if(!Objects.isNull(thisUser)){
                    thisUser.setActiveOnline(minDiff > 20); // 20 min in-active
                    this.userRepository.save(thisUser);
                }

            }
            TimeUnit.SECONDS.sleep(5);

        } catch (InterruptedException ex) {
            logger.error(ex.toString());
            throw new IllegalStateException(ex);
        }

    }





}
