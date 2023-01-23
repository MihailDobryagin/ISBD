package com.company.business.repositories.food;

import com.company.business.models.food.FoodType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class JdbcFoodTypeRepository implements FoodTypeRepository {
  private final static RowMapper<FoodType> foodTypeRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    int hp = rs.getInt("hp");
    int mana = rs.getInt("mana");
    int stamina = rs.getInt("stamina");
    return new FoodType(id, name, hp, mana, stamina);
  };
  private final static String GET_ALL_QUERY =
    "select id, name, hp, mana, stamina from food_types";
  private final static String GET_BY_ID_QUERY =
    "select id, name, hp, mana, stamina from food_types where id = ?";
  private final static String GET_BY_NAME_QUERY =
    "select id, name, hp, mana, stamina from food_types where name = ?";
  private final static String GET_BY_NAMES_QUERY =
    "select id, name, hp, mana, stamina from food_types where name in (:names)";
  private final static String INSERT_QUERY =
    "insert into food_types (name, hp, mana, stamina) values (?, ?, ?, ?) returning id";
  private final JdbcTemplate jdbcTemplate;

  public JdbcFoodTypeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<FoodType> getAll() {
    return jdbcTemplate.query(GET_ALL_QUERY, foodTypeRowMapper);
  }

  @Override
  public FoodType get(String name) {
    return jdbcTemplate.query(GET_BY_NAME_QUERY, foodTypeRowMapper, name).get(0);
  }

  @Override
  public List<FoodType> get(Set<String> names) {
    var query = GET_BY_NAMES_QUERY.replace(
      ":name", String.join(", ", Collections.nCopies(names.size(), "?"))
    );

    return jdbcTemplate.query(query, foodTypeRowMapper);
  }

  @Override
  public FoodType get(int id) {
    return jdbcTemplate.query(GET_BY_ID_QUERY, foodTypeRowMapper, id).get(0);
  }

  @Override
  public int save(FoodType foodType) {
    Integer id = jdbcTemplate.queryForObject(INSERT_QUERY, Integer.class,
      foodType.getName(), foodType.getHp(), foodType.getMana(), foodType.getStamina()
    );

    if (id == null) {
      // TODO
      throw new IllegalArgumentException("Stub");
    }

    return id;
  }
}
