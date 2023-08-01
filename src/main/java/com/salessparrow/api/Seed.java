package com.salessparrow.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;

@Component
public class Seed implements CommandLineRunner {

  @Autowired
  private CoreConstants coreConstants;

  private final JdbcTemplate jdbcTemplate;

  public Seed(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void run(String... args) {
      String dbName = coreConstants.dbName();

      if (!checkDatabaseExists(dbName)) {
          createDatabase(dbName);
      }
  }

  private boolean checkDatabaseExists(String dbName) {
      String checkDbExistsSql = "SELECT 1 FROM pg_database WHERE datname = ?";
      Integer result = jdbcTemplate.queryForObject(checkDbExistsSql, Integer.class, dbName);
      return result != null && result == 1;
  }

  private void createDatabase(String dbName) {
      String createDatabaseSql = "CREATE DATABASE " + dbName;
      jdbcTemplate.execute(createDatabaseSql);
  }
}
