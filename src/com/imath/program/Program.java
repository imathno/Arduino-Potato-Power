package com.imath.program;

import com.imath.influx.InfluxDBConnection;
import gnu.io.*;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.TooManyListenersException;

public class Program implements SerialPortEventListener {

    private SerialPort serialPort;
    private CommPortIdentifier portId;
    private Scanner dataInput = null;

    private final int DATA_RATE = 9600;
    private final int TIME_OUT = 1000;
    private static final String PORT_NAME = "COM7";

    private InfluxDBConnection influxConnection = null;
    private boolean displayData;

    private boolean initialized;

    public Program(boolean displayData) {
        this.displayData = displayData;
        this.initialized = false;
    }

    public void initialize() {
        try {
            this.portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            while (portId == null && portEnum.hasMoreElements()) {
                CommPortIdentifier currentPortId = ((CommPortIdentifier) portEnum.nextElement());

                if (currentPortId.equals(PORT_NAME) || currentPortId.getName().startsWith(PORT_NAME)) {
                    serialPort = ((SerialPort) currentPortId.open(getClass().getSimpleName(), TIME_OUT));
                    this.portId = currentPortId;
                    this.initialized = true;

                    setUpSerialPort();
                }
            }
            if (portId == null || serialPort == null) {
                throw new NullPointerException("portId:" + portId + "\n serialPort:" + serialPort);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (displayData) {
            setUpInflux();
        }
    }

    private void setUpInflux() {
        influxConnection = influxConnection.getInstance();
    }

    private void setUpSerialPort() throws UnsupportedCommOperationException, TooManyListenersException {
        if (!initialized) {
            notInitializedError();
        }
        serialPort.setSerialPortParams(DATA_RATE,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

        serialPort.addEventListener(this);
        serialPort.notifyOnDataAvailable(true);
    }

    @Override
    public synchronized void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                this.dataInput = new Scanner(serialPort.getInputStream());
                while (dataInput.hasNextLine()) {
                    double voltage = Double.parseDouble(dataInput.nextLine());
                    updateGraph(voltage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dataInput.close();
    }

    private void updateGraph(double value) {
        System.out.println(value);
        if (influxConnection != null) {
            influxConnection.writePoint(influxConnection.getPoint(value));
        }
    }

    private void notInitializedError() {
        try {
            throw new Exception("initialized:" + initialized);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
