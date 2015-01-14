####Goran Hrženjak
#####Ruby
==================
Implementation of SA-IS algorithm in Ruby

Usage
-----------
	$ cd GoranHrzenjak_Ruby
    $ ruby main.rb <input file name> [<input file format>] [<output file name>]

Input arguments	
  * input file name : path to the file to read from
  * input file form [optional] 
    * 0 - regular string [DEFAULT]
	* 1 - FASTA format
  * output file name [optional] : file name of the file to write in in *output* folder
    if no name is provided, output will be written in output/[<input file name>]_output.txt file
	
example:

    $ ruby main.rb input_data [<input file format>] [<output file name>]