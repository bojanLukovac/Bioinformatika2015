#!/usr/bin/env python

from sais_parser import *
from sais import *

input_parser = sais_parser()
input_parser.parse_input_args()

for input_string in input_parser.get_input_strings():

	#input_string_ASCII = input_string.unpack("c*")
  
	sa_is = sais()
	sa_is.initialize(input_string)
	sa_is.set_trace(False)

	sa_is.calculate_suffix_array()
	suffix_array = sa_is.get_output_SA()
	
	input_parser.write_output(suffix_array)

