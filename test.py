import time
import matplotlib
import matplotlib.pyplot as plt
from subprocess import Popen
import csv
import numpy as np
import random
import RPi.GPIO as GPIO  # import RPi.GPIO module
from time import sleep  # lets us have a delay
import gc
from gpiozero import LED
import sys
from array import *
import os

gc.disable()
#gc.enable()
csv.field_size_limit(sys.maxsize)
tsJava = list()
#GPIO.setmode(GPIO.BCM)  # choose BCM or BOARD
#GPIO.setup(20, GPIO.OUT)  # set GPIO24 as an output
#GPIO.output(20, 0)
rounds = int(input('Enter number of rounds: '))
#p = Popen(['java',"-XX:+UnlockExperimentalVMOptions" ,"-XX:+UseEpsilonGC", "-Xmx2500M", "-Xms2500M", "-XX:+AlwaysPreTouch", '-jar', 'target/InputLatencyTest-1.0-SNAPSHOT.jar', str(rounds)])
#p = Popen(['java',"-Xmx256M", "-Xms256M", '-jar', 'target/InputLatencyTest-1.0-SNAPSHOT.jar', str(rounds)])
p = Popen(['java', '-jar', 'target/InputLatencyTest-1.0-SNAPSHOT.jar', str(rounds)])
cpu_load_process = None
#cpu_load_process = Popen(['sysbench', '--test=cpu', '--cpu-max-prime=800000',"--num-threads=4", 'run'])
ts = [None] * rounds

sleep(10)
# GPIO.setup(20, GPIO.OUT)
led = LED(20)
led.off()


counter = 0
print('Starting in 3')
sleep(1)
print('Starting in 2')
sleep(1)
print('Starting in 1')
sleep(1)
print('Running...')
interval = 50
duration = 50

load_started = False
try:
    while counter < rounds:
        # GPIO.output(20, 1)  # set GPIO24 to 1/GPIO.HIGH/True
        counter += 1
        #print(counter)
        led.on()
        ts[counter-1] = time.time_ns()

        sleep(duration / 1000)
        led.off()
        # GPIO.output(20, 0)  # set GPIO24 to 0/GPIO.LOW/False
        sleep(interval / 1000)
       # if counter % round(rounds/10) == 0:
           # gc.collect()
  #      if counter >= cpu_load_start and counter < cpu_load_end and not load_started:
     #       load_started = True
   #     elif counter >= cpu_load_end and load_started:
         #   cpu_load_process.terminate()
    #        load_started = False

      #  if counter % round(rounds / 10) == 0:
       #     print(str(counter * 100 / rounds) + '%')
            #gc.collect()
        #    sleep(2)

    # if counter % 50 == 0:
    # gc.collect()
    #    sleep(3)



except KeyboardInterrupt:  # keyboard interrupt
    GPIO.cleanup()
    p.terminate()

sleep(5)

#ts = ts.tolist()

current_time = time.time()

with open('tsPython' + str(current_time) +'.csv', 'w', newline='') as myfile:
    wr = csv.writer(myfile, quoting=csv.QUOTE_ALL)
    wr.writerow(ts)

with open('tsJava.csv', newline='') as csvfile:
    reader = csv.reader(csvfile, delimiter=' ', quotechar='|')
    for row in reader:
        tsJava = row

os.rename('tsJava.csv', 'tsJava' + str(current_time) + '.csv')



tsJava = tsJava[0].split(',')
gcIndices = list()
for x in range(len(tsJava)):
    if tsJava[x] == "-1":
        gcIndices.append(x)

    tsJava[x] = int(tsJava[x])


    # print(tsJava[x] - ts[x])

tsJava = np.asarray(tsJava)
tsPython = np.asarray(ts)

differences = tsJava - tsPython


gcIndices.reverse()

for x in gcIndices:
    differences = np.delete(differences, x)

differences = np.delete(differences, 0)


print(gcIndices)
print(differences.argmax(axis=0))
print(differences.argmin(axis=0))
differences = np.true_divide(differences, 1000000)
average = "%.2f" % (np.mean(differences))
standard_deviation = "%.2f" % (np.std(differences))
print('Number of datapoints: ' + str(len(differences)))
print('Average(ms) is: ' + average)
print('Standard Deviation(ms) is: ' + standard_deviation)

print(differences)

line = plt.plot(range(len(differences)), differences)
plt.ylabel("Response time (ms)")
plt.xlabel("Rounds")
plt.title("GPIO Latency Raspberry Pi 4")
cell_text = [[str(interval), str(duration), str(rounds), average, standard_deviation,
              "None"]]
columns = ('Interval(ms)', 'Duration(ms)', 'Rounds', "Average(ms)", "SD(ms)", "Load Type")

#table = plt.table(cellText=cell_text,
 #                 colLabels=columns,
  #                loc='bottom',
   #               cellLoc='center', rowLoc='center',
    #              bbox=[0, -0.7, 1, 0.5])
#table.auto_set_font_size(False)
#table.set_fontsize(8)

# Adjust layout to make room for the table:
#plt.subplots_adjust(bottom=0.4)

plt.savefig(str(current_time) + 'avg:' + average + 'sd:' + standard_deviation + '.png')
p.terminate()



if cpu_load_process is not None:
    cpu_load_process.terminate()
