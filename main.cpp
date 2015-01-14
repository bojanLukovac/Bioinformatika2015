#include <iostream>
#include <stdlib.h>
using namespace std;

#if !defined( unix )
#include <io.h>
#include <fcntl.h>
#endif

int main(int argc, char **argv) {
  
	fprintf( stderr, "\nStarted computing suffix array by SA-IS algorithm" );
	if ( argc > 1 ) freopen( argv[ 1 ], "rb", stdin );
	if ( argc > 2 ) freopen( argv[ 2 ], "wb", stdout );
	#if !defined( unix )
	setmode( fileno( stdin ), O_BINARY );
	setmode( fileno( stdout ), O_BINARY );
	#endif

	fprintf(stderr, "\nComputing of the suffix array finished");

	delete [] SA;
	delete [] s_ch;
	return 0;
}
