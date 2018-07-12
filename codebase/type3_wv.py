import gensim, logging
import numpy as np
from sklearn.manifold import TSNE
from scipy import spatial


'''configurations'''
size = 100
window = 10
sg = 0
hs = 0
negative = 0
alpha = 0.025
iter = 10
sample = 1e-05


file_start = 1
file_end = 767
folder = 't3'

'''data saving'''
result = open('result.csv', 'a')
header = 'euc_com,euc_un,city_com,city_un,hamm_com,hamm_un,cheby_com,cheby_un,cos_com,cos_un,jacc,type\n'
#result.write(header)


'''implementation part starts'''

def jaccard_sim(list1, list2):
    intersection = len(list(set(list1).intersection(list2)))
    #print(list(set(list1).intersection(list2)))
    union = (len(list1) + len(list2)) - intersection
    return float(intersection / union)



'''for all clones'''
for i in range(file_start, file_end):
    print 'pair', i, 'of', file_end

    '''input data'''
    file1 = open('dataset/' + folder + '/copy/' + str(i) + '_frag1.java.normal')
    file2 = open('dataset/' + folder + '/copy/' + str(i) + '_frag2.java.normal')


    #print file1.read()
    #print '=============================='
    #print file2.read()

    '''build sentence list'''
    sentences1 = []
    sentences2 = []

    for line in file1:
        currline = line.split()
        if len(currline) != 0:
            #print currline
            sentences1.append(currline)

    #print '=============================='

    for line in file2:
        currline = line.split()
        if len(currline) != 0:
            #print currline
            sentences2.append(currline)

    '''save words'''
    words1 = []
    words2 = []

    for lst in sentences1:
        for w in lst:
            words1.append(w)

    for lst in sentences2:
        for w in lst:
            words2.append(w)

    #print words1
    #print words2

    '''remove duplicate words'''
    uniqwords1 = []
    uniqwords2 = []

    for w in words1:
        if w not in uniqwords1:
            uniqwords1.append(w)

    for w in words2:
        if w not in uniqwords2:
            uniqwords2.append(w)

    #print uniqwords1
    #print uniqwords2


    '''model implementation'''
    m1 = gensim.models.Word2Vec(sentences1, min_count=1, size = size, window = window, sg = sg, hs = hs, negative = negative, alpha = alpha, iter = iter, sample = sample)
    m2 = gensim.models.Word2Vec(sentences2, min_count=1, size = size, window = window, sg = sg, hs = hs, negative = negative, alpha = alpha, iter = iter, sample = sample)

    '''matrix generation, row = tokens, columns = features'''
    tokfeat1 = []
    tokfeat2 = []

    for word in uniqwords1:
        tokfeat1.append(m1[word])

    for word in uniqwords2:
        tokfeat2.append(m2[word])

    #print tokfeat1
    #print tokfeat2

    #print len(words1)
    #print len(words2)


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

    #print normframe1
    #print normframe2


    '''structure'''
    d1 = {}
    d2 = {}

    #print len(uniqwords1) == len(normframe1)
    #print len(uniqwords2) == len(normframe2)

    for j in range(0, len(uniqwords1)):
        d1[uniqwords1[j]] = normframe1[j]

    for j in range(0, len(uniqwords2)):
        d2[uniqwords2[j]] = normframe2[j]

    #for k in d1:
    #    print k, '->', d1[k]
    #for k in d2:
    #    print k, '->', d2[k]

    '''common token vector'''
    commonvec1 = []
    commonvec2 = []

    for k in d1:
        if k in d2:
            commonvec1.append(d1[k])
            commonvec2.append(d2[k])

    #print commonvec1
    #print commonvec2


    '''uncommon token vector'''
    uncommonvec1 = []
    uncommonvec2 = []

    for k in d1:
        if k not in d2:
            uncommonvec1.append(d1[k])

    for k in d2:
        if k not in d1:
            uncommonvec2.append(d2[k])

    uncommonvec1.sort()
    uncommonvec2.sort()

    #print uncommonvec1
    #print uncommonvec2

    '''distance measurement'''

    ### fixing empty vectors
    if len(commonvec1) == 0:
        commonvec1 = [1]

    if len(commonvec2) == 0:
        commonvec2 = [1]

    if len(uncommonvec1) == 0:
        uncommonvec1 = [1]

    if len(uncommonvec2) == 0:
        uncommonvec2 = [1]

    ### common

    euclidean_common = spatial.distance.euclidean(commonvec1, commonvec2)
    cityblock_common = spatial.distance.cityblock(commonvec1, commonvec2)
    hamming_common = spatial.distance.hamming(commonvec1, commonvec2)
    chebyshev_common = spatial.distance.chebyshev(commonvec1, commonvec2)
    cosine_common = spatial.distance.cosine(commonvec1, commonvec2)

    ### uncommon

    minlen = min( len(uncommonvec1), len(uncommonvec2))

    euclidean_uncommon = spatial.distance.euclidean(uncommonvec1[0:minlen], uncommonvec2[0:minlen])
    cityblock_uncommon = spatial.distance.cityblock(uncommonvec1[0:minlen], uncommonvec2[0:minlen])
    hamming_uncommon = spatial.distance.hamming(uncommonvec1[0:minlen], uncommonvec2[0:minlen])
    chebyshev_uncommon = spatial.distance.chebyshev(uncommonvec1[0:minlen], uncommonvec2[0:minlen])
    cosine_uncommon = spatial.distance.cosine(uncommonvec1[0:minlen], uncommonvec2[0:minlen])

    ### jaccard
    jaccard = jaccard_sim(uniqwords1, uniqwords2)


    '''save results'''
    #print euclidean_common, cityblock_common, hamming_common, chebyshev_common, cosine_common
    #print euclidean_uncommon, cityblock_uncommon, hamming_uncommon, chebyshev_uncommon, cosine_uncommon
    #print jaccard

    # 'euc_com,euc_un,city_com,city_un,hamm_com,hamm_un,cheby_com,cheby_un,cos_com,cos_un,jacc,type\n'
    result_line = ','.join([str(euclidean_common), str(euclidean_uncommon), str(cityblock_common), str(cityblock_uncommon),
                   str(hamming_common), str(hamming_uncommon), str(chebyshev_common), str(chebyshev_uncommon),
                   str(cosine_common), str(cosine_uncommon), str(jaccard), folder])



    '''*** *** ***'''
    '''data cleansing logic - type specific'''
    if sentences1 != sentences2:
        print result_line

        result.write(result_line + '\n')
    else:
        print 'omitting...'



    print '*** *** ***'


    #break



print 'done'