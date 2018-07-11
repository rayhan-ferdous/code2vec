import gensim, logging
import numpy as np
from sklearn.manifold import TSNE
from scipy import spatial

size = 100
window = 10
sg = 0
hs = 0
negative = 0
alpha = 0.005
iter = 10
sample = .01

file_start = 1
file_end = 767
folder = 't3'

'''data'''
distances = []
result = open('result.csv', 'a')

'''for all clones'''
for i in range(file_start, file_end):
    print 'pair', i

    try:
        'input data'
        file1 = open('dataset/' + folder + '/copy/' + str(i) + '_frag1.java.normal')
        file2 = open('dataset/' + folder + '/copy/' + str(i) + '_frag2.java.normal')


        #print file1.read()
        #print '=============================='
        #print file2.read()

        '''build sentence list'''
        sentence1 = []
        sentence2 = []

        for line in file1:
            currline = line.split()
            if len(currline) != 0:
                #print currline
                sentence1.append(currline)

        #print '=============================='

        for line in file2:
            currline = line.split()
            if len(currline) != 0:
                #print currline
                sentence2.append(currline)

        '''save words'''
        words1 = []
        words2 = []

        for lst in sentence1:
            for w in lst:
                words1.append(w)

        for lst in sentence2:
            for w in lst:
                words2.append(w)

        #print words1
        #print words2

        '''model implementation'''
        m1 = gensim.models.Word2Vec(sentence1, min_count=1, size = size, window = window, sg = sg, hs = hs, negative = negative, alpha = alpha, iter = iter, sample = sample)
        m2 = gensim.models.Word2Vec(sentence2, min_count=1, size = size, window = window, sg = sg, hs = hs, negative = negative, alpha = alpha, iter = iter, sample = sample)

        '''matrix generation, row = tokens, columns = features'''
        tokfeat1 = []
        tokfeat2 = []

        for word in words1:
            tokfeat1.append(m1[word])

        for word in words2:
            tokfeat2.append(m2[word])

        #print tokfeat1
        #print tokfeat2

        print 'frag1 word count =', len(words1)
        print 'frag2 word count =', len(words2)


        '''reduce features'''
        X1 = np.array(tokfeat1)
        X2 = np.array(tokfeat2)

        #print X1.shape
        #print X2.shape

        X1_embedded = TSNE(n_components=1).fit_transform(X1)
        #print X1_embedded.shape

        #print X1_embedded

        X2_embedded = TSNE(n_components=1).fit_transform(X2)
        #print X2_embedded.shape

        #print X2_embedded

        '''create 1D frames'''
        frame1 = []
        frame2 = []

        for j in X1_embedded:
            frame1.append(j[0])

        for j in X2_embedded:
            frame2.append(j[0])

        #print frame1
        #print frame2


        '''normalize'''
        normframe1 = [float(j) / sum(frame1) for j in frame1]
        normframe2 = [float(j) / sum(frame1) for j in frame2]

        #print len(normframe1)
        #print len(normframe2)

        '''sort'''
        normframe1.sort()
        normframe2.sort()

        print normframe1
        print normframe2


        '''differentiate'''
        minframelen = min(len(normframe1), len(normframe2))

        cosinedist = spatial.distance.cosine(normframe1[0:minframelen], normframe2[0:minframelen])
        print 'cosine dist =', cosinedist

        result.write(str(cosinedist) + '\n')
        distances.append(cosinedist)

        print '*** *** ***'

        #break

    except:
        print 'ERROR pair', i
        print '*** *** ***'



#print distances
print 'done'