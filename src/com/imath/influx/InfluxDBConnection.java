package com.imath.influx;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

public class InfluxDBConnection {

    private static InfluxDBConnection instance;

    private final InfluxDB influxDB;
    private final String dbName = "FruitVoltageData";

    private InfluxDBConnection() {
        influxDB = InfluxDBFactory.connect("http://" + "127.0.0.1" + ":8086", "root", "root");
        int flushEveryPointsCount = 2000;
        int flushEveryMS = 1000;
        influxDB.enableBatch(flushEveryPointsCount, flushEveryMS, TimeUnit.MILLISECONDS);

        System.out.println(influxDB.ping().getResponseTime());
    }
    public void writePoint(Point point) {
        if(influxDB != null) {
            influxDB.write(dbName, "autogen", point);
        }
    }

    public Point getPoint(double voltage) {
        Point.Builder telemetryPoints = Point.measurement("Fruit Voltage")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("voltage", voltage);
        return telemetryPoints.build();
    }

    public static synchronized InfluxDBConnection getInstance() {
        if (instance == null) {
            instance = new InfluxDBConnection();
        }
        return instance;
    }
}