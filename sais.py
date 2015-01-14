import sys


def buckets(char, b, m , end):
    summ = 0
    for i in range(m):
        b[i] = 0
    for i in range(m):
        summ += char[1]
        if end:
            b[i]=summ-1
        else:
            b[i] = summ - i



def countsymb(stri,char):
    for i in stri:
        char[ord(i)] += 1

def symbset(stri, t, n, size):
    j = 0
    t[n-1] = 1
    t[n-2] = 0
    for i in range(len(stri)-1):
        c0 = stri[i]
        c1 = stri[i+1]
        while c0==c1:
            j += 1
            c1=stri[i+j+1]
        j = 0
        if c0>c1:
            t[i] = 0
        else:
            t[i] = 1


def induceSA(stri, SA, char, b, t, n, m):
    countsymb(stri,char)
    buckets(char,b,m,0)




def sais(stri, SA, leng, wat):

    b = [0 for i in range(wat+1)]
    char = [0 for i in range(wat+1)]
    t = [0 for i in range(leng)]

    #countsymb part, stage 1
    countsymb(stri, char)
    buckets(char,b,wat,1)
    symbset(stri,t,leng, wat)
    print t

    for i in range(leng-1):
        if t[i] == 1 and t[i - 1]==0:
            u = b[ord(stri[i])] - 1
            SA[u] = i

    for i in range(n-1):
        if t[i] == 1 and t[i-1] == 0:
            b[ord(stri[i])] -= 1
            SA[b[ord(stri[i])]] = i
    SA[0] = stri[-1]
    print SA




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
    stri = input_file
    SA = [-1 for i in range(n)]
    print n

    sais(stri, SA, n, 255)
