package com.thikthak.app.repository.geoloc;

import com.thikthak.app.acl.auth.domain.User;
import com.thikthak.app.domain.geoloc.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

// https://thorben-janssen.com/jpa-native-queries/
// *** https://www.logicbig.com/tutorials/spring-framework/spring-data/native-query.html
// **  https://springframework.guru/spring-data-jpa-query/

@SuppressWarnings("SqlDialectInspection")
@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> , JpaSpecificationExecutor<UserLocation> {

        UserLocation getByUser(User user);


        // Good working
        @SuppressWarnings("SqlNoDataSourceInspection")
        @Query(value = "SELECT L.* from USER_LOCATION l inner join AUTH_USER u on l.USER_ID = u.ID WHERE U.ENABLED = 1", nativeQuery = true)
        List<Object[]> loadUsersLocation(@Param("passParam") String passParam);


        // Good working
        @SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
        @Query(value = "SELECT L.* from USER_LOCATION l inner join AUTH_USER u on l.USER_ID = u.ID WHERE U.ENABLED = 1 AND U.USER_TYPE = :userType", nativeQuery = true)
        List<UserLocation> loadUsersLocation2(@Param("userType") String userType);


        // Good working
        @SuppressWarnings("SqlNoDataSourceInspection")
        @Query(value = "SELECT L.* from USER_LOCATION l inner join AUTH_USER u on l.USER_ID = u.ID WHERE U.ENABLED = 1", nativeQuery = true)
        List<UserLocation> loadAllUsersLocation(@Param("passParam") String passParam);


}
