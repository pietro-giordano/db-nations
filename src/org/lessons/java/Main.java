package org.lessons.java;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/nation";
        String user = "root";
        String password = "root";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT `countries`.`country_id`, `countries`.`name`, `regions`.`name`, `continents`.`name` " +
                    "FROM `countries` " +
                    "JOIN `regions` ON `countries`.`region_id` = `regions`.`region_id` " +
                    "JOIN `continents` ON `regions`.`continent_id` = `continents`.`continent_id` " +
                    "ORDER BY `countries`.`name`";
            try(PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int countryId = rs.getInt(1);
                        String countryName = rs.getString(2);
                        String regionName = rs.getString(3);
                        String continentName = rs.getString(4);
                        System.out.println(countryId + " - " + countryName + " - " + regionName + " - " + continentName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
