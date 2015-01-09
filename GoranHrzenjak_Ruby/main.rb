#!/usr/bin/env ruby

$LOAD_PATH << '.'
require "sais_algorithm"
require "suffix_array"
require "parser"


input_parser = Parser.new
input_parser.parse_input_args

input_parser.get_input_strings.each do |input_string|
  #puts input_string
  #puts input_string.length
  
  #convert to ASCII values
  input_string_ASCII = input_string.unpack("c*")
  puts input_string_ASCII
  
  sa_is = SAIS.new(input_string_ASCII)
  sa_is.calculate_suffix_array
  
  
end
