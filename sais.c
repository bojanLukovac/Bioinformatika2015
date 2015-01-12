#include <stdio.h>
#include <stdlib.h>
#define sign(i) (size==sizeof(int)?((int*)str)[i]:((unsigned char *)str)[i])

//count the appearance of every symbol in string
void countsymb(unsigned char *str, int *chr, int n, int m, int size){
int i, c;
	for (i=0;i<m;i++)
  	chr[i]=0;
  for (i=0;i<n;i++){
  	c=sign(i);
  	chr[c]++;
 }
}

//define beginning or end for every bucket
//each type of symbol has its own bucket 
void buckets(int *chr, int *b, int m, int end){
int i, sum;
	sum=0;
	for(i=0;i<=m;i++) 
		b[i]=0;
	for (i=0;i<=m;i++){
		sum+=chr[i];
		if(end) 
		 b[i]=sum-1;
		else
		 b[i]=sum-chr[i];
	}
}

//define if a symbol is S-type or L-type
//put 1 or 0 in array t if a symbol is S-type or L-type
void symbset(unsigned char *str, int *t, int n, int size){
int i,j,c0,c1;
j=0;
t[n-1]=1;
t[n-2]=0;
for(i=0;i<n-2;i++){
	c0=sign(i);
	c1=sign(i+1);
	while (c0==c1){
		j++;
		c1=sign(i+1+j);
	}
	j=0;
	if(c0>c1) t[i]=0;
	 else t[i]=1;
}
}

//induce sort SA
void induceSA(unsigned char *str, int *SA, int *chr, int *b, int *t, int n, int m, int size){
	int i,j,l,c;
	countsymb(str,chr,n,m,size);
	buckets(chr,b,m,0); 
	for(i=0;i<n;i++){
		j=SA[i];
		if(j>0){
			l=SA[i]-1;
			if(l>=0 && t[l]==0){
				c=sign(l);
				SA[b[c]++]=l;
			}	
		}
	}
	countsymb(str,chr,n,m,size);
	buckets(chr,b,m,1);
	for(i=n-1;i>=0;i--){
		j=SA[i];
		if(j>0){
			l=SA[i]-1;
			if(l>=0 && t[l]==1){
				c=sign(l);
				SA[b[c]--]=l;
			}
		}
	}
}

//sa-is algorithm
void sais(unsigned char *str, int *SA, int n, int m, int size){
int *b, *chr, *t, *s1, *SA1;
int i,j,c,n1,c0,c1,flag,LMSsub;

b = (int*)malloc(sizeof(int)*(m+1));
chr = (int*)malloc(sizeof(int)*(m+1));
t = (int*) malloc(sizeof(int)*n);

//initialize SA with -1
for(i=0;i<n;i++)
 SA[i]=-1;

//stage 1: reduce the problem by at least 1/2
//count the appearance of all the symbols,
//find the end of every bucket and define all symbols as S-type or L-type
countsymb(str,chr,n,m,size);
buckets(chr,b,m,1);
symbset(str,t,n,size);

//define if a symbol is LMS and write it in SA, to the right place in a bucket
for(i=0;i<n-1;i++){
if(t[i]==1 && t[i-1]==0){
	SA[b[sign(i)]--]=i;
	}
}
//the last symbol (sentinel) goes to the first place in SA
SA[0]=n-1; 

//make induced sort
induceSA(str,SA,chr,b,t,n,m,size);
	
free(b);

//move all the sorted LMS into the first n1 items of SA
n1=1;
for(i=1;i<n;i++){
 	j=SA[i];	
 if(t[j]==1 && t[j-1]==0){
	SA[n1++]=SA[i];
}
 }
 
//initialize the rest of SA as -1 
for(i=n1;i<n;i++)
SA[i]=-1;

//define order of LMS substrings
flag=0;
LMSsub=0;
//sentinel is defined as 0
SA[SA[0]]=0;
//compare LMS substrings  
for(i=0;i<n1-1;i++){
	c0=SA[i];
	c1=SA[i+1];
	flag=0;
	 for(j=0;j<n;j++){
	 	//check if one of the compared symbols is a sentinel
	 	if((c0+j)==n-1||(c1+j)==n-1){
	 		flag=1;
	 		break;
		 }
		//check if symbols have different lexicographical value
		//or they are of different type (S-type or L-type)
		 else if((sign(c0+j)!=sign(c1+j))||(t[c0+j]!=t[c1+j])){
		 	flag=1;
		 	break;		 	
		 }
		 //check if one of the symbols is LMS
		 else if(j>0){
	 		if((t[c0+j]==1 && t[c0+j-1]==0)||(t[c1+j]==1 && t[c1+j-1]==0)) {
			break;
		  }
	    }
	 }
	 if(flag) LMSsub++;
	 SA[c1/2+n1]=LMSsub;
	 }
LMSsub++;

s1 =(int*) malloc(sizeof(int)*n1);
SA1 =(int*) malloc(sizeof(int)*n);

//define s1 
j=0;
for(i=n1;i<n;i++){
if(SA[i]!=-1){
 s1[j]=SA[i];
 j++;
}
}

//define SA1
j=n-1;
for(i=n-1;i>n1;i--)
if(SA[i]!=-1){
SA[j]=SA[i];
j--;
}
SA1=SA;

//stage 2: solve the reduced problem

//stage 3: induce the result for the original problem


free(b); 
free(t);
}
