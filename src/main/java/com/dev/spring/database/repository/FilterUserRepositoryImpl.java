package com.dev.spring.database.repository;

import com.dev.spring.database.entity.Role;
import com.dev.spring.database.entity.User;
import com.dev.spring.database.querydsl.QPredicates;
import com.dev.spring.dto.PersonalInfo;
import com.dev.spring.dto.UserFilter;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dev.spring.database.entity.QUser.user;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository{

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String FIND_BY_COMPANY_AND_ROLE = "select firstname, lastname, birth_date as birthDate " +
                                                            "from users where company_id = ? " +
                                                             "and role = ?";

    public static final String UPDATE_COMPANY_AND_ROLE = "update users " +
                                                            "set company_id = ?, " +
                                                            "role = ? " +
                                                            "where id = ?";

    public static final String UPDATE_COMPANY_AND_ROLENAMED = "update users " +
                                                                "set company_id = :companyId, " +
                                                                "role = :role " +
                                                                "where id = :id";

//    @Override
//    public List<User> findAllByFilter(UserFilter filter) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//
//        CriteriaQuery<User> criteria = cb.createQuery(User.class);
//
//        Root<User> user = criteria.from(User.class);
//
//        criteria.select(user);
//
//        List<Predicate> predicates = new ArrayList<>();
//
//        if(filter.getFirstname() != null){
//            predicates.add(cb.like(user.get("firstname"), filter.getFirstname()));
//        }
//        if(filter.getLastname() != null){
//            predicates.add(cb.like(user.get("lastname"), filter.getLastname()));
//        }
//        if(filter.getBirthDate() != null){
//            predicates.add(cb.lessThan(user.get("birthDate"), filter.getBirthDate()));
//        }
//        criteria.where(predicates.toArray(Predicate[]::new));
//
//        return entityManager.createQuery(criteria).getResultList();
//    }

    @Override
    public List<User> findAllByFilter(UserFilter filter) {
        var predicate = QPredicates.builder()
                .add(filter.getFirstname(), user.firstname::containsIgnoreCase)
                .add(filter.getLastname(), user.lastname::containsIgnoreCase)
                .add(filter.getBirthDate(), user.birthDate::before)
                .build();

        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .fetch();
    }

    @Override
    public List<PersonalInfo> findAllByCompanyIdAndRole (Integer companyId, Role role) {
        return jdbcTemplate.query(FIND_BY_COMPANY_AND_ROLE, (rs, rowNum) -> new PersonalInfo(
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getDate("birthDate").toLocalDate()
        ), companyId, role.name());
    }

    @Override
    public void updateCompanyAndRole(List<User> list) {
        List<Object[]> objects = list.stream().map(user -> new Object[]{
                user.getCompany().getId(),
                user.getRole().name(),
                user.getId()
        }).collect(Collectors.toList());
        jdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE, objects);
    }

    @Override
    public void updateCompanyAndRolenamed(List<User> list) {
        MapSqlParameterSource[] sources = list.stream()
                .map(user -> Map.of(
                        "companyID", user.getCompany().getId(),
                        "role", user.getRole().name(),
                        "birthDate", user.getBirthDate()
                ))
                .map(MapSqlParameterSource::new)
                .toArray(MapSqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLENAMED, sources);
    }
}
