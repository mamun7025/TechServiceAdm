package com.thikthak.app.tasksch;

import com.thikthak.app.acl.auth.domain.User;
import com.thikthak.app.acl.auth.repository.UserRepository;
import com.thikthak.app.repository.geoloc.UserLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("SqlNoDataSourceInspection")
@Configuration
@EnableScheduling
public class TechnicianActivitySetterScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    UserRepository userRepository;
    UserLocationRepository userLocationRepository;
    EntityManagerFactory emf;

    public TechnicianActivitySetterScheduler( UserRepository userRepository,
                                              UserLocationRepository userLocationRepository,
                                              EntityManagerFactory emf){
        this.userRepository = userRepository;
        this.userLocationRepository = userLocationRepository;
        this.emf = emf;
    }


    // necessary methods...................................

    public List<User> getActiveOnlineTechnicians(){
        List<User> activeTechs;
        activeTechs = userRepository.findByActiveOnlineAndUserType(true, "Technician");
        return activeTechs;
    }


    @SuppressWarnings({"SqlDialectInspection", "unchecked"})
    public Integer getNumOfCompleteJob(User activeTech){

        int jobNumber;
        BigDecimal jobNumberBd = BigDecimal.valueOf(0.00);

        String sqlString = "" +
                "SELECT\n" +
                "    COUNT(*) AS total_order_rcv\n" +
                "FROM\n" +
                "    service_orders\n" +
                "WHERE\n" +
                "    technician_user_id = "+activeTech.getId()+"";
//        System.out.println(sqlString);

        EntityManager em = this.emf.createEntityManager();
        try {

            Query query3 = em.createNativeQuery(sqlString);
            List<Object[]> results = (List<Object[]>)query3.getResultList();
            for (Object result : results) {
                jobNumberBd = (BigDecimal)result;
            }

        } catch ( NoResultException e ){
            return null;
        } finally {
            em.close();
        }

        // System.out.println(jobNumberBd);
        jobNumber = jobNumberBd.intValue();
        return jobNumber;

    }



    @SuppressWarnings({"SqlDialectInspection", "unchecked"})
    public Integer getNumOfPendingJob(User activeTech){

        int jobNumber;
        BigDecimal jobNumberBd = BigDecimal.valueOf(0.00);

        String sqlString = "" +
                "SELECT\n" +
                "    COUNT(*) AS total_order_rcv\n" +
                "FROM\n" +
                "    service_orders\n" +
                "WHERE\n" +
                "    (status = 1 OR status = 0 OR status = 3 OR status is null )\n" +
                "    AND technician_user_id = "+activeTech.getId()+"";
//        System.out.println(sqlString);

        EntityManager em = this.emf.createEntityManager();
        try {

            Query query3 = em.createNativeQuery(sqlString);
            List<Object[]> results = (List<Object[]>)query3.getResultList();
            for (Object result : results) {
                jobNumberBd = (BigDecimal)result;
            }

        } catch ( NoResultException e ){
            return null;
        } finally {
            em.close();
        }

        // System.out.println(jobNumberBd);
        jobNumber = jobNumberBd.intValue();
        return jobNumber;

    }




    // Note:
    // this scheduler only for is for active online technician
    // for others, calculate and set during profile page visit
    // do this by calling method, for using from other classes
    /*
    Integer numOfCmplJob;
    Integer numOfPendingJob;
    Integer pendingJobLimit;        // come from configuration
    Integer dailyJobAssignLimit;    // come from configuration
    */

    @Scheduled(fixedDelay = 25000) // 25 second
    public void scheduleTaskWithFixedDelay() {

        logger.info("Technician Activity Setter Scheduler :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {

            List<User> activeTechs;
            activeTechs = this.getActiveOnlineTechnicians();
            for (User activeTech : activeTechs){
                Integer numOfCmplJob = this.getNumOfCompleteJob(activeTech);
                Integer numOfPndJob = this.getNumOfPendingJob(activeTech);
                activeTech.setNumOfCmplJob(numOfCmplJob);
                activeTech.setNumOfPendingJob(numOfPndJob);
                this.userRepository.save(activeTech);
            }
            TimeUnit.SECONDS.sleep(5);

        } catch (InterruptedException ex) {
            logger.error(ex.toString());
            throw new IllegalStateException(ex);
        }

    }




}
