package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


public class AlertRabbit {

    private static Connection connection;

    public static void main(String[] args) throws InterruptedException {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            Properties prop = AlertRabbit.getProp("src/main/resources/rabbit.properties");
            JobDataMap data = new JobDataMap();
            try (Connection connect = getConnection(prop)) {
                data.put("connect", connect);
                JobDetail job = newJob(Rabbit.class)
                        .usingJobData(data)
                        .build();
                SimpleScheduleBuilder times = simpleSchedule()
                        .withIntervalInSeconds(Integer.parseInt(prop.getProperty("rabbit.interval")))
                        .repeatForever();
                Trigger trigger = newTrigger()
                        .startNow()
                        .withSchedule(times)
                        .build();
                scheduler.scheduleJob(job, trigger);
                Thread.sleep(10000);
                scheduler.shutdown();
            }
        } catch (SchedulerException | SQLException se) {
            se.printStackTrace();
        }
    }

    private static Properties getProp(String propPlace) {
        Properties cfg = new Properties();
        try (FileInputStream in =
                     new FileInputStream(propPlace)) {
            cfg.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfg;
    }

    public static Connection getConnection(Properties cfg) {
        if (connection == null) {
            try {
                Class.forName(cfg.getProperty("jdbc.driver"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                connection = DriverManager.getConnection(
                        cfg.getProperty("jdbc.url"),
                        cfg.getProperty("jdbc.username"),
                        cfg.getProperty("jdbc.password"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return connection;
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            Connection stm = (Connection) context.getJobDetail().getJobDataMap().get("connect");
            try (PreparedStatement pr = stm.prepareStatement("Insert into rabbit(created_date) values(now())");
            ){
                pr.execute();
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}