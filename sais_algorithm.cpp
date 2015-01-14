#include <iostream>
#include <stdlib.h>
using namespace std;
#include <memory.h>

const int EMPTY = -1;
unsigned char mask[] = {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};
#define tget(i) ( (t[(i)/8]&mask[(i)%8]) ? 1 : 0 )
#define tset(i, b) t[(i)/8]=(b) ? (mask[(i)%8]|t[(i)/8]) : ((~mask[(i)%8])&t[(i)/8])
#define chr(i) (cs==sizeof(int)?((int*)s)[i]:((unsigned char *)s)[i])
#define isLMS(i) (i>0 && tget(i) && !tget(i-1))

// determine head or tail of each bucket
void getBuckets(unsigned char *s, int *bkt, int n, int K, int cs, bool end) { 
  int i, sum=0;
  
  for(i=0; i<=K; i++) bkt[i]=0; // clear all buckets
  for(i=0; i<n; i++) bkt[chr(i)]++; // compute the size of each bucket
  for(i=0; i<=K; i++) { sum+=bkt[i]; bkt[i]=end ? sum-1 : sum-bkt[i]; }
}

// SA-IS algorithm implementation
void SA_IS(unsigned char *s, int *SA, int n, int K, int cs, int level) {
  static double redu_ratio=0;
  static long sum_n=0, sum_n1=0;
  int i, j, pos, n1;
  bool diff;
  
  unsigned char *t=(unsigned char *)malloc(n/8+1); // allocate array of input string character types; using bits instead of 'L' and 'S', '1' stands for 'S'
  tset(n-2, 0); tset(n-1, 1);
  for(i=n-3; i>=0; i--) tset(i, (chr(i)<chr(i+1) || (chr(i)==chr(i+1) && tget(i+1)==1))?1:0); // classify type of each character

  int *bkt = (int *)malloc(sizeof(int)*(K+1)); // allocate bucket counter array

  // sort substrings
  // first step
  getBuckets(s, bkt, n, K, cs, true); // find tails of buckets
  for(i=0; i<n; i++) SA[i]=EMPTY;
  SA[0] = n-1; // set the LMS-substring that contains only the non alphabetical character
  for(i=n-2; i>=0; i--)
    if(isLMS(i)) SA[bkt[chr(i)]--]=i;
  // second step
  getBuckets(s, bkt, n, K, cs, false); // find heads of buckets
  if(level==0) bkt[0]++; 
  for(i=0; i<n; i++)
    if(SA[i]!=EMPTY) {
	  j=SA[i]-1; 
	  if(j>=0 && !tget(j)) SA[bkt[chr(j)]++]=j;
    }
  // third step
  getBuckets(s, bkt, n, K, cs, true); // find tails of buckets
  for(i=n-1; i>=0; i--)
    if(SA[i]!=EMPTY) {
	  j = SA[i]-1; 
	  if(j>=0 && tget(j)) SA[bkt[chr(j)]--] = j;
    }
  
  free(bkt); 
  free(t);
}
