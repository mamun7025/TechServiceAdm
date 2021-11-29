package com.thikthak.app.tasksch;

import com.thikthak.app.acl.auth.domain.User;
import com.thikthak.app.acl.auth.repository.UserRepository;
import com.thikthak.app.acl.auth.service.UserService;
import com.thikthak.app.domain.geoloc.UserLocation;
import com.thikthak.app.domain.service.ServiceOrders;
import com.thikthak.app.repository.comn.NotificationRepository;
import com.thikthak.app.repository.geoloc.UserLocationRepository;
import com.thikthak.app.repository.service.ServiceOrdersRepository;
import com.thikthak.app.util.NotificationMgrService;
import com.thikthak.app.util.place.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Configuration
@EnableScheduling
public class AutoTechAssignToServiceReq {

    UserLocationRepository userLocationRepository;
    ServiceOrdersRepository serviceOrdersRepository;

    UserService userService;
    UserRepository userRepository;
    NotificationRepository notificationRepository;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public AutoTechAssignToServiceReq(
            UserLocationRepository userLocationRepository,
            ServiceOrdersRepository serviceOrdersRepository,
            UserService userService,
            UserRepository userRepository,
            NotificationRepository notificationRepository
    ){
        this.userLocationRepository = userLocationRepository;
        this.serviceOrdersRepository = serviceOrdersRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }


    @SuppressWarnings("Convert2Lambda")
    public void sendAckNotification(ServiceOrders serviceOrders){

        // Send message notification by thread Start ---------------------------------------------------------------
        UserService userS = this.userService;
        UserRepository userR = this.userRepository;
        NotificationRepository notR = this.notificationRepository;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                // prepare message params for client
                Map<String, Object> params = new HashMap<>();
                params.put("title", "Technician Assign Alert !");
                params.put("message", "Technician assign to your order: " + serviceOrders.getOrderCode() + "\nTechnician name: " + serviceOrders.getTechnicianUser().getDisplayName());
                params.put("orderCode", serviceOrders.getOrderCode());
                params.put("user", serviceOrders.getClientUser().getId());
                params.put("userName", serviceOrders.getClientUser().getUsername());
                params.put("status", serviceOrders.getStatus());
                params.put("isPromotional", false);
                params.put("timestamp", new Date().getTime() );
                String deviceToken = serviceOrders.getClientUser().getDeviceToken();

                // prepare message params for technician
                Map<String, Object> params2 = new HashMap<>();
                params2.put("title", "New Order Assign Alert !");
                params2.put("message", "New order has been assign to you, code: " + serviceOrders.getOrderCode() + "\nClient name: " + serviceOrders.getClientUser().getDisplayName());
                params2.put("orderCode", serviceOrders.getOrderCode());
                params2.put("user", serviceOrders.getTechnicianUser().getId());
                params2.put("userName", serviceOrders.getTechnicianUser().getUsername());
                params2.put("status", serviceOrders.getStatus());
                params2.put("isPromotional", false);
                params2.put("timestamp", new Date().getTime() );
                String deviceTokenTech = serviceOrders.getTechnicianUser().getDeviceToken();

                try {
                    NotificationMgrService nInst = new NotificationMgrService(notR, userS, userR);
                    // client
                    nInst.sendNotification( nInst.prepareMgsParams(params), deviceToken);
                    // technician
                    nInst.sendNotification( nInst.prepareMgsParams(params2), deviceTokenTech);

                } catch (Exception e){
                    System.out.println(e.toString());
                }

            }
        });
        t.start();
        // Send message notification by thread End -----------------------------------------------------------------

    }



    private void callerMethod(){

        try {

            PlaceService placeService = new PlaceService(this.userLocationRepository);
            Pageable topTwenty = PageRequest.of(0, 5);
            List<ServiceOrders> serviceOrdersList = this.serviceOrdersRepository.findAllByIsScheduleOrderFalseAndTechnicianUserIsNull(topTwenty);
            /*int k = 0;
            serviceOrdersList.forEach((v)->{
                System.out.println("Item : " + k + " Count : " + v);
            });*/
            for(ServiceOrders serviceOrders : serviceOrdersList){
                System.out.println("Processing order: " + serviceOrders.getOrderCode());

                Double clientLat = serviceOrders.getClientLatitude();
                Double clientLon = serviceOrders.getClientLongitude();
                String serviceItemName = serviceOrders.getServiceItemName();
                LinkedHashMap<User, Double> nPeoples = placeService.getNearestPeoples(clientLat, clientLon, 50);

                // assign technician
                for( Map.Entry<User, Double> entry : nPeoples.entrySet() ) {
                    User tech = entry.getKey();
                    Double dist = entry.getValue();

                    System.out.println("tech : " + tech.getDisplayName() + " dist : " + dist);

                    String techExpertiseArea = tech.getExpertiseArea();
                    // validation
                    if (techExpertiseArea == null || techExpertiseArea.equals("")) continue;
                    if (serviceItemName == null || serviceItemName.equals("")) continue;
                    System.out.println("@techExpertiseArea --- "+techExpertiseArea);
                    System.out.println("@serviceItemName --- "+serviceItemName);

                    // check technician acceptance limit
                    Integer pendingJobLimit = tech.getPendingJobLimit();
                    Integer numOfPendingJob = tech.getNumOfPendingJob();
                    if(numOfPendingJob > pendingJobLimit) continue;

                    if (techExpertiseArea.trim().contains(serviceItemName)) {
                        serviceOrders.setStatus(0);
                        serviceOrders.setTechnicianUser(tech);
                        serviceOrders.setTechnicianUserName(tech.getDisplayName());
                        UserLocation techLoc = this.userLocationRepository.getByUser(tech);
                        if (Objects.nonNull(techLoc)) {
                            serviceOrders.setTechLatitude(techLoc.getLatitude());
                            serviceOrders.setTechLongitude(techLoc.getLongitude());
                        }
                        this.serviceOrdersRepository.save(serviceOrders);
                        this.sendAckNotification(serviceOrders);
                        System.out.println("Assign order : " + serviceOrders.getOrderCode() + " to technician : " + tech.getDisplayName() + ", " + tech.getUsername());
                        return; // order assigned, skip next queue technician
                    }
                }

            }
            TimeUnit.SECONDS.sleep(5);

        } catch (InterruptedException ex) {
            logger.error(ex.toString());
            throw new IllegalStateException(ex);
        }

    }



    @Scheduled(fixedDelay = 10000) // 25 second
    public void scheduleTaskWithFixedDelay() {

        logger.info("Auto Tech Assign To Service Req :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
//        this.callerMethod();

    }




}
