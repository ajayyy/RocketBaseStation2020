package uorocketry.basestation;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class DataHandler {
	
	static final int TIMESTAMP = 0;
	static final int ALTITUDE = 1;
	static final int LATITUDE = 2;
	static final int LONGITUDE = 3;
	static final int PITCH = 4;
	static final int ROLL = 5;
	static final int YAW = 6;
	static final int ACCELX = 7;
	static final int ACCELY = 8;
	static final int ACCELZ = 9;
	final int VELOCITY = 20;
	static final int BRAKE_PERCENTAGE = 11;
	static final int ACTUAL_BRAKE_VALUE = 12;
	static final int GPS_FIX = 13;
	static final int GPS_FIX_QUALITY = 14;
	
	Data[] data = new Data[Main.DATA_LENGTH];
	
	public String getFormattedData(String[] labels) {
		String text = "<html>";
		
		for (int i = 0; i < data.length; i++) {
			text += labels[i] + ": " + data[i].getFormattedString() + "<br>";
		}
		
		return text + "</html>";
	}
	
	public void updateTableUIWithData(JTable table, String[] labels) {
		TableModel tableModel = table.getModel();
		
		for (int i = 0; i < data.length; i++) {
			// Set label
			tableModel.setValueAt(labels[i], i, 0);
			
			// Set data
			tableModel.setValueAt(data[i].getFormattedString(), i, 1);
		}
	}
	
	public void set(int index, String currentData) {
		
		// Check for special cases first
		if (index == LATITUDE || index == LONGITUDE) {
			float degrees = 0;
			float minutes = 0;
			
			int minutesIndex = currentData.indexOf(".") - 2;
			//otherwise, it is badly formatted and probably still zero
			if (minutesIndex >= 0) {
				minutes = Float.parseFloat(currentData.substring(minutesIndex, currentData.length()));
				degrees = Float.parseFloat(currentData.substring(0, minutesIndex));
			}
			
			data[index] = new Data(degrees, minutes);
		} else {
			
			// Normal case
			float floatData = -1;
			
			try {
				floatData = Float.parseFloat(currentData);
			} catch (NumberFormatException e) {
				if (currentData.equals("ovf")) {
					// ovf means overflow
					floatData = Float.MAX_VALUE;
				} else {
					System.err.println("Number conversion failed for '" + currentData + "', -1 being used instead");
				}
			}
			
			data[index] = new Data(floatData);
		}
		
	}
}
