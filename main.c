#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>


void sais(unsigned char *str, int *SA, int n, int m, int size);

int main(int argc, char **argv) {
FILE *src, *dest;
int i,n,q;
unsigned char *str;
int *SA;
clock_t start, finish;

//open source and destination files
//check if arguments are correct
src = fopen (argv[1],"rb");
	if (src==NULL) {
 	    fputs ("File error - no input file",stderr); 
 		exit (1);
	}
	dest = fopen(argv[2],"wb");	
	if (dest==NULL) {
 		fputs ("File error - no output file",stderr); 
 		exit (1);
	}

//find the length of input string
fseek(src, 0, SEEK_END);
n=ftell(src);
if(n==0) {
	fputs ("Error - string is empty",stderr); 
	exit (2);
	}	
n++; 
rewind (src);	

str=(unsigned char*) malloc(sizeof(unsigned char)*n);
SA =(int*) malloc(sizeof(int)*n);
	
fread((unsigned char *) str, 1, n-1, src);

//define a sentinel	
str[n-1]=0; 

start = clock();
//call sa-is algorithm
sais(str, SA, n, 255, sizeof(char));
finish = clock();

fprintf(stderr, "Time=%.4f sec\n", (double)(finish - start) / (double)CLOCKS_PER_SEC);

//check if string is sorted
q=checkSort(str,SA+1,n-1);
if(q==1) printf("SA is sorted correctly!");
else printf("Problem with sorting!");

for(i=1; i<n; i++)
	fprintf(dest, "%d ", SA[i]);
		
free(SA);
free(str);
return 0;
}
