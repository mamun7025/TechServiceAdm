package com.thikthak.app.acl.auth.domain;

import com.thikthak.app.domain.base.Organization;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="AUTH_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator="UserPkIdSeq")
    @SequenceGenerator(name="UserPkIdSeq",sequenceName="USER_PKID_SEQ", allocationSize=5)
    @Column(name = "ID")
    private Long id; // userId

    @Size(max = 15)
    @NotEmpty
    @NotBlank(message = "*Name is mandatory")
    @Column(name = "USERNAME", length = 15, nullable = false, unique = true)
    private String username;
    @NotBlank(message = "*Password is mandatory")
    @Column(name = "PASSWORD")
    private String password;
    @Value("${true}")
    @Column(columnDefinition = "boolean default true")
    private boolean enabled;
//    private boolean enabled = true;
    private boolean accountExpired;
    boolean accountLocked;
    boolean passwordExpired;

    // added attributes
    String phoneNumber;          // as username // maximum length of 15 digits
    String firstName;
    String lastName;
    @Column(nullable=false)
    String displayName;          // marge firstName and lastName
    String profession;

    @Email(message="{errors.invalid_email}")
    String email;                // [user]@[mysite].com = 64 + 255, but it should be 254
    String city;                 // [Dhaka, Chattogram, Sylhet...]
    String fullAddress;
    String userType;             // Group Checkbox [client, technician-default technician now]
    String expertiseArea;        // [Electronics, Electrical, Software, Mechanical.....]
    String expertiseKeywords;    // [Laptop, Mobile, TV]
    String gender;               // Optional
    Date birthDate;              // Optional
    Double targetEarningPerMonth;// Optional (25000, 35000, 50000... --- Inspirational )
    // securityCode ------------ 4 digit code sent to mobile and need put this input box
    Double rating;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date registrationDate;
    Boolean isApproved;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date approvalDate;

    String deviceType;
    String deviceToken;
    Boolean activeOnline;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    private Organization organization;


    @Column(name = "PROFILE_PIC_PATH", length = 300)
    String profilePicPath;

    @Column(name = "PROFILE_PIC_PATH2", length = 300)
    String profilePicPath2;


    // below attributes only for technician
    @Column(name = "NUM_OF_CMPL_JOB")
    Integer numOfCmplJob;          // cmplTaskCounter // all completedTaskCounter
    //    @Column(name = "TODAY_CMPL_TASK_COUNTER")
//    Integer todayCmplTaskCounter;   // todayCompletedTaskCounter
//    @Column(name = "DAILY_TASK_CR_LIMIT")
//    Integer dailyTaskCrLimit;       // maximum capability of a day // it make Technician Busy / Available
//    @Column(name = "DAILY_UNCMPL_TASK_CR_LIMIT")
//    Integer dailyUncmplTaskCrLimit; // dailyUncompleteTaskCrLimit // todayNumOfIncompleteTask // maximum capability of a day // it make Technician Busy / Available
    @Column(name = "NUM_OF_PENDING_JOB")
    Integer numOfPendingJob;
    @Column(name = "PENDING_JOB_LIMIT")
    Integer pendingJobLimit;
//    @Column(name = "TODAY_JOB_TAG_NO")
//    Integer todayJobTagNo; // use later if needed
    @Column(name = "DAILY_JOB_ASSIGN_LIMIT")
    Integer dailyJobAssignLimit; // may be default 10 job // Integer dailyTaskCrLimit; // maximum capability of a day // it make Technician Busy / Available
    Boolean isBusy;


    public Integer getNumOfCmplJob() {
        return numOfCmplJob;
    }

    public void setNumOfCmplJob(Integer numOfCmplJob) {
        this.numOfCmplJob = numOfCmplJob;
    }
    public Integer getNumOfPendingJob() {
        return numOfPendingJob;
    }

    public void setNumOfPendingJob(Integer numOfPendingJob) {
        this.numOfPendingJob = numOfPendingJob;
    }

    public Integer getPendingJobLimit() {
        return pendingJobLimit;
    }

    public void setPendingJobLimit(Integer pendingJobLimit) {
        this.pendingJobLimit = pendingJobLimit;
    }

    public Integer getDailyJobAssignLimit() {
        return dailyJobAssignLimit;
    }

    public void setDailyJobAssignLimit(Integer dailyJobAssignLimit) {
        this.dailyJobAssignLimit = dailyJobAssignLimit;
    }


    public Boolean getBusy() {
        return isBusy;
    }

    public void setBusy(Boolean busy) {
        isBusy = busy;
    }



    public Boolean getActiveOnline() {
        return activeOnline;
    }

    public void setActiveOnline(Boolean activeOnline) {
        this.activeOnline = activeOnline;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getExpertiseArea() {
        return expertiseArea;
    }

    public void setExpertiseArea(String expertiseArea) {
        this.expertiseArea = expertiseArea;
    }

    public String getExpertiseKeywords() {
        return expertiseKeywords;
    }

    public void setExpertiseKeywords(String expertiseKeywords) {
        this.expertiseKeywords = expertiseKeywords;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Double getTargetEarningPerMonth() {
        return targetEarningPerMonth;
    }

    public void setTargetEarningPerMonth(Double targetEarningPerMonth) {
        this.targetEarningPerMonth = targetEarningPerMonth;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean approved) {
        isApproved = approved;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
    public String getProfession() {
        return profession;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }


//    @ManyToMany(cascade=CascadeType.MERGE)
//    @ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(
//            name="auth_user_role",
//            joinColumns={@JoinColumn(name="user_id", referencedColumnName="id")},
//            inverseJoinColumns={@JoinColumn(name="role_id", referencedColumnName="id")}
//            )
//    private List<Role> roles;
    @ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "auth_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public String getProfilePicPath2() {
        return profilePicPath2;
    }

    public void setProfilePicPath2(String profilePicPath2) {
        this.profilePicPath2 = profilePicPath2;
    }

    @Override
    public String toString() {
        return "UserEntity [id=" + id + ", username=" + username +
                ", displayName=" + displayName + ", email=" + email   + "]";
    }




}
