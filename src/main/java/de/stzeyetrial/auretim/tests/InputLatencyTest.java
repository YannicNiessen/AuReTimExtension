package de.stzeyetrial.auretim.tests;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.GpioController;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author strasser
 */
public final class InputLatencyTest {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {


        Runnable r = new Runnable() {
            @Override
            public void run() {


                final GpioController gpio = GpioFactory.getInstance();

                final GpioPinDigitalInput input = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28);
                final int[] counter = {0};

                int limit = Integer.parseInt(args[0]);


                final String[] results = new String[limit];

                input.addListener((GpioPinListenerDigital) (final GpioPinDigitalStateChangeEvent event) -> {

                    if (event.getState() == PinState.HIGH) {
                        Instant inst = Instant.now();
                        long nanos = TimeUnit.SECONDS.toNanos(inst.getEpochSecond()) + inst.getNano();
                        results[counter[0]] = String.valueOf(nanos);
                        counter[0]++;


                        if (results[limit - 1] != null) {
                            System.out.println("Java reached limit. Writing values to file...");
                            String objectsCommaSeparated = String.join(",", results);

                            PrintWriter writer = null;
                            try {
                                writer = new PrintWriter("tsJava.csv", "UTF-8");
                                writer.println(objectsCommaSeparated);
                                writer.close();
                            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            synchronized (lock) {
                                lock.notify();
                            }

                        }
                    }

                });

                System.out.println("Java program running...");


                Runtime.getRuntime().addShutdownHook(new Thread(() -> gpio.unprovisionPin(input)));

            }
        };

        (new Thread(r)).start();

        synchronized (lock) {
            lock.wait();
        }

    }
}

