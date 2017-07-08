package com.imath.program;

import com.imath.gui.VoltGui;
import com.imath.influx.InfluxDBConnection;
import gnu.io.*;

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
    private boolean displayGraph;
    private boolean displayGui;
    private VoltGui voltGui;

    private boolean initialized;

    public Program(boolean displayGraph, boolean displayGui) {
        this.displayGraph = displayGraph;
        this.displayGui = displayGui;
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
        if (displayGraph) {
            setUpInflux();
        }
        if (displayGui) {
            setUpGui();
        }
    }

    private void setUpInflux() {
        influxConnection = influxConnection.getInstance();
    }

    private void setUpGui() {
        voltGui = VoltGui.getInstance();
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
                    updateData(voltage);
                }
            } catch (Exception e) {
                System.err.println("\n--------------------\n"
                                    + e
                                    + "\n--------------------\n");
            }
        }
        dataInput.close();
    }

    private void updateData(double value) {
        System.out.println(value + " Volts");
        if (influxConnection != null) {
            influxConnection.writePoint(influxConnection.getPoint(value));
        }
        if (voltGui != null) {
            voltGui.updateTextBox(value + " Volts");
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
