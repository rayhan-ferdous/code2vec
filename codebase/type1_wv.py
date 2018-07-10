import gensim, logging

for i in range(1, 2368):

    'input data'
    file1 = open('dataset/t1/' + str(i) + '_frag1.java')
    file2 = open('dataset/t1/' + str(i) + '_frag2.java')

    '''
    print file1.read()
    print '----------------------------------------------------'
    print file2.read()
    '''

    'build lists'
    sentences1 = []
    sentences2 = []

    for line in file1:
        print line



    break