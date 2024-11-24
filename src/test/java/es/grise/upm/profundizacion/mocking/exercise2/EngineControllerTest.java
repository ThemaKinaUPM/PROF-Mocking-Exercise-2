package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class EngineControllerTest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Logger log;
    Speedometer spd;
    Gearbox gb;
    Time time;
    EngineController eg;

    /*
    * Creating class and mocks
    */
    @BeforeEach
    public void builder(){
        log=mock(Logger.class);
        spd = mock(Speedometer.class);
        gb = spy(Gearbox.class);
        time=mock(Time.class);
        eg = new EngineController(log,spd,gb,time);
        when(time.getCurrentTime()).thenReturn(new Timestamp(0));
    }

    /*
    * testing string format
    */
    @Test
    public void test01(){
        doAnswer(invocation -> {
            Object string = invocation.getArgument(0);

            // verify the invocation is called with the correct index and element
            assertEquals("1970-01-01 01:00:00 Gear changed to STOP", string);

            return null;
        }).when(log).log(any(String.class));
        eg.recordGear(GearValues.STOP);
    }

    /*
    * testing speed alverage
    */
    @Test
    public void test02(){
        when(spd.getSpeed()).thenReturn(20.0);
        double sol=eg.getInstantaneousSpeed();
        verify(spd,times(3)).getSpeed();
        assertEquals(20,sol);
    }

    /*
    * testing correct calling
    */
    @Test
    public void test03(){

        //EngineController eg = spy(EngineController.class);
        eg.adjustGear();
        verify(spd,times(3)).getSpeed();
    }

    /*
    * testing gear logs
    */
    @Test
    public void test04() {
        when(eg.getInstantaneousSpeed()).thenReturn(15.0);//change de speed to obtain diferent gear values
        doAnswer(invocation -> {
            String str = invocation.getArgument(0).toString();
            assertTrue(str.matches(String.format(".*Gear changed to (%s|%s|%s)$", 
                    GearValues.FIRST, GearValues.SECOND, GearValues.STOP)));
            System.out.println(str);
            return null;
        }).when(log).log(any(String.class));
        eg.adjustGear();
    }
     /*
    * testing gear asignment
    */
    @Test
    public void test05() {
        when(eg.getInstantaneousSpeed()).thenReturn(15.0);
        doAnswer(invocation -> {
            Object string = invocation.getArgument(0);

            assertEquals("1970-01-01 01:00:00 Gear changed to FIRST", string);

            return null;
        }).when(log).log(any(String.class));
        eg.adjustGear();

    }
}
