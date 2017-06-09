import gnu.io.*;

import java.util.Enumeration;
import java.util.TooManyListenersException;

public class Main implements SerialPortEventListener {

    private SerialPort serialPort;

    private static final String PORT_NAME = "COM3";

    private final int TIME_OUT = 1000;
    private final int DATA_RATE = 9600;

    public static void main(String[] args) {

    }

    private void initialize() {
        try {
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            while (portId == null && portEnum.hasMoreElements()) {
                CommPortIdentifier currentPortId = ((CommPortIdentifier) portEnum.nextElement());

                if (currentPortId.equals(PORT_NAME) || currentPortId.getName().startsWith(PORT_NAME)) {
                    serialPort = ((SerialPort) currentPortId.open(getClass().getSimpleName(), TIME_OUT));
                    portId = currentPortId;
                }
            }

            if (portId == null || serialPort == null) {
                throw new NullPointerException("portId:" + portId + "\n serialPort:" + serialPort);
            }
            setUpSerialPort();

            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpSerialPort() throws UnsupportedCommOperationException, TooManyListenersException {
        serialPort.setSerialPortParams(DATA_RATE,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

        serialPort.addEventListener(this);
        serialPort.notifyOnDataAvailable(true);
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
    }
}
