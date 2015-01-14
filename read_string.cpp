#include <iostream>
#include <math.h>
#include <stdlib.h>
using namespace std;

#if !defined( unix )
#include <io.h>
#include <fcntl.h>
#endif

int main(int argc, char **argv) {
  int n;
  
	fprintf( stderr, "\nStarted computing suffix array by SA-IS algorithm" );
	if ( argc > 1 ) freopen( argv[ 1 ], "rb", stdin );
	if ( argc > 2 ) freopen( argv[ 2 ], "wb", stdout );
	#if !defined( unix )
	setmode( fileno( stdin ), O_BINARY );
	setmode( fileno( stdout ), O_BINARY );
	#endif

	// allocate 5n bytes memory for input string (n bytes) and output suffix array (4n bytes)
	fseek(stdin, 0, SEEK_END);
	n = ftell(stdin);
	if(n==0) {
		fprintf(stderr, "\nEmpty string, nothing to sort");
		return 0;
	}
	n++; // add one extra byte count for the non alphabetical character
	fprintf(stderr, "\nAllocating input and output space");
	unsigned char *s_ch=new unsigned char[n]; // allocate input string buffer
	int *SA = new int[n]; // allocate output suffix array
	if(s_ch==NULL || SA==NULL) {
		delete [] s_ch;
        delete [] SA; 
		fprintf(stderr, "\nInsufficient memory, exit!");
		return 0;
	}
  fprintf(stderr, "\nAllocation successful");

	// read input string to buffer
	fseek(stdin, 0, SEEK_SET );
	fread((unsigned char *) s_ch, 1, n-1, stdin);
	
	*s_ch[n-1] = '$'; //set non alphabetical character
	
	fprintf(stderr, "\nComputing of the suffix array finished");

	delete [] SA;
	delete [] s_ch;
	return 0;
}
