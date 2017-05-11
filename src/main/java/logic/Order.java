package logic;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;

import data.management.DBManager;

public class Order {
	/**
	 * @author Inbal Matityahu
	 * @since 8/5/16 This class represent an order for renting a parking slot
	 */
	
	// The order id. Should be a unique value.
	private String id;
	
	// The driver's id 
	private String driverId;
		
	// The demand slot id
	private String slotId;
		
	// The demand day
	private Date date;
	
	// The desired end time
	private String hour;
	
	// The desired amount of hours
	private int hoursAmount;
	
	private final String objectClass = "Order";
	
	/* Constructors */
	
	public Order(){
		
	}

	// Create a new order. Will result in a new order in the DB.
	public Order(final String driverId, final String slotId, Date startTime, Date endTime) throws ParseException, InterruptedException {
		DBManager.initialize();
		checkParameters(driverId, slotId, startTime, endTime);
		String idToString=driverId.toString()+(new Date().toString());
		Map<String, Object> fields = new HashMap<String, Object>(), keyValues = new HashMap<String, Object>();
		fields.put("driverId", driverId);
		fields.put("slotId", slotId);
		fields.put("date", startTime);
		int hours =hoursDifference(endTime, startTime);
		if (hours<=0)
			return;
		fields.put("hoursAmount", hours+1);
		int id=0;
		Calendar cal = Calendar.getInstance(); // creates calendar
	    cal.setTime(startTime); // sets calendar time/date
		for (int i=0; i<=hours; i++){
			id++;
			idToString=driverId.toString()+(startTime.toString())+id;
			fields.put("hour", cal.getTime().toString());
		    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
			keyValues.put("id", idToString);
			DBManager.insertObject(objectClass, keyValues, fields);
			Thread.sleep(6000);
		}
	}
	
	public Order(final ParseObject obj) throws ParseException {
		DBManager.initialize();

		this.driverId = obj.getString("driverId");
		this.date = obj.getDate("date");
		this.slotId = obj.getString("slotId");
		this.hour = obj.getString("hour");
		this.hoursAmount = (int)obj.get("hoursAmount");
		
	}
	
	public Order(final String id) throws ParseException {
		DBManager.initialize();
		
		Map<String, Object> keys = new HashMap<>();
		keys.put("id", id);
		Map<String,Object> returnV = DBManager.getObjectFieldsByKey(objectClass, keys);
		
		this.driverId=returnV.get("driverId") + "";
		this.slotId= returnV.get("slotId") + "";
		this.date= (Date)returnV.get("date");
		this.hour= returnV.get("hour")+"";
		this.hoursAmount= (int) returnV.get("hoursAmount");
		this.id=id;
	}

	/* Getters */

	public String getId() {
		DBManager.initialize();
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("id", id);
		return DBManager.getObjectFieldsByKey(objectClass, key).get("id") + "";
	}
	
	public String getDriverId() {
		DBManager.initialize();
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("id", id);
		return DBManager.getObjectFieldsByKey(objectClass, key).get("driverId") + "";
	}
	
	public String getSlotId() {
		DBManager.initialize();
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("id", id);
		return DBManager.getObjectFieldsByKey(objectClass, key).get("slotId") + "";
	}
	
	public Date getDate() {
		DBManager.initialize();
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("id", id);
		return (Date)DBManager.getObjectFieldsByKey(objectClass, key).get("date");
	}
	
	public String getHour() {
		DBManager.initialize();
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("id", id);
		return (String) DBManager.getObjectFieldsByKey(objectClass, key).get("hour");
	}
	
	public int getHoursAmount() {
		DBManager.initialize();
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("id", id);
		return (int) DBManager.getObjectFieldsByKey(objectClass, key).get("hoursAmount");
	}
	
	/* Setters */

	public void setDriverId(final String newDriverId) throws ParseException {
		if (newDriverId == null)
			throw new IllegalArgumentException("driver id can not be empty!");
		Map<String, Object> newFields = new HashMap<String, Object>();
		newFields.put("driverId", newDriverId);
		newFields.put("slotId", this.slotId);
		newFields.put("hour", this.hour);
		newFields.put("date", this.date);

		Map<String, Object> keys = new HashMap<String, Object>();
		keys.put("id", this.id);
		DBManager.update(objectClass, keys, newFields);
	}

	public void setSlotId(final String newSlot) throws ParseException {
		if (newSlot == null)
			throw new IllegalArgumentException("slot id can not be empty!");
		Map<String, Object> newFields = new HashMap<String, Object>();
		newFields.put("driverId", this.driverId);
		newFields.put("slotId", newSlot);
		newFields.put("hour", this.hour);
		newFields.put("date", this.date);

		Map<String, Object> keys = new HashMap<String, Object>();
		keys.put("id", this.id);
		DBManager.update(objectClass, keys, newFields);
	}
	
	public void setStartTime(final Date newStart) throws ParseException {
		if (newStart == null)
			throw new IllegalArgumentException("start date can not be empty!");
		Map<String, Object> newFields = new HashMap<String, Object>();
		newFields.put("driverId", this.driverId);
		newFields.put("slotId", this.slotId);
		newFields.put("hour", newStart);
		newFields.put("date", this.date);

		Map<String, Object> keys = new HashMap<String, Object>();
		keys.put("id", this.id);
		DBManager.update(objectClass, keys, newFields);
	}
	
	public void setDate(final Date newDate) throws ParseException {
		if (newDate == null)
			throw new IllegalArgumentException("end time can not be empty!");
		Map<String, Object> newFields = new HashMap<String, Object>();
		newFields.put("driverId", this.driverId);
		newFields.put("slotId", this.slotId);
		newFields.put("hour", this.hour);
		newFields.put("date", newDate);

		Map<String, Object> keys = new HashMap<String, Object>();
		keys.put("id", this.id);
		DBManager.update(objectClass, keys, newFields);
	}
	
	/* Methods */
	
	private static int hoursDifference(Date date1, Date date2) {
	    final int MILLI_TO_HOUR = 1000 * 60 * 60;
	    return (int) (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
	}
	
	private static void checkParameters(final String driverId, final String slotId, Date startTime, Date endTime) throws ParseException{
		if (checkIfNull(driverId, slotId, startTime, endTime))
			throw new IllegalArgumentException("parameters can not be empty!");
	}
		
	private static boolean checkIfNull(final String driverId, final String slotId, Date startTime, Date endTime){
		if (driverId==null || slotId==null || startTime==null || endTime==null){
			return true;
		}
		return false;
	}
	
	public void removeDriverFromDB() throws ParseException, InterruptedException {
		DBManager.initialize();
		Map<String, Object> fields = new HashMap<String, Object>();
		int newid=1;
		String idToString = driverId.toString()+this.date.toString()+newid;
		for(int i=0; i<this.hoursAmount; i++){
			fields.put("id", idToString);
			DBManager.deleteObject(objectClass, fields);
			Thread.sleep(6000);
			newid++;
			idToString = driverId.toString()+this.date.toString()+newid;
		}
	}
	
}
