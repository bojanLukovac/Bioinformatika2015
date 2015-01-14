#!/usr/bin/env ruby


$LOAD_PATH << '.'

require "parser"
require "helpers"
require "sais_methods"


input_parser = Parser.new
input_parser.parse_input_args

input_parser.get_input_strings.each do |input_string|
  #puts input_string
  #puts input_string.length
  
  #convert to ASCII values

  input_string_ASCII = input_string.unpack("c*")
  
  sa_is = SAIS.new(input_string_ASCII)
  
  
end