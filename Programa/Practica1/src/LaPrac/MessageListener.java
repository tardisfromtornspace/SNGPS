package LaPrac;

import java.util.ArrayList;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

public class MessageListener implements SerialPortMessageListener
{
   @Override
   public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }

   @Override
   public byte[] getMessageDelimiter() {return new byte[] { (byte)'\r', (byte)'\n' }; } // 0x0D 0x0A

   @Override
   public boolean delimiterIndicatesEndOfMessage() {return true;}

   public void serialEvent(SerialPortEvent event)
   {
	   // TO BE OVERRIDEN
   }
}