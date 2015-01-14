// determine head or tail of each bucket
void getBuckets(unsigned char *s, int *bkt, int n, int K, int cs, bool end) { 
  int i, sum=0;
  
  for(i=0; i<=K; i++) bkt[i]=0; // clear all buckets
  for(i=0; i<n; i++) bkt[chr(i)]++; // compute the size of each bucket
  for(i=0; i<=K; i++) { sum+=bkt[i]; bkt[i]=end ? sum-1 : sum-bkt[i]; }
}
