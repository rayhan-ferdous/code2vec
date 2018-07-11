import os

for i in range(1, 1628):
    cmd1 = 'txl ' + str(i) + '_frag1.java removecomments.txl | txl stdin spacer.txl > copy/' + str(i) + '_frag1.java.normal'
    cmd2 = 'txl ' + str(i) + '_frag2.java removecomments.txl | txl stdin spacer.txl > copy/' + str(i) + '_frag2.java.normal'

    print cmd1
    print cmd2

    os.system(cmd1)
    os.system(cmd2)

    