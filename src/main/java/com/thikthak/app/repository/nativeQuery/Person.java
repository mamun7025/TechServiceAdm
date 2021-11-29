package com.thikthak.app.repository.nativeQuery;

import com.thikthak.app.repository.nativeQuery.mapres.PersonMapRes;

import javax.persistence.*;


@Entity
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Person.findAllPersons",
                query =
                        "SELECT id, display_name " +
                                "FROM AUTH_USER ", resultClass = Person.class
        ),
        @NamedNativeQuery(
                name = "Person.findPersonByName",
                query =
                        "SELECT * " +
                                "FROM AUTH_USER p " +
                                "WHERE p.display_name = ?", resultClass = Person.class)

})
final public class Person {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "ID")
        public Long id;
        @Column(name = "DISPLAY_NAME")
        public String displayName;


}
