Goran Hrženjak
-----------
Implementation of SA-IS suffix array construction algorithm in **Ruby**.

Ruby required (version 1.9.3 or newer distributions)
[Download](https://www.ruby-lang.org/en/downloads/)

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
Input data
-----------	
Inside folder input_data there are are some input files for testing purposes
e.g.

| File          | Format          | Count      |
| :------------ |-----------------| ----------:|
| E_coli.fa     | FASTA           |  4 758 629 |
| test5.txt     | string          |     10 980 |
| test10.txt    | string          |  1 002 840 |
| bac2.txt      | FASTA           |  3 075 407 |
