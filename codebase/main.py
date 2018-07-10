'''data preprocssing'''

import pandas as pd

df = pd.read_csv('clone data.csv')

itercount = 0

serial_t1 = 1
serial_t2 = 1
serial_t3 = 1
serial_x = 1

for index, row in df.iterrows():
    '''first file'''
    file1 = open(row['srcfile1'])
    start1 = row['startline1']
    end1 = row['endline1']

    fraglines1 = []

    current = 1
    for line in file1:
        if start1 <= current <= end1:
            fraglines1.append(line)

        current = current+1


    '''second file'''
    file2 = open(row['srcfile2'])
    start2 = row['startline2']
    end2 = row['endline2']

    print start2, end2
    print fraglines1

    fraglines2 = []


    current = 1
    for line in file2:
        if start2 <= current <= end2:
            fraglines2.append(line)

        current = current+1

    print start2, end2
    print fraglines1


    '''filtering'''

    if row['isclone'] == 'f':
        '''writing files'''
        out1 = open('dataset/x/' + str(serial_x) + '_frag1.java', 'w')
        out1.write('\n'.join(fraglines1))


        out2 = open('dataset/x/' + str(serial_x) + '_frag2.java', 'w')
        out2.write('\n'.join(fraglines2))

        serial_x = serial_x+1

    elif row['isclone'] == 't':
        maxsim = max(float(row['type1sim']), float(row['type2sim']), float(row['type3sim']))

        if maxsim == float(row['type1sim']):
            '''writing files'''
            out1 = open('dataset/t1/' + str(serial_t1) + '_frag1.java', 'w')
            out1.write('\n'.join(fraglines1))

            out2 = open('dataset/t1/' + str(serial_t1) + '_frag2.java', 'w')
            out2.write('\n'.join(fraglines2))

            serial_t1 = serial_t1 + 1


        elif maxsim == float(row['type2sim']):
            '''writing files'''
            out1 = open('dataset/t2/' + str(serial_t2) + '_frag1.java', 'w')
            out1.write('\n'.join(fraglines1))

            out2 = open('dataset/t2/' + str(serial_t2) + '_frag2.java', 'w')
            out2.write('\n'.join(fraglines2))

            serial_t2 = serial_t2 + 1

        elif maxsim == float(row['type3sim']):
            '''writing files'''
            out1 = open('dataset/t3/' + str(serial_t3) + '_frag1.java', 'w')
            out1.write('\n'.join(fraglines1))

            out2 = open('dataset/t3/' + str(serial_t3) + '_frag2.java', 'w')
            out2.write('\n'.join(fraglines2))

            serial_t3 = serial_t3 + 1



'''
    if itercount > 10:
        break
    itercount = itercount + 1
'''

