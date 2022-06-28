import csv
from os import walk
from pathlib import Path

print("Welcome to AuReTim Correcture.\nPlease enter path to results folder (Default -> ./results)")

folder_name = input()

if folder_name == "":
    folder_name = "./results"

files_in_folder = []
for (_, _, filenames) in walk(folder_name):
    files_in_folder.extend(filenames)
    break

files_in_folder = list(filter(lambda file_name: "csv" in file_name, files_in_folder))

folder_name = folder_name + ("" if folder_name[-1] == "/" else "/")

if not files_in_folder:
    print("Given folder doesn't contain any csv files. Exiting...")
    exit()

Path(folder_name + "corrected/").mkdir(parents=True, exist_ok=True)

for filename in files_in_folder:
    print(filename)
    results = []
    with open(folder_name + filename) as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            results.append(row)
    dataPointStartRow = 0

    for row in results:
        print(row)
        if "timepoint" not in row[0]:
            dataPointStartRow += 1
        else:
            break

    dataPointStartRow += 1
    print(dataPointStartRow)
    dataPoints = results[dataPointStartRow:]
    dataPoints = list(map(lambda point: point[0].split("\t"), dataPoints))
    dataPoints = list(filter(lambda point: point[2] == "TRUE_POSITIVE", dataPoints))
    reaction_speed_sum = 0

    print(dataPoints)

    for point in dataPoints:
        reaction_speed_sum += 1000 / int(point[1])

    reaction_speed_average = reaction_speed_sum / len(dataPoints)

    results.insert(19 + (1 if "sequence" in results[2][0] else 0), ['# reaction speed basner=' + str(reaction_speed_average)])

    with open(folder_name + "corrected/" + filename, 'w') as f:
        for row in results:
            f.write("%s\n" % row[0])

print(str(len(files_in_folder)) + " Files corrected and written into " + folder_name + "corrected/")
