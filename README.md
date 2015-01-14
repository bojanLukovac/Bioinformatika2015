Goran Hrženjak
-----------
Implementation of SA-IS suffix array construction algorithm in **Ruby**.

Usage
-----------
	$ cd GoranHrzenjak_Ruby
    $ ruby main.rb <input file name> [<input file format>] [<output file name>]

Input arguments	
  * **input file name**
	* Path to the file to read from
  * **input file format** [optional] 
    * 0 - regular string [DEFAULT]
	* 1 - FASTA format
  * **output file name** [optional]
	* File inside *output* folder where output will be written in
	* If no name is provided, output will be written in output/[input file name]_output.txt file
	
Examples
-----------
    $ ruby main.rb input_data/test2.txt 0 test2_new.txt
	$ ruby main.rb input_data/test1_fasta.fa 1
	
	