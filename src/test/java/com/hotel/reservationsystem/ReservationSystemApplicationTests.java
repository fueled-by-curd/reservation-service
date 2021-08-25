package com.hotel.reservationsystem;

import com.hotel.reservationsystem.tests.unit.controller.GuestControllerTest;
import com.hotel.reservationsystem.tests.unit.controller.RoomControllerTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@RunWith(Suite.class)
@Suite.SuiteClasses({
		GuestControllerTest.class,
		RoomControllerTest.class
})
class ReservationSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
