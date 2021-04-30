import csv
import numpy as np

tsJava = None
tsPython = None

with open('tsJava1619316546.4062428.csv', newline='') as csvfile:
    reader = csv.reader(csvfile, delimiter=' ', quotechar='|')
    for row in reader:
        tsJava = row


with open('tsPython1619316546.4062428.csv', newline='') as csvfile:
    reader = csv.reader(csvfile, delimiter=' ', quotechar='|')
    for row in reader:
        tsPython = row

tsJava = tsJava[0].split(',')
tsPython = tsPython[0].split(',')

for x in range(len(tsJava)):
    tsJava[x] = int(tsJava[x])

for x in range(len(tsPython)):
    tsPython[x] = int(tsPython[x][1:len(tsPython[x])-1])

tsJava = np.asarray(tsJava)
tsPython = np.asarray(tsPython)
#print(tsJava)
#print(tsPython)

differences = tsJava - tsPython

print(differences)

sum = 0

for x in range(len(differences)-1):
    sum += abs(differences[x+1] - differences[x])

print("jitter = " + str(sum/(len(differences))))
