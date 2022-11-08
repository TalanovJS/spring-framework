package com.dev.spring.database.repository;

import com.dev.spring.database.entity.Role;
import com.dev.spring.database.entity.User;
import com.dev.spring.dto.PersonalInfo;
import com.dev.spring.dto.PersonalInfo2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends
        JpaRepository<User, Long>,
        FilterUserRepository,
        RevisionRepository<User, Long, Integer>,
        QuerydslPredicateExecutor<User> {

    @Query("select u from User u " +
            "where u.firstname like %:firstname% " +
            "and u.lastname like %:lastname%")
    List<User> findAllBy(String firstname, String lastname);

    @Query(nativeQuery = true, value = "select u.* from users u where u.username = :username")
    List<User> findAllByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.role = :role where u.id in (:ids)")
    int updateRole(Role role, Long...ids);

    Optional<User> findTopByOrderByIdDesc();

    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "50"))
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<User> findTop3ByBirthDateBefore(LocalDate birthDate, Sort sort);

    @EntityGraph(attributePaths = {"company", "company.locales"})
    @Query(value = "select u from User u",
           countQuery = "select count(distinct u.firstname) from User u")
    Page<User> findAllBy (Pageable pageable);

    @EntityGraph(attributePaths = {"userChats", "company", "company.locales"})
    @Query(value = "select u from User u join fetch u.userChats where u.id in (:ids)")
    List<User> findById(Long...ids);

//    List<PersonalInfo> findAllByCompanyId(Integer companyId);

//   <T> List<T> findAllByCompanyId(Integer companyId, Class<T> clazz);

    @Query(nativeQuery = true,
    value = "select firstname, lastname, birth_date as birthDate from users where company_id = :companyId")
    List<PersonalInfo2> findAllByCompanyId(Integer companyId);

    Optional<User> findByUsername(String username);
}
