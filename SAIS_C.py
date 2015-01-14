import sys


def buckets(char, b, m , end):
    summ = 0
    for i in range(m):
        b[i] = 0
    for i in range(m):
        summ += char[i]
        if end:
            b[i]=summ-1
        else:
            b[i] = summ - char[i]



def countsymb(stri,char):
    for i in range(len(char)):
        char[i] = 0

    for i in range(len(stri)):
        char[ord(stri[i])] += 1
        print char[ord(stri[i])]

def symbset(stri, t, n, size):
    j = 0
    t[n-1] = 1
    t[n-2] = 0
    for i in range(len(stri)-2):
        c0 = stri[i]
        c1 = stri[i+1]
        while c0==c1:
            j += 1
            c1=stri[i+1+j]
        j = 0
        if c0>c1:
            t[i] = 0
        else:
            t[i] = 1


def induceSA(stri, SA, char, b, t, n, m):
    countsymb(stri,char)
    buckets(char,b,m,0)
    for i in range(n):
        j=SA[i]
        if j > 0:
            l = SA[i] - 1
            if l >= 0 and t[l] == 0:
                c = stri[i]
                b[ord(c)] += 1
                SA[b[ord(c)]] = l

    countsymb(stri,char,n,m)
    buckets(char, b,m,1)
    for i in range(n)[::-1]:#reverse it
        j=SA[i]
        if j > 0:
            l = SA[i] - 1
            if l >= 0 and t[l] == 1:
                c = stri[i]
                b[ord(c)] -= 1
                SA[b[ord(c)]] = l






def sais(stri, SA, leng, wat):

    b = [0 for i in range(wat+1)]
    char = [0 for i in range(wat+1)]
    t = [0 for i in range(leng+1)]

    #countsymb part, stage 1
    countsymb(stri, char)
    buckets(char,b,wat,1)
    symbset(stri,t,leng, wat)
    print t
    print b
    print char

    for i in range(leng-1):
        if t[i] == 1 and t[i - 1]==0:
            u = b[ord(stri[i])] - 1
            SA[u] = i

#    for i in range(n-1):
#        if t[i] == 1 and t[i-1] == 0:
#            b[ord(stri[i])] -= 1
#            SA[b[ord(stri[i])]] = i

    SA[0] = ord(stri[-1])
    induceSA(stri, SA, char, b,t,leng,wat)

    n1=1
    for i in range(leng):
        j=SA[i]
        if t[j]==1 and t[j-1]==0:
            n1 += 1
            SA[n1]  = SA[i]






if __name__ == "__main__":
    try:
        with open(sys.argv[1], "r") as fh:
            input_file = fh.read()
        output_file = open(sys.argv[2], "rw")
    except:
        exit()

    n = len(input_file)
    if n==0:
        exit()

    n = n + 1
    stri = input_file.strip()
    SA = [-1 for i in range(n)]

    sais(stri, SA, n, 255)
