package com.salessparrow.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.salessparrow.api.domain.User;

//Keywords Documentation for queries (https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation | https://docs.spring.io/spring-data/jpa/docs/current-SNAPSHOT/reference/html/#reference)
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  //find by email and sort
  //List<User> findByEmailAndSort(String email, Sort sort);
  //Eg: findByEmailAndSort("email", Sort.by("name").descending());


  //find by email and page
  //Page<User> findByEmailAndPage(String email, Pageable pageable);
  //Eg: findByEmailAndPage("email", PageRequest.of(0, 10));

  //find by email and page and limit 10
  //List<User> findByEmailAndPage(String email, Pageable pageable);
  //Eg: findByEmailAndPage("email", PageRequest.of(0, 10));

  //Update by email
  //@Modifying
  //@Query("update User u set u.password = ?1 where u.email = ?2")
  //int updatePasswordByEmail(String password, String email);

}
