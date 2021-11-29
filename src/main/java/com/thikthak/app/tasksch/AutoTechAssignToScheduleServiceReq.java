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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Configuration
@EnableScheduling
public class AutoTechAssignToScheduleServiceReq {

    UserLocationRepository userLocationRepository;
    ServiceOrdersRepository serviceOrdersRepository;

    UserService userService;
    UserRepository userRepository;
    NotificationRepository notificationRepository;

    Map<Long, Long> failAssignTechMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public AutoTechAssignToScheduleServiceReq(
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





    public Boolean matchScheduleTime(Date scheduleOrderTime){
        if(scheduleOrderTime == null) scheduleOrderTime = new Date();
        Date currentDateTime = new Date(System.currentTimeMillis() - 3600 * 1000); // 1 hour back

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a dd-MMM-yyyy");
        System.out.println("@M scheduleOrderTime:  " + sdf.format(scheduleOrderTime));
        System.out.println("@M currentDateTime:  " + sdf.format(currentDateTime));

        long sTime = scheduleOrderTime.getTime();
        long cTime = currentDateTime.getTime();
        System.out.println("@M scheduleOrderTime ST:  " + sTime);
        System.out.println("@M currentDateTime ST:  " + cTime);
        // Note
        // Schedule time will be hire
        if(sTime == cTime){
            return true;
        } else if(sTime < cTime){
            return true;
        }
//        if ( currentDateTime.before(scheduleOrderTime) ) {
//            System.out.println("@M I am before: scheduleTime");
//            return true;
//        } else if( currentDateTime.equals(scheduleOrderTime) ){
//            System.out.println("@M I am equal: scheduleTime");
//            return true;
//        }
        return false;
    }


    public void callerMethod(){

        try {

            Pageable topTwenty = PageRequest.of(0, 5);
            PlaceService placeService = new PlaceService(this.userLocationRepository);
            List<ServiceOrders> serviceOrdersList = this.serviceOrdersRepository.findAllByIsScheduleOrderAndTechnicianUserIsNull(true, topTwenty);
            /*int k = 0;
            serviceOrdersList.forEach((v)->{
                System.out.println("Item : " + k + " Count : " + v);
            });*/
            for(ServiceOrders serviceOrders : serviceOrdersList){
                System.out.println("Processing order: " + serviceOrders.getOrderCode());

                Double clientLat = serviceOrders.getClientLatitude();
                Double clientLon = serviceOrders.getClientLongitude();
                String serviceItemName = serviceOrders.getServiceItemName();
                Date clientSchTime = serviceOrders.getScheduleDate();

                if(this.matchScheduleTime(clientSchTime)){
                    LinkedHashMap<User, Double> nPeoples = placeService.getNearestPeoples(clientLat, clientLon, 50);
                    // assign technician
                    for( Map.Entry<User, Double> entry : nPeoples.entrySet() ) {
                        User tech = entry.getKey();
                        Double dist = entry.getValue();

                        System.out.println("tech : " + tech.getDisplayName() + " dist : " + dist);
                        String techExpertiseArea = tech.getExpertiseArea();

                        // validation
                        if (serviceItemName == null || serviceItemName.equals("")) continue;
                        if (this.failAssignTechMap != null && this.failAssignTechMap.containsKey(tech.getId())) return;
                        if ((techExpertiseArea == null || techExpertiseArea.equals(""))) {
                            this.failAssignTechMap.put(tech.getId(), serviceOrders.getId());
                            return;
                        }

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

            }
            Thread.sleep(5000);


        } catch (InterruptedException ex) {
            logger.error(ex.toString());
        }

    }

    @SuppressWarnings("ConstantConditions")
    @Scheduled(fixedDelay = 10000) // 25 second
    public void scheduleTaskWithFixedDelay() {

        logger.info("Auto Tech Assign To Schedule Service Req :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
//        this.callerMethod();

    }





}
