package apitest;

import api.CarAPI;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import api.sensor.*;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarAPITest {

    private final String CAR_NAME = "car";
    private final String SERVER = "127.0.0.1";
    private final int PORT = 1883;

    private MockCar mockCar;
    private CarAPI carAPI;

    @BeforeEach
    public void initialise() throws MqttException {

        mockCar = new MockCar(SERVER,PORT,CAR_NAME);
        carAPI = new CarAPI(SERVER,CAR_NAME);

    }


    @Test
    public void testGyroscopeListener() throws Exception{

        CountDownLatch latch = new CountDownLatch(1);
        final double testValue = randomDouble();
        final TestResult result = new TestResult();
        carAPI.addGyroscopeListener(degrees -> {
            checkAssertion(testValue,degrees,result);
            latch.countDown();
        });
        mockCar.sendGyroscopeData(testValue);
        latch.await();
        if (!result.isSuccess()) throw result.getException();
    }

    @Test
    public void testUltraSonicListener()throws Exception{
        CountDownLatch latch = new CountDownLatch(1);
        final double testValue = randomDouble();
        final TestResult result = new TestResult();
        carAPI.addUltraSonicListener(distance -> {
            checkAssertion(testValue,distance,result);
            latch.countDown();
        });
        mockCar.sendUltraSonicData(testValue);
        latch.await();
        if (!result.isSuccess()) throw result.getException();
    }

    @Test
    public void testInfraredListeners() throws Exception{

        for (Infrared i : Infrared.values()) { //testing all 4 infrared sensors separately
            double testValue = randomDouble();
            testInfraredListener(i,testValue);
        }
    }

    private void testInfraredListener(Infrared infrared, double testValue) throws Exception{

        CountDownLatch latch = new CountDownLatch(1);
        final TestResult result = new TestResult();
        carAPI.addInfraredListener(infrared, distance ->{
            checkAssertion(testValue,distance,result);
            latch.countDown();
        });
        mockCar.sendInfraredData(infrared,testValue);
        latch.await();
        if (!result.isSuccess()) throw result.getException();
    }

    @Test
    public void testOdometerSpeedListeners() throws Exception{
        for (Odometer o : Odometer.values()) {
            double testValue = randomDouble();
            testOdometerSpeedListener(o,testValue);
        }
    }

    private void testOdometerSpeedListener(Odometer odometer, double testValue) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final TestResult result = new TestResult();
        carAPI.addOdometerSpeedListener(odometer, speed ->{
            checkAssertion(testValue,speed,result);
            latch.countDown();
        });
        mockCar.sendOdometerSpeedData(odometer,testValue);
        latch.await();
        if (!result.isSuccess()) throw result.getException();
    }

    @Test
    public void testOdometerTotalDistanceListeners() throws Exception{
        for (Odometer o : Odometer.values()) {
            double testValue = randomDouble();
            testOdometerTotalDistanceListener(o,testValue);
        }
    }

    private void testOdometerTotalDistanceListener(Odometer odometer, double testValue) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final TestResult result = new TestResult();
        carAPI.addOdometerTotalDistanceListener(odometer, totalDistance ->{
            checkAssertion(testValue,totalDistance,result);
            latch.countDown();
        });
        mockCar.sendOdometerTotalDistanceData(odometer,testValue);
        latch.await();
        if (!result.isSuccess()) throw result.getException();
    }

    private double randomDouble(){
        Random random =  new Random();
        return random.nextDouble();
    }

    private void checkAssertion(double expected, double actual, TestResult result){
        try {
            assertEquals(expected,actual);
            result.setSuccess(true);
        }
        catch (AssertionFailedError e){
            result.setSuccess(false);
            result.setException(e);
        }
    }


}
