package org.lessons.java;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/nation";
        String user = "root";
        String password = "root";

        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserire parola di ricerca per filtrare nomi di nazioni o premi direttamente invio per cercarle tutte:");
        String search = scanner.nextLine();

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT `countries`.`country_id`, `countries`.`name`, `regions`.`name`, `continents`.`name` " +
                    "FROM `countries` " +
                    "JOIN `regions` ON `countries`.`region_id` = `regions`.`region_id` " +
                    "JOIN `continents` ON `regions`.`continent_id` = `continents`.`continent_id` " +
                    "WHERE `countries`.`name` LIKE '%' ? '%' " +
                    "ORDER BY `countries`.`name`";
            try(PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, search);
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

            System.out.println("Inserisci id per ricevere dettagli della nazione selezionata:");
            int id = Integer.parseInt(scanner.nextLine());
            String sql2 = "SELECT `languages`.`language`, `countries`.`name` " +
                    "FROM `languages` " +
                    "JOIN `country_languages` ON `languages`.`language_id` = `country_languages`.`language_id`" +
                    "JOIN `countries` ON `country_languages`.`country_id` = `countries`.`country_id` " +
                    "WHERE `countries`.`country_id` = ? ";
            try(PreparedStatement ps = con.prepareStatement(sql2)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next()) {
                        String countryName = rs.getString(2);
                        System.out.println("Dettagli per: " + countryName);
                        System.out.print("Lingue: ");
                    }
                    while (rs.next()) {
                        String language = rs.getString(1);
                        if (rs.isLast()) {
                            System.out.println(language);
                        } else {
                            System.out.print(language + ", ");
                        }
                    }
                }
            }
            System.out.println("Statistiche recenti:");
            String sql3 = "SELECT `country_stats`.* " +
                    "FROM `countries` " +
                    "JOIN `country_stats` ON `countries`.`country_id` = `country_stats`.`country_id` " +
                    "WHERE `countries`.`country_id` = ? " +
                    "ORDER BY `country_stats`.`year` DESC " +
                    "LIMIT 1";
            try(PreparedStatement ps = con.prepareStatement(sql3)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String year = rs.getString(2);
                        System.out.println("Anno: " + year);
                        String population = rs.getString(3);
                        System.out.println("Popolazione: " + population);
                        String gdp = rs.getString(4);
                        System.out.println("GDP: " + gdp);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
