package database;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import data.management.DBManager;
import data.members.MapLocation;
import data.members.ParkingArea;
import data.members.ParkingSlot;
import data.members.ParkingSlotStatus;
import data.members.StickersColor;

/*
 *  @Author Tom Nof
 *  @Inbal Matityauh
 *  @Author David Cohen
 */

public class parkingAreaTest {

	private ParkingSlot slot1;
	private ParkingSlot slot2;
	private ParkingArea area;
	private ParseQuery<ParseObject> query;

	@Before
	public void BeforeTest() {
		DBManager.initialize();
		try {
			slot1 = new ParkingSlot("testS1", ParkingSlotStatus.FREE, StickersColor.RED, StickersColor.RED,
					new MapLocation(0, 0), new Date());
			slot2 = new ParkingSlot("testS2", ParkingSlotStatus.FREE, StickersColor.RED, StickersColor.RED,
					new MapLocation(0, 0), new Date());

			final Set<ParkingSlot> slots = new HashSet<ParkingSlot>();
			slots.add(slot1);
			slots.add(slot2);

			area = new ParkingArea(0, "t1", new MapLocation(0, 0), slots, StickersColor.RED);

			query = ParseQuery.getQuery("ParkingArea");
			query.whereEqualTo("name", "t1");

		} catch (final ParseException ¢) {
			¢.printStackTrace();
		}
	}

	@After
	public void AfterTest() {
		try {
			// Clean up DB
			area.deleteParseObject();
			slot1.deleteParseObject();
			slot2.deleteParseObject();
		} catch (final ParseException ¢) {
			¢.printStackTrace();
		}
	}

	@Test
	public void test0() {
		try {
			assert area != null;
		} catch (final Exception ¢) {
			¢.printStackTrace();
			fail();
		}
	}

	@Test
	public void test1() {
		try {
			Assert.assertEquals(2, area.getNumOfParkingSlots());
		} catch (final Exception ¢) {
			¢.printStackTrace();
			fail();
		}
	}

	@Test
	public void testAddRemoveSlots() {
		try {
			final List<ParseObject> areaList = query.find();
			if (areaList == null || areaList.isEmpty())
				throw new RuntimeException("There should be an area with areaId=" + 0);
			area.removeParkingSlot(slot2);
			Assert.assertEquals(1, area.getNumOfFreeSlots());

			final ParkingSlot slot3 = new ParkingSlot("testS2", ParkingSlotStatus.FREE, StickersColor.RED, StickersColor.RED,
					new MapLocation(0, 0), new Date());
			area.addParkingSlot(slot3);
			Assert.assertEquals(2, area.getNumOfFreeSlots());

			// Clean up DB
			slot3.deleteParseObject();

		} catch (final ParseException ¢) {
			¢.printStackTrace();
			fail();
		}

	}

	@Test
	public void testGetAllSlots() {
		try {

			final List<ParseObject> areaList = query.find();
			if (areaList == null || areaList.isEmpty())
				throw new RuntimeException("There should be an area with areaId=" + 0);
			final List<ParseObject> allSlots = new ParkingArea(areaList.get(0)).getAllSlots();
			if (allSlots == null)
				throw new RuntimeException("There should be a slots in area with areaId=" + 0);
			Assert.assertEquals(2, allSlots.size());

		} catch (final ParseException ¢) {
			¢.printStackTrace();
			fail();
		}
	}

	@Test
	public void testConvertSlots() {
		try {

			final List<ParseObject> areaList = query.find();
			if (areaList == null || areaList.isEmpty())
				throw new RuntimeException("There should be an area with areaId=" + 0);
			final List<ParseObject> allSlots = new ParkingArea(areaList.get(0)).getAllSlots();
			if (allSlots == null)
				throw new RuntimeException("There should be a slots in area with areaId=" + 0);
			Assert.assertEquals(2, new ParkingArea(areaList.get(0)).convertToSlots(allSlots).size());

		} catch (final ParseException ¢) {
			¢.printStackTrace();
			fail();
		}
	}

	@Test
	public void testGetSlotsByStatus() {
		DBManager.initialize();
		try {

			final List<ParseObject> areaList = query.find();
			if (areaList == null || areaList.isEmpty())
				throw new RuntimeException("There should be an area with areaId=" + 0);
			final Set<ParkingSlot> slotsByStatus = new ParkingArea(areaList.get(0)).getSlotsByStatus(ParkingSlotStatus.FREE);
			if (slotsByStatus == null)
				throw new RuntimeException("There should slots in area with areaId=" + 0);
			Assert.assertEquals(2, slotsByStatus.size());

		} catch (final ParseException ¢) {
			¢.printStackTrace();
			fail();
		}
	}

	@Test
	public void testGetSlotsByStatus2() {
		try {
			final List<ParseObject> areaList = query.find();
			if (areaList == null || areaList.isEmpty())
				throw new RuntimeException("There should be an area with areaId=" + 0);
			final Set<ParkingSlot> slotsByTakenStatus = new ParkingArea(areaList.get(0))
					.getSlotsByStatus(ParkingSlotStatus.TAKEN);
			if (slotsByTakenStatus == null)
				throw new RuntimeException("There should slots in area with areaId=" + 0);
			Assert.assertEquals(0, slotsByTakenStatus.size());

		} catch (final ParseException ¢) {
			¢.printStackTrace();
			fail();
		}
	}

	@Test
	public void testNumberOfStatusSlots() {
		try {
			final List<ParseObject> areaList = query.find();
			if (areaList == null || areaList.isEmpty())
				throw new RuntimeException("There should be an area with areaId=" + 0);
			Assert.assertEquals(2, area.getNumOfFreeSlots());
			Assert.assertEquals(0, area.getNumOfTakenSlots());
		} catch (final ParseException ¢) {
			¢.printStackTrace();
			fail();
		}
	}

	@Test
	public void getAreaByName() {
		try {
			// Act
			final ParkingArea returnedArea = new ParkingArea(area.getName());

			// Assert
			Assert.assertEquals(area.getAreaId(), returnedArea.getAreaId());
			Assert.assertEquals(area.getObjectId(), returnedArea.getObjectId());
			Assert.assertEquals(area.getColor(), returnedArea.getColor());

		} catch (final Exception ¢) {
			¢.printStackTrace();
			Assert.fail();
		}
	}

}
