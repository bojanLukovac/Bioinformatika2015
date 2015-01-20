#include <iostream>
#include <stdlib.h>
#include <stdio.h>
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
  
  // compact sorted substrings into the first n1 items of SA; 2*n1 shouldn't be larger than n
  n1 = 0;
  for(i=0; i<n; i++)
    if(isLMS(SA[i]))
      SA[n1++]=SA[i];

  for(i=n1; i<n; i++) SA[i] = EMPTY; // initialize substring lexicographic name array buffer
  
  // find lexicographic names of all substrings
  int name = 0, prev = -1;
  for(i=0; i<n1; i++) {
    pos = SA[i];
    diff = false;
    for(int d=0; d<n; d++)
      if(prev==-1 || pos+d==n-1 || prev+d==n-1 || chr(pos+d)!=chr(prev+d) || tget(pos+d)!=tget(prev+d)) {
        diff = true;
        break;
      } else if(isLMS(pos+d) || isLMS(prev+d))
        break;

    if(diff) 
      { name++; prev=pos; }
    pos = pos/2; //(pos%2==0)?pos/2:(pos-1)/2;
    SA[n1+pos]=name-1; 
  }
  for(i=n-1, j=n-1; i>=n1; i--)
	  if(SA[i]!=EMPTY) SA[j--] = SA[i];

   // create s1 and SA1
  int *SA1 = SA, *s1 = SA+n-n1;
  
  // recurse if names are not unique
  if(name<n1) {
    SA_IS((unsigned char*)s1, SA1, n1, name-1, sizeof(int), level+1);
  } else { // generate the suffix array of s1 directly
    for(i=0; i<n1; i++) SA1[s1[i]] = i;
  }
  
  bkt = (int *)malloc(sizeof(int)*(K+1)); // bucket counters

  // put all left-most S characters into their buckets
  getBuckets(s, bkt, n, K, cs, true); // find ends of buckets
  j = 0;
  for(i=1; i<n; i++)
  	if(isLMS(i)) s1[j++] = i; // get p1
  for(i=0; i<n1; i++) SA1[i] = s1[SA1[i]]; // get index in s1
  for(i=n1; i<n; i++) SA[i] = EMPTY; // init SA[n1..n-1]
  for(i=n1-1; i>=0; i--) {
	j=SA[i]; SA[i]=EMPTY;
	if(level==0 && i==0)
        	SA[0] = n-1;
      	else
        	SA[bkt[chr(j)]--]=j;
  }
  getBuckets(s, bkt, n, K, cs, false); // find heads of buckets
  if(level==0) bkt[0]++; 
  for(i=0; i<n; i++)
    if(SA[i]!=EMPTY) {
	  j = SA[i]-1; 
	  if(j>=0 && !tget(j)) SA[bkt[chr(j)]++] = j;
    }
  getBuckets(s, bkt, n, K, cs, true); // find tails of buckets
  for(i=n-1; i>=0; i--)
    if(SA[i]!=EMPTY) {
	  j = SA[i]-1; 
	  if(j>=0 && tget(j)) SA[bkt[chr(j)]--] = j;
    }

  free(bkt);
  free(t);
}
