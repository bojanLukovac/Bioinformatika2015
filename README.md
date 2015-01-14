####Goran Hrženjak
#####Ruby
==================
Implementation of SA-IS algorithm in Ruby

Usage
-----------
	$ cd GoranHrzenjak_Ruby
    $ ruby main.rb <input file name> [<input file format>] [<output file name>]

Input arguments	
  * **input file name** - path to the file to read from
  * **input file format** [optional] 
    * 0 - regular string [DEFAULT]
	* 1 - FASTA format
  * **output file name** [optional] : file to write in - if no name is provided, output will be written in output/[<input file name>]_output.txt file
	otherwise in output/[<output file name>]
	
Example
-----------
    $ ruby main.rb input_data/test2.txt 0 test2_new.txt